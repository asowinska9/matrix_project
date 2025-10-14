
import random
import time
import argparse

p = argparse.ArgumentParser()
p.add_argument("--sizes", default="50,100,200,500",
            help="EN: Matrix sizes separated by commas")
p.add_argument("--repeats", type=int, default=3,
            help="Number of repetitions per test")
args = p.parse_args()

sizes = [int(x) for x in args.sizes.split(",")]
repeats = args.repeats

def multiply_and_time(n: int) -> float:
    """EN: Function generates n×n matrices, multiplies them, and returns execution time"""
    A = [[random.random() for _ in range(n)] for _ in range(n)]
    B = [[random.random() for _ in range(n)] for _ in range(n)]
    C = [[0.0 for _ in range(n)] for _ in range(n)]

    start = time.perf_counter()

    for i in range(n):
        for j in range(n):
            s = 0.0
            for k in range(n):
                s += A[i][k] * B[k][j]
            C[i][j] = s

    end = time.perf_counter()
    return end - start

print("Size | Average time (s)")
print("-----------------------")

for n in sizes:
    times = []
    for _ in range(repeats):
        t = multiply_and_time(n)
        times.append(t)
    avg_time = sum(times) / repeats
    print(f"{n:4d} | {avg_time:.6f}")
