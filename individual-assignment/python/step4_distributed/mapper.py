# python/step4_distributed/mapper.py

from typing import List, Tuple

Matrix = List[List[float]]

def multiply_chunk(args: Tuple[Matrix, Matrix, int, int]) -> Tuple[int, Matrix]:
    """
    counts matrix C = A * B from row_start to row_end (bez row_end).
    return (row_start, C_chunk), where C_chunk has (row_end - row_start).
    """
    A, B, row_start, row_end = args

    n = len(A)
    k = len(A[0])
    assert k == len(B), "Inner dimensions must match"
    m = len(B[0])

    chunk_height = row_end - row_start
    C_chunk: Matrix = [[0.0] * m for _ in range(chunk_height)]

    for local_i, i in enumerate(range(row_start, row_end)):
        for j in range(m):
            s = 0.0
            for p in range(k):
                s += A[i][p] * B[p][j]
            C_chunk[local_i][j] = s

    return row_start, C_chunk