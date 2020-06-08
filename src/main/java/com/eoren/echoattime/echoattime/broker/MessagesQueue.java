package com.eoren.echoattime.echoattime.broker;

import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.springframework.stereotype.Component;

@Component
public class MessagesQueue {

  private BlockingQueue<TimedMessage> blockingQueue;

  public MessagesQueue() {
    this.blockingQueue = new LinkedBlockingDeque<>();
  }

  public BlockingQueue<TimedMessage> getBlockingQueue() {
    return blockingQueue;
  }

}
