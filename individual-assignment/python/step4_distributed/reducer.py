# python/step4_distributed/reducer.py

from typing import List, Tuple

Matrix = List[List[float]]

def assemble_matrix(partials: List[Tuple[int, Matrix]], n: int, m: int) -> Matrix:
    """
    do matrix C frome pieces couted by workery.

    partials: list (row_start, C_chunk)
    n, m: size matrix x C
    """
    C: Matrix = [[0.0] * m for _ in range(n)]

    # each 
    for row_start, chunk in partials:
        for offset, row in enumerate(chunk):
            C[row_start + offset] = row

    return C