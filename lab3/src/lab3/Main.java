package lab3;

import java.util.concurrent.*;

public class Main {

    static int[] labs = {10, 20, 100};
    static String[] subjects = {"OOP", "HighMath", "Physics"};

    public static void main(String[] args) {

        BlockingQueue<Student> blockingQueue = new LinkedBlockingQueue<>(10);

        new Thread(new ProducerThread(blockingQueue)).start();

        for (int i = 0; i < 3; ++i) {
            new Thread(new ConsumerThread(subjects[i], blockingQueue)).start();
        }


    }

}
