#include <vector>
#include <random>
#include <chrono>
#include <iostream>
#include <iomanip>
#include <cstring>

// PRODUCTION CODE
static void matmul(const std::vector<double>& A,
                   const std::vector<double>& B,
                   std::vector<double>& C,
                   int n)
{
    auto idx = [n](int i, int j){ return i* (long long)n + j; };
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j) {
            double s = 0.0;
            for (int k = 0; k < n; ++k)
                s += A[idx(i,k)] * B[idx(k,j)];
            C[idx(i,j)] = s;
        }
}

// TEST / BENCHMARK CODE
static void gen_matrix(std::vector<double>& M, int n, std::mt19937_64& g) {
    std::uniform_real_distribution<double> d(0.0, 1.0);
    for (auto& x : M) x = d(g);
}

static double run_once(int n, std::mt19937_64& g) {
    std::vector<double> A(n*n), B(n*n), C(n*n);
    gen_matrix(A, n, g); gen_matrix(B, n, g);
    auto t0 = std::chrono::steady_clock::now();
    matmul(A, B, C, n);
    auto t1 = std::chrono::steady_clock::now();
    return std::chrono::duration<double>(t1 - t0).count();
}

static void run_benchmark(const std::vector<int>& sizes, int repeats) {
    std::cout << "Size | Average time (s)\n";
    std::cout << "-----------------------\n";
    std::mt19937_64 g(42);
    for (int n : sizes) {
        double total = 0.0;
        for (int r = 0; r < repeats; ++r) total += run_once(n, g);
        std::cout << std::setw(4) << n << " | "
                  << std::fixed << std::setprecision(6) << (total/repeats) << "\n";
    }
}

int main(int argc, char** argv) {
    std::vector<int> sizes = {50,100,200,500};
    int repeats = 3;

    // CLI: --sizes 50,100,200 --repeats 3
    for (int i = 1; i < argc; ++i) {
        if (!std::strcmp(argv[i], "--sizes") && i+1 < argc) {
            sizes.clear();
            for (char* p = std::strtok(argv[++i], ","); p; p = std::strtok(nullptr, ","))
                sizes.push_back(std::stoi(p));
        } else if (!std::strcmp(argv[i], "--repeats") && i+1 < argc) {
            repeats = std::stoi(argv[++i]);
        }
    }

    run_benchmark(sizes, repeats);
    return 0;
}
