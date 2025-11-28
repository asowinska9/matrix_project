package com.example.parallel;

import java.util.stream.IntStream;

public class MmulStreams {

    public static double[][] multiply(double[][] A, double[][] B) {

        int m = A.length;
        int k = A[0].length;
        int n = B[0].length;

        double[][] C = new double[m][n];

        IntStream.range(0, m).parallel().forEach(i -> {
            for (int p = 0; p < k; p++) {
                double a = A[i][p];
                double[] rowB = B[p];
                for (int j = 0; j < n; j++) {
                    C[i][j] += a * rowB[j];
                }
            }
        });

        return C;
    }

    // test ręczny
    public static void main(String[] args) {
        int n = 256;

        double[][] A = MmulSequential.rand(n, n, 1);
        double[][] B = MmulSequential.rand(n, n, 2);

        long t0 = System.nanoTime();
        double[][] C = multiply(A, B);
        long t1 = System.nanoTime();

        System.out.printf("Streams parallel n=%d: %.3f s%n", 
            n, (t1 - t0) / 1e9);
    }
}