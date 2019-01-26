/*
 * $Id: StopWatchUI.java 84 2008-04-07 17:29:50Z matt.zipay $
 */

package net.ninthtest.stopwatch;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import net.ninthtest.stopwatch.model.DAOFactory;
import net.ninthtest.stopwatch.model.DataAccessException;
import net.ninthtest.stopwatch.model.Interruption;
import net.ninthtest.stopwatch.model.InterruptionDAO;
import net.ninthtest.stopwatch.model.Task;
import net.ninthtest.stopwatch.model.TaskDAO;

/**
 * This class defines the user interface for the <i>StopWatch</i> application.
 *
 * @author  Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 84 $
 */
public class StopWatchUI extends javax.swing.JFrame {
    private static final String CLASSNAME = StopWatchUI.class.getName();
    
    private static final Logger LOGGER = Logger.getLogger(StopWatchUI.class.getPackage().getName());
    
    private Task currentTask;
    
    private Timer taskTimer;
    
    private Interruption currentInterruption;
    
    private Timer interruptionTimer;
    
    /**
     * Creates the <i>StopWatch</i> application's main form.
     */
    public StopWatchUI() {
        final String methodName = "<init>";
        LOGGER.entering(CLASSNAME, methodName);
        
        initComponents();
        setLocation(Math.max(0, Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width), 0);
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    private void startup() {
        final String methodName = "startup";
        LOGGER.entering(CLASSNAME, methodName);
        
        try {
            DAOFactory.initializeResources();
        } catch (DataAccessException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "failed to initialize data access resources", ex);
            JOptionPane.showMessageDialog(this, "Initialization failed!", "Error", JOptionPane.ERROR_MESSAGE);
            shutdown();
        }
        
        taskTimerLabel.setText("");
        taskTimerLabel.setFont(new java.awt.Font("Courier New", 1, 24));
        taskTimerLabel.setForeground(new java.awt.Color(153, 153, 153));
        taskTimerLabel.setText("00:00:00");
        taskTimerLabel.setToolTipText("(no current task)");
        
        fileMenu.setEnabled(true);
        beginTaskButton.setEnabled(true);
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    private void shutdown() {
        final String methodName = "shutdown";
        LOGGER.entering(CLASSNAME, methodName);
        
        // discard any current task and/or interruption
        currentTask = null;
        currentInterruption = null;
        
        // stop all timers
        if ((taskTimer != null) && taskTimer.isRunning()) {
            taskTimer.stop();
        }
        if ((interruptionTimer != null) && interruptionTimer.isRunning()) {
            interruptionTimer.stop();
        }
        
        // disable all interactive controls
        fileMenu.setEnabled(false);
        beginTaskButton.setEnabled(false);
        endTaskButton.setEnabled(false);
        interruptionToggleButton.setEnabled(false);
        
        interruptionTimerLabel.setForeground(new Color(153, 153, 153));
        interruptionTimerLabel.setText("00:00:00");
        
        // show a shutdown status message
        taskTimerLabel.setText("");
        taskTimerLabel.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11));
        taskTimerLabel.setForeground(java.awt.Color.BLACK);
        taskTimerLabel.setText("Shutting down...");
        taskTimerLabel.setToolTipText("Shutting down...");
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                int status = 0;
                try {
                    DAOFactory.releaseResources();
                } catch (DataAccessException ex) {
                    LOGGER.logp(Level.WARNING, CLASSNAME, methodName, "failed to release data access resources", ex);
                    status = 1;
                }
                System.exit(status);
            }
        });
        
        LOGGER.exiting(CLASSNAME, methodName);
    }
    
    private boolean initializeTask(Task task) {
        final String methodName = "initializeTask";
        LOGGER.entering(CLASSNAME, methodName, task);
        
        boolean successful = true;
        TaskDAO taskDAO = null;
        
        try {
            taskDAO = (TaskDAO) DAOFactory.getImplementation(TaskDAO.class);
        } catch (DataAccessException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error getting TaskDAO implementation", ex);
            successful =  false;
        }
        
        if (successful) {
            try {
                taskDAO.saveTask(task);
            } catch (DataAccessException ex) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error saving task", ex);
                successful =  false;
            }
        }
        
        LOGGER.exiting(CLASSNAME, methodName, Boolean.valueOf(successful));
        return successful;
    }
    
    private boolean finalizeTask(Task task) {
        final String methodName = "finalizeTask";
        LOGGER.entering(CLASSNAME, methodName, task);
        
        boolean successful = true;
        TaskDAO taskDAO = null;
        
        try {
            taskDAO = (TaskDAO) DAOFactory.getImplementation(TaskDAO.class);
        } catch (DataAccessException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error getting TaskDAO implementation", ex);
            successful =  false;
        }
        
        if (successful) {
            try {
                taskDAO.saveTask(task);
            } catch (DataAccessException ex) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error updating task", ex);
                successful =  false;
            }
        }
        
        LOGGER.exiting(CLASSNAME, methodName, Boolean.valueOf(successful));
        return successful;
    }
    
    private boolean initializeInterruption(Interruption interruption) {
        final String methodName = "initializeInterruption";
        LOGGER.entering(CLASSNAME, methodName, interruption);
        
        boolean successful = true;
        InterruptionDAO intrDAO = null;
        
        try {
            intrDAO = (InterruptionDAO) DAOFactory.getImplementation(InterruptionDAO.class);
        } catch (DataAccessException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error getting InterruptionDAO implementation", ex);
            successful = false;
        }
        
        if (successful) {
            try {
                intrDAO.saveInterruption(interruption);
            } catch (DataAccessException ex) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error saving interruption", ex);
                successful = false;
            }
        }
        
        LOGGER.exiting(CLASSNAME, methodName, Boolean.valueOf(successful));
        return successful;
    }
    
    private boolean finalizeInterruption(Interruption interruption) {
        final String methodName = "finalizeInterruption";
        LOGGER.entering(CLASSNAME, methodName, interruption);
        
        boolean successful = true;
        InterruptionDAO intrDAO = null;
        
        try {
            intrDAO = (InterruptionDAO) DAOFactory.getImplementation(InterruptionDAO.class);
        } catch (DataAccessException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error getting InterruptionDAO implementation", ex);
            successful = false;
        }
        
        if (successful) {
            try {
                intrDAO.saveInterruption(interruption);
            } catch (DataAccessException ex) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error updating interruption", ex);
                successful = false;
            }
        }
        
        LOGGER.exiting(CLASSNAME, methodName, Boolean.valueOf(successful));
        return successful;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        beginTaskButton = new javax.swing.JButton();
        endTaskButton = new javax.swing.JButton();
        taskTimerLabel = new javax.swing.JLabel();
        interruptionToggleButton = new javax.swing.JToggleButton();
        interruptionTimerLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        viewMenuItem = new javax.swing.JMenuItem();
        exportMenuItem = new javax.swing.JMenuItem();
        exitSep = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        usageMenuItem = new javax.swing.JMenuItem();
        aboutSep = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("StopWatch");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("StopWatchFrame");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        beginTaskButton.setMnemonic('B');
        beginTaskButton.setText("Begin task");
        beginTaskButton.setEnabled(false);
        beginTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beginTaskButtonActionPerformed(evt);
            }
        });

        endTaskButton.setMnemonic('E');
        endTaskButton.setText("End task");
        endTaskButton.setEnabled(false);
        endTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endTaskButtonActionPerformed(evt);
            }
        });

        taskTimerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        taskTimerLabel.setText("Initializing...");
        taskTimerLabel.setToolTipText("Initializing...");
        taskTimerLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        interruptionToggleButton.setMnemonic('I');
        interruptionToggleButton.setText("Interruption");
        interruptionToggleButton.setEnabled(false);
        interruptionToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interruptionToggleButtonActionPerformed(evt);
            }
        });

        interruptionTimerLabel.setFont(new java.awt.Font("Courier New", 1, 18));
        interruptionTimerLabel.setForeground(new java.awt.Color(153, 153, 153));
        interruptionTimerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        interruptionTimerLabel.setText("00:00:00");
        interruptionTimerLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");
        fileMenu.setEnabled(false);
        viewMenuItem.setMnemonic('V');
        viewMenuItem.setText("View database...");
        viewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(viewMenuItem);

        exportMenuItem.setMnemonic('E');
        exportMenuItem.setText("Export database...");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exportMenuItem);

        fileMenu.add(exitSep);

        exitMenuItem.setMnemonic('X');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");
        usageMenuItem.setMnemonic('U');
        usageMenuItem.setText("Using StopWatch");
        usageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usageMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(usageMenuItem);

        helpMenu.add(aboutSep);

        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(interruptionToggleButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(interruptionTimerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(beginTaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(taskTimerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(endTaskButton, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {interruptionTimerLabel, interruptionToggleButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(beginTaskButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taskTimerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(endTaskButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(interruptionTimerLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(interruptionToggleButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {interruptionTimerLabel, interruptionToggleButton});

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuItemActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                (new ExportFrame()).setVisible(true);
            }
        });
    }//GEN-LAST:event_exportMenuItemActionPerformed
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            DAOFactory.releaseResources();
        } catch (DataAccessException ex) {
            LOGGER.log(Level.WARNING, "failed to release data access resources", ex);
        }
    }//GEN-LAST:event_formWindowClosing
    
    private void viewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewMenuItemActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                (new ViewFrame()).setVisible(true);
            }
        });
    }//GEN-LAST:event_viewMenuItemActionPerformed
    
    private void interruptionToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interruptionToggleButtonActionPerformed
        final String methodName = "interruptionToggleButtonActionPerformed";
        LOGGER.entering(CLASSNAME, methodName, evt);
        
        if (interruptionToggleButton.isSelected()) {
            currentInterruption = new Interruption(currentTask);
            if (!initializeInterruption(currentInterruption)) {
                JOptionPane.showMessageDialog(this, "The interruption could not be initialized!", "Error", JOptionPane.ERROR_MESSAGE);
                currentInterruption = null;
                return;
            }
            
            endTaskButton.setEnabled(false);
            
            taskTimerLabel.setForeground(Color.YELLOW);
            interruptionTimerLabel.setForeground(Color.RED);
            
            interruptionTimer = new Timer(1000, new ClockTick() {
                public void updateClock(String clockFace) {
                    interruptionTimerLabel.setText(clockFace);
                }
            });
            currentInterruption.begin();
            interruptionTimer.start();
        } else {
            String interruptionDesc = "";
            while (interruptionDesc.trim().equals("")) {
                interruptionDesc = JOptionPane.showInputDialog(this, "Enter a short phrase that describes the interruption:", "Describe interruption", JOptionPane.QUESTION_MESSAGE);
                if (interruptionDesc == null) {
                    // user clicked "Cancel" - retoggle the "Interruption" button and continue timing
                    interruptionToggleButton.setSelected(true);
                    return;
                }
            }
            
            interruptionTimer.stop();
            currentInterruption.end();
            
            currentInterruption.setDescription(interruptionDesc);
            if (!finalizeInterruption(currentInterruption)) {
                JOptionPane.showMessageDialog(this, "The interruption could not be finalized!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            interruptionTimerLabel.setForeground(new Color(153, 153, 153));
            interruptionTimerLabel.setText("00:00:00");
            
            currentInterruption = null;
            endTaskButton.setEnabled(true);
            
            taskTimerLabel.setForeground(Color.GREEN);
        }
        
        LOGGER.exiting(CLASSNAME, methodName);
    }//GEN-LAST:event_interruptionToggleButtonActionPerformed
    
    private void endTaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endTaskButtonActionPerformed
        final String methodName = "endTaskButtonActionPerformed";
        LOGGER.entering(CLASSNAME, methodName, evt);
        
        taskTimer.stop();
        currentTask.end();
        
        interruptionToggleButton.setEnabled(false);
        endTaskButton.setEnabled(false);
        
        taskTimerLabel.setForeground(new Color(153, 153, 153));
        taskTimerLabel.setText("00:00:00");
        taskTimerLabel.setToolTipText("(no current task)");
        
        if (!finalizeTask(currentTask)) {
            JOptionPane.showMessageDialog(this, "The task could not be finalized!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        currentTask = null;
        beginTaskButton.setEnabled(true);
        
        LOGGER.exiting(CLASSNAME, methodName);
    }//GEN-LAST:event_endTaskButtonActionPerformed
    
    private void beginTaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beginTaskButtonActionPerformed
        final String methodName = "beginTaskButtonActionPerformed";
        LOGGER.entering(CLASSNAME, methodName, evt);
        
        TaskDescriptionDialog taskDescriptionDialog = new TaskDescriptionDialog(this);
        taskDescriptionDialog.setVisible(true);
        
        String taskDesc =  taskDescriptionDialog.getTaskDescription();
        if ((taskDesc == null) || (taskDesc.trim().equals(""))) {
            // do not begin a new task
            return;
        }
        
        currentTask = new Task(taskDesc);
        if (!initializeTask(currentTask)) {
            JOptionPane.showMessageDialog(this, "The task could not be initialized!", "Error", JOptionPane.ERROR_MESSAGE);
            currentTask = null;
            return;
        }
        
        beginTaskButton.setEnabled(false);
        taskTimerLabel.setForeground(Color.GREEN);
        taskTimerLabel.setToolTipText(taskDesc);
        
        taskTimer = new Timer(1000, new ClockTick() {
            public void updateClock(String clockFace) {
                taskTimerLabel.setText(clockFace);
            }
        });
        currentTask.begin();
        taskTimer.start();
        
        endTaskButton.setEnabled(true);
        interruptionToggleButton.setEnabled(true);
        
        LOGGER.exiting(CLASSNAME, methodName);
    }//GEN-LAST:event_beginTaskButtonActionPerformed
    
    private void usageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usageMenuItemActionPerformed
        final StopWatchUI parent = this;
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UsageFrame usageFrame = new UsageFrame();
                usageFrame.setLocation(Math.max(0, Toolkit.getDefaultToolkit().getScreenSize().width - usageFrame.getSize().width - parent.getSize().width), 0);
                usageFrame.setVisible(true);
            }
        });
    }//GEN-LAST:event_usageMenuItemActionPerformed
    
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        JOptionPane.showMessageDialog(this, "StopWatch time tracking utility\nVersion 1.0\nby Matthew Zipay (ninthtest@gmail.com)\n", "About StopWatch", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItemActionPerformed
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        LOGGER.logp(Level.FINE, CLASSNAME, "exitMenuItemActionPerformed", "File/Exit chosen; exiting");
        
        if (currentTask != null) {
            int answer = JOptionPane.showConfirmDialog(this, "The currently running task will be discarded!\nDo you still want to exit?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (answer == JOptionPane.NO_OPTION) {
                return;
            }
        }
        
        shutdown();
    }//GEN-LAST:event_exitMenuItemActionPerformed
    
    /**
     * Executes the <i>StopWatch</i> application.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // default look and feel will be used
            LOGGER.logp(Level.INFO, CLASSNAME, "main", "failed to set system look and feel", ex);
        }
        
        try {
            LogManager.getLogManager().readConfiguration(Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.properties"));
        } catch (Exception ex) {
            // default logging settings will be used
            LOGGER.logp(Level.WARNING, CLASSNAME, "main", "failed to read StopWatch logging settings", ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final StopWatchUI stopWatch = new StopWatchUI();
                stopWatch.setVisible(true);
                
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        stopWatch.startup();
                    }
                });
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JSeparator aboutSep;
    private javax.swing.JButton beginTaskButton;
    private javax.swing.JButton endTaskButton;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JSeparator exitSep;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel interruptionTimerLabel;
    private javax.swing.JToggleButton interruptionToggleButton;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel taskTimerLabel;
    private javax.swing.JMenuItem usageMenuItem;
    private javax.swing.JMenuItem viewMenuItem;
    // End of variables declaration//GEN-END:variables
    
}
