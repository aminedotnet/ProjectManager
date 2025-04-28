package CLass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class create_task extends JFrame {
    public create_task(Project pro, DefaultTableModel model, FileManager file, ArrayList<Project> proj, Runnable onEnd) {
        JFrame window = new JFrame();
        window.setTitle("Create Task");
        window.setSize(400, 300);
        window.setLayout(new GridLayout(7, 2));
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameField = new JTextField();

        JLabel descLabel = new JLabel("Description:");
        JTextField descField = new JTextField();

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        JTextField startDateField = new JTextField();

        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        JTextField endDateField = new JTextField();

        JLabel assignedToLabel = new JLabel("Assigned To:");
        String[] teamMembers = {"Mouaad", "Abdou", "Mohamed", "Ahmed", "Redha"};
        JComboBox<String> assignedToComboBox = new JComboBox<>(teamMembers);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        window.add(nameLabel);
        window.add(nameField);
        window.add(descLabel);
        window.add(descField);
        window.add(startDateLabel);
        window.add(startDateField);
        window.add(endDateLabel);
        window.add(endDateField);
        window.add(assignedToLabel);
        window.add(assignedToComboBox);
        window.add(saveButton);
        window.add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                String title = nameField.getText().trim();
                String description = descField.getText().trim();
                LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
                LocalDate endDate = LocalDate.parse(endDateField.getText().trim());
                String status = "Not started" ; 
                String assignedTo = (String) assignedToComboBox.getSelectedItem();

                if (endDate.isBefore(startDate)) {
                    JOptionPane.showMessageDialog(window, "End date must be after start date!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!pro.get_tasks().isEmpty()) {
                    LocalDate lastEndDate = pro.get_tasks().get(pro.get_tasks().size() - 1).getEndDate();
                    if (startDate.isBefore(lastEndDate)) {
                        JOptionPane.showMessageDialog(window, "Start date must be after the last task's end date!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Task newTask = new Task(title, description, startDate, endDate, assignedTo, status);
                pro.addtask(newTask);

                model.addRow(new Object[]{title, description, startDate, endDate, status, assignedTo, newTask.getDuration()});

                file.saveProjects(proj);
                
                onEnd.run();
                
                window.dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(window, "Invalid date format! Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window, "Error creating task: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> window.dispose());

        window.setVisible(true);
    }
}
