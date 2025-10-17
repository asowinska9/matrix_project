import random
from mmul.core import naive_ijk

def make_matrix(n, seed=123):
    rng = random.Random(seed)
    return [[rng.random() for _ in range(n)] for _ in range(n)]

def almost_equal(C1, C2, tol=1e-9):
    for i in range(len(C1)):
        for j in range(len(C1[0])):
            if abs(C1[i][j] - C2[i][j]) > tol:
                return False
    return True

def test_teacher_vs_core():
    
    import python.mmul.matrix_teacher as teacher

    n = 64  
    A = make_matrix(n, 1)
    B = make_matrix(n, 2)

    # function result
    C1 = naive_ijk(A, B)

    C2 = [[0 for _ in range(n)] for _ in range(n)]
    for i in range(n):
        for j in range(n):
            for k in range(n):
                C2[i][j] += A[i][k] * B[k][j]

    assert almost_equal(C1, C2)
