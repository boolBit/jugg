package com.lorabit.net.nettyserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lorabit
 * @since 16-2-28
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {


  private ByteBuf buf;

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) {
    buf = ctx.alloc().buffer(128); // (1)
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) {
    buf.release(); // (1)
    buf = null;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf m = (ByteBuf) msg;
    System.out.println(m.toString());
    m.release();
    ctx.close();
  }


  //  @Override
//  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//    System.out.println("receive from server");
//    byte[] read = new byte[msg.readableBytes()];
//    msg.readBytes(read);
//    System.out.println("received from server + " + new String(read, Charsets.UTF_8));
//  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    byte[] str = "now".getBytes();
    ByteBuf buf = Unpooled.buffer(str.length);
    System.out.println("write msg");
    buf.writeBytes(str);
    ctx.writeAndFlush(buf);
    ctx.flush();
  }

}
