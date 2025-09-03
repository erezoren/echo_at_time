package com.eoren.echoattime.echoattime.server;

import com.eoren.echoattime.echoattime.redis.KeyGenerator;
import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import java.util.Date;
import java.util.function.Function;

public class MessageConverter implements Function<String, TimedMessage> {

  private final KeyGenerator keyGenerator;

  public MessageConverter(KeyGenerator keyGenerator) {
    this.keyGenerator = keyGenerator;
  }


  /*
   * Validates and parses the incoming message format: "message:content;time:delay_ms"
   * Adds basic input sanitization and validation
   */
  @Override
  public TimedMessage apply(String rawMessage) {
    if (rawMessage == null || rawMessage.trim().isEmpty()) {
      return null;
    }
    
    try {
      String[] parts = rawMessage.split(";");
      if (parts.length != 2) {
        return null;
      }
      
      String[] messagePart = parts[0].split(":", 2);
      String[] timePart = parts[1].split(":", 2);
      
      if (messagePart.length != 2 || timePart.length != 2) {
        return null;
      }
      
      if (!"message".equalsIgnoreCase(messagePart[0].trim()) || 
          !"time".equalsIgnoreCase(timePart[0].trim())) {
        return null;
      }
      
      String message = messagePart[1].trim();
      if (message.isEmpty() || message.length() > 1000) { // Reasonable message size limit
        return null;
      }
      
      long delay = Long.parseLong(timePart[1].trim());
      if (delay < 0 || delay > 86400000) { // Max 24 hours delay
        return null;
      }
      
      long timeToEcho = new Date().getTime() + delay;
      return new TimedMessage(keyGenerator.generate(), message, timeToEcho);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

}
