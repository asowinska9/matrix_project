import random, time, argparse

# PRODUCTION CODE
def matrix_multiply(A, B):
    n = len(A)
    #fill C with empty cells
    C = [[0.0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            s = 0.0
            for k in range(n):
                s += A[i][k] * B[k][j]
            C[i][j] = s
    return C

def generate_matrix(n):
    return [[random.random() for _ in range(n)] for _ in range(n)]

# TEST CODE
def run_benchmark(n):
    A = generate_matrix(n)
    B = generate_matrix(n)
    t0 = time.perf_counter()
    _ = matrix_multiply(A, B)
    t1 = time.perf_counter()
    return t1 - t0

def run_benchmarks(sizes, repeats):
    print("Size | Average time (s)")
    print("-----------------------")
    for n in sizes:
        total = sum(run_benchmark(n) for _ in range(repeats))
        print(f"{n:4d} | {total / repeats:.6f}")

#MAIN PROGRAM
parser = argparse.ArgumentParser()
parser.add_argument("--sizes", default="50,100,200,500")
parser.add_argument("--repeats", type=int, default=3)
args = parser.parse_args()
sizes = [int(x) for x in args.sizes.split(",")]
run_benchmarks(sizes, args.repeats)