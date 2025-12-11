# python/step4_distributed/run_distributed.py

from __future__ import annotations

import time
import multiprocessing as mp
from typing import List
from mapper import multiply_chunk
from reducer import assemble_matrix

Matrix = List[List[float]]


def create_random_matrix(n: int, m: int, seed: int = 42) -> Matrix:
    import random
    rng = random.Random(seed)
    return [[rng.random() for _ in range(m)] for _ in range(n)]


def distributed_multiply(A: Matrix, B: Matrix, num_workers: int | None = None) -> Matrix:
    """
    Performs distributed (multi-process) matrix multiplication C = A * B.
    Each process counts a portion of the rows of A – this is the MAP stage.
    Combine the results – this is the REDUCE stage.
    """
    n = len(A)
    k = len(A[0])
    assert k == len(B), "Inner dimensions must match"
    m = len(B[0])

    if num_workers is None:
        num_workers = mp.cpu_count()

    # devide A to num_workers 
    chunk_size = (n + num_workers - 1) // num_workers
    tasks = []
    for start in range(0, n, chunk_size):
        end = min(start + chunk_size, n)
        tasks.append((A, B, start, end))

    t0 = time.perf_counter()
    with mp.Pool(processes=num_workers) as pool:
        partials = pool.map(multiply_chunk, tasks)
    t1 = time.perf_counter()

    C = assemble_matrix(partials, n, m)
    print(f"Distributed multiply with {num_workers} workers took {t1 - t0:.3f} s")

    return C


def main():
    import argparse

    parser = argparse.ArgumentParser(description="Distributed matrix multiplication (Python, Step 4)")
    parser.add_argument("--n", type=int, default=1000, help="Rows of A / C")
    parser.add_argument("--k", type=int, default=1000, help="Cols of A / rows of B")
    parser.add_argument("--m", type=int, default=1000, help="Cols of B / C")
    parser.add_argument("--workers", type=int, default=None, help="Number of worker processes")
    parser.add_argument("--runs", type=int, default=3, help="How many runs per experiment")
    args = parser.parse_args()

    print(f"Generating random matrices {args.n}x{args.k} and {args.k}x{args.m}...")
    A = create_random_matrix(args.n, args.k, seed=1)
    B = create_random_matrix(args.k, args.m, seed=2)

    times = []
    for r in range(args.runs):
        t0 = time.perf_counter()
        distributed_multiply(A, B, num_workers=args.workers)
        t1 = time.perf_counter()
        times.append(t1 - t0)

    avg = sum(times) / len(times)
    print(f"Average wall time over {args.runs} runs: {avg:.3f} s")


if __name__ == "__main__":
    mp.set_start_method("spawn", force=True)  
    main()