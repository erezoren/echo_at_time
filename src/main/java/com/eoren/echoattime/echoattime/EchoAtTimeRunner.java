package com.eoren.echoattime.echoattime;

import com.eoren.echoattime.echoattime.server.AppServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EchoAtTimeRunner implements CommandLineRunner {

  private final AppServer appServer;

  public EchoAtTimeRunner(AppServer appServer) {
    this.appServer = appServer;
  }

  @Override
  public void run(String... args) throws Exception {
    appServer.test();
  }
}
