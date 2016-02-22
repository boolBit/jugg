package com.lorabit.net.aioserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author lorabit
 * @since 16-2-22
 */
public class AsyncTimeClient implements Runnable, CompletionHandler<Void, AsyncTimeClient> {
  private AsynchronousSocketChannel client;
  private String host;
  private int port;
  private CountDownLatch latch;

  public AsyncTimeClient(String host, int port) {
    try {
      client = AsynchronousSocketChannel.open();
      this.host = host;
      this.port = port;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    latch = new CountDownLatch(1);
    client.connect(new InetSocketAddress(host, port), this, this);
    try {
      latch.await();
    } catch (InterruptedException e) {
      latch.countDown();
      e.printStackTrace();
    }
  }

  @Override
  public void completed(Void result, AsyncTimeClient attachment) {
    byte[] req = "now".getBytes();
    ByteBuffer buffer = ByteBuffer.allocate(req.length);
    buffer.put(req);
    buffer.flip();
    client.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
      @Override
      public void completed(Integer result, ByteBuffer attachment) {
        if (attachment.hasRemaining()) {
          client.write(attachment, attachment, this);
        } else {
          ByteBuffer readBuffer = ByteBuffer.allocate(1024);
          client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
              attachment.flip();
              byte[] bytes = new byte[attachment.remaining()];
              attachment.get(bytes);
              try {
                System.out.println(new String(bytes, "UTF-8"));
              } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
              }
//              latch.countDown();
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
              try {
                client.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          });
        }
      }

      @Override
      public void failed(Throwable exc, ByteBuffer attachment) {
        try {
          client.close();
        } catch (IOException e) {
          e.printStackTrace();
          latch.countDown();
        }
      }
    });
  }

  @Override
  public void failed(Throwable exc, AsyncTimeClient attachment) {
    try {
      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    latch.countDown();
  }
}
