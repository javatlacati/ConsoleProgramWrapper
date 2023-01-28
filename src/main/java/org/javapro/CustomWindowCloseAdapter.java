package org.javapro;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author javatlacati
 */
public class CustomWindowCloseAdapter extends WindowAdapter {
  private final Writer writer;
  private Process process;

  public CustomWindowCloseAdapter(Writer writer) {
    this.writer = writer;
  }

  public void setProcess(Process process) {
    this.process = process;
  }

  @Override
  public void windowClosing(WindowEvent e) {
    System.out.println("window closing");
    if (null != writer) {
      try {
        writer.close();
        System.out.println("closed cmd writer");
      } catch (IOException ex) {
        Logger.getLogger(CustomWindowCloseAdapter.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    if (null != process) {
      process.destroy();
    }
    System.exit(0);
  }
}
