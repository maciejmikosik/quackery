package org.quackery.report;

public class ReportException extends RuntimeException {
  public ReportException() {}

  public ReportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ReportException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReportException(String message) {
    super(message);
  }

  public ReportException(Throwable cause) {
    super(cause);
  }
}
