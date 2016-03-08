package com.lorabit.net.nettyserver;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lorabit
 * @since 16-2-28
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ch.pipeline().addLast(new TimeServerHandler());
  }
}
