package com.customplugin.activeseg.filter_core;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

/* References -
    1. http://homepages.inf.ed.ac.uk/rbf/CVonline/LOCAL_COPIES/SHUTLER3/node10.html
    2. FAST COMPUTATION OF LEGENDRE AND ZERNIKE MOMENTS (only Definition of actual Legendre moments in images has been taken)
        https://www.sciencedirect.com/science/article/pii/003132039500011N
 */


public class LegendreMoments_v2 {

    private int degree_m;
    private int degree_n;
    private double[][] coefficients;
    private double [][] polynomial_matrix_X;
    private double [][] polynomial_matrix_Y;
    private int M,N;

    LegendreMoments_v2(int degree_m, int degree_n){
        this.degree_m = degree_m;
        this.degree_n = degree_n;
        coefficients = new double[degree_m+1][degree_m+1];

        // initialise with negative number, note that coefficient value cannot be negative
        // if we ignore (-1)^(n-j) part

        for(int i=0;i<=degree_m;i++){
            for(int j=0;j<=degree_m;j++){
                coefficients[i][j] = -1;
            }
        }
    }




    // takes value of x, calculates and stores value of - P0(x),P1(x),...,Pdegree_m(x)
    // all polynomials of order 0 to degree_m using the recursive definition of Legendre Polynomials

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

        System.out.println("Start Legendre moment extraction process");
        M = ip.getHeight();
        N = ip.getWidth();
        System.out.println("M "+M+" N "+N);

        // for all degrees we need value of polynomials upto this much x or y, in moment calculation


        // Matrix containing value of polynomials at all x for all degrees, has dimension = no_of_rows*degree_m
        polynomial_matrix_X = new double[M][degree_m+1];
        // Matrix containing value of polynomials at all y for all degrees, has dimension = no_of_cols*degree_n
        polynomial_matrix_Y = new double[N][degree_n+1];

        // initialising with 0
        for(int i=0;i<M;i++){
            for(int j=0;j<=degree_m;j++){
                polynomial_matrix_X[i][j] = 0.0;
            }
        }

        // initialising with 0
        for(int i=0;i<N;i++){
            for(int j=0;j<=degree_n;j++){
                polynomial_matrix_Y[i][j] = 0.0;
            }
        }

        // Calculating polynomial values for Px
        for(int i = 1;i<=M;i++){
            for(int j =0;j<=degree_m;j++){
                /*System.out.println("LOOK order = "+ j + " index "+ i );*/
                double value = calculate_polynomial(j,(2*i-M-1.0)/(M-1),i-1,polynomial_matrix_X);
                //System.out.println("value = " + value);
                // i-> index and value of x | j-> order
                polynomial_matrix_X[i-1][j] = value;
            }
        }
        print_array(polynomial_matrix_X,M,degree_m+1);


        // Calculating polynomial values for Py
        for(int i = 1;i<=N;i++){
            for(int j =0;j<=degree_n;j++){
                double value = calculate_polynomial(j,(2*i-N-1.0)/(N-1),i-1,polynomial_matrix_Y);
                //System.out.println("value = " + value);
                // i-> index and value of x | j-> order
                polynomial_matrix_Y[i-1][j] = value;
            }
        }
        print_array(polynomial_matrix_Y,N,degree_n+1);

        // Later this would return moment matrix
        return polynomial_matrix_X;
    }

    public double moment_value(int x_order, int y_order, int M, int N, ImageProcessor ip){
        double value = 0.0;
        for(int i=0;i<M;i++){
            for(int j=0;j<N;j++){
                System.out.println("Px is : "+polynomial_matrix_X[i][x_order]+" Py is: "+polynomial_matrix_Y[j][y_order]+" ");
                value = value + polynomial_matrix_X[i][x_order]*polynomial_matrix_Y[j][y_order]*ip.getPixel(i,j);
            }
        }
        return ((2*x_order+1)*(2*y_order+1)*value)/((M-1)*(N-1));
    }

    public static void main(String[] args){
        String path="/media/albus/Horcrux/CustomPlugin/test_images/very_small.png";
        ImagePlus imp=IJ.openImage(path);
        ImageConverter ic=new ImageConverter(imp);
        ic.convertToGray8();
        ImageProcessor ip = imp.getProcessor();
        for(int i=0;i<ip.getHeight();i++){
            for(int j=0;j<ip.getWidth();j++){
                System.out.print(ip.getPixel(i,j)+" ");
            }
            System.out.println();
        }

        int degree_m = 10;
        int degree_n = 10;
        LegendreMoments_v2 zm=new LegendreMoments_v2(degree_m,degree_n);
        zm.extractLegendreMoment(ip);
    }



    public static void print_array(double[][]array,int m,int n){
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

}