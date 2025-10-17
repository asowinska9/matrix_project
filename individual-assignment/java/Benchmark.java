import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Benchmark {

    static double measureOnce(double[][] A, double[][] B, String algo, int block) {
        long t0 = System.nanoTime();
        switch (algo) {
            case "ijk":
                MatrixFunctions.multiplyUsingIJK(A, B);
                break;
            case "ikj":
                MatrixFunctions.multiplyUsingIKJ(A, B);
                break;
            case "blocked":
                MatrixFunctions.multiplyUsingBlocking(A, B, block);
                break;
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algo);
        }
        long t1 = System.nanoTime();
        return (t1 - t0) / 1e9;
    }

    public static void main(String[] args) throws IOException {
        int[] sizes = {64, 128, 192};
        int repeats = 5;
        int warmup = 2;
        int blockSize = 64;
        long seedA = 1L, seedB = 2L;
        String[] algos = {"ijk", "ikj", "blocked"};
        String csvPath = "java_results.csv";

        System.out.println("Sizes: " + Arrays.toString(sizes));
        System.out.println("Repeats: " + repeats + " (warmup " + warmup + ")");
        System.out.println("Block size: " + blockSize);
        System.out.println("------------------------------------");

        try (FileWriter out = new FileWriter(csvPath)) {
            out.write("language,algorithm,n,block,run,time_s\n");

            for (String algo : algos) {
                for (int n : sizes) {
                    double[][] A = MatrixFunctions.createRandomSquareMatrix(n, seedA);
                    double[][] B = MatrixFunctions.createRandomSquareMatrix(n, seedB);

                    for (int w = 0; w < warmup; w++) {
                        measureOnce(A, B, algo, blockSize);
                    }

                    double best = Double.POSITIVE_INFINITY;
                    double[] times = new double[repeats];
                    for (int r = 0; r < repeats; r++) {
                        double t = measureOnce(A, B, algo, blockSize);
                        times[r] = t;
                        if (t < best) best = t;
                        out.write("java," + algo + "," + n + "," 
                                + (algo.equals("blocked") ? blockSize : "") + "," 
                                + (r + 1) + "," + t + "\n");
                    }

                    Arrays.sort(times);
                    double median = times[times.length / 2];
                    System.out.printf("n=%4d  algo=%-8s  median=%.4f s  best=%.4f s%n",
                            n, algo, median, best);
                }
            }
        }

        System.out.println("Done. Results saved to " + csvPath);
    }
}
