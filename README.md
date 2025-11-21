# Individual Assignment – Matrix Multiplication (Dense & Sparse)
This project implements and benchmarks dense and sparse matrix multiplication in  
**Python**, **C++**, and **Java**, following the requirements of the assignment  
(Big Data / HPC).

The goal is to compare:
- language performance,
- sparse vs dense computation,
- effect of algorithmic design (naive loops, CSR format),
- performance on real-world sparse matrices.

All results are included in the LaTeX report in this folder.

---

## Repository Structure
<img width="314" height="551" alt="obraz" src="https://github.com/user-attachments/assets/f16e900d-3755-4abe-8b95-aaf1255c146b" />


## Installation & Requirements

Python >= 3.10
pip install matplotlib
pip install pytest
pip install scipy (optional for SciPy baseline)

C++17 or newer
Compiler: g++ / clang / MSVC
Recommended flags: -O3

Java 17 or newer
javac *.java

## Running Python Benchmarks
### Dense
```bash
python python/bench/run_bench.py --sizes 128,256,512 --algo naive_ikj --reps 5 --warmup 2 --csv data/outputs/py_dense.csv

Sparse synthetic

python python/bench/run_sparse.py --sizes 512,768 --density 0.05 --reps 5 --csv data/outputs/py_sparse.csv

Sparse real matrix (mc2depi.mtx)
python python/bench/run_real_sparse.py --mtx data/inputs/mc2depi.mtx --mode spmv --reps 5
python python/bench/run_real_sparse.py --mtx data/inputs/mc2depi.mtx --mode spmm --cols 16 --reps 5

Running C++ Benchmarks
Dense
cd cpp/
g++ -O3 dense.cpp -o dense
./dense

Sparse (CSR)
g++ -O3 bench_sparse.cpp -o bench_sparse
./bench_sparse --mtx data/inputs/mc2depi.mtx --mode spmv --reps 5 --csv data/outputs/cpp_mc2depi_spmv.csv
./bench_sparse --mtx data/inputs/mc2depi.mtx --mode spmm --cols 16 --reps 5 --csv data/outputs/cpp_mc2depi_spmm16.csv

Running Java Benchmarks
Dense
cd java/
javac *.java
java Benchmark

Sparse
java SparseBenchmark --mtx data/inputs/mc2depi.mtx --mode spmv
java SparseBenchmark --mtx data/inputs/mc2depi.mtx --mode spmm --cols 16
```

The final LaTeX report is available as:

PDF: Raport_matrix.pdf

TeX: Raport_matrix.tex

The report includes:

methodology
detailed tables
generated plots
interpretation of results
discussion and conclusions
references
statement on AI use


ata Used
mc2depi

From SuiteSparse Matrix Collection

Size: 525,825 × 525,825

nnz: 2,100,225

Density: 0.00076%

Source: https://sparse.tamu.edu/Williams/mc2depi

Statement on AI Use

Parts of this project (e.g., text structuring, LaTeX formatting and explanation)
were assisted by AI (ChatGPT by OpenAI).


License

This project is for academic purposes.
