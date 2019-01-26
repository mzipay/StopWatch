/*
 * $Id: TaskDescriptionDialog.java 84 2008-04-07 17:29:50Z matt.zipay $
 */
package net.ninthtest.stopwatch;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import net.ninthtest.stopwatch.model.DAOFactory;
import net.ninthtest.stopwatch.model.DataAccessException;
import net.ninthtest.stopwatch.model.Task;
import net.ninthtest.stopwatch.model.TaskDAO;

/**
 * This class defines a custom modal dialog containing an editable combo box.
 *
 * @author Matthew Zipay (ninthtest@gmail.com)
 * @version $Revision: 84 $
 */
public class TaskDescriptionDialog extends JDialog {

    private static final String CLASSNAME = TaskDescriptionDialog.class.getName();
    private static final Logger LOGGER = Logger.getLogger(TaskDescriptionDialog.class.getPackage().getName());
    private static final int PAD_VERT = 11;
    private static final int PAD_HORZ = 7;
    private String taskDescription;

    /**
     * Creates a new task description dialog.
     *
     * @param parent the component that owns this dialog
     */
    public TaskDescriptionDialog(JFrame parent) {
        super(parent, "Describe task", true);

        final String methodName = "<init>";
        LOGGER.entering(CLASSNAME, methodName, parent);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        JLabel instructionsLabel = new JLabel("Enter a short phrase that describes the task, or select a previous task description.");

        JComboBox taskDescriptionComboBox = new JComboBox(getTaskDescriptions());
        taskDescriptionComboBox.setMinimumSize(new Dimension(250, taskDescriptionComboBox.getHeight()));
        taskDescriptionComboBox.setEditable(true);
        taskDescriptionComboBox.setMaximumRowCount(17);
        taskDescriptionComboBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                taskDescriptionComboBoxItemStateChanged(e);
            }
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButtonActionPerformed(e);
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        });

        int dialogWidth = Math.max(instructionsLabel.getWidth(), taskDescriptionComboBox.getWidth()) + (2 * PAD_HORZ);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createVerticalStrut(PAD_VERT));

        Box instructionsLabelBox = Box.createHorizontalBox();
        instructionsLabelBox.add(Box.createHorizontalStrut(PAD_HORZ));
        instructionsLabelBox.add(instructionsLabel);
        instructionsLabelBox.add(Box.createHorizontalStrut(dialogWidth - instructionsLabel.getWidth() - PAD_HORZ));
        contentPane.add(instructionsLabelBox);

        contentPane.add(Box.createVerticalStrut(PAD_VERT));

        Box taskDescriptionComboBoxBox = Box.createHorizontalBox();
        taskDescriptionComboBoxBox.add(Box.createHorizontalStrut(PAD_HORZ));
        taskDescriptionComboBoxBox.add(taskDescriptionComboBox);
        taskDescriptionComboBoxBox.add(Box.createHorizontalStrut(dialogWidth - taskDescriptionComboBox.getWidth() - PAD_HORZ));
        contentPane.add(taskDescriptionComboBoxBox);

        contentPane.add(Box.createVerticalStrut(PAD_VERT));

        Box buttonsBox = Box.createHorizontalBox();
        int buttonHorzPad = (okButton.getWidth() + PAD_HORZ + cancelButton.getWidth()) / 2;
        buttonsBox.add(Box.createHorizontalStrut(buttonHorzPad));
        buttonsBox.add(okButton);
        buttonsBox.add(Box.createHorizontalStrut(PAD_HORZ));
        buttonsBox.add(cancelButton);
        buttonsBox.add(Box.createHorizontalStrut(buttonHorzPad));
        contentPane.add(buttonsBox);

        contentPane.add(Box.createVerticalStrut(PAD_VERT));

        pack();
        setLocation(Math.max(0, Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width), 0);

        LOGGER.exiting(CLASSNAME, methodName);
    }

    /**
     * Returns the selected task description.
     *
     * @return the task description
     */
    public String getTaskDescription() {
        return taskDescription;
    }

    private Vector getTaskDescriptions() {
        final String methodName = "getTaskDescriptions";
        LOGGER.entering(CLASSNAME, methodName);

        Set uniqueTaskDescriptions = new HashSet();
        uniqueTaskDescriptions.add("");

        try {
            TaskDAO taskDAO = (TaskDAO) DAOFactory.getImplementation(TaskDAO.class);

            List allTasks = taskDAO.findAllTasks();

            for (Iterator taskIter = allTasks.iterator(); taskIter.hasNext();) {
                Task task = (Task) taskIter.next();
                uniqueTaskDescriptions.add(task.getDescription());
            }
        } catch (DataAccessException ex) {
            LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "error getting TaskDAO implementation", ex);
        }

        Vector taskDescriptions = new Vector(uniqueTaskDescriptions);
        Collections.sort(taskDescriptions, new Comparator() {
            public int compare(Object o1, Object o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });

        LOGGER.exiting(CLASSNAME, methodName, taskDescriptions);
        return taskDescriptions;
    }

    private void taskDescriptionComboBoxItemStateChanged(ItemEvent e) {
        final String methodName = "taskDescriptionComboBoxItemStateChanged";
        LOGGER.entering(CLASSNAME, methodName, e);

        if (e.getStateChange() == ItemEvent.SELECTED) {
            taskDescription = e.getItem().toString();
        }

        LOGGER.exiting(CLASSNAME, methodName);
    }

    private void okButtonActionPerformed(ActionEvent e) {
        final String methodName = "okButtonActionPerformed";
        LOGGER.entering(CLASSNAME, methodName, e);

        if ((taskDescription != null) && !taskDescription.trim().equals("")) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(getContentPane(), "The task description must not be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        LOGGER.exiting(CLASSNAME, methodName);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        final String methodName = "cancelButtonActionPerformed";
        LOGGER.entering(CLASSNAME, methodName, e);

        taskDescription = null;
        dispose();

        LOGGER.exiting(CLASSNAME, methodName);
    }
}
