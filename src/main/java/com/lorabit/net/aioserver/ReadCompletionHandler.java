package com.lorabit.net.aioserver;

import com.google.common.base.Charsets;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * @author lorabit
 * @since 16-2-22
 */
public class ReadCompletionHandler implements java.nio.channels.CompletionHandler<Integer, java.nio.ByteBuffer> {
  AsynchronousSocketChannel channel;

  public ReadCompletionHandler(AsynchronousSocketChannel result) {
    if (this.channel == null) {
      this.channel = result;
    }
  }

  @Override
  public void completed(Integer result, ByteBuffer attachment) {
    attachment.flip();
    byte[] body = new byte[attachment.remaining()];
    attachment.get(body);
    String req = new String(body, Charsets.UTF_8);
    System.out.println("received req " + req);
    if ("NOW".equalsIgnoreCase(req)) {
      doWrite(new Date().getTime() + "");
    } else {
      doWrite("your req is " + req);
    }
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    doWrite("4000mills esplase " + req);
  }

  private void doWrite(String s) {
    if (StringUtils.isEmpty(s)) {
      return;
    }
    byte[] body = s.getBytes();
    ByteBuffer buffer = ByteBuffer.allocate(body.length);
    buffer.put(body);
    buffer.flip();
    channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {

      @Override
      public void completed(Integer result, ByteBuffer attachment) {
        if (attachment.hasRemaining()) {
          channel.write(attachment, attachment, this);
        }
      }

      @Override
      public void failed(Throwable exc, ByteBuffer attachment) {
        try {
          channel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  public void failed(Throwable exc, ByteBuffer attachment) {
    try {
      this.channel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
