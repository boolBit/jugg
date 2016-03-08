package com.lorabit.net.gettingstart;

import com.lorabit.net.gettingstart.model.UnixTime;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author lorabit
 * @since 16-2-29
 */
public class TimeDecoder extends ByteToMessageDecoder {
  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (in.readableBytes() < 4) {
      return;
    }

    out.add(new UnixTime(in.readUnsignedInt()));
  }
}

//public class TimeDecoder extends ReplayingDecoder<Void> {
//  @Override
//  protected void decode(
//      ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
//    out.add(in.readBytes(4));
//  }
//}
