package com.eoren.echoattime.echoattime.broker;

import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import org.springframework.stereotype.Component;

@Component
public class MessagesProducer {

  private final MessagesQueue messagesQueue;

  public MessagesProducer(MessagesQueue messagesQueue) {
    this.messagesQueue = messagesQueue;
  }

  public void produce(TimedMessage timedMessage) throws InterruptedException {
    messagesQueue.getBlockingQueue().put(timedMessage);
  }
}
