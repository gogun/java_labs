package lab3;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;

public class ProducerThread implements Runnable {

    private int index = 0;
    private BlockingQueue<Student> blockingQueue;

    ProducerThread(BlockingQueue<Student> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            generateStudents();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void generateStudents() throws InterruptedException {
        while (true){
            Student student = new Student();
            student.setNumber(index++);
            System.out.println(Duration.between(Main.startTime, LocalTime.now()).toMinutes() + ":" +
                    Duration.between(Main.startTime, LocalTime.now()).toSeconds() +
                    " студент " + student.getNumber() + "  с лабами по " + student.getSubjectName() + " заходит в кабинет");
            blockingQueue.put(student);
        }
    }
}
