package com.lorabit.net.nettyserver.factorial;

import java.math.BigInteger;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

/**
 * @author lorabit
 * @since 16-3-1
 */
public class BigIntegerDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if (in.readableBytes() < 5) {
      return;
    }

    in.markReaderIndex();
    short magicNum = in.readUnsignedByte();
    if (magicNum != 'F') {
      in.resetReaderIndex();
      throw new CorruptedFrameException("Invalid magic number: " + magicNum);
    }

    int dataLength = in.readInt();  //INT：4字节 表示的真正数据的长度
    if (in.readableBytes() < dataLength) {
      in.resetReaderIndex();
      return;
    }

    // Convert the received data into a new BigInteger.
    byte[] decoded = new byte[dataLength];
    in.readBytes(decoded);

    out.add(new BigInteger(decoded));

  }

}
