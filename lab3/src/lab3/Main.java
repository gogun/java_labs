package lab3;

import java.time.LocalTime;
import java.util.concurrent.*;

public class Main {

    static int[] labs = {10, 20, 100};
    static String[] subjects = {"OOP", "HighMath", "Physics"};

    static LocalTime startTime = LocalTime.now();

    public static void main(String[] args) {

        BlockingQueue<Student> blockingQueue = new LinkedBlockingQueue<>(10);

        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        service.execute(new ProducerThread(blockingQueue));

        for (int i = 0; i < 3; ++i) {
            service.execute(new ConsumerThread(subjects[i], blockingQueue));
        }




    }

}
