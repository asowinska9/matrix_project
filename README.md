# Matrix Multiplication Performance Comparison

## Project Overview
This project compares the performance of **matrix multiplication algorithms** implemented in **Python**, **C++**, and **Java**.  
Three algorithmic variants were analyzed:
- **Naive IJK** – basic triple-nested loop,
- **Naive IKJ** – optimized loop order for better cache performance,
- **Blocked (b=64)** – cache-aware implementation dividing matrices into smaller tiles.

The study evaluates how algorithm design and programming language implementation affect computational efficiency.

---

##  Project Structure
individual-assignment/
├── data/
│ └── outputs/ # CSV benchmark results
├── python/
│ ├── mmul/ # core algorithms (core.py)
│ ├── bench/ # benchmarking scripts (run_bench.py)
│ └── tests/ # unit tests
├── cpp/
│ ├── matrix_functions.cpp
│ └── benchmark.cpp
├── java/
│ ├── MatrixFunctions.java
│ └── Benchmark.java
├── Raport.pdf # Final paper in PDF
└── README.md # This file

## How to Run

### Python
Run benchmarks:
```bash
python python/bench/run_bench.py --sizes 64,128,192 --algo naive_ikj --reps 5 --warmup 2 --csv data/outputs/py_naive_ikj.csv

g++ benchmark.cpp -O2 -o benchmark
./benchmark

javac Benchmark.java
java Benchmark
```

All results are saved as .csv files in data/outputs/.
| n   | Python (IKJ) | Java (IKJ) | C++ (IKJ) |
| --- | ------------ | ---------- | --------- |
| 64  | 0.0179 s     | 0.00252 s  | 0.00039 s |
| 128 | 0.1336 s     | 0.00165 s  | 0.00274 s |
| 192 | 0.5176 s     | 0.00489 s  | 0.00775 s |

C++ achieved the best performance due to native code optimization and direct memory control.
Python was the slowest because of interpreter overhead, while Java performed in between thanks to JIT compilation.
The blocked algorithm improved performance for larger matrices.


Repository Contents

Raport.pdf – final report (LaTeX compiled)

data/outputs/ – CSV files with results

python/, cpp/, java/ – source code folders

Author
Anna Sowińska DEY61301
Course: Big Data 40955
October 2025
