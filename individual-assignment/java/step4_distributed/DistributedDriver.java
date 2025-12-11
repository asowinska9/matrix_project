import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DistributedDriver {

    public static double[][] distributedMultiply(double[][] A, double[][] B, int numWorkers)
            throws InterruptedException {

        MatrixFunctions.checkDims(A, B);
        int n = A.length;
        int m = B[0].length;

        if (numWorkers <= 0) {
            numWorkers = Runtime.getRuntime().availableProcessors();
        }

        int chunkSize = (n + numWorkers - 1) / numWorkers;

        ExecutorService pool = Executors.newFixedThreadPool(numWorkers);
        List<Future<DistributedMapper.Result>> futures = new ArrayList<>();

        for (int start = 0; start < n; start += chunkSize) {
            int end = Math.min(start + chunkSize, n);
            DistributedMapper task = new DistributedMapper(A, B, start, end);
            futures.add(pool.submit(task));
        }

        List<DistributedMapper.Result> results = new ArrayList<>();
        for (Future<DistributedMapper.Result> f : futures) {
            try {
                results.add(f.get());
            } catch (ExecutionException e) {
                pool.shutdownNow();
                throw new RuntimeException("Worker failed", e.getCause());
            }
        }

        pool.shutdown();

        return DistributedReducer.assembleMatrix(results, n, m);
    }

    public static void main(String[] args) throws InterruptedException {
        int n = 500;
        int workers = 4;
        int runs = 5;

        if (args.length >= 1) n = Integer.parseInt(args[0]);
        if (args.length >= 2) workers = Integer.parseInt(args[1]);
        if (args.length >= 3) runs = Integer.parseInt(args[2]);

        System.out.printf("Distributed Java multiply, n=%d, workers=%d, runs=%d%n",
                n, workers, runs);

        double[][] A = MatrixFunctions.createRandomSquareMatrix(n, 1L);
        double[][] B = MatrixFunctions.createRandomSquareMatrix(n, 2L);

        double total = 0.0;

        for (int r = 0; r < runs; r++) {
            long t0 = System.nanoTime();
            double[][] C = distributedMultiply(A, B, workers);
            long t1 = System.nanoTime();
            double elapsed = (t1 - t0) / 1e9;
            total += elapsed;
            System.out.printf("Run %d: %.3f s%n", r + 1, elapsed);

            // check
            double check = C[0][0];
            if (Double.isNaN(check)) {
                System.out.println("This will never happen, just avoiding dead code.");
            }
        }

        double avg = total / runs;
        System.out.printf("Average over %d runs: %.3f s%n", runs, avg);
    }
}