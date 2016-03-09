package com.lorabit.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Kryo.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.lorabit.rpc.demo.impl.DemoService;
import com.lorabit.rpc.exception.RpcException;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lorabit
 * @since 16-3-9
 */
public class KryoUtil {
  public static Kryo kryo;
  private static Map<Class<?>, Registration> cachedSchema = new ConcurrentHashMap<>();

  static {
    kryo = new Kryo();
    kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    kryo.register(RpcException.class, new JavaSerializer());
  }

  @SuppressWarnings("unchecked")
  private static Registration getRegistration(Class cls) {
    Registration registration = cachedSchema.get(cls);
    if (registration == null) {
      synchronized (KryoUtil.class) {
        registration = kryo.register(cls);
        if (registration != null) {
          cachedSchema.put(cls, registration);
        }
      }
    }
    return registration;
  }


  /**
   * 序列化（对象 -> 字节数组）
   */
  public synchronized static <T> byte[] serialize(T obj) {
    Output output = new Output(1, 4096);
    kryo.writeObject(output, obj);
    output.flush();
    return output.toBytes();
  }

  public synchronized static <T> byte[] serializeAndObject(T obj) {
    Output output = new Output(1, 4096);
    kryo.writeClassAndObject(output, obj);
    output.flush();
    return output.toBytes();
  }

  /**
   * 反序列化（字节数组 -> 对象）
   */
  public synchronized static <T> T deserialize(byte[] data, Class<T> cls) {
    Input input = new Input(data);
    T s = (T) kryo.readObject(input, getRegistration(cls).getType());
    input.close();
    return s;
  }

  public synchronized static Object deserialize(byte[] data) {
    Input input = new Input(data);
    Object s = kryo.readClassAndObject(input);
    input.close();
    return s;
  }

  public synchronized static <T> T deserialize(byte[] data, int offset, int len, Class<T> cls) {
    Input input = new Input(data, offset, len);
    T s = (T) kryo.readObject(input, getRegistration(cls).getType());
    input.close();
    return s;
  }

  public static Object deserialize(byte[] data, int i, int remaining) {
    Input input = new Input(data);
    Object s = kryo.readClassAndObject(input);
    input.close();
    return s;
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException {
//    TestObject obj = new TestObject();
//
//    byte[] kryoBytes = KryoUtil.serialize(obj);
//    System.out.println(kryoBytes.length);
//
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    ObjectOutputStream objOut = new ObjectOutputStream(out);
//    objOut.writeObject(obj);
//    byte[] javaBytes = out.toByteArray();
//    System.out.println(javaBytes.length);
//
//
//    TestObject objKryo = KryoUtil.deserialize(kryoBytes, TestObject.class);
//    ByteArrayInputStream in = new ByteArrayInputStream(javaBytes);
//    ObjectInputStream objIn = new ObjectInputStream(in);
//    TestObject objJava = (TestObject) objIn.readObject();

//    String s = "hello world";
//    byte[] bytes = KryoUtil.serializeAndObject(s);
//
//
//    ByteBuffer buf = ByteBuffer.allocate(bytes.length);
//
//    System.out.println(bytes.length);
//    System.out.println(buf.arrayOffset());
//    System.out.println(buf.remaining());
//    System.out.println(buf.array().length);
//    Object sa = KryoUtil.deserialize(bytes);
//    System.out.println((String)sa);
    DemoService service = new DemoService();
    Kryo kryo = new Kryo();
    kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    kryo.register(RpcException.class, new JavaSerializer());
    byte[] bs = null;
    try {
      service.exception();
    } catch (Exception e) {
      RpcException ee = new RpcException(e);
      System.out.println(e.getStackTrace().length);
      Output out = new Output(1, 4096);
      kryo.writeClassAndObject(out, ee);
      bs = out.getBuffer();
    }
    Input in = new Input(bs);
    Throwable t2 = (Throwable) kryo.readClassAndObject(in);
    System.out.println("Print");


    t2.printStackTrace();


    System.out.println("Print After");

    System.out.println(t2.getMessage());
    System.out.println("hah");
  }


}
