package com.example.parallel;

import java.util.concurrent.*;

public class MmulExecutor {

    public static double[][] multiply(double[][] A, double[][] B, int threads)
            throws InterruptedException {

        int m = A.length;
        int k = A[0].length;
        int n = B[0].length;

        double[][] C = new double[m][n];

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < m; i++) {
            final int row = i;
            executor.submit(() -> {
                for (int p = 0; p < k; p++) {
                    double a = A[row][p];
                    double[] rowB = B[p];
                    for (int j = 0; j < n; j++) {
                        C[row][j] += a * rowB[j];
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        return C;
    }

    // Test ręczny
    public static void main(String[] args) throws Exception {
        int n = 256;
        int threads = Runtime.getRuntime().availableProcessors();

        double[][] A = MmulSequential.rand(n, n, 1);
        double[][] B = MmulSequential.rand(n, n, 2);

        long t0 = System.nanoTime();
        double[][] C = multiply(A, B, threads);
        long t1 = System.nanoTime();

        System.out.printf("Executor (%d threads) n=%d: %.3f s%n",
                threads, n, (t1 - t0) / 1e9);
    }
}