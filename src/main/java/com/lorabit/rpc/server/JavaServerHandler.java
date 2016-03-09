package com.lorabit.rpc.server;

import com.lorabit.rpc.exception.RpcException;
import com.lorabit.rpc.meta.BinaryPacketData;
import com.lorabit.rpc.meta.BinaryPacketHelper;
import com.lorabit.rpc.meta.BinaryPacketRaw;
import com.lorabit.rpc.processor.Processor;
import com.lorabit.rpc.processor.RpcContext;

import java.nio.ByteBuffer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lorabit
 * @since 16-3-8
 */
@ChannelHandler.Sharable
public class JavaServerHandler extends SimpleChannelInboundHandler<BinaryPacketRaw> {

  private Processor<BinaryPacketRaw> processor;

  public JavaServerHandler(Processor processor) {
    this.processor = processor;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, BinaryPacketRaw msg) throws RpcException {
    msg.ctx = ctx;
//    System.out.println("process msg " + msg);
    try {
      processor.process(new RpcContext(), msg);
    } catch (Throwable e) {
      System.out.println("channelRead error " +e.getStackTrace());
      msg.setError(ByteBuffer.wrap(e.getMessage().getBytes()));
      BinaryPacketData data ;
      try {
        data = BinaryPacketHelper.fromRawToData(msg);
      } catch (Exception e1) {
        e1.printStackTrace();
        data = new BinaryPacketData();
        data.ex = e;
      }
      ctx.write(data.getBytes()); // immediately error
    }
  }
}
