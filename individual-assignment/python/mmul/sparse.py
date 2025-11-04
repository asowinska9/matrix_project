# Minimal CSR implementation + operations: SpMV (A@x) and SpMM (A@B)
from typing import List, Tuple
import random

class CSR:
    __slots__ = ("m","n","indptr","indices","data")
    def __init__(self, m:int, n:int, indptr:List[int], indices:List[int], data:List[float]):
        self.m, self.n = m, n
        self.indptr, self.indices, self.data = indptr, indices, data

def random_csr(m:int, n:int, density:float, seed:int=123) -> CSR:
    """
    Creates a random m×n sparse matrix in CSR format with a given density (0..1). Average m*n*density of non-zero elements.
    """
    rng = random.Random(seed)
    indptr = [0]*(m+1)
    indices = []
    data = []
    nnz_per_row = max(0, int(round(n * density)))
    for i in range(m):
        # columns
        if nnz_per_row == 0:
            indptr[i+1] = indptr[i]
            continue
        cols = rng.sample(range(n), k=min(nnz_per_row, n))
        cols.sort()
        for j in cols:
            indices.append(j)
            data.append(rng.random())
        indptr[i+1] = len(indices)
    return CSR(m, n, indptr, indices, data)

def spmv_csr(A: CSR, x: List[float]) -> List[float]:
    assert A.n == len(x)
    y = [0.0]*A.m
    for i in range(A.m):
        row_start, row_end = A.indptr[i], A.indptr[i+1]
        acc = 0.0
        for t in range(row_start, row_end):
            j = A.indices[t]
            acc += A.data[t] * x[j]
        y[i] = acc
    return y

def spmm_csr_dense(A: CSR, B: List[List[float]]) -> List[List[float]]:
    """C = A (m×k, CSR)  @  B (k×n, dense)  ->  C (m×n)"""
    k = A.n
    assert len(B) == k, "B wrong size (w. = k)"
    n = len(B[0]) if B else 0
    C = [[0.0]*n for _ in range(A.m)]
    for i in range(A.m):
        row_start, row_end = A.indptr[i], A.indptr[i+1]
        c_i = C[i]
        for t in range(row_start, row_end):
            col = A.indices[t]  
            a = A.data[t]
            rowB = B[col]
            # axpy C[i,:]
            for j in range(n):
                c_i[j] += a * rowB[j]
    return C
