package lab3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
        while (Main.i.get() != 100){
            Student student = new Student();
            student.setNumber(index++);
            System.out.println("студент " + student.getNumber() + "  с лабами по " + student.getSubjectName() + " заходит в кабинет");
            blockingQueue.put(student);
            Main.i.incrementAndGet();
        }
    }
}
