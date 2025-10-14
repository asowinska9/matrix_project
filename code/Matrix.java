import java.util.Random;

public class Matrix {

    // PRODUCTION CODE
    public static double[][] matmul(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                double s = 0.0;
                for (int k = 0; k < n; k++)
                    s += A[i][k] * B[k][j];
                C[i][j] = s;
            }
        return C;
    }

    // TEST / BENCHMARK CODE
    private static double[][] genMatrix(int n, Random r) {
        double[][] M = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                M[i][j] = r.nextDouble();
        return M;
    }

    private static double runOnce(int n, Random r) {
        double[][] A = genMatrix(n, r);
        double[][] B = genMatrix(n, r);
        long t0 = System.nanoTime();
        double[][] C = matmul(A, B);
        long t1 = System.nanoTime();
        return (t1 - t0) / 1e9; // seconds
    }

    private static void runBenchmark(int[] sizes, int repeats) {
        System.out.println("Size | Average time (s)");
        System.out.println("-----------------------");
        Random r = new Random(42);
        for (int n : sizes) {
            double total = 0.0;
            for (int i = 0; i < repeats; i++) total += runOnce(n, r);
            System.out.printf("%4d | %.6f%n", n, total / repeats);
        }
    }

    public static void main(String[] args) {
        int[] sizes = {50, 100, 200, 500};
        int repeats = 3;

        // CLI: --sizes 50,100,200 --repeats 3
        for (int i = 0; i < args.length; i++) {
            if ("--sizes".equals(args[i]) && i + 1 < args.length) {
                String[] parts = args[++i].split(",");
                sizes = new int[parts.length];
                for (int j = 0; j < parts.length; j++) sizes[j] = Integer.parseInt(parts[j]);
            } else if ("--repeats".equals(args[i]) && i + 1 < args.length) {
                repeats = Integer.parseInt(args[++i]);
            }
        }

        runBenchmark(sizes, repeats);
    }
}
