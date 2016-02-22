package com.lorabit.net.aioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author lorabit
 * @since 16-2-22
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,AsyncTimeServer> {

  @Override
  public void completed(AsynchronousSocketChannel result, AsyncTimeServer attachment) {
    attachment.server.accept(attachment,this); // 继续下一个client的处理
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    result.read(buffer,buffer,new ReadCompletionHandler(result));
  }

  @Override
  public void failed(Throwable exc, AsyncTimeServer attachment) {
    try {
      attachment.server.close();
      attachment.latch.countDown();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
