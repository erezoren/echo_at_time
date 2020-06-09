package com.eoren.echoattime.echoattime.server;

import com.eoren.echoattime.echoattime.redis.RedisAccessor;
import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;

public class AppServer {

  private final RedisAccessor redisAccessor;
  private final SocketWrapper socketWrapper;

  public AppServer(RedisAccessor redisAccessor, SocketWrapper socketWrapper) {
    this.redisAccessor = redisAccessor;
    this.socketWrapper = socketWrapper;
  }

  public synchronized void serve() {
    socketWrapper.printInstructions();
    while (socketWrapper.shouldWait()) {
      TimedMessage timedMessage = socketWrapper.waitForRawMessages();
      if (timedMessage == null) {
        socketWrapper.printValidationEerror();
        socketWrapper.printInstructions();
      } else {
        redisAccessor.insertNewTimesMessage(timedMessage);
      }
    }
  }

  public void out(TimedMessage message) {
    socketWrapper.out(message);
  }
}


