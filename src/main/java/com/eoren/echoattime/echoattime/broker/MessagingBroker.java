package com.eoren.echoattime.echoattime.broker;

import com.eoren.echoattime.echoattime.common.DateUtil;
import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessagingBroker {

  private final MessagesConsumer messagesConsumer;
  private final MessagesProducer messagesProducer;

  public MessagingBroker(MessagesConsumer messagesConsumer, MessagesProducer messagesProducer) {
    this.messagesConsumer = messagesConsumer;
    this.messagesProducer = messagesProducer;
  }

  /*
  Waits one milli after last execution
   */
  @Scheduled(fixedDelay = 1)
  public void poll() {
    TimedMessage message = messagesConsumer.consume();
    if (message != null) {
      System.out.println(
          String.format("Message: \"%s\" Displayed on %s", message.getMessage(), DateUtil.formatDate(message.getTimeInMillisToEcho())));
    }
  }

  @Scheduled(fixedDelay = 1)
  public void push() {
    messagesProducer.checkForMessages();
  }
}
