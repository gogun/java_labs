package lab3;

public class Student {
    int labCount;
    String subjectName;
    int number;

    Student() {
        this.labCount = Main.labs[(int) (Math.random() * 3)];
        this.subjectName = Main.subjects[(int) (Math.random() * 3)];
    }
}
