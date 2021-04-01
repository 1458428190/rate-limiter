package org.gucha.ratelimiter.common.exception;

/**
 * The exception represents that the url is invalid.
 */
public class InvalidUrlException extends Exception {

  public InvalidUrlException(String message) {
    super(message);
  }

  public InvalidUrlException(String message, Throwable e) {
    super(message, e);
  }

}
