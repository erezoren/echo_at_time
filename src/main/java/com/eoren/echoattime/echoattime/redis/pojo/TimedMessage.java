package com.eoren.echoattime.echoattime.redis.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("TimesMessages")
public class TimedMessage {

  @Id
  private String id;
  private String message;
  private long timeInMillisToEcho;

  public TimedMessage(String id, String message, long timeInMillisToEcho) {
    this.id = id;
    this.message = message;
    this.timeInMillisToEcho = timeInMillisToEcho;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getTimeInMillisToEcho() {
    return timeInMillisToEcho;
  }

  public void setTimeInMillisToEcho(long timeInMillisToEcho) {
    this.timeInMillisToEcho = timeInMillisToEcho;
  }
}
