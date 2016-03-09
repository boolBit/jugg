package com.lorabit.rpc.server;

import com.lorabit.rpc.meta.BinaryPacketRaw;
import com.lorabit.rpc.processor.Processor;
import com.lorabit.rpc.processor.RpcContext;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lorabit
 * @since 16-3-8
 */
@ChannelHandler.Sharable
public class JavaServerHandler extends SimpleChannelInboundHandler<BinaryPacketRaw> {

  private Processor<BinaryPacketRaw> processor;

  public JavaServerHandler(Processor processor) {
    this.processor = processor;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, BinaryPacketRaw msg) throws Exception {
    msg.ctx = ctx;
    System.out.println("process msg "+msg);
    processor.process(new RpcContext(), msg);
  }
}
