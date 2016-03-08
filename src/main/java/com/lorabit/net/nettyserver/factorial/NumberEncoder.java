package com.lorabit.net.nettyserver.factorial;

import java.math.BigInteger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author lorabit
 * @since 16-3-1
 */
public class NumberEncoder extends MessageToByteEncoder<Number> {
  @Override
  protected void encode(ChannelHandlerContext ctx, Number msg, ByteBuf out) throws Exception {
    BigInteger num;
    if (msg instanceof BigInteger) {
      num = (BigInteger) msg;
    } else {
      num = new BigInteger(String.valueOf(msg));
    }

    byte[] numBytes = num.toByteArray();

    out.writeByte((byte)'F');
    out.writeInt(numBytes.length);
    out.writeBytes(numBytes);

  }
}
