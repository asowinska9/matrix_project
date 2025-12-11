import java.util.List;

public class DistributedReducer {

    public static double[][] assembleMatrix(List<DistributedMapper.Result> parts, int n, int m) {
        double[][] C = new double[n][m];

        for (DistributedMapper.Result part : parts) {
            int rowStart = part.rowStart;
            double[][] chunk = part.chunk;
            for (int i = 0; i < chunk.length; i++) {
                C[rowStart + i] = chunk[i];
            }
        }

        return C;
    }
}