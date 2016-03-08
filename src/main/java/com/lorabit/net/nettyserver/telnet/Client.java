package com.lorabit.net.nettyserver.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lorabit
 * @since 16-3-1
 */
public class Client {
  public static void main(String[] args) throws IOException {
    Bootstrap boot = new Bootstrap();
    EventLoopGroup group = new NioEventLoopGroup();
    boot.group(group)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new ClientInitHandler());
    try {
      Channel ch =boot.connect(new InetSocketAddress(8033)).sync().channel();
      // Read commands from the stdin.
      ChannelFuture lastWriteFuture = null;
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      for (; ; ) {
        String line = in.readLine();
        if (line == null) {
          break;
        }

        // Sends the received line to the server.
        lastWriteFuture = ch.writeAndFlush(line + "\n");

        // If user typed the 'bye' command, wait until the server closes
        // the connection.
        if ("bye".equals(line.toLowerCase())) {
          ch.closeFuture().sync();
          break;
        }
      }

//       Wait until all messages are flushed before closing the channel.
      if (lastWriteFuture != null) {
        lastWriteFuture.sync();
      }
      ch.close();
      System.out.println("close");
    } catch (InterruptedException e) {
      e.printStackTrace();
      group.shutdownGracefully();
    }
  }
}
