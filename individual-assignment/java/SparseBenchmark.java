import java.io.*;
import java.util.*;

public class SparseBenchmark {

    private static class Args {
        String mtx = "";
        String mode = "spmv"; // spmv | spmm
        int cols = 16;        // for spmm
        int reps = 5;
        int warmup = 1;
        String csv = "data/outputs/java_results/java_sparse_results.csv";
    }

    private static Args parse(String[] argv){
        Args a = new Args();
        for (int i=0; i<argv.length; i++){
            String s = argv[i];
            String next = (i+1<argv.length ? argv[i+1] : "");
            switch (s){
                case "--mtx":    a.mtx = next; i++; break;
                case "--mode":   a.mode = next; i++; break;
                case "--cols":   a.cols = Integer.parseInt(next); i++; break;
                case "--reps":   a.reps = Integer.parseInt(next); i++; break;
                case "--warmup": a.warmup = Integer.parseInt(next); i++; break;
                case "--csv":    a.csv = next; i++; break;
            }
        }
        return a;
    }

    private static double nowMs(){ return System.nanoTime() / 1e6; }

    public static void main(String[] argv) throws Exception {
        Args a = parse(argv);
        if (a.mtx.isEmpty()) {
            System.err.println("Provide --mtx path/to/matrix.mtx");
            return;
        }

        // folder results exists
        File outFile = new File(a.csv);
        if (outFile.getParentFile() != null) outFile.getParentFile().mkdirs();

        System.out.println("Loading MTX: " + a.mtx);
        CsrMatrix A = CsrMatrix.fromMatrixMarket(a.mtx);
        System.out.println("A: " + A.m + " x " + A.n + "  nnz=" + A.nnz());

        try (PrintWriter out = new PrintWriter(new FileWriter(outFile))) {
            out.println("language,lib,mode,mtx,n,nnz,cols,run,time_ms");

            if ("spmv".equals(a.mode)) {
                double[] x = CsrMatrix.randVec(A.n, 1L);
                // warmup
                for (int w=0; w<a.warmup; w++) A.spmv(x);
                for (int r=1; r<=a.reps; r++) {
                    double t0 = nowMs();
                    double[] y = A.spmv(x);
                    double ms = nowMs() - t0;
                    out.printf("java,pure,spmv,%s,%d,%d,,%d,%.3f%n", a.mtx, A.m, A.nnz(), r, ms);
                    System.out.printf("run %d: %.3f ms%n", r, ms);
                }
            } else { // spmm
                double[] B = CsrMatrix.randMatRowMajor(A.n, a.cols, 2L);
                // warmup
                for (int w=0; w<a.warmup; w++) A.spmm(B, a.cols);
                for (int r=1; r<=a.reps; r++) {
                    double t0 = nowMs();
                    double[] C = A.spmm(B, a.cols);
                    double ms = nowMs() - t0;
                    out.printf("java,pure,spmm,%s,%d,%d,%d,%d,%.3f%n", a.mtx, A.m, A.nnz(), a.cols, r, ms);
                    System.out.printf("run %d: %.3f ms%n", r, ms);
                }
            }
            System.out.println("Saved: " + outFile.getPath());
        }
    }
}
