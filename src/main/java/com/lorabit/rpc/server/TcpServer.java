package com.lorabit.rpc.server;

import com.lorabit.rpc.meta.BinaryPacketRaw;
import com.lorabit.rpc.processor.Processor;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author lorabit
 * @since 16-3-9
 */
public class TcpServer implements GenericFutureListener, RpcServer {

  private Processor<BinaryPacketRaw> processor;

  private ServerBootstrap bootstrap;
  private EventLoopGroup worker = new NioEventLoopGroup();
  private EventLoopGroup boss = new NioEventLoopGroup();

  private int port;

  public void start(int port) {
    this.port = port;
    start();
  }

  @Override
  public void operationComplete(Future future) throws Exception {
    System.out.println("shutdown complete tcp server at port : " + port);
    stop();
  }

  @Override
  public void start() {
    bootstrap = new ServerBootstrap();
    bootstrap.group(boss, worker)
        .option(ChannelOption.SO_BACKLOG, 200)
        .option(ChannelOption.TCP_NODELAY, true)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .channel(NioServerSocketChannel.class)
        .childHandler(new RpcServerHandlerInitializer(new JavaServerHandler(processor)));
    try {
      ChannelFuture future = bootstrap.bind(port).sync();
      future.channel().closeFuture().addListener(this);
    } catch (InterruptedException e) {
    }
  }

  @Override
  public void setProcess(Processor processor) {
    this.processor = processor;
  }

  @Override
  public void stop() {
    worker.shutdownGracefully();
    boss.shutdownGracefully();
  }

  public void setProcessor(Processor<BinaryPacketRaw> processor) {
    this.processor = processor;
  }
}
