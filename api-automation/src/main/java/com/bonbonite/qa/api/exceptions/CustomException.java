package com.bonbonite.qa.api.exceptions;

/**
 * Framework specific exception.
 *
 * <p>It separates automation infrastructure failures — missing configuration, absent
 * data, unexpected service contract — from functional failures of the product under
 * test, which are reported through assertions. Messages are written in Spanish
 * because they end up in the execution report.</p>
 */
public class CustomException extends RuntimeException {

  /**
   * Creates the exception with a descriptive message.
   *
   * @param message detail of what caused the failure
   */
  public CustomException(String message) {
    super(message);
  }

  /**
   * Creates the exception keeping the original cause.
   *
   * @param message detail of what caused the failure
   * @param cause   exception that originated the failure
   */
  public CustomException(String message, Throwable cause) {
    super(message, cause);
  }
}
