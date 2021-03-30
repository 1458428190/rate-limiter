package org.gucha.ratelimiter.common.exception;

/**
 * config resolve exception
 */
public class ConfigurationException extends RuntimeException {

  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(String message, Throwable e) {
    super(message, e);
  }

}

