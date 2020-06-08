package com.eoren.echoattime.echoattime.server;

import com.eoren.echoattime.echoattime.Exception.AppServerException;
import com.eoren.echoattime.echoattime.redis.RedisAccessor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class AppServer {

  private ServerSocket serverSocket;
  private final RedisAccessor redisAccessor;

  public AppServer(int port, RedisAccessor redisAccessor) throws AppServerException {
    this.redisAccessor = redisAccessor;
    tryConnect(port);
  }

  private void tryConnect(int port) throws AppServerException {
    try {
      this.serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      throw new AppServerException(String.format("Could not connect to server due to: %s", e.getMessage()));
    }
  }

  public void serve() throws IOException {
    Socket clientSocket = serverSocket.accept();
    InputStream inputToServer = clientSocket.getInputStream();
    OutputStream outputFromServer = clientSocket.getOutputStream();
    Scanner scanner = new Scanner(inputToServer, "UTF-8");

    PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
    serverPrintOut.println("Please enter a message and future time in seconds to print it");
    serverPrintOut.println("Example: message:this is my message|time:15");
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      serverPrintOut.println(line);
      if (scanner.hasNextLine()) {
        if (!validateInput(line)) {
          serverPrintOut.println("Wrong cli format");
        }
      }
    }
  }

  public void test() {
    printInstructions();
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (!validateInput(line)) {
        printValidationEerror();
        printInstructions();
      } else {
        System.out.println("Persisting message Redis");
        redisAccessor.insertNewTimesMessage(new MessageConverter().apply(line));
      }
    }
  }

  private void printValidationEerror() {
    System.err.println("-----------------");
    System.err.println("Wrong cli format");
    System.err.println("-----------------");
  }

  private void printInstructions() {
    System.out.println("Please enter a message and future time in seconds to print it");
    System.out.println("Example: message:this is my message;time:15");
    System.out.println("\n");
  }

  private boolean validateInput(String line) {
    String lowered = line.toLowerCase();
    return lowered.trim().contains("message:") && lowered.contains("time:") && line
        .contains(";");
  }
}


