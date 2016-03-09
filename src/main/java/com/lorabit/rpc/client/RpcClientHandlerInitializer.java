package com.lorabit.rpc.client;


import com.lorabit.rpc.base.RpcPacketDecoderProxy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcClientHandlerInitializer extends ChannelInitializer<SocketChannel> {

  SimpleChannelInboundHandler handler;

  public RpcClientHandlerInitializer() {
  }

  public RpcClientHandlerInitializer(SimpleChannelInboundHandler handler) {
    this.handler = handler;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast("frame", new RpcPacketDecoderProxy());
    pipeline.addLast("handler", handler);
  }
}
