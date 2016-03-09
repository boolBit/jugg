package com.lorabit.rpc.server;


import com.lorabit.rpc.base.RpcServerDecodeProxy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lorabit
 * @since 16-3-9
 */
public class RpcServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

  SimpleChannelInboundHandler handler;

  public RpcServerHandlerInitializer(SimpleChannelInboundHandler handler) {
    this.handler = handler;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast("frame", new RpcServerDecodeProxy());
    pipeline.addLast("handler", handler);
  }
}
