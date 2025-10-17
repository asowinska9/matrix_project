import java.util.Random;

public class MatrixFunctions {

    public static double[][] createRandomSquareMatrix(int n, long seed) {
        Random rng = new Random(seed);
        double[][] M = new double[n][n];
        for (int i = 0; i < n; i++) {
            double[] row = M[i];
            for (int j = 0; j < n; j++) {
                row[j] = rng.nextDouble();
            }
        }
        return M;
    }

    public static void checkDims(double[][] A, double[][] B) {
        if (A == null || B == null || A.length == 0 || B.length == 0)
            throw new IllegalArgumentException("Empty matrix");
        if (A[0].length != B.length)
            throw new IllegalArgumentException("Inner dimensions do not match");
    }

    private static double[][] zeros(int n, int m) {
        return new double[n][m];
    }

    public static double[][] multiplyUsingIJK(double[][] A, double[][] B) {
        checkDims(A, B);
        int n = A.length;
        int k = A[0].length;
        int m = B[0].length;
        double[][] C = zeros(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double sum = 0.0;
                for (int p = 0; p < k; p++) {
                    sum += A[i][p] * B[p][j];
                }
                C[i][j] = sum;
            }
        }
        return C;
    }

    public static double[][] multiplyUsingIKJ(double[][] A, double[][] B) {
        checkDims(A, B);
        int n = A.length;
        int k = A[0].length;
        int m = B[0].length;
        double[][] C = zeros(n, m);
        for (int i = 0; i < n; i++) {
            double[] Ai = A[i];
            double[] Ci = C[i];
            for (int p = 0; p < k; p++) {
                double a_ip = Ai[p];
                double[] Bp = B[p];
                for (int j = 0; j < m; j++) {
                    Ci[j] += a_ip * Bp[j];
                }
            }
        }
        return C;
    }

    public static double[][] multiplyUsingBlocking(double[][] A, double[][] B, int block) {
        checkDims(A, B);
        int n = A.length;
        int k = A[0].length;
        int m = B[0].length;
        double[][] C = zeros(n, m);
        for (int ii = 0; ii < n; ii += block) {
            for (int pp = 0; pp < k; pp += block) {
                for (int jj = 0; jj < m; jj += block) {
                    int iMax = Math.min(ii + block, n);
                    int pMax = Math.min(pp + block, k);
                    int jMax = Math.min(jj + block, m);
                    for (int i = ii; i < iMax; i++) {
                        double[] Ai = A[i];
                        double[] Ci = C[i];
                        for (int p = pp; p < pMax; p++) {
                            double a_ip = Ai[p];
                            double[] Bp = B[p];
                            for (int j = jj; j < jMax; j++) {
                                Ci[j] += a_ip * Bp[j];
                            }
                        }
                    }
                }
            }
        }
        return C;
    }
}
