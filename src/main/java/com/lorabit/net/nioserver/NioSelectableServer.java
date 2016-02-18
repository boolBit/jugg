package com.lorabit.net.nioserver;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lorabit
 * @since 16-2-18
 */
public class NioSelectableServer implements Runnable {

  private volatile boolean halt;
  private Selector selector;
  private ServerSocketChannel serverSocketChannel;

  public NioSelectableServer(int port) {
    try {
      selector = Selector.open();
      serverSocketChannel = ServerSocketChannel.open();
      serverSocketChannel.socket().bind(new InetSocketAddress(port), 50);  //backlog
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      System.out.println("start server at " + port);
    } catch (IOException e) {
      if (selector != null) {
        try {
          selector.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
      e.printStackTrace();
    }
  }


  @Override
  public void run() {
    while (!halt) {
      try {
        selector.select(1000);  //block only for most 1 second
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> ite = keys.iterator();
        while (ite.hasNext()) {
          SelectionKey key = ite.next();
          ite.remove();
          try {
            handle(key);
          } catch (Exception e) {
            key.cancel();
            if (key.channel() != null) {
              key.channel().close();
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (selector != null) {
      try {
        selector.close(); //关联的channel 和 pipe都会被关闭关闭
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void handle(SelectionKey key) throws IOException {
    if (key.isValid()) {
      if (key.isAcceptable()) {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        //after accept , register to selector again
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
      }
      if (key.isReadable()) {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int num = sc.read(buffer); // 是非阻塞的
        if (num > 0) {
          buffer.flip();
          byte[] bytes = new byte[buffer.remaining()];
          buffer.get(bytes);
          String msg = new String(bytes, Charsets.UTF_8);
          System.out.println("coming from client " + msg);
          if ("now".equalsIgnoreCase(msg)) {
            doWrite(sc, "now:" + System.currentTimeMillis());
          } else {
            doWrite(sc, "input is:" + msg);
          }
        } else if (num == 0) {
        } else {
          if (key != null) {
            sc.close();
            key.cancel();
          }
        }
      }
    }
  }

  private void doWrite(SocketChannel sc, String msg) throws IOException {
    byte[] msgs = msg.getBytes();
    ByteBuffer buffer = ByteBuffer.allocate(msgs.length);
    buffer.put(msgs);
    buffer.flip();
    System.out.println("write msg:" + msg);
    sc.write(buffer);
  }

  public void setHalt(boolean halt) {
    this.halt = halt;
  }
}
