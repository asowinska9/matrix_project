#include <vector>
#include <cstdlib>
#include <stdexcept>
#include <algorithm>

using Matrix = std::vector<std::vector<double>>;

// create a random matrix with values between 0 and 1
Matrix create_random_matrix(int n) {
    Matrix M(n, std::vector<double>(n));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            M[i][j] = static_cast<double>(std::rand()) / RAND_MAX;
        }
    }
    return M;
}

// simple check if matrices can be multiplied
void check_dimensions(const Matrix& A, const Matrix& B) {
    if (A.empty() || B.empty()) throw std::runtime_error("Empty matrix");
    if (A[0].size() != B.size()) throw std::runtime_error("Matrix dimensions do not match");
}

// classic i-j-k version
Matrix multiply_ijk(const Matrix& A, const Matrix& B) {
    check_dimensions(A, B);
    int n = A.size();
    int m = B[0].size();
    int k = B.size();

    Matrix C(n, std::vector<double>(m, 0.0));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            double sum = 0.0;
            for (int p = 0; p < k; p++) {
                sum += A[i][p] * B[p][j];
            }
            C[i][j] = sum;
        }
    }
    return C;
}

// i-k-j version (usually a bit faster)
Matrix multiply_ikj(const Matrix& A, const Matrix& B) {
    check_dimensions(A, B);
    int n = A.size();
    int m = B[0].size();
    int k = B.size();

    Matrix C(n, std::vector<double>(m, 0.0));
    for (int i = 0; i < n; i++) {
        for (int p = 0; p < k; p++) {
            double a_ip = A[i][p];
            for (int j = 0; j < m; j++) {
                C[i][j] += a_ip * B[p][j];
            }
        }
    }
    return C;
}

// simple blocked version
Matrix multiply_blocked(const Matrix& A, const Matrix& B, int block) {
    check_dimensions(A, B);
    int n = A.size();
    int m = B[0].size();
    int k = B.size();

    Matrix C(n, std::vector<double>(m, 0.0));

    for (int ii = 0; ii < n; ii += block) {
        for (int pp = 0; pp < k; pp += block) {
            for (int jj = 0; jj < m; jj += block) {
                int i_max = std::min(ii + block, n);
                int p_max = std::min(pp + block, k);
                int j_max = std::min(jj + block, m);
                for (int i = ii; i < i_max; i++) {
                    for (int p = pp; p < p_max; p++) {
                        double a_ip = A[i][p];
                        for (int j = jj; j < j_max; j++) {
                            C[i][j] += a_ip * B[p][j];
                        }
                    }
                }
            }
        }
    }
    return C;
}
