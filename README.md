# Performance Analysis of Matrix Multiplication Step 4

This repository contains the solution for the **Individual Assignment** for the course  
**Big Data 40955**.

The project focuses on the implementation and performance analysis of matrix
multiplication using **sequential, parallel, and distributed approaches**.
Experiments were conducted mainly in **Java**, with an additional distributed
implementation in **Python**.

---

## Project Structure
<img width="164" height="157" alt="obraz" src="https://github.com/user-attachments/assets/0f9f3ebf-50f9-448e-b32a-06240814454c" />

---

## Implementations

The following variants of matrix multiplication were implemented and tested:

- **Java Sequential** – single-threaded baseline implementation  
- **Java Parallel** – multi-threaded implementation using multiple workers  
- **Java Distributed** – distributed-style execution using task decomposition  
- **Python Distributed** – multiprocessing-based distributed execution  

All implementations perform multiplication of square matrices.

---

## ⏱ Benchmarking Methodology

- Each experiment was executed **5 times**
- The **average execution time** was used as the final result
- Matrix size used in the main comparison: **500 × 500**
- Number of workers: **4**

### Timing tools
- **Java**: `System.nanoTime()`
- **Python**: `time.perf_counter()`

---

## Results

The results show that:

- Parallel execution in Java provides a noticeable speedup over sequential execution
- Distributed Java implementation achieved the lowest execution time
- Python distributed implementation was significantly slower due to interpreter
  overhead and inter-process communication costs

Detailed results, tables, and visualizations are included in the final report.

---

## Report

The full analysis, including:
- methodology,
- benchmark results,
- tables and figures,
- discussion and conclusions,

is available in:

**Performance Analysis of Matrix Multiplication**

The report was written in **LaTeX**.

---

## Reproducibility

All code, benchmark outputs, and the LaTeX report are included in this repository.
The experiments can be reproduced by running the benchmark classes and scripts
provided in the corresponding language directories.

---

## Acknowledgement

Parts of this project and report were prepared with assistance from AI-based tools
to support code organization, benchmarking, and documentation.

---

## Author

**Anna Sowińska**  
