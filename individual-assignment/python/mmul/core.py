# python/mmul/core.py

def naive_ijk(A, B):
    m, k, n = len(A), len(A[0]), len(B[0])
    C = [[0.0]*n for _ in range(m)]
    for i in range(m):
        for j in range(n):
            s = 0.0
            for p in range(k):
                s += A[i][p] * B[p][j]
            C[i][j] = s
    return C

def naive_ikj(A, B):
    m, k, n = len(A), len(A[0]), len(B[0])
    C = [[0.0]*n for _ in range(m)]
    for i in range(m):
        for p in range(k):
            a_ip = A[i][p]
            rowB = B[p]
            c_i = C[i]
            for j in range(n):
                c_i[j] += a_ip * rowB[j]
    return C

def blocked(A, B, block: int = 64):
    m, k, n = len(A), len(A[0]), len(B[0])
    C = [[0.0]*n for _ in range(m)]
    for ii in range(0, m, block):
        for pp in range(0, k, block):
            for jj in range(0, n, block):
                imax = min(ii + block, m)
                pmax = min(pp + block, k)
                jmax = min(jj + block, n)
                for i in range(ii, imax):
                    c_i = C[i]
                    for p in range(pp, pmax):
                        a_ip = A[i][p]
                        rowB = B[p]
                        for j in range(jj, jmax):
                            c_i[j] += a_ip * rowB[j]
    return C

if __name__ == "__main__":
    import random, time
    n = 256
    A = [[random.random() for _ in range(n)] for _ in range(n)]
    B = [[random.random() for _ in range(n)] for _ in range(n)]
    for name, fn, kwargs in [
        ("naive_ijk", naive_ijk, {}),
        ("naive_ikj", naive_ikj, {}),
        ("blocked",   blocked,   {"block": 64}),
    ]:
        t0 = time.time(); fn(A,B, **kwargs); t1 = time.time()
        print(f"{name}: {t1-t0:.3f} s")
