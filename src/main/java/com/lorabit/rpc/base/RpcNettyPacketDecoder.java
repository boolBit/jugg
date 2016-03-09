package com.lorabit.rpc.base;

import com.lorabit.rpc.exception.RpcException;
import com.lorabit.rpc.meta.BinaryPacketData;
import com.lorabit.rpc.meta.BinaryPacketRaw;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcNettyPacketDecoder {

  // check:
  // magic_code(2) + total(4) + checksum(8) + float(4) + flag(4) + uuid(8)
  static final int HEADER = 30;
  private static final int EMPTY_READ_WATERMARK = 50;

  private BinaryPacketRaw raw;
  private int empty_read = 0;
  private int suspect_loop = 0;
  private int state;
  protected int[] szBuf = new int[1];


  public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws RpcException {
    ByteBuffer buff;
    boolean flag = true;
    while (in.readableBytes() > 1 && flag) {
      if (raw == null) {
        raw = setUpRawPacket(in);
        if (raw != null) {
          state = 3;
        }
        if (suspect_loop > 0) {
          // caution: how to explain? brute cut off
          System.out.println("hit loop forever at setUpRawPacket");
          throw new RpcException("hit loop forever at setUpRawPacket");
        }
        if (empty_read > EMPTY_READ_WATERMARK) {
          System.out.println("hit empty read at setUpRawPacket");
          throw new RpcException("hit empty read at setUpRawPacket");
        }
        continue; // anyway check if has remaining
      }
      switch (state) {
        case 3: // config size
          flag = readSize(in, szBuf);
          if (flag) {
            raw.setSzConf(szBuf[0]);
          }
          break;
        case 4: // config
          if (raw.getConf() == null && raw.getSzConf() > 0) {
            raw.setConf(ByteBuffer.allocate(raw.getSzConf()));
          }
          buff = raw.getConf();
          flag = readBytes(in, raw.getSzConf(), buff);
          break;

        case 5: // DOMAIN_SIZE
          flag = readSize(in, szBuf);
          if (flag) {
            raw.setSzDomainName(szBuf[0]);
          }
          break;
        case 6: // DOMAIN
          if (raw.getDomainName() == null && raw.getSzDomainName() > 0) {
            raw.setDomainName(ByteBuffer.allocate(raw.getSzDomainName()));
          }
          buff = raw.getDomainName();
          flag = readBytes(in, raw.getSzDomainName(), buff);
          break;

        case 7: // METHOD_SIZE
          flag = readSize(in, szBuf);
          if (flag) {
            raw.setSzMethodName(szBuf[0]);
          }
          break;
        case 8: // METHOD
          if (raw.getMethodName() == null && raw.getSzMethodName() > 0) {
            raw.setMethodName(ByteBuffer.allocate(raw.getSzMethodName()));
          }
          buff = raw.getMethodName();
          flag = readBytes(in, raw.getSzMethodName(), buff);
          break;

        case 9: // PARAMETER_SIZE
          flag = readSize(in, szBuf);
          if (flag) {
            raw.setSzParameter(szBuf[0]);
          }
          break;
        case 10: // PARAMETER
          if (raw.getParameter() == null && raw.getSzParameter() > 0) {
            raw.setParameter(ByteBuffer.allocate(raw.getSzParameter()));
          }
          buff = raw.getParameter();
          flag = readBytes(in, raw.getSzParameter(), buff);
          break;

        case 11: // RETURN_SIZE
          flag = readSize(in, szBuf);
          if (flag) {
            raw.setSzRet(szBuf[0]);
          }
          break;
        case 12: // RETURN
          if (raw.getRet() == null && raw.getSzRet() > 0) {
            raw.setRet(ByteBuffer.allocate(raw.getSzRet()));
          }
          buff = raw.getRet();
          flag = readBytes(in, raw.getSzRet(), buff);
          break;

        case 13: // EXCEPTION_SIZE
          flag = readSize(in, szBuf);
          if (flag) {
            raw.setSzError(szBuf[0]);
            if (szBuf[0] == 0) {   // for there is no more any bytes coming...
              state = 15;
            }
          }
          break;
        case 14: // EXCEPTION
          if (raw.getError() == null && raw.getSzError() > 0) {
            raw.setError(ByteBuffer.allocate(raw.getSzError()));
          }
          buff = raw.getError();
          flag = readBytes(in, raw.getSzError(), buff);
          break;
        default:
          break;
      }
      if (state == 15) { // end
        out.add(raw);
        raw = null;
        state = 0;
      }
    }
  }

  private boolean readBytes(ByteBuf in, int size, ByteBuffer buff) {
    if (size == 0) {
      state++;
      return true;
    }
    int remain = size - buff.position();
    if (remain == 0) {
      state++;
      return true;
    }
    if (!in.isReadable()) {
      return false;
    }
    int readLen = remain <= in.readableBytes() ? remain : in.readableBytes();
    boolean ret = remain <= in.readableBytes();

    in.readBytes(buff.array(), buff.position(), readLen);
    buff.position(buff.position() + readLen);
    if (ret) {
      state++;
      buff.flip();
    }
    return ret;
  }

  private BinaryPacketRaw setUpRawPacket(ByteBuf in) {
    if (in.readableBytes() < HEADER) {
      empty_read++;
      return null;
    }

    int pos = in.readerIndex();
    byte[] magics = new byte[]{in.readByte(), in.readByte()};
    if (magics[0] == BinaryPacketData.MAGIC_CODE[0] && magics[1] == BinaryPacketData.MAGIC_CODE[1]) {
      in.markReaderIndex(); // mark start
      // magic code
      int total = in.readInt();
      long cksum = in.readLong();
      float version = in.readFloat();
      int flag = in.readInt();
      long uuid = in.readLong();
      int pos2 = in.readerIndex();
      Checksum ck = new Adler32();
      byte[] ttt = new byte[6];
      in.readerIndex(pos);
      in.readBytes(ttt);
      in.readerIndex(pos2);
      ck.update(ttt, 0, 6);
      if (cksum == ck.getValue()) {
        // bingo
        BinaryPacketRaw ret = new BinaryPacketRaw();
        ret.setTotal(total);
        ret.setVersion(version);
        ret.setFlag(flag);
        ret.setUuid(uuid);
        suspect_loop = 0;
        empty_read = 0;
        return ret;
      }
      // sticky! invalid packet
      // ignore this magic code
      in.resetReaderIndex(); // just skip, try later
    }
    suspect_loop++;

    return null;
  }

  protected boolean readSize(ByteBuf in, int[] size) {
    if (in.readableBytes() < 4) { // if long ready
      return false;
    }
    size[0] = in.readInt();
    state++;
    return true;
  }


}
