package com.lorabit.net.gettingstart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lorabit
 * @since 16-2-29
 */
public class NettyTimeClientHandler extends ChannelInboundHandlerAdapter {

  private ByteBuf buf;

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) {
    buf = ctx.alloc().buffer(4); // (1)
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) {
    buf.release(); // (1)
    buf = null;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf m = (ByteBuf) msg;
    System.out.println(m.toString());
    ctx.close();
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    byte[] str = "now".getBytes();
    ByteBuf buf = Unpooled.buffer(str.length);
    System.out.println("write msg");
    buf.writeBytes(str);
    ctx.writeAndFlush(buf);
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
