import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L; // Serialization ID for versioning
    private String name;
    private int rollNumber;
    private String grade;

    // Constructor
    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    // String representation of the Student object
    @Override
    public String toString() {
        return "Roll Number: " + rollNumber + ", Name: " + name + ", Grade: " + grade;
    }
}
