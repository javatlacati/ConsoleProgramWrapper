package org.javapro;

/**
 * @author javatlacati
 */
public class ExitCode {

  private int code;
  private String message;
  private boolean isError;

  public ExitCode(int code, String message, boolean isError) {
    this.code = code;
    this.message = message;
    this.isError = isError;
  }

  public void performExit() {
    if (isError) {
      System.err.println(message);
    } else {
      System.out.println(message);
    }
    System.exit(code);
  }
}
