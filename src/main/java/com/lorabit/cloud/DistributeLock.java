package com.lorabit.cloud;

import com.lorabit.memcache.ICatService;
import com.lorabit.util.CtxHolder;

import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * @author lorabit
 * @since 16-2-19
 *
 * this use memcache not zk
 */
public class DistributeLock {

  private final static Logger log = Logger.getLogger(DistributeLock.class);

  public static void acquireAndExecute(
      String biz,
      int expire,
      Callable<Void> exe,
      int retryNum) throws Exception {
    int n = retryNum;
    ICatService catService ;
    boolean ok = false;
    while (n-- > 0) {
      try {
        catService = (ICatService) CtxHolder.getBean("catService");
        ok = catService.acquireLock(biz, expire);
        break;
      } catch (Exception e) {
        log.error("acquireAndExecute_failed_and_will_try_again:" + n);
      }
    }

    if (ok) {
      exe.call();
    }
  }

  public static void tryAcquireAndExecute(
      String biz,
      int expire,
      int timeout,
      boolean doExe,
      Callable<Void> exe) throws Exception {

    ICatService catService ;
    boolean ok = false;
    try {
      catService = (ICatService) CtxHolder.getBean("catService");
      ok = catService.acquireLock(biz, expire);
      if (!ok) {
        Thread.sleep(timeout);
        ok = catService.acquireLock(biz, expire);
      }
    } catch (Exception e) {
      log.error("tryAcquireAndExecute_failed:");
    }

    if (ok || doExe) {
      exe.call();
    }
  }


  public static void releaseForcibly(String biz) {
    try {
      ICatService catService = (ICatService) CtxHolder.getBean("catService");
      catService.releaseLock(biz);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
