package com.lorabit.rpc.router;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcPacketDecoder extends ByteToMessageDecoder {
  static AttributeKey<RpcNettyPacketDecoder> DECODER_NAME = AttributeKey.valueOf("DECODER");


  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (!in.isReadable()) {
      return;
    }

    Attribute<RpcNettyPacketDecoder> attr = ctx.attr(DECODER_NAME);
    if (attr.get() == null) {
      RpcNettyPacketDecoder decoder = new RpcNettyPacketDecoder();
      attr.compareAndSet(null, decoder);
    }
    RpcNettyPacketDecoder decoder = attr.get();

    decoder.decode(ctx, in, out);

  }
}
