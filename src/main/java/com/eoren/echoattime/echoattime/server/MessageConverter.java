package com.eoren.echoattime.echoattime.server;

import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import java.util.Date;
import java.util.function.Function;

public class MessageConverter implements Function<String, TimedMessage> {

  /*
  Ideally should validate parsing or structure the incoming input
   */

  @Override
  public TimedMessage apply(String rawMessage) {
    String[] parts = rawMessage.split(";");
    String message = parts[0].split(":")[1];
    long timeToEcho = new Date().getTime() + Long.parseLong(parts[1].split(":")[1]);
    return new TimedMessage(message, timeToEcho);
  }
}
