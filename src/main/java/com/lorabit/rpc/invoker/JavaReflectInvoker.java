package com.lorabit.rpc.invoker;

import com.google.common.collect.Maps;

import com.lorabit.rpc.base.ClientId;
import com.lorabit.rpc.exception.RpcException;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class JavaReflectInvoker implements Invoker {

  protected ClientId clientId;
  protected Class iface;
  protected Object impl;

  // direct name lookup
  protected Map<String, Method> methods = Maps.newHashMap();

  // parameter types
  protected Map<String, Class[]> types = Maps.newHashMap();  //just for not java client...

  /**
   * <pre>
   * generic parameterized types caution:
   *
   * void aaa(Map<String, Float>, Map<Double, Boolean>)
   *
   * 	will =>
   *
   *    Class[][] = {
   *    				[String, Float],
   *                  [Double, Boolean]
   * }
   *
   * <pre>
   */
  protected Map<String, Class[][]> paramTypes = Maps.newHashMap();

  public JavaReflectInvoker(ClientId clientId, Class iface, Object impl) {
    this.clientId = clientId;
    this.iface = iface;
    this.impl = impl;
    init();
  }

  private void init() {
    Method[] methods = iface.getMethods();    // 所有的public方法 包括继承了父类
    for (Method method : methods) {
      this.methods.put(method.getName(), method);
    }
  }


  @Override
  public Object invoke(String name, Object[] parameters) throws RpcException {
    Method m = getMethod(name);
    return invokeMethod(m, parameters);
  }

  private Object invokeMethod(Method m, Object[] parameters) throws RpcException {
    try {
      Object ret = m.invoke(impl, parameters);
      return ret;
    } catch (Exception e) {
      StringBuilder sb = new StringBuilder();
      sb.append(m.getClass().getName()).append(".").append(m.getName()).append("(");
      for (int i = 0; i < parameters.length; i++) {
        if (!(parameters[i] == null)) {
          sb.append(parameters[i].getClass().getName()).append(",");
        }
      }
      throw new RpcException(sb.toString(), e);
    }
  }

  protected Method getMethod(String name) throws RpcException {
    Method ret = methods.get(name);
    if (ret == null) {
      throw new RpcException("Not found method[" + name + "]");
    }
    return ret;
  }

  @Override
  public Class[] lookupParameterTypes(String name) throws RpcException {
    return new Class[0];
  }

  @Override
  public Class[][] lookupParameterizedType(String name) throws RpcException {
    return new Class[0][];
  }
}
