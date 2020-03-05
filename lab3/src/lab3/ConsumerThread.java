package lab3;

import java.time.Duration;
import java.time.LocalTime;
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
            while (true) {
                Student student = blockingQueue.peek();
                if (student != null && student.getSubjectName().equals(subject)) {
                    System.out.println(Duration.between(Main.startTime, LocalTime.now()).toMinutes() + ":" +
                            Duration.between(Main.startTime, LocalTime.now()).toSeconds() + " студент " +
                            student.getNumber() + " с лабами по " + subject + " начинает сдавать лабы");
                    int labs = blockingQueue.take().getLabCount();

                    while (labs != 0) {
                        Thread.sleep(1000);
                        labs -= 5;
                        System.out.println(Duration.between(Main.startTime, LocalTime.now()).toMinutes() + ":" +
                                Duration.between(Main.startTime, LocalTime.now()).toSeconds() + " "
                                + subject + " студент "+ student.getNumber() +" сдал 5 лаб, осталось " + labs);

                    }

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
