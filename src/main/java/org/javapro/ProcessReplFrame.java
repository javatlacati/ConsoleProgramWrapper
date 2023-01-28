package org.javapro;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 *
 * @author javatlacati
 */
public class ProcessReplFrame extends JFrame {

  public String processName;
  private Writer writer;
  private final CustomWindowCloseAdapter windowAdapter;

  /** Creates new form CmdFrame */
  public ProcessReplFrame(final String processName) {
    this.processName = processName;
    initComponents();
    windowAdapter = new CustomWindowCloseAdapter(writer);
    this.addWindowListener(windowAdapter);
    initializeProcessBuilder();
  }

  private void initializeProcessBuilder() {
    try {
      ProcessBuilder builder = new ProcessBuilder(processName);
      builder.redirectErrorStream(true);
      Process process = builder.start();
      windowAdapter.setProcess(process);
      // Obtener los flujos de entrada y salida del proceso
      InputStream is = process.getInputStream();
      setupReadingThread(is);
      OutputStream os = process.getOutputStream();
      this.writer = new BufferedWriter(new OutputStreamWriter(os));
    } catch (IOException ex) {
      Logger.getLogger(ProcessReplFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void setupReadingThread(InputStream is) {
    Thread readFromCmdThread =
        new Thread(
            () -> {
              try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                  textArea.append(line + "\n");
                  SwingUtilities.invokeLater(
                      () -> textArea.setCaretPosition(textArea.getText().length()));
                }
              } catch (IOException ioe) {
                Logger.getLogger(ProcessReplFrame.class.getName()).log(Level.SEVERE, null, ioe);
              }
            });
    // Crear un hilo para leer la salida del proceso y mostrarla en el JTextArea
    readFromCmdThread.start();
  }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JScrollPane scrlTerminal = new JScrollPane();
        textArea = new JTextArea();
        JPanel commandsPanel = new JPanel();
        JLabel lbl1 = new JLabel();
        txtInput = new JTextField();
        JButton btnSendCommand = new JButton();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(this.processName);
        setSize(new Dimension(800, 600));

        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setRows(5);
        scrlTerminal.setViewportView(textArea);

        getContentPane().add(scrlTerminal, BorderLayout.CENTER);

        commandsPanel.setLayout(new GridLayout(1, 0));

        lbl1.setText("Write your command here: >");
        commandsPanel.add(lbl1);

        txtInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtInputActionPerformed(evt);
            }
        });
        commandsPanel.add(txtInput);

        btnSendCommand.setText("Send command");
        btnSendCommand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSendCommandActionPerformed(evt);
            }
        });
        commandsPanel.add(btnSendCommand);

        getContentPane().add(commandsPanel, BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendCommandActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSendCommandActionPerformed
        sendCommandToProcess();
    }//GEN-LAST:event_btnSendCommandActionPerformed

    private void txtInputActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtInputActionPerformed
        sendCommandToProcess();
    }//GEN-LAST:event_txtInputActionPerformed

    private void sendCommandToProcess() {
        String line = txtInput.getText();
        textArea.append("> " + line + "\n\n");
        txtInput.setText("");
        try {
            writer.write(line + "\n");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ProcessReplFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                 InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProcessReplFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    // </editor-fold>
        String processPath = getProcessPath(args);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ProcessReplFrame cmdFrame = new ProcessReplFrame(processPath);
                cmdFrame.setVisible(true);
            }
        });
    }

  private static String getProcessPath(String[] args) {
    String processPath = "";
    if (args.length > 0) {
      if (null == args[0]) {
        System.err.println("unrecognized option");
        System.exit(1);
      } else
        switch (args[0]) {
          case "--help":
            System.out.println("Usage:\n --help show this help\n --f specify process path");
            System.exit(0);
          case "--f":
            processPath = args[1];
            break;
          default:
            System.err.println("unrecognized option");
            System.exit(1);
        }
    } else {
      int option =
          JOptionPane.showConfirmDialog(
              null, "is your program registered in PATH?", "", JOptionPane.YES_NO_OPTION);
      if (option == JOptionPane.YES_OPTION) {
        processPath =
            JOptionPane.showInputDialog(null, "write the name of your process", "cmd.exe");
      } else {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          processPath = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
          System.exit(2);
        }
      }
    }
    return processPath;
  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextArea textArea;
    private JTextField txtInput;
    // End of variables declaration//GEN-END:variables
}