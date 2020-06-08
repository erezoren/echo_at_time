package com.eoren.echoattime.echoattime.broker;

import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import org.springframework.stereotype.Component;

@Component
public class MessagesConsumer {

  private final MessagesQueue messagesQueue;
  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");


  public MessagesConsumer(MessagesQueue messagesQueue) {
    this.messagesQueue = messagesQueue;
  }

  public void consume() {
    BlockingQueue<TimedMessage> blockingQueue = messagesQueue.getBlockingQueue();
    while (!blockingQueue.isEmpty()) {
      TimedMessage timedMessage = blockingQueue.poll();
      System.out.println(String.format("Message: %s  Displayed on %s", timedMessage.getMessage(), formatData(timedMessage)));
    }
  }

  private String formatData(TimedMessage timedMessage) {
    return dateFormat.format(new Date(timedMessage.getTimeInMillisToEcho()));
  }
}
