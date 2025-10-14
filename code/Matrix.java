import java.util.Random;

public class Matrix {

    // Function that multiplies two matrices and measures execution time
    public static double multiplyAndTime(int n) {
        Random rand = new Random();
        double[][] A = new double[n][n];
        double[][] B = new double[n][n];
        double[][] C = new double[n][n];

        // Fill matrices with random values
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = rand.nextDouble();
                B[i][j] = rand.nextDouble();
            }
        }

        long start = System.nanoTime();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }

        long end = System.nanoTime();
        return (end - start) / 1e9; // convert ns to seconds
    }

    public static void main(String[] args) {
        // Default values
        int[] sizes = {50, 100, 200, 500};
        int repeats = 3;

        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--sizes") && i + 1 < args.length) {
                String[] parts = args[i + 1].split(",");
                sizes = new int[parts.length];
                for (int j = 0; j < parts.length; j++) {
                    sizes[j] = Integer.parseInt(parts[j]);
                }
            } else if (args[i].equals("--repeats") && i + 1 < args.length) {
                repeats = Integer.parseInt(args[i + 1]);
            }
        }

        System.out.println("Size | Average time (s)");
        System.out.println("-----------------------");

        for (int n : sizes) {
            double total = 0.0;
            for (int r = 0; r < repeats; r++) {
                total += multiplyAndTime(n);
            }
            double avg = total / repeats;
            System.out.printf("%4d | %.6f\n", n, avg);
        }
    }
}
