#pragma once
#include <vector>
#include <string>

struct CSR {
    int m = 0, n = 0;
    std::vector<int> indptr;   // size m+1
    std::vector<int> indices;  // size nnz
    std::vector<double> data;  // size nnz
    int nnz() const { return (int)data.size(); }
};

void spmv(const CSR& A, const std::vector<double>& x, std::vector<double>& y);           // y = A*x
void spmm(const CSR& A, const std::vector<double>& B, int k, std::vector<double>& C);    // C = A*B, B: (n x k), C: (m x k) row-major

CSR load_matrix_market_to_csr(const std::string& path); // wczytaj .mtx (coordinate) → CSR
