package com.lorabit.net.nettyserver.telnet;

import com.google.common.base.Charsets;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Timer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author lorabit
 * @since 16-3-1
 */
public class ClientInitHandler extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    ch.pipeline().addLast(new StringDecoder(Charsets.UTF_8));
    ch.pipeline().addLast(new StringEncoder(Charsets.UTF_8));
    ch.pipeline().addLast(new ClientHanler());
//    ConcurrentLinkedQueue
  }

  public static void main(String[] args) throws IOException {
   Timer timer = new Timer();
    timer.schedule(null,1l);

    System.out.println(new ObjectMapper().writeValueAsString(null));
  }
}
