# Step 3 Parallelization Benchmark of Matrix Multiplication (Java)

This branch contains the implementation and benchmarking of *parallel matrix multiplication in Java*, following the requirements of Task 3 of the Individual Assignment.

## Task 3
The goal of this task is to evaluate how different *parallelization strategies* and *synchronization mechanisms* affect the performance of matrix multiplication. The implementations include:

- *Sequential version* (baseline)
- *ExecutorService* with fixed thread pool
- *Parallel Streams*
- *synchronized* block
- *AtomicLongArray* (CAS-based updates)
- *Semaphore-based* synchronization

All performance measurements were carried out using *JMH (Java Microbenchmark Harness)*.

---

## Project Structure

![Zdjęcie WhatsApp 2025-11-28 o 21 50 33_0bb703c0](https://github.com/user-attachments/assets/38488fb3-baf2-4f37-90c4-34a191394cdc)

---

## How to Build

Inside the java/ directory:

```bash
mvn -q package
```

This generates the JMH benchmark jar:

target/mmul-parallel-1.0-SNAPSHOT-shaded.jar

How to Run the Benchmark (JMH)

java -jar target/mmul-parallel-1.0-SNAPSHOT-shaded.jar -wi 3 -i 5 -f 1

Where:
	•	-wi 3 = warmup iterations
	•	-i 5 = measurement iterations
	•	-f 1 = number of forks

Summary of Results (n = 256)

![results](https://github.com/user-attachments/assets/95dc4efa-8d8d-4550-afa3-d1e3de993898)


Conclusion

Task 3 demonstrates that Java offers highly effective tools for parallel computation, but the performance strongly depends on the synchronization strategy used. Lightweight mechanisms (streams, semaphores) greatly outperform heavy locking (synchronized, atomic CAS operations).

Author

Anna Sowińska
Universidad de Las Palmas de Gran Canaria
