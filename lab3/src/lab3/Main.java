package lab3;

import java.time.LocalTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static int[] labs = {10, 20, 100};
    static String[] subjects = {"OOP", "HighMath", "Physics"};
    static AtomicInteger i = new AtomicInteger(0);
    static LocalTime startTime = LocalTime.now();

    public static void main(String[] args) {

        BlockingQueue<Student> blockingQueue = new LinkedBlockingQueue<>(10);

        ExecutorService serv = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        serv.execute(new ProducerThread(blockingQueue));



        for (int i = 0; i < 3; ++i) {
            serv.execute(new ConsumerThread(subjects[i], blockingQueue));
        }




    }

}
