package com.lorabit.net.nettyserver.telnet;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author lorabit
 * @since 16-3-1
 */
public class TelnetServerInitializer extends ChannelInitializer<SocketChannel> {

//  private StringDecoder decoder = new StringDecoder();
//  private StringEncoder encoder = new StringEncoder();

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline cp = ch.pipeline();
//    cp.addLast("frameDecoder", new LineBasedFrameDecoder(80));
    cp.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    cp.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
    // Encoder
    cp.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
    cp.addLast(new TelnetServerHandler());
  }
}
