#include <iostream>
#include <vector>
#include <random>
#include <chrono>
#include <iomanip>

using namespace std;
using namespace chrono;

// Function that returns time
double multiply_and_time(int n) {
    vector<vector<double>> A(n, vector<double>(n));
    vector<vector<double>> B(n, vector<double>(n));
    vector<vector<double>> C(n, vector<double>(n, 0.0));

    // Mix na=umbers
    random_device rd;
    mt19937 gen(rd());
    uniform_real_distribution<> dis(0.0, 1.0);

    // Fill matrix 
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j) {
            A[i][j] = dis(gen);
            B[i][j] = dis(gen);
        }

    auto start = high_resolution_clock::now();

    // Multiplication matrix
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < n; ++j) {
            double sum = 0.0;
            for (int k = 0; k < n; ++k)
                sum += A[i][k] * B[k][j];
            C[i][j] = sum;
        }

    auto end = high_resolution_clock::now();
    duration<double> elapsed = end - start;
    return elapsed.count();
}

int main() {
    vector<int> sizes = {50, 100, 200, 500};  
    int repeats = 3;                          

    cout << "Size | Average time (s)\n";
    cout << "-----------------------\n";

    for (int n : sizes) {
        double total = 0.0;
        for (int r = 0; r < repeats; ++r)
            total += multiply_and_time(n);
        double avg = total / repeats;
        cout << setw(4) << n << " | " << fixed << setprecision(6) << avg << "\n";
    }

    return 0;
}
