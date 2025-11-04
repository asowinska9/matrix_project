import java.io.*;
import java.util.*;

public class CsrMatrix {
    public final int m, n;
    public final int[] indptr;     // m+1
    public final int[] indices;    // nnz
    public final double[] data;    // nnz

    public CsrMatrix(int m, int n, int[] indptr, int[] indices, double[] data){
        this.m = m; this.n = n; this.indptr = indptr; this.indices = indices; this.data = data;
    }
    public int nnz(){ return data.length; }

    // y = A * x
    public double[] spmv(double[] x){
        if (x.length != n) throw new IllegalArgumentException("x has wrong size");
        double[] y = new double[m];
        for (int i=0;i<m;i++){
            double acc = 0.0;
            for (int t = indptr[i]; t < indptr[i+1]; t++){
                acc += data[t] * x[indices[t]];
            }
            y[i] = acc;
        }
        return y;
    }

    // C = A * B, B (n x k) in row-major (1D: B[row*k + col])
    // C as (m x k) row-major
    public double[] spmm(double[] B, int k){
        if (B.length != (long)n * k) throw new IllegalArgumentException("B has wrong size");
        double[] C = new double[(int)((long)m * k)];
        for (int i=0;i<m;i++){
            int start = indptr[i], end = indptr[i+1];
            int baseC = i * k;
            for (int t=start; t<end; t++){
                int col = indices[t];
                double a = data[t];
                int baseB = col * k;
                for (int j=0;j<k;j++){
                    C[baseC + j] += a * B[baseB + j];
                }
            }
        }
        return C;
    }

    // Minimal loader Matrix Market (.mtx) → CSR (format "coordinate")
    public static CsrMatrix fromMatrixMarket(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); 
            if (line == null) throw new IOException("Empty file");

            do {
                line = br.readLine();
                if (line == null) throw new IOException("Missing size line");
            } while (line.startsWith("%"));

            String[] parts = line.trim().split("\\s+");
            int m = Integer.parseInt(parts[0]);
            int n = Integer.parseInt(parts[1]);
            int nnz = Integer.parseInt(parts[2]);

            int[] rows = new int[nnz];
            int[] cols = new int[nnz];
            double[] vals = new double[nnz];

            int read = 0;
            while (read < nnz && (line = br.readLine()) != null) {
                if (line.isBlank() || line.charAt(0) == '%') continue;
                String[] p = line.trim().split("\\s+");
                int i = Integer.parseInt(p[0]) - 1;
                int j = Integer.parseInt(p[1]) - 1;
                double v = (p.length >= 3) ? Double.parseDouble(p[2]) : 1.0;
                rows[read] = i; cols[read] = j; vals[read] = v; read++;
            }
            if (read != nnz) throw new IOException("Could not read all entries");

            int[] indptr = new int[m+1];
            for (int r=0;r<nnz;r++) indptr[rows[r] + 1]++;
            for (int i=0;i<m;i++) indptr[i+1] += indptr[i];

            int[] indices = new int[nnz];
            double[] data = new double[nnz];
            int[] next = indptr.clone();

            for (int r=0;r<nnz;r++){
                int i = rows[r];
                int pos = next[i]++;
                indices[pos] = cols[r];
                data[pos] = vals[r];
            }
            return new CsrMatrix(m, n, indptr, indices, data);
        }
    }

    public static double[] randVec(int n, long seed){
        Random r = new Random(seed);
        double[] x = new double[n];
        for (int i=0;i<n;i++) x[i] = r.nextDouble();
        return x;
    }
    public static double[] randMatRowMajor(int n, int k, long seed){
        Random r = new Random(seed);
        double[] B = new double[(int)((long)n * k)];
        for (int i=0;i<n;i++) for (int j=0;j<k;j++) B[i*k + j] = r.nextDouble();
        return B;
    }
}
