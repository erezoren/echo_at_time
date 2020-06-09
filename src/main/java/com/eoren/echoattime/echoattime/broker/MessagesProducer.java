package com.eoren.echoattime.echoattime.broker;

import com.eoren.echoattime.echoattime.redis.RedisAccessor;
import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MessagesProducer {

  private final MessagesQueue messagesQueue;
  private final RedisAccessor redisAccessor;

  public MessagesProducer(MessagesQueue messagesQueue, RedisAccessor redisAccessor) {
    this.messagesQueue = messagesQueue;
    this.redisAccessor = redisAccessor;
  }

  /*
Runs when server starts
 */
  @PostConstruct
  public void checkForMessages() {
    List<TimedMessage> allWaitingMessages = redisAccessor.getAllStuckMessages();
    allWaitingMessages.stream().forEach(m -> {
      try {
        produce(m);
      } catch (InterruptedException e) {
        System.err.println(e.getMessage());
      }
      redisAccessor.deleteTimesMessage(m);
    });
  }

  public void produce(TimedMessage timedMessage) throws InterruptedException {
    messagesQueue.getBlockingQueue().put(timedMessage);
  }
}
