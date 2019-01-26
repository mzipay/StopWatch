/*
 * $Id: UsageFrame.java 84 2008-04-07 17:29:50Z matt.zipay $
 */

package net.ninthtest.stopwatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class defines the UI frame displayed when a user chooses to view the
 * <i>StopWatch</i> usage instructions.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 84 $
 */
public class UsageFrame extends javax.swing.JFrame {
    private static final String CLASSNAME = UsageFrame.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(UsageFrame.class.getPackage().getName());
    
    private static final String USAGE_MESSAGE;
    
    static {
        final String methodName = "<clinit>";
        LOGGER.entering(CLASSNAME, methodName);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("usage.txt")));
        
        StringBuffer buffer = new StringBuffer();
        try {
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line).append("\r\n");
                line = reader.readLine();
            }
            
            reader.close();
        } catch (IOException ex) {
            LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "error reading usage.txt", ex);
        }
        
        if (buffer.length() == 0) {
            buffer.append("Sorry, an error occurred while preparing the usage message.");
        }
        
        USAGE_MESSAGE = buffer.toString().trim();
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    /**
     * Creates a new usage frame.
     */
    public UsageFrame() {
        final String methodName = "<init>";
        LOGGER.entering(CLASSNAME, methodName);

        initComponents();

        usageTextArea.setText(USAGE_MESSAGE);
        usageTextArea.setCaretPosition(0);
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        usageScrollPane = new javax.swing.JScrollPane();
        usageTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("StopWatch usage");
        usageScrollPane.setAutoscrolls(true);
        usageScrollPane.setEnabled(false);
        usageScrollPane.setFont(new java.awt.Font("Times New Roman", 0, 12));
        usageTextArea.setColumns(20);
        usageTextArea.setEditable(false);
        usageTextArea.setFont(new java.awt.Font("Courier New", 0, 12));
        usageTextArea.setRows(5);
        usageTextArea.setMargin(new java.awt.Insets(5, 5, 5, 5));
        usageScrollPane.setViewportView(usageTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane usageScrollPane;
    private javax.swing.JTextArea usageTextArea;
    // End of variables declaration//GEN-END:variables
    
}
