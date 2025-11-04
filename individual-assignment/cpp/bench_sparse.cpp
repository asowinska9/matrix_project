#include "csr.hpp"
#include <chrono>
#include <iostream>
#include <fstream>
#include <random>
#include <string>

static double now_ms() {
    using namespace std::chrono;
    return duration<double, std::milli>(high_resolution_clock::now().time_since_epoch()).count();
}
static std::vector<double> rand_vec(int n, unsigned seed=1){
    std::mt19937 g(seed); std::uniform_real_distribution<> d(0.0,1.0);
    std::vector<double> x(n); for(int i=0;i<n;++i) x[i]=d(g); return x;
}
static std::vector<double> rand_mat_rm(int n, int k, unsigned seed=2){
    std::mt19937 g(seed); std::uniform_real_distribution<> d(0.0,1.0);
    std::vector<double> B((size_t)n*k);
    for(int r=0;r<n;++r) for(int c=0;c<k;++c) B[(size_t)r*k+c]=d(g);
    return B;
}

int main(int argc, char** argv){
    std::string mtx; std::string mode="spmv"; int cols=16; int reps=5, warmup=1;
    std::string csv="data/outputs/cpp_sparse_results.csv";

    for(int i=1;i<argc;++i){
        std::string a=argv[i]; auto next=[&]{return (i+1<argc)?std::string(argv[++i]):std::string();};
        if(a=="--mtx") mtx=next();
        else if(a=="--mode") mode=next();
        else if(a=="--cols") cols=std::stoi(next());
        else if(a=="--reps") reps=std::stoi(next());
        else if(a=="--warmup") warmup=std::stoi(next());
        else if(a=="--csv") csv=next();
    }
    if(mtx.empty()){ std::cerr<<"Provide --mtx path\n"; return 1; }

    std::cout<<"Loading MTX: "<<mtx<<"\n";
    CSR A = load_matrix_market_to_csr(mtx);
    std::cout<<"A: "<<A.m<<" x "<<A.n<<"  nnz="<<A.nnz()<<"\n";

    std::ofstream out(csv);
    out<<"language,lib,mode,mtx,n,nnz,cols,run,time_ms\n";

    if(mode=="spmv"){
        auto x = rand_vec(A.n,1); std::vector<double> y;
        for(int w=0; w<warmup; ++w) spmv(A,x,y);
        for(int r=1; r<=reps; ++r){
            double t0=now_ms(); spmv(A,x,y); double ms=now_ms()-t0;
            out<<"cpp,pure,spmv,"<<mtx<<","<<A.m<<","<<A.nnz()<<",,"<<r<<","<<ms<<"\n";
            std::cout<<"run "<<r<<": "<<ms<<" ms\n";
        }
    } else {
        auto B = rand_mat_rm(A.n, cols, 2); std::vector<double> C;
        for(int w=0; w<warmup; ++w) spmm(A,B,cols,C);
        for(int r=1; r<=reps; ++r){
            double t0=now_ms(); spmm(A,B,cols,C); double ms=now_ms()-t0;
            out<<"cpp,pure,spmm,"<<mtx<<","<<A.m<<","<<A.nnz()<<","<<cols<<","<<r<<","<<ms<<"\n";
            std::cout<<"run "<<r<<": "<<ms<<" ms\n";
        }
    }
    std::cout<<"Saved: "<<csv<<"\n";
}
