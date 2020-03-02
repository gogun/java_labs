package lab3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerThread implements Runnable {

    private AtomicInteger index = new AtomicInteger(0);
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
        while (true) {
            Student student = new Student();
            student.number = index.getAndIncrement();
            System.out.println("студент " + student.number + "  с лабами по " + student.subjectName + " заходит в кабинет");
            blockingQueue.put(student);

        }
    }
}
