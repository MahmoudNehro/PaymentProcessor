package org.example;

import org.example.db.Constants.AccountType;
import org.example.db.DBConnection;
import org.example.db.user.Account;
import org.example.db.user.User;
import org.example.db.user.UserRepo;
import org.example.service.ThreadPoolSizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class.getName());
    private static final long avgWaitTimeNs = 40_000_000;
    private static final long avgServiceTimeNs = 1_000_000;
    private static final int noOfThreads = ThreadPoolSizer.calculateOptimalThreads(avgWaitTimeNs, avgServiceTimeNs);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads);

    public static void main(String[] args) {
        LOGGER.info("No of threads in the pool are {}", noOfThreads);
        DBConnection.initDBConnection();
        mainMenu();
        closeService();
    }

    private static void closeService() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down thread pool...");
            executorService.shutdown();
        }));
    }

    private static void mainMenu() {
        int userChoice = 0;
        System.out.println("Choose an option! ");
        System.out.println("-1 exit");
        System.out.println("0- back to menu");
        System.out.println("1- Create user");
        System.out.println("2- Simulate creating many users");
        System.out.println("3- Create account for the user");
        do {
            userChoice = scanner.nextInt();
            switch (userChoice) {
                case -1 -> System.exit(0);
                case 0 -> mainMenu();
                case 1 -> createUser();
                case 2 -> simulateUsers(10000);
                case 3 -> createAccount();
            }
        } while (true);
    }

    private static void createAccount() {
        scanner.nextLine();
        System.out.println("Creating account....");
        System.out.println("Enter user id: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.println("Enter balance: ");
        BigDecimal balance = BigDecimal.valueOf(scanner.nextDouble());
        scanner.nextLine();
        System.out.println("Enter currency: ");
        String currency = scanner.nextLine();
        System.out.println("Enter account type: ");
        AccountType accountType = AccountType.valueOf(scanner.nextLine());
        Account account = new Account(userId,accountNumber,balance,currency, accountType);
        executorService.submit(() -> {
            LOGGER.info("Creating account on thread {}", Thread.currentThread().getName());
            new UserRepo().createAccount(account);
        });
    }

    private static void createUser() {
        scanner.nextLine();
        System.out.println("Creating user....");
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        User user = new User(name, email);
        executorService.submit(() -> {
            LOGGER.info("Creating user on thread {}", Thread.currentThread().getName());
            new UserRepo().createUser(user);
        });

    }

    private static void simulateUsers(int count) {
        CountDownLatch latch = new CountDownLatch(count);
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            int id = i;
            executorService.submit(() -> {
                try {
                    User user = new User("User" + id, "user" + id + "@test.com");
                    new UserRepo().createUser(user);
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long finish = System.nanoTime();
        long elapsedNs = finish - start;
        double elapsedSec = elapsedNs / 1_000_000_000.0;

        System.out.printf("%d requests completed in %.3f seconds%n", count, elapsedSec);
        System.out.printf("Average latency = %.3f ms/request%n", (elapsedSec * 1000) / count);
        System.out.printf("Throughput = %.2f requests/sec%n", count / elapsedSec);
    }
}