package com.lorabit.net.nettyserver.factorial;

/**
 * @author lorabit
 * @since 16-3-1
 *
 * 28   * Sends a sequence of integers to a {@link FactorialServer} to calculate 29   * the
 * factorial of the specified integer. 30
 */
/**
 28   * Sends a sequence of integers to a {@link FactorialServer} to calculate
 29   * the factorial of the specified integer.
 30   */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;


public final class Client {
  static final boolean SSL = false;
  static final String HOST = System.getProperty("host", "127.0.0.1");
  static final int PORT = Integer.parseInt(System.getProperty("port", "8022"));
  static final int COUNT = Integer.parseInt(System.getProperty("count", "1000"));

  public static void main(String[] args) throws Exception {
    // Configure SSL.
    SslContext sslCtx = null;
    if (SSL) {
      SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
    } else {
      sslCtx = null;
    }

    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .handler(new FactorialClientInitializer(sslCtx));

      // Make a new connection.
      ChannelFuture f = b.connect(HOST, PORT).sync();

      // Get the handler instance to retrieve the answer.
      FactorialClientHandler handler =
          (FactorialClientHandler) f.channel().pipeline().last();

      // Print out the answer.
      System.err.format("Factorial of %,d is: %,d", COUNT, handler.getFactorial());
    } finally {
      group.shutdownGracefully();
    }
  }
}
