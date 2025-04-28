package CLass;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class tasks_list extends JFrame {
	public tasks_list(Project pro, ArrayList<Project> proj, FileManager file) {
		JFrame window = new JFrame();
		window.setTitle("Project Manager");
		window.setSize(1000, 700);
		window.setLayout(null);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel instruction = new JLabel("project title : " + pro.getTitle());
		instruction.setBounds(100, 20, 500, 30);
		window.add(instruction);

		JLabel instruction2 = new JLabel("project description : " + pro.getDescription());
		instruction2.setBounds(100, 40, 500, 30);
		window.add(instruction2);

		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0 || column == 1 || column == 4 || column == 5 || column == 2 || column == 3;

			}

		};
		


		JPanel graphRoot = new JPanel();
		graphRoot.setBounds(20, 400, 750, 50);
		graphRoot.setBackground(Color.GRAY);
		graphRoot.setLayout(null);

		model.addColumn("Task Name");
		model.addColumn("Task Description");
		model.addColumn("Start Date");
		model.addColumn("End Date");
		model.addColumn("Status");
		model.addColumn("Assigned To");
		model.addColumn("Duration");

		ArrayList<Task> taskList = (ArrayList<Task>) pro.get_tasks();
		for (Task task : taskList) {
			model.addRow(new Object[] { task.getTitle(), task.getDescription(), task.getStartDate(), task.getEndDate(),
					task.getStatus(), task.getAssignedTo(), task.getDuration() });
		}

		JTable table = new JTable(model);

		String[] statuses = { "Not started", "In progress", "Completed" };
		JComboBox<String> statusComboBox = new JComboBox<>(statuses);
		TableColumn statusColumn = table.getColumnModel().getColumn(4);
		statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox) {
			@Override
			public boolean stopCellEditing() {
				int row = table.getSelectedRow();

				if (row > 0) {
					String prevStatus = model.getValueAt(row - 1, 4).toString();

					if (!prevStatus.equals("Completed")) {
						JOptionPane.showMessageDialog(window,
								"You cannot change the status until the previous task is completed!", "Action Denied",
								JOptionPane.ERROR_MESSAGE);

						cancelCellEditing();
						return true;
					}
				}

				if (row < pro.get_tasks().size() - 1) {
					for (int i = row + 1; i < pro.get_tasks().size(); i++) {
						if (!model.getValueAt(i, 4).equals("Not started")) {
							JOptionPane.showMessageDialog(window,
									"You cannot change the status while future task is in progress or complete!",
									"Action Denied", JOptionPane.ERROR_MESSAGE);

							cancelCellEditing();
							return true;
						}
					}
				}

				return super.stopCellEditing();
			}
		});

		String[] teamMembers = { "Mouaad", "Abdou", "Mohamed", "Ahmed", "Redha" };
		JComboBox<String> assignedToComboBox = new JComboBox<>(teamMembers);
		TableColumn assignedToColumn = table.getColumnModel().getColumn(5);
		assignedToColumn.setCellEditor(new DefaultCellEditor(assignedToComboBox));

		statusColumn.setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				if (value != null) {
					switch (value.toString()) {
					case "Not started":
						label.setBackground(Color.RED);
						label.setForeground(Color.BLACK);
						break;
					case "In progress":
						label.setBackground(Color.YELLOW);
						label.setForeground(Color.BLACK);
						break;
					case "Completed":
						label.setBackground(Color.GREEN);
						label.setForeground(Color.BLACK);
						break;
					default:
						label.setBackground(Color.WHITE);
						label.setForeground(Color.BLACK);
						break;
					}
				}
				label.setOpaque(true);
				return label;
			}
		});

		model.addTableModelListener(new TableModelListener() {
			private boolean isAdjusting = false; // متغير لمنع التكرار اللانهائي

			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE && !isAdjusting) {
					int row = e.getFirstRow();
					int column = e.getColumn();

					Task task = pro.get_task(row);

					if (column == 4) {
						String newState = model.getValueAt(row, column).toString();
						task.setStatus(newState);

					}

					if (column == 0) {
						String newTitle = model.getValueAt(row, column).toString();
						task.setTitle(newTitle);
					}

					if (column == 1) {
						String newDesc = model.getValueAt(row, column).toString();
						task.setDescription(newDesc);
					}

					if (column == 5) {
						String newAssigned = model.getValueAt(row, column).toString();
						task.setAssignedTo(newAssigned);
					}

					if (row >= 0) {
						Task editedTask = taskList.get(row);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

						if (column == 2 || column == 3) { // تعديل Start Date أو End Date
							try {
								LocalDate startDate = LocalDate.parse(model.getValueAt(row, 2).toString(), formatter);
								LocalDate endDate = LocalDate.parse(model.getValueAt(row, 3).toString(), formatter);

								// التحقق من أن Start Date ≤ End Date
								if (startDate.isAfter(endDate)) {
									JOptionPane.showMessageDialog(window, "Error: Start Date must be before End Date!",
											"Invalid Date", JOptionPane.ERROR_MESSAGE);

									// تعطيل المستمع أثناء التعديل لتجنب التكرار
									isAdjusting = true;
									model.setValueAt(editedTask.getStartDate().format(formatter), row, 2);
									model.setValueAt(editedTask.getEndDate().format(formatter), row, 3);
									isAdjusting = false;

									return;
								}

								// التحقق من عدم تجاوز Start Date للمهمة التالية
								if (row < taskList.size() - 1) {
									LocalDate nextStartDate = taskList.get(row + 1).getStartDate();
									if (endDate.isAfter(nextStartDate)) {
										JOptionPane.showMessageDialog(window,
												"Error: End Date must not be after the next task's Start Date!",
												"Invalid Date", JOptionPane.ERROR_MESSAGE);

										isAdjusting = true;
										model.setValueAt(editedTask.getEndDate().format(formatter), row, 3);
										isAdjusting = false;

										return;
									}
								}

								// التحقق من عدم تجاوز End Date للمهمة السابقة
								if (row > 0) {
									LocalDate prevEndDate = taskList.get(row - 1).getEndDate();
									if (startDate.isBefore(prevEndDate)) {
										JOptionPane.showMessageDialog(window,
												"Error: Start Date must not be before the previous task's End Date!",
												"Invalid Date", JOptionPane.ERROR_MESSAGE);

										isAdjusting = true;
										model.setValueAt(editedTask.getStartDate().format(formatter), row, 2);
										isAdjusting = false;

										return;
									}
								}

								// تحديث القيم بعد التحقق
								editedTask.setStartDate(startDate);
								editedTask.setEndDate(endDate);

								model.setValueAt(editedTask.getDuration(), row, 6);

							} catch (DateTimeParseException ex) {
								JOptionPane.showMessageDialog(window, "Invalid date format! Use YYYY-MM-DD.",
										"Format Error", JOptionPane.ERROR_MESSAGE);
							}
						}

						file.saveProjects(proj);
						generateGraph(pro, graphRoot);
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(20, 80, 750, 300);
		window.add(scrollPane);

		generateGraph(pro, graphRoot);

		JButton button = new JButton("Create a Task");
		JButton button2 = new JButton("Delete a Task");
		JButton button3 = new JButton("Close");

		JPanel panel = new JPanel();
		panel.setLocation(175, 480);
		panel.setSize(500, 60);
		panel.setLayout(new FlowLayout());
		panel.add(button);
		panel.add(button2);
		panel.add(button3);
		window.add(panel);
		window.add(graphRoot);

		button.addActionListener(e -> new create_task(pro, model, file, proj, () -> generateGraph(pro, graphRoot)));
		button2.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();

			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(window, "Please select a task to delete!", "No Selection",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			int confirmation = JOptionPane.showConfirmDialog(window, "Are you sure you want to delete this task?",
					"Confirm Deletion", JOptionPane.YES_NO_OPTION);

			if (confirmation == JOptionPane.YES_OPTION) {
				// حذف المهمة من القائمة
				taskList.remove(selectedRow);
				// حذف المهمة من الجدول
				model.removeRow(selectedRow);
				// حفظ التغييرات
				file.saveProjects(proj);
			}
		});
		button3.addActionListener(e -> {
			file.saveProjects(proj);
			window.dispose();
		});

		window.setVisible(true);
	}
	
	private void generateGraph(Project pro, JPanel graphRoot) {
		graphRoot.removeAll();
		
		LocalDate start = pro.get_tasks().getFirst().getStartDate();
		LocalDate end = pro.get_tasks().getLast().getEndDate();

		int totalDays = durationToDays(Period.between(start, end));

		for (Task task : pro.get_tasks()) {
			int taskStart = durationToDays(Period.between(task.getStartDate(), start));

			int taskX = (int) (taskStart * 750.0 / totalDays);
			int taskW = (int) (task.getDuration() * 750.0 / totalDays);

			JLabel taskPan = new JLabel("  " + task.getTitle());
			taskPan.setOpaque(true);
			
			taskPan.setBounds(taskX, 10, taskW, 30);
			
			switch (task.getStatus()) {
			case "Not started":
				taskPan.setBackground(Color.RED);
				break;
			case "In progress":
				taskPan.setBackground(Color.YELLOW);
				break;
			case "Completed":
				taskPan.setBackground(Color.GREEN);
				break;
			default:
				taskPan.setBackground(Color.WHITE);
				break;
			}


			graphRoot.add(taskPan);
		}
		
		graphRoot.repaint();
	}
	
	public int durationToDays(Period dur) {
		return Math.abs(dur.getYears() * 365 + dur.getMonths() * 30 + dur.getDays());
	}
}
