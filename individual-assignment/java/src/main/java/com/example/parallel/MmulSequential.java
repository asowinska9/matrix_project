package com.example.parallel;

public class MmulSequential {

    // main function
    public static double[][] multiply(double[][] A, double[][] B) {
        int m = A.length;        
        int k = A[0].length;     
        int n = B[0].length;    

        double[][] C = new double[m][n];  // result

        for (int i = 0; i < m; i++) {
            for (int p = 0; p < k; p++) {
                double a = A[i][p];
                double[] rowB = B[p];
                double[] rowC = C[i];
                for (int j = 0; j < n; j++) {
                    rowC[j] += a * rowB[j];
                }
            }
        }
        return C;
    }

    // function to multiply matrix
    public static double[][] rand(int m, int n, long seed) {
        java.util.Random r = new java.util.Random(seed);
        double[][] M = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] = r.nextDouble();
            }
        }
        return M;
    }

    // test
    public static void main(String[] args) {
        int n = 256;
        double[][] A = rand(n, n, 1);
        double[][] B = rand(n, n, 2);

        long t0 = System.nanoTime();
        double[][] C = multiply(A, B);
        long t1 = System.nanoTime();

        System.out.printf("Sequential n=%d: %.3f s%n", n, (t1 - t0) / 1e9);
    }
}