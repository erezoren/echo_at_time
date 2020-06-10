package com.eoren.echoattime.echoattime.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

  private final static DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
  private final static DateFormat todayFormat = new SimpleDateFormat("dd_MMM_yyyy");

  public static String formatDate(long time) {
    return dateFormat.format(new Date(time));
  }

  public static String todayAsString() {
    return todayFormat.format(System.currentTimeMillis());
  }
}
