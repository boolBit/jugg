package com.lorabit.view.cloud;

import com.lorabit.cloud.RuntimeConfig;
import com.lorabit.cloud.status.RuntimeStatus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author lorabit
 * @since 16-2-19
 */
@Controller()
@RequestMapping("console")
public class ConsoleController {

  @Resource
  RuntimeConfig runtimeConfig;

  @RequestMapping("trigger/")
  @ResponseBody()
  public Map dumpTriggerStat() throws IOException {
    return RuntimeStatus.get().dumpTriggerStat();
  }

  @RequestMapping("task/")
  @ResponseBody()
  public Map dumpTaskInfo() throws IOException {
    return RuntimeStatus.get().dumpTaskInfos();
  }

  @RequestMapping("halt/")
  @ResponseBody
  public String halt() throws InterruptedException {
    runtimeConfig.halt();
    Thread.sleep(10 * 1000);
    Runtime.getRuntime().exit(0);
    return "success";
  }
}
