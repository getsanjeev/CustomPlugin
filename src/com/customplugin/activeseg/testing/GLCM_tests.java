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

    public static String PATH_CONSTANT_UNITY = "/media/albus/Horcrux/CustomPlugin/test_images/constant_unity.png";
    public static String PATH_ARTIFICIAL = "/media/albus/Horcrux/CustomPlugin/test_images/test2_artificial.png";
    public static String PATH_ENCODING_TEST = "/media/albus/Horcrux/CustomPlugin/test_images/encoding_test_image.png";

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
        ArrayList<Pair<String,Pair<String[],Double>>> features = glcm.getFeatures();
        System.out.println("THE SIZE OF FEATURES "+features.size());
        for(int i= 0;i<features.size();i++){
            System.out.println(features.get(i).first+" "+features.get(i).second.first[0]+" "+features.get(i).second.first[1]+" "+features.get(i).second.first[2]+" "+features.get(i).second.second);
        }
    }
}
