package com.eoren.echoattime.echoattime.server;

import com.eoren.echoattime.echoattime.Exception.AppServerException;
import com.eoren.echoattime.echoattime.common.DateUtil;
import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class SocketWrapper {

  private static final String LINE = "**************************************************************";
  private static final String TIMED_MESSAGE_HEADER = "~~<\uD83D\uDC9A>~~ TIMED MESSAGE ~~<\uD83D\uDC9A>~~";
  private Socket clientSocket;
  private InputStream inputToServer;
  private OutputStream outputFromServer;
  private Scanner scanner;
  private PrintWriter serverPrintOut;
  private final MessageConverter messageConverter;

  public SocketWrapper(ServerSocket serverSocket,
      MessageConverter messageConverter) throws AppServerException {
    this.messageConverter = messageConverter;
    tryConnect(serverSocket);
  }

  private synchronized void tryConnect(ServerSocket serverSocket) throws AppServerException {
    try {
      clientSocket = serverSocket.accept();
      inputToServer = clientSocket.getInputStream();
      outputFromServer = clientSocket.getOutputStream();
      scanner = new Scanner(inputToServer, "UTF-8");
      serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
    } catch (IOException e) {
      throw new AppServerException(String.format("Could not connect to server due to: %s", e.getMessage()));
    }
  }

  public boolean shouldWait() {
    return scanner.hasNextLine();
  }

  public TimedMessage waitForRawMessages() {
    String line = scanner.nextLine();
    return handleRawMessage(line);
  }

  private TimedMessage handleRawMessage(String line) {
    if (!validateInput(line)) {
      return null;
    } else {
      serverPrintOut.println(LINE);
      serverPrintOut.println(String.format("Persisting message Redis on %s", DateUtil.formatDate(new Date().getTime())));
      serverPrintOut.println(LINE);
      return messageConverter.apply(line);
    }
  }

  private boolean validateInput(String line) {
    String lowered = line.toLowerCase();
    return lowered.contains("message:") && lowered.contains("time:") && line
        .contains(";");
  }

  public void out(TimedMessage message) {
    serverPrintOut.println(TIMED_MESSAGE_HEADER);
    serverPrintOut.println(
        String.format("\033[1mMessage\033[0m: %s \033[1mDisplayed on\033[0m %s", message.getMessage(), DateUtil.formatDate(message.getTimeInMillisToEcho())));
    serverPrintOut.println(TIMED_MESSAGE_HEADER);
  }

  public void printValidationEerror() {
    serverPrintOut.println("!-!-!-!-!-!-!-!-!-!");
    serverPrintOut.println("WRONG CLI FORMAT");
    serverPrintOut.println("!-!-!-!-!-!-!-!-!-!");
  }

  public void printInstructions() {
    serverPrintOut.println(LINE);
    serverPrintOut.println("Please enter a message and future time in seconds to print it");
    serverPrintOut.println("Example: message:this is my message;time:15");
    serverPrintOut.println(LINE);
    serverPrintOut.println("\n");
  }
}
