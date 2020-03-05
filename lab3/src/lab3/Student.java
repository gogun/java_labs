package lab3;

public class Student {
    private int labCount;
    private String subjectName;

    public int getLabCount() {
        return labCount;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getNumber() {
        return number;
    }

    private int number;

    Student() {
        this.labCount = Main.labs[(int) (Math.random() * 3)];
        this.subjectName = Main.subjects[(int) (Math.random() * 3)];
    }
}
