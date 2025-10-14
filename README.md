# Matrix Multiplication Benchmark

## Objective
This project was developed as part of the **Big Data** course at **Universidad de Las Palmas de Gran Canaria**.  
The goal was to implement and benchmark **matrix multiplication** in three programming languages — **Python**, **Java**, and **C++** — to analyze performance differences and scalability.

---

## Project Structure

matrix_project/
│
├── code/ # Source code files
│ ├── matrix.py # Python implementation
│ ├── Matrix.java # Java implementation
│ ├── matrix.cpp # C++ implementation
│
├── data/ # Benchmark result files
│ ├── results_py.txt
│ ├── results_java.txt
│ ├── results_cpp.txt

## ⚙️ Features Implemented
- **Separation of Production and Testing Code**  
  Each language has:
  - `matmul()` → main matrix multiplication logic  
  - `run_benchmark()` → performs timing and averages multiple runs  

- **Parametrization**  
  Matrix size and number of runs are configurable.

- **Multiple Runs per Experiment**  
  Each experiment was repeated three times to get stable average results.

---

## Results Summary (Average Time in Seconds)

| Size | Python  |  Java  |  C++   |
|------|--------:|------: |-------:|
| 50   | 0.0083  | 0.0009 | 0.0001 |
| 100  | 0.0588  | 0.0012 | 0.0010 |
| 200  | 0.5068  | 0.0110 | 0.0089 |
| 500  | 10.1659 | 0.1752 | 0.1856 |

> **C++** was the fastest, followed closely by **Java**.  
> **Python** was significantly slower for large matrix sizes.

---

## How to Run

### Python
```bash
python code/matrix.py
