package com.lorabit.net.nettyserver;


import com.lorabit.net.gettingstart.NettyTimeClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lorabit
 * @since 16-2-28
 */
public class TimeClient {

  public static void main(String[] args) {
    String host = "localhost";
    int port = 8015;
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    Bootstrap boot = new Bootstrap();
    boot.group(workerGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)// (4)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyTimeClientHandler());
          }
        });

    try {
      ChannelFuture future = boot.connect(host, port).sync();
      System.out.println(future.isSuccess());
//      future.await(2000);
      Thread.sleep(2000);
      System.out.println(future.isSuccess());
//      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
//      workerGroup.shutdownGracefully();
    }
  }
}
