package com.eoren.echoattime.echoattime.broker;

import com.eoren.echoattime.echoattime.common.DateUtil;
import com.eoren.echoattime.echoattime.redis.RedisAccessor;
import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MessagesProducer {

  private static final String WILD_CARD = "*";
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
  private void printMissOuts() {
    System.out.println("Printing Missed Messages");
    checkForStuckMessages();
  }

  public void checkForStuckMessages() {
    List<TimedMessage> allWaitingMessages = redisAccessor.getAllStuckMessages();
    produceAndDelete(allWaitingMessages);
  }

  public void checkForMessages() {
    List<TimedMessage> allWaitingMessages = redisAccessor.getAllMessageByDatePattern(createDatePattern());
    produceAndDelete(allWaitingMessages);
  }

  private void produceAndDelete(List<TimedMessage> allWaitingMessages) {
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

  private String createDatePattern() {
    return WILD_CARD + DateUtil.todayAsString() + WILD_CARD;
  }
}
