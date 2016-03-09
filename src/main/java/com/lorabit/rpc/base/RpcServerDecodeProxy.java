package com.lorabit.rpc.base;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @author lorabit
 * @since 16-3-9
 */
public class RpcServerDecodeProxy extends ByteToMessageDecoder {
  static AttributeKey<RpcNettyPacketDecoder> DECODER_NAME = AttributeKey.valueOf("SERVER_DECODER");


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
    System.out.println("server decode : " );
    decoder.decode(ctx, in, out);

  }
}
