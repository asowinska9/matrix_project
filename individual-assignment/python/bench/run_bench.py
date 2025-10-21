import argparse
import time
import random
import csv
from mmul.core import naive_ijk, naive_ikj, blocked  # <-- pasuje do Twojego core.py

ALGOS = {
    "naive_ijk": naive_ijk,
    "naive_ikj": naive_ikj,
    "blocked": blocked,
}

def make_matrix(n, seed=123):
    rng = random.Random(seed)
    return [[rng.random() for _ in range(n)] for _ in range(n)]

def run_once(algo_name, A, B, block):
    t0 = time.perf_counter()
    if algo_name == "blocked":
        ALGOS[algo_name](A, B, block)
    else:
        ALGOS[algo_name](A, B)
    t1 = time.perf_counter()
    return t1 - t0

def main():
    p = argparse.ArgumentParser()
    p.add_argument("--sizes", default="64,128,192")
    p.add_argument("--algo", choices=list(ALGOS.keys()), default="naive_ikj")
    p.add_argument("--block", type=int, default=64)
    p.add_argument("--reps", type=int, default=5)
    p.add_argument("--warmup", type=int, default=2)
    p.add_argument("--csv", default=None)
    args = p.parse_args()

    sizes = [int(x) for x in args.sizes.split(",") if x.strip()]
    print(f"Running {args.algo} | sizes={sizes} | reps={args.reps} | warmup={args.warmup}" + (f" | block={args.block}" if args.algo=='blocked' else ""))

    rows = []
    for n in sizes:
        # stałe A,B dla danego n (mniej szumu)
        A = make_matrix(n, 1)
        B = make_matrix(n, 2)

        for _ in range(args.warmup):
            _ = run_once(args.algo, A, B, args.block)

        for r in range(1, args.reps+1):
            t = run_once(args.algo, A, B, args.block)
            rows.append([args.algo, n, r, t])
            print(f"n={n:4d}  run={r}  time={t:.6f} s")

    if args.csv:
        with open(args.csv, "w", newline="") as f:
            w = csv.writer(f)
            w.writerow(["algo","n","run","time_s"])
            w.writerows(rows)
        print(f"\nSaved -> {args.csv}")

if __name__ == "__main__":
    main()
