package com.lorabit.rpc.router;

import com.lorabit.rpc.base.LifeCycle;
import com.lorabit.rpc.client.JavaClientHandler;

import org.apache.commons.lang.math.NumberUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcIOSession implements LifeCycle {
  private String connHostPort;
  private long timeout;
  private Bootstrap bootstrap;
  private EventLoopGroup worker = new NioEventLoopGroup();
  private ChannelFuture cf;
  private Channel session;
  private boolean initialed = false;

  private AtomicLong uuid = new AtomicLong(0);

  public RpcIOSession(String connHostPort, long timeout) {
    this.connHostPort = connHostPort;
    this.timeout = timeout;
    this.bootstrap = new Bootstrap();
    bootstrap.group(worker);
    bootstrap.option(ChannelOption.TCP_NODELAY, true);
    bootstrap.channel(NioSocketChannel.class);
    bootstrap.handler(new RpcHandlerInitializer(new JavaClientHandler()));
    String[] host_port = connHostPort.split(":");
    cf = bootstrap.connect(
        new InetSocketAddress(host_port[0], NumberUtils.toInt(host_port[1])));
  }

  public void init() throws IOException {
    if (initialed) {
      return;
    }
    try {
      // ensure connect stable, should > 1s
      // so connect is very heavy action
      long t = timeout >= 2000 ? timeout : 2000;
      this.cf.await(t);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (!this.cf.isSuccess()) {
      this.close();
      throw new IOException("create connection to " + connHostPort + " failed!");
    }

    this.session = cf.channel();
    this.initialed = true;
  }

  public boolean isConnected() {
    return session.isActive();
  }

  public AtomicLong getUuid() {
    return uuid;
  }

  @Override
  public boolean isAlive() {
    return this.isConnected();
  }

  @Override
  public void close() throws IOException {
    if (session != null) {
      try {
        session.close().sync();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (cf != null) {
      cf.cancel(true);
    }
  }
}
