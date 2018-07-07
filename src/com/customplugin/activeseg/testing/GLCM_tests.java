package com.customplugin.activeseg.testing;


import com.customplugin.activeseg.filters.GLCM_filter_;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ijaux.scale.Pair;

import java.util.ArrayList;

import static com.customplugin.activeseg.filter_core.utility.display_image;

public class GLCM_tests {
    //TODO : This should be made more meaningful, add checks on return style like done in LegendreMomentTests
    public static String BASE_PATH = "/home/albus/CustomPlugin/test_images/";
    public static String PATH_CONSTANT_UNITY = BASE_PATH+"constant_unity.png";
    public static String PATH_ARTIFICIAL = BASE_PATH+"/test2_artificial.png";
    public static String PATH_ENCODING_TEST = BASE_PATH+"encoding_test_image.png";

    public static Double ERROR_EPSILON = 0.0001;


    public static void main(String[] args){
        check_return_encodings();
    }

    public static ArrayList<Roi> get_Roi(){
        Roi roi1 = new Roi(0,0,8,8);
        roi1.setName("one");
        Roi roi2 = new Roi(8,8,4,4);
        roi2.setName("two");
        Roi roi3 = new Roi(9,0,4,4);
        roi3.setName("three");

        ArrayList<Roi> list = new ArrayList<>();
        list.add(roi2);
        list.add(roi1);
        list.add(roi3);
        return list;
    }

    public static void check_return_encodings(){
        ImagePlus imp=IJ.openImage(PATH_ENCODING_TEST);
        ImageConverter ic=new ImageConverter(imp);
        ic.convertToGray8();
        ImageProcessor ip = imp.getProcessor();
        display_image(ip);
        ArrayList<Roi> rois = get_Roi();
        int roi_size = rois.size();
        GLCM_filter_ glcm = new GLCM_filter_();
        glcm.applyFilter(ip,"image",rois);
        ArrayList<Pair<String,Pair<String[],Double[]>>> features = glcm.getFeatures();
        System.out.println("THE SIZE OF FEATURES "+features.size());
        for(int i= 0;i<features.size();i++){
            System.out.println(features.get(i).first+" "+features.get(i).second.first[0]+" "+features.get(i).second.first[1]+" "+features.get(i).second.first[2]+" "+features.get(i).second.second[0]+" "+features.get(i).second.second[1]);
        }
    }
}
