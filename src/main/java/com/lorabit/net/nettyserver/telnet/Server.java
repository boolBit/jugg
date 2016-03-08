package com.lorabit.net.nettyserver.telnet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author lorabit
 * @since 16-3-1
 */
public class Server {

  public static void main(String[] args) {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workGroup = new NioEventLoopGroup();
    ServerBootstrap boot = new ServerBootstrap();
    boot.channel(NioServerSocketChannel.class)
        .group(bossGroup, workGroup)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new TelnetServerInitializer());

    try {
      boot.bind(8033).sync().channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
      bossGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }
}
