package com.lorabit.net.nettyserver.factorial;

import java.math.BigInteger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lorabit
 * @since 16-3-1
 *
 * 已经decode成BigInteger
 */
public class FactorialServerHandler extends SimpleChannelInboundHandler<BigInteger> {
  private BigInteger lastMultiplier = new BigInteger("1");
  private BigInteger factorial = new BigInteger("1");

  @Override
  public void channelRead0(ChannelHandlerContext ctx, BigInteger msg) throws Exception {
    // Calculate the cumulative factorial and send it to the client.
    lastMultiplier = msg;
    factorial = factorial.multiply(msg);
    ctx.writeAndFlush(factorial);   // 会被encode
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    System.err.printf("Factorial of %d is: %d%n", lastMultiplier, factorial);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
