# PRODUCTION CODE
def matmul(A, B):
    n = len(A)
    C = [[0.0]*n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            s = 0.0
            for k in range(n):
                s += A[i][k] * B[k][j]
            C[i][j] = s
    return C

# TEST / BENCHMARK CODE
import random, time, argparse

def gen_matrix(n):
    return [[random.random() for _ in range(n)] for _ in range(n)]

def run_once(n):
    A = gen_matrix(n); B = gen_matrix(n)
    t0 = time.perf_counter()
    _ = matmul(A, B)
    t1 = time.perf_counter()
    return t1 - t0

def run_benchmark(sizes, repeats):
    print("Size | Average time (s)")
    print("-----------------------")
    for n in sizes:
        total = sum(run_once(n) for _ in range(repeats))
        print(f"{n:4d} | {total/repeats:.6f}")

if __name__ == "__main__":
    p = argparse.ArgumentParser()
    p.add_argument("--sizes", default="50,100,200,500")
    p.add_argument("--repeats", type=int, default=3)
    args = p.parse_args()
    sizes = [int(x) for x in args.sizes.split(",")]
    run_benchmark(sizes, args.repeats)
