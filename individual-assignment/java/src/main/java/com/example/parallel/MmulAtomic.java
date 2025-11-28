package com.example.parallel;

import java.util.concurrent.atomic.AtomicLongArray;
import java.util.stream.IntStream;

public class MmulAtomic {

    public static double[][] multiply(double[][] A, double[][] B) {

        int m = A.length;
        int k = A[0].length;
        int n = B[0].length;

        // AtomicLongArray dla każdego wiersza
        AtomicLongArray[] C_atomic = new AtomicLongArray[m];
        for (int i = 0; i < m; i++) {
            C_atomic[i] = new AtomicLongArray(n);
        }

        IntStream.range(0, m).parallel().forEach(i -> {
            for (int p = 0; p < k; p++) {
                double a = A[i][p];
                for (int j = 0; j < n; j++) {
                    double value = a * B[p][j];

                    long bits = Double.doubleToLongBits(value);

                    // Atomic addition – CAS loop
                    while (true) {
                        long oldBits = C_atomic[i].get(j);
                        double oldVal = Double.longBitsToDouble(oldBits);

                        double newVal = oldVal + value;
                        long newBits = Double.doubleToLongBits(newVal);

                        if (C_atomic[i].compareAndSet(j, oldBits, newBits)) {
                            break;
                        }
                    }
                }
            }
        });

        // Konwersja atomic → zwykła tablica
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = Double.longBitsToDouble(C_atomic[i].get(j));

        return C;
    }

    // test
    public static void main(String[] args) {
        int n = 256;
        double[][] A = MmulSequential.rand(n, n, 1);
        double[][] B = MmulSequential.rand(n, n, 2);

        long t0 = System.nanoTime();
        double[][] C = multiply(A, B);
        long t1 = System.nanoTime();

        System.out.printf("Atomic parallel n=%d: %.3f s%n",
                n, (t1 - t0) / 1e9);
    }
}