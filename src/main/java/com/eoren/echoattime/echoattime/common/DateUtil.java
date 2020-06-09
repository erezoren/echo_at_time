package com.eoren.echoattime.echoattime.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

  private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

  public static String formatDate(long time) {
    return dateFormat.format(new Date(time));
  }
}
