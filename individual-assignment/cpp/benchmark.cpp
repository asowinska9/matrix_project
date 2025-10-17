#include <iostream>
#include <vector>
#include <string>
#include <chrono>
#include <fstream>
#include <cstdlib>
#include "matrix_functions.cpp"  

// measure time for one multiplication
double measure_time(const Matrix& A, const Matrix& B, const std::string& algo, int block) {
    auto start = std::chrono::high_resolution_clock::now();

    if (algo == "ijk") multiply_ijk(A, B);
    else if (algo == "ikj") multiply_ikj(A, B);
    else if (algo == "blocked") multiply_blocked(A, B, block);
    else throw std::runtime_error("Unknown algorithm");

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> diff = end - start;
    return diff.count(); // seconds
}

int main() {
    std::srand(42); // random seed (same each time)
    std::vector<int> sizes = {64, 128, 192};
    int repeats = 3;

    std::vector<std::string> algos = {"ijk", "ikj", "blocked"};
    std::ofstream file("cpp_results.csv");
    file << "algorithm,n,run,time_s\n";

    for (auto algo : algos) {
        for (auto n : sizes) {
            Matrix A = create_random_matrix(n);
            Matrix B = create_random_matrix(n);
            double total = 0.0;

            for (int r = 0; r < repeats; r++) {
                double t = measure_time(A, B, algo, 64);
                total += t;
                file << algo << "," << n << "," << (r+1) << "," << t << "\n";
            }
            std::cout << "Algorithm " << algo << ", n=" << n
                      << ", avg time = " << total / repeats << " s\n";
        }
    }

    file.close();
    std::cout << "Results saved to cpp_results.csv\n";
    return 0;
}
