package com.customplugin.activeseg.testing;

import com.customplugin.activeseg.filter_core.LegendreMoments_elm;
import com.customplugin.activeseg.filters.Legendre_filter_;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ijaux.scale.Pair;

import java.util.ArrayList;

import static com.customplugin.activeseg.filter_core.utility.display_image;

public class Legendre_moment_tests {

    public static Double [] constant_unity_moments_3_3_ELM = {1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    public static Double [] test_artificial_moments_3_3_ELM = {5.6874,-0.3281,0.8203,0.4990,0.7969,0.2461,-1.3184,0.2410,-0.8203,-0.4395,5.4932,0.4486,-0.5674,-0.6819,-0.9613,2.0240};
    public static String BASE_PATH = "/home/albus/CustomPlugin/test_images/";
    public static String PATH_CONSTANT_UNITY = BASE_PATH+"constant_unity.png";
    public static String PATH_ARTIFICIAL = BASE_PATH+"test2_artificial.png";
    public static String PATH_ENCODING_TEST = BASE_PATH+"encoding_test_image.png";

    public static Double ERROR_EPSILON = 0.0001;


    public static void main(String[] args){

        if(check_moment_values()){
            System.out.println("THE APPROXIMATIONS ARE CORRECT");
        }

        if(check_return_encodings()){
            System.out.println("ENCODINGS ARE CORRECT");
        }
    }


    public static boolean check_moment_values(){
        ImagePlus imp=IJ.openImage(PATH_CONSTANT_UNITY);
        ImageConverter ic=new ImageConverter(imp);
        ic.convertToGray8();
        ImageProcessor ip = imp.getProcessor();

        int degree_m = 3;
        int degree_n = 3;

        LegendreMoments_elm zm2=new LegendreMoments_elm(degree_m,degree_n);
        double[][] legendreMoment_matrix2 = zm2.extractLegendreMoment(ip);
        //print_array(legendreMoment_matrix2,degree_m+1,degree_n+1);
        boolean passed = true;
        int k =0;

        for(int i=0;i<=degree_m;i++){
            for(int j=0;j<=degree_n;j++){
                if(Math.abs(legendreMoment_matrix2[i][j]-constant_unity_moments_3_3_ELM[k])>ERROR_EPSILON){
                    passed = false;
                    System.out.println("BAD APPROXIMATION "+legendreMoment_matrix2[i][j]+" "+constant_unity_moments_3_3_ELM[k]);
                }
                k++;
            }
        }

        if(passed){
            //System.out.println("TEST PASSED SUCCESSFULLY FOR EXACT MOMENT CALCULATION fOR CONSTANT UNITY IMAGE");
        }
        else {
            System.out.println("BAD APPROXIMATION");
            return passed;
        }

        k=0;
        imp=IJ.openImage(PATH_ARTIFICIAL);
        ic=new ImageConverter(imp);
        ic.convertToGray8();
        ip = imp.getProcessor();

        legendreMoment_matrix2 = zm2.extractLegendreMoment(ip);

        for(int i=0;i<=degree_m;i++){
            for(int j=0;j<=degree_n;j++){
                if(Math.abs(legendreMoment_matrix2[i][j]-test_artificial_moments_3_3_ELM[k])>ERROR_EPSILON){
                    passed = false;
                    System.out.println("BAD APPROXIMATION- "+legendreMoment_matrix2[i][j]+" "+test_artificial_moments_3_3_ELM[k]+" "+k);
                }
                k++;
            }
        }

        if(passed){
            //System.out.println("TEST PASSED SUCCESSFULLY FOR EXACT MOMENT CALCULATION fOR ARTIFICIAL IMAGE");
        }
        else{
            System.out.println("BAD APPROXIMATION");
        }

        return passed;
    }

    public static ArrayList<Roi> get_Roi(){
        Roi roi1 = new Roi(0,0,8,8);
        roi1.setName("one");
        Roi roi2 = new Roi(8,8,4,4);
        roi2.setName("two");
        Roi roi3 = new Roi(9,0,4,4);
        roi3.setName("three");

        ArrayList<Roi> list = new ArrayList<>();
        // keep the order same, hard-coded for testing
        list.add(roi2);
        list.add(roi1);
        list.add(roi3);
        return list;
    }

    public static boolean check_return_encodings(){
        ImagePlus imp=IJ.openImage(PATH_ENCODING_TEST);
        ImageConverter ic=new ImageConverter(imp);
        ic.convertToGray8();
        ImageProcessor ip = imp.getProcessor();
        display_image(ip);
        ArrayList<Roi> rois = get_Roi();
        boolean passed = true;

        Legendre_filter_ lm = new Legendre_filter_();
        lm.applyFilter(ip,"encoding_image",get_Roi());
        ArrayList<Pair<String,Pair<String[],Double>>> features = lm.getFeatures();

        // display the features
        /*for(int i=0;i<features.size();i++){
            System.out.println(lm.getFeatures().get(i).first+" "+lm.getFeatures().get(i).second.first[0]+" "+lm.getFeatures().get(i).second.first[1]+" "+ lm.getFeatures().get(i).second.first[2]+" "+lm.getFeatures().get(i).second.second);
        }*/

        int k = 0;
        int roi_count = 0;
        int moment_value_count = 0;
        int d_m = 0;
        int d_n = 0;

        Double[] current_roi_moment = test_artificial_moments_3_3_ELM;

        for(int i=0;i<lm.getFeatures().size();i++){

            if(k%16!=0 || k==0){
                if(!lm.getFeatures().get(i).first.equals(rois.get(roi_count).getName())){
                    System.out.println("NAME ERROR IN ENCODING");
                    passed = false;
                    return passed;
                }
                if(!lm.getFeatures().get(i).second.first[0].equals("LM") || !lm.getFeatures().get(i).second.first[1].equals(Integer.toString(d_m)) || !lm.getFeatures().get(i).second.first[2].equals(Integer.toString(d_n))){
                    System.out.println("MOMENT INDEX ERROR IN ENCODING");
                    passed = false;
                    return passed;
                }
                if(Math.abs(lm.getFeatures().get(i).second.second-current_roi_moment[moment_value_count])>ERROR_EPSILON){
                    System.out.println("MOMENT VALUE ERROR IN ENCODING");
                    passed = false;
                    return passed;
                }
                if((d_n+1)%4==0 && d_n!=0){
                    d_m++;
                    d_n = -1;
                }

            }
            else {
                roi_count++;
                k = 0;
                current_roi_moment = test_artificial_moments_3_3_ELM;
                if(roi_count==1){
                    current_roi_moment = constant_unity_moments_3_3_ELM;
                }
                moment_value_count=0;
                d_m = 0;
                d_n = 0;
                i--;
                continue;
            }

            moment_value_count ++;
            k++;
            d_n++;
        }
        return passed;
    }

}
