package com.eoren.echoattime.echoattime.exception;

public class AppServerException extends RuntimeException {

  public AppServerException(String error) {
    super(error);
  }
  
  public AppServerException(String error, Throwable cause) {
    super(error, cause);
  }
}