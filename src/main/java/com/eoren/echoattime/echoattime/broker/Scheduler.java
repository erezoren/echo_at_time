package com.eoren.echoattime.echoattime.broker;

import com.eoren.echoattime.echoattime.redis.RedisAccessor;
import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

  private final RedisAccessor redisAccessor;
  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

  public Scheduler(RedisAccessor redisAccessor) {
    this.redisAccessor = redisAccessor;
  }

  /*
  Runs when server starts
   */
  @PostConstruct
  private void printAllStuckMessages() {
   // System.out.println("Printing messages left in DB");
    List<TimedMessage> allWaitingMessages = redisAccessor.getAllStuckMessages();
    allWaitingMessages.stream().forEach(m -> {
      System.out.println(String.format("Message: %s  Displayed on %s", m.getMessage(), formatData(m)));
      redisAccessor.deleteTimesMessage(m);
    });
  }

  /*
  Waits one milli after last execution
   */
  @Scheduled(fixedDelay = 1)
  public void tryConsume() {
    //TEMP
    printAllStuckMessages();
  }

  private String formatData(TimedMessage timedMessage) {
    return dateFormat.format(new Date(timedMessage.getTimeInMillisToEcho()));
  }
}
