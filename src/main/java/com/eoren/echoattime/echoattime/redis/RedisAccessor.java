package com.eoren.echoattime.echoattime.redis;

import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import java.util.List;

public interface RedisAccessor {

  void insertNewTimesMessage(TimedMessage timedMessage);

  List<TimedMessage> getAllStuckMessages();

  void deleteTimesMessage(TimedMessage timedMessage);

  List<TimedMessage> getAllMessageByDatePattern(String datePattern);
}
