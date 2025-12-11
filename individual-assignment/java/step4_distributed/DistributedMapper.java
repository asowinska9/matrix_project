import java.util.concurrent.Callable;

public class DistributedMapper implements Callable<DistributedMapper.Result> {

    public static class Result {
        public final int rowStart;
        public final double[][] chunk;

        public Result(int rowStart, double[][] chunk) {
            this.rowStart = rowStart;
            this.chunk = chunk;
        }
    }

    private final double[][] A;
    private final double[][] B;
    private final int rowStart;
    private final int rowEnd;

    public DistributedMapper(double[][] A, double[][] B, int rowStart, int rowEnd) {
        this.A = A;
        this.B = B;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
    }

    @Override
    public Result call() {
        int k = A[0].length;
        int m = B[0].length;
        int rows = rowEnd - rowStart;

        double[][] chunk = new double[rows][m];

        for (int local = 0; local < rows; local++) {
            int i = rowStart + local;
            for (int j = 0; j < m; j++) {
                double sum = 0.0;
                for (int p = 0; p < k; p++) {
                    sum += A[i][p] * B[p][j];
                }
                chunk[local][j] = sum;
            }
        }

        return new Result(rowStart, chunk);
    }
}