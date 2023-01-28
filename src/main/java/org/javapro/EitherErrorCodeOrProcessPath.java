package org.javapro;

import java.util.Optional;

/**
 * @author javatlacati
 */
public class EitherErrorCodeOrProcessPath {
  Optional<String> processPath = Optional.empty();
  Optional<ExitCode> exitCode = Optional.empty();

  public Optional<String> getProcessPath() {
    return processPath;
  }

  public void setProcessPath(Optional<String> processPath) {
    this.processPath = processPath;
  }

  public Optional<ExitCode> getExitCode() {
    return exitCode;
  }

  public void setExitCode(Optional<ExitCode> exitCode) {
    this.exitCode = exitCode;
  }

  public void exitOnErrorCode() {
    if (!processPath.isPresent()) {
      exitCode = Optional.of(new ExitCode(2, "no file selected", true));
    }
    exitCode.ifPresent(ExitCode::performExit);
  }
}
