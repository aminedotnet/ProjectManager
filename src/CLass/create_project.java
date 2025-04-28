package CLass;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class create_project extends JFrame {
    public create_project(ArrayList<Project> pro_list, JComboBox<String> choise, FileManager file) {
        JFrame window = new JFrame();
        window.setTitle("Project Manager");
        window.setSize(1000, 700);
        window.setLayout(null); 
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel instruction = new JLabel("Please Enter the Project title");
        instruction.setBounds(235, 100, 500, 30);
        instruction.setFont(new Font("Arial", Font.BOLD, 18));
        window.add(instruction);

        JTextArea title = new JTextArea();
        title.setBounds(235, 150, 500, 30);
        window.add(title);

        JLabel instruction2 = new JLabel("Please Enter the Project description");
        instruction2.setBounds(235, 200, 500, 30);
        instruction2.setFont(new Font("Arial", Font.BOLD, 18));
        window.add(instruction2);

        JTextArea description = new JTextArea();
        description.setBounds(235, 250, 500, 30);
        window.add(description);

        JButton addButton = new JButton("Add Project");
        addButton.setBounds(235, 300, 130, 60);
        window.add(addButton);

        JButton deleteButton = new JButton("Delete Project");
        deleteButton.setBounds(400, 300, 130, 60);
        window.add(deleteButton);

        
        window.setVisible(true);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titleText = title.getText().trim();
                String descriptionText = description.getText().trim();

                if (!titleText.isEmpty()) {
                    Project pro = new Project(titleText, descriptionText);
                    pro_list.add(pro);
                    choise.addItem(titleText);
                    file.saveProjects(pro_list);
                    JOptionPane.showMessageDialog(window, "Project added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(window, "Project title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = choise.getSelectedIndex();
                if (selectedIndex != -1) {
                    int confirm = JOptionPane.showConfirmDialog(window, "Are you sure you want to delete this project?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        pro_list.remove(selectedIndex);
                        choise.removeItemAt(selectedIndex);
                        file.saveProjects(pro_list);
                        JOptionPane.showMessageDialog(window, "Project deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(window, "Please select a project to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        window.repaint();
        window.revalidate();
    }
}
