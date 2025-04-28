package CLass;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class delete_task extends JFrame {
    protected JTable table;

    public delete_task(Project pro, DefaultTableModel model, ArrayList<Project> proj, FileManager file) {
        setTitle("Project Manager");
        setSize(1000, 700);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel instruction = new JLabel("Select the task row you want to delete and click Enter:");
        instruction.setBounds(235, 20, 500, 30);
        add(instruction);

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(235, 60, 500, 300);
        add(scrollPane);

        JButton button = new JButton("Enter");
        button.setBounds(235, 380, 130, 60);
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this task?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        ArrayList<Task> taskList = pro.get_tasks();
                        taskList.remove(selectedRow);
                        model.removeRow(selectedRow);
                        pro.update_tasks(taskList);

                        for (int j = 0; j < proj.size(); j++) {
                            if (proj.get(j).equals(pro)) {
                                proj.set(j, pro);
                                break;
                            }
                        }

                        file.saveProjects(proj);
                        JOptionPane.showMessageDialog(null, "Task deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        setVisible(true);
    }
}
