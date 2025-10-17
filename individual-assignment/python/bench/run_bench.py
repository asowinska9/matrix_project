# python/bench/run_bench.py
import argparse, csv, gc, time, random, statistics, os
from mmul.core import naive_ijk, naive_ikj, blocked

ALGOS = {
    "naive_ijk": naive_ijk,
    "naive_ikj": naive_ikj,
    "blocked": blocked,
}

def make_matrix(n, seed=123):
    rng = random.Random(seed)
    return [[rng.random() for _ in range(n)] for _ in range(n)]

def run_once(func, A, B, **kwargs):
    gc.disable()
    t0 = time.perf_counter()
    func(A, B, **kwargs)
    t1 = time.perf_counter()
    gc.enable()
    return (t1 - t0)*1000.0  # ms

def main():
    p = argparse.ArgumentParser()
    p.add_argument("--sizes", default="128,256,512")
    p.add_argument("--algo", choices=list(ALGOS.keys()), default="naive_ikj")
    p.add_argument("--block", type=int, default=64)
    p.add_argument("--reps", type=int, default=5)
    p.add_argument("--warmup", type=int, default=2)
    p.add_argument("--csv", default="data/outputs/py_results.csv")
    args = p.parse_args()

    sizes = [int(s) for s in args.sizes.split(",") if s.strip()]
    os.makedirs(os.path.dirname(args.csv), exist_ok=True)

    with open(args.csv, "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["language","algorithm","n","block","run","time_ms"])
        for n in sizes:
            A = make_matrix(n, seed=1)
            B = make_matrix(n, seed=2)

            # warmup 
            for _ in range(args.warmup):
                ALGOS[args.algo](A, B, **({"block": args.block} if args.algo=="blocked" else {}))

            # results
            times = []
            for r in range(1, args.reps+1):
                t = run_once(ALGOS[args.algo], A, B, **({"block": args.block} if args.algo=="blocked" else {}))
                times.append(t)
                w.writerow(["python", args.algo, n, args.block if args.algo=="blocked" else "", r, f"{t:.3f}"])

            print(f"n={n:4d}  algo={args.algo:10s}  median={statistics.median(times):.1f} ms  best={min(times):.1f} ms")

if __name__ == "__main__":
    main()
