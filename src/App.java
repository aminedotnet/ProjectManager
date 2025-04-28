import javax.swing.*;
import CLass.FileManager;
import CLass.Project;
import CLass.create_project;
import CLass.tasks_list;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        ArrayList<Project> project_list = new ArrayList<>();
        FileManager file = new FileManager("file1.txt");

        
        ArrayList<Project> loadedProjects = file.loadProjects();
        if (!file.isFileEmpty()) {
            project_list.addAll(loadedProjects);
        }

        JFrame window = new JFrame("Project Manager");
        window.setSize(1000, 700);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel instruction = new JLabel("Please select the project you want:");
        instruction.setBounds(235, 100, 500, 30);
        instruction.setFont(new Font("Arial", Font.BOLD, 18));
        window.add(instruction);

        JComboBox<String> choice = new JComboBox<>();
        for (Project project : project_list) {
            choice.addItem(project.getTitle());
        }
        choice.setBounds(235, 150, 300, 30);
        window.add(choice);

        JButton enterButton = new JButton("Enter");
        JButton createButton = new JButton("Create a new project");
        JButton deleteButton = new JButton("Delete Project");

        enterButton.setSize(130, 60);
        createButton.setSize(130, 60);
        deleteButton.setSize(130, 60);

        JPanel panel = new JPanel();
        panel.setBounds(175, 200, 500, 60);
        panel.setLayout(new FlowLayout());
        panel.add(enterButton);
        panel.add(createButton);
        panel.add(deleteButton);
        window.add(panel);

        createButton.addActionListener(e -> new create_project(project_list, choice, file));

        enterButton.addActionListener(e -> {
            if (project_list.isEmpty()) {
                JOptionPane.showMessageDialog(window, "Options are empty. Please create a new project first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String selectedProjectName = (String) choice.getSelectedItem();
            if (selectedProjectName == null) {
                JOptionPane.showMessageDialog(window, "Please select a project.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Project selectedProject = null;
            for (Project project : project_list) {
                if (project.getTitle().equals(selectedProjectName)) {
                    selectedProject = project;
                    break;
                }
            }

            if (selectedProject != null) {
                new tasks_list(selectedProject, project_list, file);
            } else {
                JOptionPane.showMessageDialog(window, "Project not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedIndex = choice.getSelectedIndex();
            if (selectedIndex != -1) {
                int confirm = JOptionPane.showConfirmDialog(window, "Are you sure you want to delete this project?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    project_list.remove(selectedIndex);
                    choice.removeItemAt(selectedIndex);
                    file.saveProjects(project_list);
                    JOptionPane.showMessageDialog(window, "Project deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(window, "Please select a project to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        window.setVisible(true);
    }
}
