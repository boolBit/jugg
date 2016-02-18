package com.lorabit.net.nioserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author lorabit
 * @since 16-2-18
 */
public class NioClient implements Runnable {
  private volatile boolean halt;
  int port;
  SocketChannel channel;
  Selector selector;

  public NioClient(int port) {
    this.port = port;
    try {
      selector = Selector.open();
      channel = SocketChannel.open();
      channel.configureBlocking(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      doConnetct();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    SelectionKey key = null;
    while (!halt) {
      try {
        selector.select(1000);
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> keyIte = keys.iterator();
        while (keyIte.hasNext()) {
          key = keyIte.next();
          keyIte.remove();
          handle(key);
        }
      } catch (IOException e) {
        if (key != null) {
          key.cancel();
        }
        e.printStackTrace();
      }
    }
    if (selector != null) {
      try {
        selector.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void handle(SelectionKey key) throws IOException {
    if (key.isValid()) {
      final SocketChannel channel = (SocketChannel) key.channel();
      if (key.isConnectable()) {
        if (channel.finishConnect()) {
          channel.register(selector, SelectionKey.OP_READ);
          new Thread() {
            @Override
            public void run() {
              try {
                while (!halt) {
                  doWrite(channel);
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }.start();
          System.out.println("sender start completed");
        } else {
          System.out.println("connect error");
          System.exit(-1);
        }
      }

      if (key.isReadable()) {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        int readBytes = channel.read(readBuffer);
        if (readBytes > 0) {
          readBuffer.flip();
          byte[] bytes = new byte[readBuffer.remaining()];
          readBuffer.get(bytes);
          String body = new String(bytes, "UTF-8");
//          System.out.println("received from server is : " + body);
          sendToFile(body);
        } else if (readBytes == 0) {
        } else {
          System.out.println("no more msg");
          key.cancel();
          channel.close();
        }
      }
    }
  }

  public void sendToFile(String msg) throws IOException {
    File file = new File("/home/hellokitty/桌面/sender");
    BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
    writer.write(msg);
    writer.newLine();
    writer.flush();
  }

  private void doWrite(SocketChannel channel) throws IOException {
    System.out.println("please input what you want to send");
    Scanner scanner = new Scanner(System.in);
    String msg = scanner.nextLine();
    if (msg.equalsIgnoreCase("bye")) {
      halt = true;
      return;
    }
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    buffer.put(msg.getBytes());
    buffer.flip();
    channel.write(buffer);
  }


  private void doConnetct() throws IOException {
    if (channel.connect(new InetSocketAddress(port))) {
      channel.register(selector, SelectionKey.OP_READ);
    } else {
      channel.register(selector, SelectionKey.OP_CONNECT);
    }
  }

  public void setHalt(boolean halt) {
    this.halt = halt;
  }
}
