package com.customplugin.activeseg.filter_core;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public class utility {
    public static void main(String[] args){
        String path="/media/albus/Horcrux/CustomPlugin/test_images/constant_unity.png";
        ImagePlus imp=IJ.openImage(path);
        ImageConverter ic=new ImageConverter(imp);
        ic.convertToGray8();
        ImageProcessor ip = imp.getProcessor();

        System.out.println("Displaying the image pixels");
        display_image(ip);

        int degree_m = 3;
        int degree_n = 3;

        LegendreMoments_zoa zm=new LegendreMoments_zoa(degree_m,degree_n);
        double[][] legendreMoment_matrix = zm.extractLegendreMoment(ip);
        System.out.println("The Legendre moments are: ");
        print_array(legendreMoment_matrix,degree_m+1,degree_n+1);
    }

    public static void display_image(ImageProcessor ip){
        for(int i=0;i<ip.getHeight();i++){
            for(int j=0;j<ip.getWidth();j++){
                System.out.print(ip.getPixel(i,j)+" ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public static void print_array(double[][]array,int m,int n){
        System.out.println();
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
