#include "csr.hpp"
#include <fstream>
#include <sstream>
#include <stdexcept>
#include <algorithm>

void spmv(const CSR& A, const std::vector<double>& x, std::vector<double>& y) {
    if ((int)x.size() != A.n) throw std::runtime_error("x has wrong size");
    y.assign(A.m, 0.0);
    for (int i = 0; i < A.m; ++i) {
        double acc = 0.0;
        for (int t = A.indptr[i]; t < A.indptr[i+1]; ++t) {
            acc += A.data[t] * x[A.indices[t]];
        }
        y[i] = acc;
    }
}

void spmm(const CSR& A, const std::vector<double>& B, int k, std::vector<double>& C) {
    if ((int)B.size() != A.n * k) throw std::runtime_error("B has wrong size");
    C.assign((size_t)A.m * k, 0.0);
    for (int i = 0; i < A.m; ++i) {
        for (int t = A.indptr[i]; t < A.indptr[i+1]; ++t) {
            int col = A.indices[t];
            double a = A.data[t];
            const double* Brow = &B[(size_t)col * k];
            double* Crow = &C[(size_t)i * k];
            for (int j = 0; j < k; ++j) {
                Crow[j] += a * Brow[j];
            }
        }
    }
}

CSR load_matrix_market_to_csr(const std::string& path) {
    std::ifstream in(path);
    if (!in) throw std::runtime_error("Cannot open " + path);

    std::string line;

    // pomiń komentarze i znajdź linię z rozmiarami
    do {
        if (!std::getline(in, line)) throw std::runtime_error("Missing size line");
    } while (!line.empty() && line[0] == '%');

    int m=0, n=0, nnz=0;
    {
        std::istringstream sizes(line);
        sizes >> m >> n >> nnz;
        if (m<=0 || n<=0 || nnz<0) throw std::runtime_error("Bad size line");
    }

    struct Triplet { int i,j; double v; };
    std::vector<Triplet> coo; coo.reserve(nnz);

    int i,j; double v;
    int read = 0;
    while (read < nnz && std::getline(in, line)) {
        if (line.empty() || line[0]=='%') continue;
        std::istringstream iss(line);
        if (!(iss >> i >> j)) continue;
        if (!(iss >> v)) v = 1.0; // pattern → 1.0
        coo.push_back({i-1, j-1, v});
        ++read;
    }
    if ((int)coo.size() != nnz) throw std::runtime_error("Could not read all nnz entries");

    CSR A; A.m=m; A.n=n;
    A.indptr.assign(m+1, 0);
    A.indices.resize(nnz);
    A.data.resize(nnz);

    for (const auto& t : coo) {
        if (t.i < 0 || t.i >= m) throw std::runtime_error("Row out of range");
        ++A.indptr[t.i+1];
    }
    for (int r=0; r<m; ++r) A.indptr[r+1] += A.indptr[r];

    std::vector<int> next = A.indptr;
    for (const auto& t : coo) {
        int pos = next[t.i]++;
        A.indices[pos] = t.j;
        A.data[pos]    = t.v;
    }
    return A;
}
