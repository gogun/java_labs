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
            while (true) {
                if (Main.i.get() == 100 && blockingQueue.isEmpty())
                    return;
                Thread.sleep(1000);
                Student student = blockingQueue.peek();
                if (student != null && student.getSubjectName().equals(subject)) {
                    System.out.println("студент " + student.getNumber() + " с лабами по " + subject + " начинает сдавать лабы");
                    int labs = blockingQueue.take().getLabCount();
                    while (labs != 0) {
                        labs -= 5;
                        System.out.println(subject + " студент "+ student.getNumber() +" сдал 5 лаб, осталось " + labs);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
