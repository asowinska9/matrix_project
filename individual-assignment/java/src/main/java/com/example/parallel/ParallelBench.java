package com.example.parallel;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class ParallelBench {

    @Param({"256"})
    public int n;

    double[][] A;
    double[][] B;

    @Setup
    public void setup() {
        A = MmulSequential.rand(n, n, 1);
        B = MmulSequential.rand(n, n, 2);
    }

    @Benchmark
    public double[][] sequential() {
        return MmulSequential.multiply(A, B);
    }

    @Benchmark
    public double[][] executor() throws Exception {
        int threads = Runtime.getRuntime().availableProcessors();
        return MmulExecutor.multiply(A, B, threads);
    }

    @Benchmark
    public double[][] streams() {
        return MmulStreams.multiply(A, B);
    }

    @Benchmark
    public double[][] sync() {
        return MmulSynchronized.multiply(A, B);
    }

    @Benchmark
    public double[][] atomic() {
        return MmulAtomic.multiply(A, B);
    }

    @Benchmark
    public double[][] semaphore() {
        return MmulSemaphore.multiply(A, B);
    }
}