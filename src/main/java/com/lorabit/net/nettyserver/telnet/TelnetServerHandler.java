package com.lorabit.net.nettyserver.telnet;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lorabit
 * @since 16-3-1
 */
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {
  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//  AttributeKey key = new AttributeKey<>("test");

  String delimeter = "\n";
  private boolean close = false;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    String resp = "";
    if (StringUtils.isEmpty(msg)) {
      resp = "plz put something...";
    } else if ("bye".equalsIgnoreCase(msg)) {
      close = true;
      resp = "Bye Bye ...";
    } else if ("now".equalsIgnoreCase(msg)) {
      resp = format.format(new Date());
    } else {
      resp = msg;
    }
    ChannelFuture ft = ctx.write(resp + delimeter);
    if (close) {
      System.out.println("close channel...");
      ft.addListener(ChannelFutureListener.CLOSE);
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.write("welcome lorabit's server" + delimeter);
    ctx.write("today is " + DateTime.now() + delimeter);
    ctx.flush();
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.channel().close();
  }
}
