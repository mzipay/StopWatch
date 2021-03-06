/*
 * $Id: ExportFrame.java 85 2008-04-07 17:33:51Z matt.zipay $
 */
package net.ninthtest.stopwatch;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import net.ninthtest.io.CSVWriter;
import net.ninthtest.stopwatch.model.DAOFactory;
import net.ninthtest.stopwatch.model.DataAccessException;
import net.ninthtest.stopwatch.model.Interruption;
import net.ninthtest.stopwatch.model.InterruptionDAO;
import net.ninthtest.stopwatch.model.Task;
import net.ninthtest.stopwatch.model.TaskDAO;

/**
 * This class defines the UI frame displayed when a user chooses to export the
 * <i>StopWatch</i> database.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 85 $
 */
public class ExportFrame extends javax.swing.JFrame {

    private static final String CLASSNAME = ExportFrame.class.getName();
    private static final Logger LOGGER = Logger.getLogger(ExportFrame.class.getPackage().getName());
    private Calendar defaultStartDate;
    private Calendar defaultEndDate;

    /**
     * Creates a new export frame.
     */
    public ExportFrame() {
        final String methodName = "<init>";
        LOGGER.entering(CLASSNAME, methodName);

        initializeDateRange();
        initComponents();

        setLocation(Math.max(0, Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width), 0);

        LOGGER.exiting(CLASSNAME, methodName);
    }

    private void initializeDateRange() {
        final String methodName = "initializeDateRange";
        LOGGER.entering(CLASSNAME, methodName);

        List allTasks = null;
        try {
            TaskDAO taskDAO = (TaskDAO) DAOFactory.getImplementation(TaskDAO.class);
            allTasks = taskDAO.findAllTasks();
        } catch (DataAccessException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error getting TaskDAO implementation", ex);
            allTasks = Collections.EMPTY_LIST;
        }

        if (allTasks.size() > 0) {
            defaultStartDate = Calendar.getInstance();
            Date startDate = ((Task) allTasks.get(0)).getStartTime();
            if (startDate != null) {
                defaultStartDate.setTime(startDate);
            }

            defaultEndDate = Calendar.getInstance();
            Date endDate = ((Task) allTasks.get(allTasks.size() - 1)).getStartTime();
            if (endDate != null) {
                defaultEndDate.setTime(endDate);
            } else {
                // results in correct date adjustment later
                defaultEndDate.setTime(defaultStartDate.getTime());
            }
        } else {
            defaultStartDate = Calendar.getInstance();

            defaultEndDate = Calendar.getInstance();
            defaultEndDate.setTime(defaultStartDate.getTime());
            defaultEndDate.add(Calendar.DATE, 1);
        }

        if (!defaultEndDate.after(defaultStartDate)) {
            defaultEndDate.add(Calendar.DATE, 1);
        }

        LOGGER.exiting(CLASSNAME, methodName);
    }

    private Calendar getDefaultStartDate() {
        final String methodName = "getDefaultStartDate";
        LOGGER.entering(CLASSNAME, methodName);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(defaultStartDate.getTime());

        LOGGER.exiting(CLASSNAME, methodName, startCalendar);
        return startCalendar;
    }

