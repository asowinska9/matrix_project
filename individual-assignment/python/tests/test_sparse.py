import random
from mmul.sparse import random_csr, spmv_csr, spmm_csr_dense
from mmul.core import naive_ijk

def make_dense(m,n,seed=1):
    rng = random.Random(seed)
    return [[rng.random() for _ in range(n)] for _ in range(m)]

def almost_equal(C1, C2, tol=1e-9):
    for i in range(len(C1)):
        for j in range(len(C1[0])):
            if abs(C1[i][j] - C2[i][j]) > tol:
                return False
    return True

def test_spmv_small():
    m,k = 8,10
    A = random_csr(m,k,density=0.2,seed=7)
    x = [i*0.1 for i in range(k)]
    y = spmv_csr(A,x)
    # dense with CSR once
    D = [[0.0]*k for _ in range(m)]
    for i in range(m):
        for t in range(A.indptr[i], A.indptr[i+1]):
            j = A.indices[t]
            D[i][j] = A.data[t]
    # D @ x 
    y_ref = [sum(D[i][j]*x[j] for j in range(k)) for i in range(m)]
    assert all(abs(a-b) < 1e-9 for a,b in zip(y,y_ref))

def test_spmm_small():
    m,k,n = 6,7,5
    A = random_csr(m,k,density=0.3,seed=3)
    B = make_dense(k,n,seed=2)
    C = spmm_csr_dense(A,B)
    # comparison with dense: D = dense with CSR
    D = [[0.0]*k for _ in range(m)]
    for i in range(m):
        for t in range(A.indptr[i], A.indptr[i+1]):
            j = A.indices[t]
            D[i][j] = A.data[t]
    C_ref = naive_ijk(D,B)
    assert almost_equal(C, C_ref)
