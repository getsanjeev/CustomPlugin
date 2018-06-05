package com.customplugin.activeseg.filter_core;

import ij.process.ImageProcessor;

/* References -
    1. http://homepages.inf.ed.ac.uk/rbf/CVonline/LOCAL_COPIES/SHUTLER3/node10.html
    2. FAST COMPUTATION OF LEGENDRE AND ZERNIKE MOMENTS https://www.sciencedirect.com/science/article/pii/003132039500011N
    3. An Efficient Method for the Computation of Legendre Moments  IEEE TRANSACTIONS ON PATTERN ANALYSIS AND MACHINE INTELLIGENCE, VOL. 27, NO. 12, DECEMBER 2005
        (Zero Order Approximation method has been followed)
 */


public class LegendreMoments_kt {

    private int degree_m;
    private int degree_n;
    private double [][] polynomial_matrix_X;
    private double [][] polynomial_matrix_Y;
    private int M,N;

    LegendreMoments_kt(int degree_m, int degree_n){
        this.degree_m = degree_m;
        this.degree_n = degree_n;
    }

    // takes value of x, calculates and stores value of - P0(x),P1(x),...,Pdegree_m(x)
    // i.e all polynomials of order 0 to degree_m using the recursive definition of Legendre Polynomials

    public double calculate_polynomial(int order, double x, int index, double [][] polynomial_matrix){
        if(order==0){
            return 1.0;
        }
        else if(order==1){
            return x;
        }
        else if(polynomial_matrix[index][order]!= 0.0){
            return polynomial_matrix[index][order];
        }
        else{
            return (((2*order-1.0)*x*calculate_polynomial(order-1,x,index,polynomial_matrix) - (order-1)*calculate_polynomial(order-2,x,index,polynomial_matrix)))/order;
        }
    }

    // Returns Legendre moment of image of degree (m+n) in form of array of m*n

    public double[][] extractLegendreMoment(ImageProcessor ip){

        System.out.println("Start Legendre moment extraction process...");
        System.out.println();

        // Height and width of image
        M = ip.getHeight();
        N = ip.getWidth();

        // Matrix containing value of polynomials at all x up to degree_m
        polynomial_matrix_X = new double[M][degree_m+1];

        // Matrix containing value of polynomials at all y up to degree_n
        polynomial_matrix_Y = new double[N][degree_n+1];


        // Calculating polynomial values for Px
        for(int i = 0;i<M;i++){
            for(int j =0;j<=degree_m;j++){
                double value = calculate_polynomial(j,(2*i-M+1.0)/(M),i,polynomial_matrix_X);
                polynomial_matrix_X[i][j] = value;
            }
        }

        if(M==N){
            polynomial_matrix_Y = polynomial_matrix_X;
        }
        else {
            // Calculating polynomial values for Py
            for(int i = 0;i<N;i++){
                for(int j =0;j<=degree_n;j++){
                    double value = calculate_polynomial(j,(2*i-N+1.0)/(N),i,polynomial_matrix_Y);
                    polynomial_matrix_Y[i][j] = value;
                }
            }
        }

        // Matrix which stores the Legendre moments up to order (m+n)
        double[][] moment_matrix = new double[degree_m+1][degree_n+1];

        // Calculation of moments (for each pair (m,n)) using the zero order approximation definition

        double moment_value;
        for(int m= 0;m<=degree_m;m++) {
            for (int n = 0; n <= degree_n; n++) {
                moment_value = 0.0;
                for(int i=0;i<M;i++){
                    moment_value = moment_value + polynomial_matrix_X[i][m]*row_moment(i,n,N,ip);
                }
                moment_matrix[m][n] = (2*m+1)*moment_value/M;
            }
        }

        //return Legendre moments in form degree_m*degree_n matrix
        return moment_matrix;
    }

    // returns nth order moment of the ith row

    public double row_moment(int i, int n, int N, ImageProcessor ip){
        double row_moment_value = 0.0;
        for(int j=0;j<N;j++){
            row_moment_value = row_moment_value + polynomial_matrix_Y[j][n]*(double)ip.getPixel(i,j);
        }
        return (2*n+1)*row_moment_value/N;
    }

}