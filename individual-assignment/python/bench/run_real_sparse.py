import time, random, argparse
import os, csv
from scipy.io import mmread
import numpy as np
from mmul.sparse import CSR, spmv_csr, spmm_csr_dense

def scipy_to_csr(A):
    A = A.tocsr()  # na wszelki wypadek
    return CSR(
        A.shape[0], A.shape[1],
        A.indptr.astype(np.int64).tolist(),
        A.indices.astype(np.int32).tolist(),
        A.data.astype(np.float64).tolist()
    )

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--mtx", required=True, help="ścieżka do pliku .mtx (Matrix Market)")
    ap.add_argument("--mode", choices=["spmv","spmm"], default="spmv", help="spmv = A·x, spmm = A·B")
    ap.add_argument("--cols", type=int, default=16, help="liczba kolumn B w trybie spmm")
    ap.add_argument("--reps", type=int, default=5, help="liczba powtórzeń pomiaru")
    ap.add_argument("--csv", default="", help="ścieżka do pliku CSV z wynikami (opcjonalnie)")
    args = ap.parse_args()

    print(f"Loading MTX: {args.mtx}")
    A_mm = mmread(args.mtx)
    csr = scipy_to_csr(A_mm)
    print(f"A: {csr.m} x {csr.n}, nnz={len(csr.data)}")

    # Przygotowanie CSV (jeśli podano)
    writer = None
    fcsv = None
    if args.csv:
        out_dir = os.path.dirname(args.csv)
        if out_dir:
            os.makedirs(out_dir, exist_ok=True)
        fcsv = open(args.csv, "w", newline="")
        writer = csv.writer(fcsv)
        writer.writerow(["mode","mtx","n","nnz","cols","run","time_ms"])

    times = []

    if args.mode == "spmv":
        rng = random.Random(1)
        x = [rng.random() for _ in range(csr.n)]
        # warmup
        _ = spmv_csr(csr, x)
        # runs
        for r in range(args.reps):
            t0 = time.perf_counter()
            _ = spmv_csr(csr, x)
            ms = (time.perf_counter() - t0)*1000
            times.append(ms)
            if writer:
                writer.writerow(["spmv", args.mtx, csr.m, len(csr.data), "", r+1, f"{ms:.3f}"])
            print(f"run {r+1}: {ms:.2f} ms")
    else:
        rng = random.Random(2)
        B = [[rng.random() for _ in range(args.cols)] for __ in range(csr.n)]
        # warmup
        _ = spmm_csr_dense(csr, B)
        # runs
        for r in range(args.reps):
            t0 = time.perf_counter()
            _ = spmm_csr_dense(csr, B)
            ms = (time.perf_counter() - t0)*1000
            times.append(ms)
            if writer:
                writer.writerow(["spmm", args.mtx, csr.m, len(csr.data), args.cols, r+1, f"{ms:.3f}"])
            print(f"run {r+1}: {ms:.2f} ms")

    print(f"median: {np.median(times):.2f} ms, best: {np.min(times):.2f} ms")

    if fcsv:
        fcsv.close()

if __name__ == "__main__":
    main()
