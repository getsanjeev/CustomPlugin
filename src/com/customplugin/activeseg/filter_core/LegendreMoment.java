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


public class LegendreMoment {

    private int degree_m;
    private int degree_n;
    private double[][] coefficients;
    private int M,N;

    LegendreMoment(int degree_m, int degree_n){
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

    // Utility function for moment calculation, returns Pm or Pn as per
    // in definition of Legendre moments

    public double get_P(double x, int degree){
        double P_n = 0.0;
        for(int j = 0;j<=degree;j++){
            P_n = P_n + calculate_Coefficient(degree,j)*Math.pow(x,j);
        }
        return P_n;
    }

    // Utility function for moment calculation, calculates coefficient value

    public double calculate_Coefficient(int n, int j){


        if((n-j)%2!=0){
            coefficients[n][j] = 0;
        }

        //coefficient has not been calculated previously, calculate it and put it in array

        else if(coefficients[n][j] < 0){
            coefficients[n][j] = (fact(n+j))/(fact((n-j)/2)*fact((n+j)/2)*fact(j)*(Math.pow(2,n)));
        }

        return Math.pow(-1,(n-j)/2)*coefficients[n][j];
    }

    // Utility to calculate factorial of a number

    public double fact(int n){
        if(n==0){
            return 1;
        }
        else{
            return n*fact(n-1);
        }
    }

    // Returns Legendre moment of image of degree (m+n) in form of array of m*n

    public double[][] extractLegendreMoment(ImageProcessor ip){

        System.out.println("Start Legendre moment extraction process");
        double x,y;
        Double moment;
        M = ip.getHeight();
        N = ip.getWidth();
        double[][] moment_table = new double[degree_m+1][degree_n+1];

        for(int k=0;k<=degree_m;k++){
            for(int l=0;l<=degree_n;l++){

                // for a particular order - (m & n); Calculates the Legendre Moment of image
                // by iterating over pixels and summing them as per definition

                moment = 0.0;

                for(int i=1;i<=M;i++){
                    x = (2*(double)i-M-1)/(M-1);
                    for(int j=1;j<=N;j++){
                        y = (2*(double)j-N-1)/(N-1);
                        moment = moment + get_P(x,k)*get_P(y,l)*ip.getPixel(i-1,j-1);
                    }
                }

                moment_table[k][l] = ((2*degree_n+1)*(2*degree_m+1)*moment)/((M-1)*(N-1));
            }
        }
        return moment_table;
    }



    public static void main(String[] args){
        String path="/media/albus/Horcrux/CustomPlugin/test_images/self_test.png";
        ImagePlus imp=IJ.openImage(path);
        ImageConverter ic=new ImageConverter(imp);
        ic.convertToGray8();
        ImageProcessor ip =imp.getProcessor();

        int degree_m = 10;
        int degree_n = 10;
        LegendreMoment zm=new LegendreMoment(degree_m,degree_n);

        zm.run_some_tests();

        //double [][] moment = zm.extractLegendreMoment(ip);
        //print_array(moment,zm.degree_m+1,zm.degree_n+1);
    }


    public static void print_array(float[][]array,int m,int n){
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void print_array(double[][]array,int m,int n){
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void run_some_tests(){

        // Legendre polynomial test
        // Values taken for check are from Mathematica LegendreP() function
        M = 10;
        for(int i=1;i<=10;i++){
            double x = (2*(double)i-M-1.0)/(M-1);
            System.out.println("order "+ i+ " index "+i + " x "+x);
            System.out.println(get_P(x,i));
        }

        int m = 10;
        int n = 10;
        LegendreMoment lm = new LegendreMoment(m,n);

        // x,order,value
        /*System.out.println("189,0");
        System.out.println("1");
        System.out.println(lm.get_P(189,0));
        System.out.println();

        System.out.println("189,1");
        System.out.println("189");
        System.out.println(lm.get_P(189,1));
        System.out.println();

        System.out.println("189,2");
        System.out.println("53581");
        System.out.println(lm.get_P(189,2));
        System.out.println();

        System.out.println("-189,2");
        System.out.println("53581");
        System.out.println(lm.get_P(-189,2));
        System.out.println();

        System.out.println("-189,3");
        System.out.println("-16877889");
        System.out.println(lm.get_P(-189,3));
        System.out.println();

        System.out.println("189,3");
        System.out.println("16877889");
        System.out.println(lm.get_P(189,3));
        System.out.println();

        System.out.println("0.0089,4");
        System.out.println("0.3747029899497304375");
        System.out.println(lm.get_P(0.0089,4));
        System.out.println();

        System.out.println("-0.0089,4");
        System.out.println("0.3747029899497304375");
        System.out.println(lm.get_P(-0.0089,4));
        System.out.println();

        System.out.println("0.0089,7");
        System.out.println("-0.01945487334128965358143636286875");
        System.out.println(lm.get_P(0.0089,7));
        System.out.println();

        System.out.println("-0.0089,7");
        System.out.println("0.01945487334128965358143636286875");
        System.out.println(lm.get_P(-0.0089,7));
        System.out.println();

        System.out.println("-0.9,7");
        System.out.println("0.36782499375");
        System.out.println(lm.get_P(-0.9,7));
        System.out.println();

        System.out.println("0.9,7");
        System.out.println("-0.36782499375");
        System.out.println(lm.get_P(0.9,7));
        System.out.println();

        System.out.println("0.0089,10");
        System.out.println("0.0218769066347030025264140267068962140234375");
        System.out.println(lm.get_P(0.0089,9));
        System.out.println();

        System.out.println("-0.0089,10");
        System.out.println("0.0218769066347030025264140267068962140234375");
        System.out.println(lm.get_P(0.0089,9));
        System.out.println();

        System.out.println("123,7");
        System.out.println("11418964351326879");
        System.out.println(lm.get_P(123,7));
        System.out.println();

        System.out.println("0.9,9");
        System.out.println("-0.3695104859765625");
        System.out.println(lm.get_P(0.9,9));
        System.out.println();*/

    }

}
