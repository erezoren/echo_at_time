package com.eoren.echoattime.echoattime.broker;

import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import com.eoren.echoattime.echoattime.server.AppServer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessagingBroker {

  private final MessagesConsumer messagesConsumer;
  private final MessagesProducer messagesProducer;
  private final AppServer appServer;

  public MessagingBroker(MessagesConsumer messagesConsumer, MessagesProducer messagesProducer,
      AppServer appServer) {
    this.messagesConsumer = messagesConsumer;
    this.messagesProducer = messagesProducer;
    this.appServer = appServer;
  }

  /*
   * Polls for messages every 100ms instead of 1ms to reduce CPU usage
   */
  @Scheduled(fixedDelay = 100)
  public void poll() {
    TimedMessage message = messagesConsumer.consume();
    if (message != null) {
      appServer.out(message);
    }
  }

  /*
   * Checks for new messages every 100ms instead of 1ms to reduce CPU usage
   */
  @Scheduled(fixedDelay = 100)
  public void push() {
    messagesProducer.checkForMessages();
  }
}
