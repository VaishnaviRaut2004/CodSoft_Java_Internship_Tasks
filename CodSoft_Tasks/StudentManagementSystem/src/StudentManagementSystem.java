import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentManagementSystem {
    private ArrayList<Student> students;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadStudentsFromFile();
    }

    // Add a student
    public void addStudent(Student student) {
        for (Student existingStudent : students) {
            if (existingStudent.getRollNumber() == student.getRollNumber()) {
                JOptionPane.showMessageDialog(null, "Student with this roll number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        students.add(student);
        saveStudentsToFile();
    }

    // Remove a student
    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
        saveStudentsToFile();
    }

    // Search for a student
    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    // Display all students
    public void displayStudents(JTable table) {
        String[] columnNames = {"Name", "Roll Number", "Grade"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Student student : students) {
            Object[] row = {student.getName(), student.getRollNumber(), student.getGrade()};
            tableModel.addRow(row);
        }

        table.setModel(tableModel);
    }

    // Load students from file
    private void loadStudentsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            students = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Save students to file
    private void saveStudentsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GUI Interaction
    public static void main(String[] args) {
        StudentManagementSystem system = new StudentManagementSystem();
        JFrame frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 350); // Frame size adjusted
        frame.setResizable(false); // Fix the frame size

        // Create panels
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Compact layout for inputs
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Buttons centered in one row
        JPanel tablePanel = new JPanel(new BorderLayout()); // Panel for table

        // Input fields and labels
        JTextField nameField = new JTextField(15);
        JTextField rollField = new JTextField(15);
        JTextField gradeField = new JTextField(15);
        JLabel nameLabel = new JLabel("Name:");
        JLabel rollLabel = new JLabel("Roll No:");
        JLabel gradeLabel = new JLabel("Grade:");

        // Table for displaying students
        JTable studentTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setPreferredSize(new Dimension(550, 150)); // Adjusted size for table

        // Buttons
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");
        JButton searchButton = new JButton("Search");
        JButton displayButton = new JButton("Display");

        // Style components with pastel color scheme
        frame.getContentPane().setBackground(new Color(249, 235, 234)); // Background: #F9EBEA (Soft Pink)

        nameField.setBackground(new Color(253, 235, 208)); // Text Field Background: #FDEBD0 (Pastel Yellow)
        rollField.setBackground(new Color(253, 235, 208));
        gradeField.setBackground(new Color(253, 235, 208));

        Color buttonBackground = new Color(213, 166, 189); // Button Background: #D5A6BD (Soft Lavender)
        Color buttonTextColor = Color.WHITE; // Button Text: #FFFFFF (White)

        JButton[] buttons = {addButton, removeButton, searchButton, displayButton};
        for (JButton button : buttons) {
            button.setBackground(buttonBackground);
            button.setForeground(buttonTextColor);
            button.setPreferredSize(new Dimension(100, 30)); // Reduced button size for better proportion
        }

        // Add Student Action
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String rollStr = rollField.getText().trim();
            String grade = gradeField.getText().trim();

            if (name.isEmpty() || rollStr.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int rollNumber = Integer.parseInt(rollStr);
                system.addStudent(new Student(name, rollNumber, grade));
                JOptionPane.showMessageDialog(frame, "Student added successfully.");
                nameField.setText("");
                rollField.setText("");
                gradeField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Roll number must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Remove Student Action
        removeButton.addActionListener(e -> {
            try {
                int rollNumber = Integer.parseInt(rollField.getText().trim());
                system.removeStudent(rollNumber);
                JOptionPane.showMessageDialog(frame, "Student removed successfully.");
                rollField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Roll number must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Search Student Action
        searchButton.addActionListener(e -> {
            try {
                int rollNumber = Integer.parseInt(rollField.getText().trim());
                Student student = system.searchStudent(rollNumber);
                if (student != null) {
                    JOptionPane.showMessageDialog(frame, student.toString(), "Student Found", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                rollField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Roll number must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Display All Action
        displayButton.addActionListener(e -> system.displayStudents(studentTable));

        // Add components to panels
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(rollLabel);
        inputPanel.add(rollField);
        inputPanel.add(gradeLabel);
        inputPanel.add(gradeField);

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(displayButton);

        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Set up frame layout
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(tablePanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