    private Calendar getDefaultEndDate() {
        final String methodName = "getDefaultEndDate";
        LOGGER.entering(CLASSNAME, methodName);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(defaultEndDate.getTime());

        LOGGER.exiting(CLASSNAME, methodName, endCalendar);
        return endCalendar;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateRangePanel = new javax.swing.JPanel();
        dateRangeInstructionLabel = new javax.swing.JLabel();
        dateRangeCheckBox = new javax.swing.JCheckBox();
        fromLabel = new javax.swing.JLabel();
        inclusiveLabel = new javax.swing.JLabel();
        toLabel = new javax.swing.JLabel();
        exclusiveLabel = new javax.swing.JLabel();
        startDateChooserCombo = new datechooser.beans.DateChooserCombo();
        endDateChooserCombo = new datechooser.beans.DateChooserCombo();
        includeInterruptionsCheckBox = new javax.swing.JCheckBox();
        exportButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Export database");
        setAlwaysOnTop(true);
        setResizable(false);

        dateRangePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Date range"));

        dateRangeInstructionLabel.setText("Leave the checkbox uncheckced to export the entire database.");

        dateRangeCheckBox.setText("Limit exported entries to a range of dates:");
        dateRangeCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dateRangeCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dateRangeCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dateRangeCheckBoxItemStateChanged(evt);
            }
        });

        fromLabel.setText("From");

        inclusiveLabel.setText("(inclusive)");

        toLabel.setText("To");

        exclusiveLabel.setText("(exclusive)");

        startDateChooserCombo.setCurrentView(new datechooser.view.appearance.AppearancesList("Swing",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    startDateChooserCombo.setNothingAllowed(false);
    startDateChooserCombo.setFormat(1);
    try {
        startDateChooserCombo.setDefaultPeriods(new datechooser.model.multiple.PeriodSet(new datechooser.model.multiple.Period(getDefaultStartDate(), getDefaultStartDate())));
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    startDateChooserCombo.setEnabled(false);
    startDateChooserCombo.setMinDate(new java.util.GregorianCalendar(1970, 0, 1));
    startDateChooserCombo.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    startDateChooserCombo.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            startDateChooserComboOnCommit(evt);
        }
    });

    endDateChooserCombo.setNothingAllowed(false);
    try {
        endDateChooserCombo.setDefaultPeriods(new datechooser.model.multiple.PeriodSet(new datechooser.model.multiple.Period(getDefaultEndDate(), getDefaultEndDate())));
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    endDateChooserCombo.setEnabled(false);
    endDateChooserCombo.setMinDate(new java.util.GregorianCalendar(1970, 0, 1));
    endDateChooserCombo.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    endDateChooserCombo.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            endDateChooserComboOnCommit(evt);
        }
    });

    javax.swing.GroupLayout dateRangePanelLayout = new javax.swing.GroupLayout(dateRangePanel);
    dateRangePanel.setLayout(dateRangePanelLayout);
    dateRangePanelLayout.setHorizontalGroup(
        dateRangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dateRangePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(dateRangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(dateRangeCheckBox)
                .addGroup(dateRangePanelLayout.createSequentialGroup()
                    .addGap(17, 17, 17)
                    .addGroup(dateRangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(fromLabel)
                        .addComponent(toLabel))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(dateRangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(endDateChooserCombo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startDateChooserCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                    .addGap(4, 4, 4)
                    .addGroup(dateRangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(exclusiveLabel)
                        .addComponent(inclusiveLabel)))
                .addComponent(dateRangeInstructionLabel))
            .addContainerGap())
    );

    dateRangePanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {endDateChooserCombo, startDateChooserCombo});

    dateRangePanelLayout.setVerticalGroup(
        dateRangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(dateRangePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(dateRangeInstructionLabel)
            .addGap(18, 18, 18)
            .addGroup(dateRangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(dateRangePanelLayout.createSequentialGroup()
                    .addComponent(dateRangeCheckBox)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(dateRangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(fromLabel)
                        .addComponent(startDateChooserCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(inclusiveLabel))
            .addGap(10, 10, 10)
            .addGroup(dateRangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(toLabel)
                .addComponent(exclusiveLabel)
                .addComponent(endDateChooserCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    includeInterruptionsCheckBox.setSelected(true);
    includeInterruptionsCheckBox.setText("Include interruptions (uncheck to export tasks only)");
    includeInterruptionsCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    includeInterruptionsCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

    exportButton.setText("Export to CSV");
    exportButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            exportButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(exportButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dateRangePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(includeInterruptionsCheckBox, javax.swing.GroupLayout.Alignment.LEADING))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(dateRangePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(includeInterruptionsCheckBox)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(exportButton)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents
    private File chooseFile(java.awt.Component parent) {
        final String methodName = "chooseFile";
        LOGGER.entering(CLASSNAME, methodName, parent);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export database to CSV");
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setDragEnabled(true);
        fileChooser.setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                return f.getPath().toLowerCase().endsWith(".csv");
            }

            public String getDescription() {
                return "CSV (Comma delimited) (*.csv)";
            }
        });
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setSelectedFile(new File("stopwatch.csv"));

        File selectedFile = null;

        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            LOGGER.logp(Level.INFO, CLASSNAME, methodName, "selected file {0}", selectedFile);
        }

        LOGGER.exiting(CLASSNAME, methodName, selectedFile);
        return selectedFile;
    }

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        final String methodName = "exportButtonActionPerformed";
        LOGGER.entering(CLASSNAME, methodName, evt);

        File file = chooseFile((java.awt.Component) evt.getSource());
        if (file != null) {
            if (file.exists()) {
                int answer = JOptionPane.showConfirmDialog(this, "Overwrite existing file \"" + file.getAbsolutePath() + "\"?", "Confirm overrwrite", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            LOGGER.logp(Level.INFO, CLASSNAME, methodName, "exporting to file {0}", file);

            try {
                TaskDAO taskDAO = (TaskDAO) DAOFactory.getImplementation(TaskDAO.class);
                InterruptionDAO intrDAO = (InterruptionDAO) DAOFactory.getImplementation(InterruptionDAO.class);

                List tasks = null;

                if (dateRangeCheckBox.isSelected()) {
                    Date startDate = startDateChooserCombo.getSelectedDate().getTime();
                    Date endDate = endDateChooserCombo.getSelectedDate().getTime();
                    LOGGER.logp(Level.INFO, CLASSNAME, methodName, "exporting tasks in range {0} - {1}", new Object[]{startDateChooserCombo.getSelectedDate().getTime(), endDateChooserCombo.getSelectedDate().getTime()});

                    tasks = taskDAO.findAllTasksInDateRange(startDate, endDate);
                } else {
                    LOGGER.logp(Level.INFO, CLASSNAME, methodName, "exporting all tasks");

                    tasks = taskDAO.findAllTasks();
                }

                List rows = new ArrayList();
                for (Iterator taskIter = tasks.iterator(); taskIter.hasNext();) {
                    Task task = (Task) taskIter.next();

                    Date taskStart = task.getStartTime();
                    Date taskEnd = task.getEndTime();
                    Long taskElapsed = null;
                    if ((taskStart != null) && (taskEnd != null)) {
                        taskElapsed = new Long(taskEnd.getTime() - taskStart.getTime());
                    }

                    List intrs = intrDAO.findInterruptionsForTask(task);

                    long totalIntrElapsed = 0;
                    List intrRows = new ArrayList();
                    for (Iterator intrIter = intrs.iterator(); intrIter.hasNext();) {
                        Interruption intr = (Interruption) intrIter.next();

                        Date intrStart = intr.getStartTime();
                        Date intrEnd = intr.getEndTime();
                        Long intrElapsed = null;
                        if ((intrStart != null) && (intrEnd != null)) {
                            intrElapsed = new Long(intrEnd.getTime() - intrStart.getTime());
                            totalIntrElapsed += intrElapsed.longValue();
                        }

                        intrRows.add(new Object[]{"Interruption", intr.getId(), intr.getRelatedTaskId(), intr.getDescription(), intrStart, intrEnd, intrElapsed, null});
                    }

                    Long taskNet = (taskElapsed != null) ? new Long(taskElapsed.longValue() - totalIntrElapsed) : null;

                    rows.add(new Object[]{"Task", task.getId(), null, task.getDescription(), taskStart, taskEnd, taskElapsed, taskNet});

                    if (includeInterruptionsCheckBox.isSelected()) {
                        rows.addAll(intrRows);
                    }
                }

                CSVWriter writer = new CSVWriter(new FileWriter(file));
                writer.registerFormat(Date.class, new SimpleDateFormat("M/d/yy h:m a"));

                writer.write(new String[]{"ENTRY", "ID", "RELATED ID", "DESCRIPTION", "START TIME", "END TIME", "ELAPSED TIME (H:M)", "NET TIME (H:M)"});

                for (Iterator rowIter = rows.iterator(); rowIter.hasNext();) {
                    Object[] row = (Object[]) rowIter.next();

                    if (row[6] != null) {
                        row[6] = formatHoursAndMinutes(((Long) row[6]).longValue());
                    }
                    if (row[7] != null) {
                        row[7] = formatHoursAndMinutes(((Long) row[7]).longValue());
                    }

                    writer.write(row);
                }

                writer.flush();
                writer.close();

                String message = new StringBuffer("Wrote file \"").append(file.getAbsolutePath()).append("\"\n").append(new Date(file.lastModified())).append(" (").append(file.length()).append(" bytes)").toString();
                JOptionPane.showMessageDialog(this, message, "Save successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "failed to write file", ex);
                JOptionPane.showMessageDialog(this, "The file could not be saved:\n\"" + ex.getMessage() + "\"", "Save failed", JOptionPane.ERROR_MESSAGE);
            }
        }

        LOGGER.exiting(CLASSNAME, methodName);
    }//GEN-LAST:event_exportButtonActionPerformed

    private String formatHoursAndMinutes(long milliseconds) {
        final String methodName = "formatHoursAndMinutes";
        LOGGER.entering(CLASSNAME, methodName, Long.valueOf(milliseconds));

        long hours = milliseconds / ClockTick.HOUR;
        long mins = (milliseconds % ClockTick.HOUR) / ClockTick.MINUTE;
        long secs = (milliseconds % ClockTick.MINUTE) / ClockTick.SECOND;

        if (secs >= 30) {
            mins++;
        }

        String hm = new StringBuffer().append(hours).append(":").append(mins < 10 ? "0" + mins : mins).toString();

        LOGGER.exiting(CLASSNAME, methodName, hm);
        return hm;
    }

    private void dateRangeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dateRangeCheckBoxItemStateChanged
        final String methodName = "dateRangeCheckBoxItemStateChanged";
        LOGGER.entering(CLASSNAME, methodName, evt);

        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            startDateChooserCombo.setEnabled(true);
            endDateChooserCombo.setEnabled(true);
        } else {
            startDateChooserCombo.setEnabled(false);
            endDateChooserCombo.setEnabled(false);
        }

        LOGGER.exiting(CLASSNAME, methodName);
    }//GEN-LAST:event_dateRangeCheckBoxItemStateChanged

    private void refreshDateRange() {
        final String methodName = "refreshDateRange";
        LOGGER.entering(CLASSNAME, methodName);

        Calendar selectedStart = startDateChooserCombo.getSelectedDate();

        if (!endDateChooserCombo.getSelectedDate().after(selectedStart)) {
            Calendar newEnd = Calendar.getInstance();
            newEnd.setTime(selectedStart.getTime());
            newEnd.add(Calendar.DATE, 1);

            endDateChooserCombo.setSelectedDate(newEnd);
        }

        LOGGER.exiting(CLASSNAME, methodName);
    }

    private void startDateChooserComboOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_startDateChooserComboOnCommit
        final String methodName = "startDateChooserComboOnCommit";
        LOGGER.entering(CLASSNAME, methodName, evt);

        refreshDateRange();

        LOGGER.exiting(CLASSNAME, methodName);
    }//GEN-LAST:event_startDateChooserComboOnCommit

    private void endDateChooserComboOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_endDateChooserComboOnCommit
        final String methodName = "endDateChooserComboOnCommit";
        LOGGER.entering(CLASSNAME, methodName, evt);

        refreshDateRange();

        LOGGER.exiting(CLASSNAME, methodName);
    }//GEN-LAST:event_endDateChooserComboOnCommit

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox dateRangeCheckBox;
    private javax.swing.JLabel dateRangeInstructionLabel;
    private javax.swing.JPanel dateRangePanel;
    private datechooser.beans.DateChooserCombo endDateChooserCombo;
    private javax.swing.JLabel exclusiveLabel;
    private javax.swing.JButton exportButton;
    private javax.swing.JLabel fromLabel;
    private javax.swing.JCheckBox includeInterruptionsCheckBox;
    private javax.swing.JLabel inclusiveLabel;
    private datechooser.beans.DateChooserCombo startDateChooserCombo;
    private javax.swing.JLabel toLabel;
    // End of variables declaration//GEN-END:variables
}
