package com.lorabit.rpc.client;

import com.lorabit.rpc.meta.BinaryPacketData;
import com.lorabit.rpc.meta.BinaryPacketHelper;
import com.lorabit.rpc.meta.BinaryPacketRaw;
import com.lorabit.rpc.meta.RpcRemoteLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class JavaClientHandler extends SimpleChannelInboundHandler<BinaryPacketRaw> {

  private static final Logger error = LoggerFactory.getLogger(JavaClientHandler.class);

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, BinaryPacketRaw msg) throws Exception {
    BinaryPacketData data ;
    data = BinaryPacketHelper.fromRawToData(msg);

    Attribute<RpcRemoteLatch> att = ctx.channel().attr(RpcRemoteLatch.LATCH_KEY);
    RpcRemoteLatch latch = att.get();
    if (latch.getUuid() != data.uuid) {
      // discard it
      error.error("wanted uuid => " + latch.getUuid() + ", ignore uuid => " + data.uuid);
      return;
    }
    if (data.ex != null) {
      latch.offerError(data.ex);
    } else {
      latch.offerResult(data.ret);
    }
  }
}
