package com.lorabit.net.nettyserver.telnet;

import org.apache.commons.lang.math.RandomUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @author lorabit
 * @since 16-3-1
 */
public class ClientHanler extends SimpleChannelInboundHandler<String> {

  final AttributeKey key = new AttributeKey<Object>("dog");

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    Attribute attr = ctx.attr(key);
    Dog dog = (Dog) attr.get();
    if (dog != null)
      dog.bark();
    System.out.println("receive form server " + msg);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Attribute attr = ctx.attr(key);
    Dog dog =new Dog(RandomUtils.nextLong());
    attr.set(dog);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.channel().close();
  }
}
