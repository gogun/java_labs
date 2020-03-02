package lab3;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static int[] labs = {10, 20, 100};
    static String[] subjects = {"OOP", "HighMath", "Physics"};

    private static class Student {
        int labCount;
        String subjectName;
        int number;

        Student() {
            this.labCount = labs[(int) (Math.random() * 3)];
            this.subjectName = subjects[(int) (Math.random() * 3)];
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Student> blockingQueue = new LinkedBlockingQueue<>(10);
        AtomicInteger index = new AtomicInteger(0);

        Thread producerThread = new Thread(() -> {
            try {

                while (true) {
                    Student student = new Student();
                    student.number = index.getAndIncrement();
                    System.out.println("студент " + student.number + "  с лабами по " + student.subjectName + " заходит в кабинет");
                    blockingQueue.put(student);
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumerOOP = new Thread(() -> {
            try {
                while (true) {
                    Student student = blockingQueue.peek();
                    if (student != null && student.subjectName.equals("OOP")) {
                        System.out.println("стдент "+ student.number+" с лабами по ооп начинает сдавать лабы");
                        AtomicInteger labs = new AtomicInteger(blockingQueue.take().labCount);
                        while (labs.get() != 0) {
                            System.out.println("ооп студент "+student.number+" сдал 5 лаб, осталось " + labs);
                            labs.updateAndGet((n) -> n - 5);
                        }

                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumerHighMath = new Thread(() -> {
            try {
                while (true) {
                    Student student = blockingQueue.peek();
                    if (student != null && student.subjectName.equals("HighMath")) {
                        System.out.println("стдент "+ student.number+" с лабами по вышмату начинает сдавать лабы");
                        AtomicInteger labs = new AtomicInteger(blockingQueue.take().labCount);
                        while (labs.get() != 0) {
                            System.out.println("вышмат студент "+ student.number+" сдал 5 лаб, осталось " + labs);
                            labs.updateAndGet((n) -> n - 5);
                        }

                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumerPhysics = new Thread(() -> {
            try {
                while (true) {
                    Student student = blockingQueue.peek();
                    if (student != null && student.subjectName.equals("Physics")) {
                        System.out.println("стдент "+ student.number+" с лабами по физике начинает сдавать лабы");
                        AtomicInteger labs = new AtomicInteger(blockingQueue.take().labCount);
                        while (labs.get() != 0) {
                            System.out.println("физика студент"+ student.number+" сдал 5 лаб, осталось " + labs);
                            labs.updateAndGet((n) -> n - 5);
                        }
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producerThread.start();

        consumerOOP.start();
        consumerHighMath.start();
        consumerPhysics.start();

        producerThread.join();

        consumerOOP.join();
        consumerHighMath.join();
        consumerPhysics.join();

    }

}
