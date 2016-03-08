package com.lorabit.net.gettingstart;

import com.lorabit.net.gettingstart.model.UnixTime;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lorabit
 * @since 16-2-29
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
//  @Override
//  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
////    try {
//    ByteBuf in = (ByteBuf) msg;
//    System.out.println(in.toString(CharsetUtil.UTF_8));
//    final ByteBuf time = ctx.alloc().buffer(4); // (2)
//    time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//    ctx.writeAndFlush(time);
//  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) { // (1)
//    final ByteBuf time = ctx.alloc().buffer(4); // (2)
//    time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//
//    final ChannelFuture f = ctx.writeAndFlush(time); // (3)

    ChannelFuture f = ctx.writeAndFlush(new UnixTime());
    f.addListener(ChannelFutureListener.CLOSE);

//    f.addListener(new ChannelFutureListener() {
//      @Override
//      public void operationComplete(ChannelFuture future) {
//        assert f == future;
//        ctx.close();
//      }
//    }); // (4)
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.channel().close();
    cause.printStackTrace();
  }
}
