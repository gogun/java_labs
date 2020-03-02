package lab3;

import java.util.concurrent.BlockingQueue;

public class ConsumerThread implements Runnable{

    private String subject;
    private BlockingQueue<Student> blockingQueue;

    ConsumerThread(String subject, BlockingQueue<Student> blockingQueue) {
        this.subject = subject;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            handleStudent();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleStudent() throws InterruptedException {
        while (true) {
            Student student = blockingQueue.peek();
            if (student != null && student.subjectName.equals(subject)) {
                System.out.println("стдент " + student.number + " с лабами по " + subject + " начинает сдавать лабы");
                int labs = blockingQueue.take().labCount;
                while (labs != 0) {
                    labs -= 5;
                    System.out.println(subject + " студент "+ student.number +" сдал 5 лаб, осталось " + labs);
                }

            }
        }
    }

}
