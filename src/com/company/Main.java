package com.company;


public class Main {

    static class MessageLoop implements Runnable {
        public void run() {
            System.out.println("Hello from a thread!");
            String importantInfo[] = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };


            for (int i = 0; i < importantInfo.length; i++) {
//                if (Thread.interrupted()) {
//                    throw new InterruptedException();
//                }
                //Pause for 0.3 seconds
                try {
                    Thread.sleep(4000);
                    threadMessage(importantInfo[i]);
                } catch (InterruptedException e) {
                    threadMessage("catch: I wasn't done!");
                    // We've been interrupted: no more messages.
                    return;
                }
                //Print a message
                System.out.println(importantInfo[i]);
            }
        }

    }

    static void threadMessage(String message) {
        String threadName =
                Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }

    public static void main(String[] args)  throws InterruptedException {
//        System.out.println("xxx");
//        (new Thread(new HelloRunnable())).start();


        // Delay, in milliseconds before
        // we interrupt MessageLoop
        // thread (default one hour).
        long patience = 1000 * 10;

        // If command line argument
        // present, gives patience
        // in seconds.
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }
        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        while (t.isAlive()) {
            threadMessage("Still waiting...");
            // Wait maximum of 1 second
            // for MessageLoop thread
            // to finish.
            t.join(10000);
            threadMessage("After t join");
//            if (((System.currentTimeMillis() - startTime) > patience)
//                    && t.isAlive()) {
//                threadMessage("Tired of waiting!");
//                t.interrupt();
//                // Shouldn't be long now
//                // -- wait indefinitely
//                t.join();
//            }
        }
        threadMessage("Finally!");
    }

    }


