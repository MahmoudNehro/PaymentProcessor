package org.example.service;

public class ThreadPoolSizer {
    public static int calculateOptimalThreads(long waitNs, long serviceNs) {
        int cores = Runtime.getRuntime().availableProcessors();

        if (serviceNs == 0) serviceNs = 1;

        double ratio = (double) waitNs / serviceNs;
        int optimal = (int) Math.ceil(cores * (1 + ratio));

        return Math.max(optimal, cores);
    }
}
