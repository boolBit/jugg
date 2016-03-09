package com.lorabit.rpc.processor;

import com.lorabit.rpc.exception.RpcException;
import com.lorabit.rpc.handler.RpcHandler;
import com.lorabit.rpc.meta.BinaryPacketData;
import com.lorabit.rpc.meta.BinaryPacketHelper;
import com.lorabit.rpc.meta.BinaryPacketRaw;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class ThreadPoolProcessor implements Processor<BinaryPacketRaw> {

  private RpcHandler handler;
  private ExecutorService executor = new ThreadPoolExecutor(
      5, 20, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20)
  );

  @Override
  public void process(RpcContext ctx, BinaryPacketRaw ret) throws RpcException {
    JobRunner job = new JobRunner(ctx, ret);
    executor.submit(job);
  }

  @Override
  public void setHandler(RpcHandler handler) {
    this.handler = handler;
  }

  class JobRunner implements Runnable {
    RpcContext ctx;
    BinaryPacketRaw raw;

    public JobRunner(RpcContext ctx, BinaryPacketRaw raw) {
      this.ctx = ctx;
      this.raw = raw;
    }

    @Override
    public void run() {
      BinaryPacketData data = null;
      try {
        data = BinaryPacketHelper.fromRawToData(raw);
        ctx.name = data.domain;
        ctx.method = data.method;
        ctx.params = data.param;
        handler.lookUp(ctx);
        handler.invoke(ctx);
        data.ret = ctx.ret;
      } catch (RpcException e) {
        if (data == null) {
          data = new BinaryPacketData();
        }
        int tries = 0;
        Throwable root = e;
        while (root.getCause() != null && tries++ < 5) root = root.getCause();
        data.ex = root;
      }

      if (raw.ctx != null) {
        raw.ctx.writeAndFlush(data.getBytes());
      }
    }
  }
}
