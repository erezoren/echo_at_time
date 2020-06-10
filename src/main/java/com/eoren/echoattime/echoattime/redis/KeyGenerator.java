package com.eoren.echoattime.echoattime.redis;

import com.eoren.echoattime.echoattime.common.DateUtil;
import java.util.UUID;

public class KeyGenerator {

  public String generate() {
    return DateUtil.todayAsString() + "_" + UUID.randomUUID();
  }
}
