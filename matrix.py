
import random
import time

# List of matrix sizes to test
sizes = [50, 100, 200, 500]

# Number of repetitions for averaging
repeats = 3

def multiply_and_time(n: int) -> float:
    """PL: Funkcja tworzy macierze n×n, mnoży je i zwraca czas wykonania
       EN: Function generates n×n matrices, multiplies them, and returns execution time"""
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
