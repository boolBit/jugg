package com.lorabit.net.nettyserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author lorabit
 * @since 16-2-28
 */
public class TimeServer {
  public void bind(int port) {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workGroup = new NioEventLoopGroup();
    ServerBootstrap boot = new ServerBootstrap();
    boot.group(bossGroup, workGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .childHandler(new ChildChannelHandler());

    try {
      ChannelFuture future = boot.bind(port).sync();


      //等待服务器端接口被关闭
      future.channel().closeFuture().sync();

    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      bossGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) {
    new TimeServer().bind(8015);
  }

}
