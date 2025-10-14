// Matrix multiplication benchmark in C for different sizes

#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

#define REPEATS 3   // repetitions per test

// function to return current time in seconds
double wall_time() {
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec + t.tv_usec * 1e-6;
}

// function multiplies two n×n matrices and returns execution time
double multiply_and_time(int n) {
    double **A = (double**) malloc(n * sizeof(double*));
    double **B = (double**) malloc(n * sizeof(double*));
    double **C = (double**) malloc(n * sizeof(double*));

    for (int i = 0; i < n; i++) {
        A[i] = (double*) malloc(n * sizeof(double));
        B[i] = (double*) malloc(n * sizeof(double));
        C[i] = (double*) malloc(n * sizeof(double));
        for (int j = 0; j < n; j++) {
            A[i][j] = (double) rand() / RAND_MAX;
            B[i][j] = (double) rand() / RAND_MAX;
            C[i][j] = 0.0;
        }
    }

    double start = wall_time();

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            double sum = 0.0;
            for (int k = 0; k < n; k++) {
                sum += A[i][k] * B[k][j];
            }
            C[i][j] = sum;
        }
    }

    double end = wall_time();

    for (int i = 0; i < n; i++) {
        free(A[i]);
        free(B[i]);
        free(C[i]);
    }
    free(A);
    free(B);
    free(C);

    return end - start;
}

int main() {
    int sizes[] = {50, 100, 200, 500};
    int num_sizes = sizeof(sizes) / sizeof(sizes[0]);

    printf("Size | Average time (s)\n");
    printf("-----------------------\n");

    for (int s = 0; s < num_sizes; s++) {
        int n = sizes[s];
        double total = 0.0;
        for (int r = 0; r < REPEATS; r++) {
            total += multiply_and_time(n);
        }
        double avg = total / REPEATS;
        printf("%4d | %0.6f\n", n, avg);
    }

    return 0;
}
