package com.customplugin.activeseg.filters;


import activeSegmentation.IFilter;
import com.customplugin.activeseg.filter_core.GLCMTextureDescriptors;
import com.customplugin.activeseg.filter_core.utility;
import ij.IJ;
import ij.gui.Roi;
import ij.process.ImageProcessor;
import ijaux.scale.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 */


public class GLCM_filter_ implements IFilter {

	public static boolean debug=IJ.debugMode;
	private boolean isEnabled=true;
	public static final int [] DIRECTIONS = {270,360,90,180};
	public static final int [] DISTANCES = {1};
	public static final String ASM_FEATURE_KEY = "ASM";
	public static final String CONTRAST_FEATURE_KEY = "contrast";
	public static final String CORRELATION_FEATURE_KEY = "correlation";
	public static final String DISSIMILARITY_FEATURE_KEY = "dissimilarity";
	public static final String ENERGY_FEATURE_KEY = "energy";
	public static final String ENTROPY_FEATURE_KEY = "entropy";
	public static final String HOMOGENEITY_FEATURE_KEY = "homogeneity";
	private ArrayList<Pair<String,Pair<String[],Double[]>>> feature_vector = new ArrayList<>();

	/* NEW VARIABLES*/

	/** A string key identifying this factory. */
	private final  String FILTER_KEY = "GLCM";

	/** The pretty name of the target detector. */
	private final String FILTER_NAME = "Texture Descriptors Filter";

	private final int TYPE=1;
	// 1 Means Segmentation
	// 2 Means Classification

	private Map<String, String> settings= new HashMap<>();


	public void filter(ImageProcessor ip,String roi_name){

		GLCMTextureDescriptors glcm = new GLCMTextureDescriptors();

		// For all directions and distances calculate GLCM descriptors for a given roi

		for (int angle: DIRECTIONS){
			for(int distance:DISTANCES){
				//GLCMTextureDescriptors glcm = new GLCMTextureDescriptors();

				// Using same GLCM object by changing parameters
				glcm.set_values(distance,angle);

				// calculate normalised GLCM matrix for a given roi & d & angle
				glcm.extractGLCMDescriptors(ip);

				// using this glcm matrix we calculate va;ues of different features in GLCMTextureDescriptors
				add_descriptorToFeature(glcm,roi_name,angle,distance,ASM_FEATURE_KEY);
				add_descriptorToFeature(glcm,roi_name,angle,distance,CONTRAST_FEATURE_KEY);
				add_descriptorToFeature(glcm,roi_name,angle,distance,CORRELATION_FEATURE_KEY);
				add_descriptorToFeature(glcm,roi_name,angle,distance,DISSIMILARITY_FEATURE_KEY);
				add_descriptorToFeature(glcm,roi_name,angle,distance,ENERGY_FEATURE_KEY);
				add_descriptorToFeature(glcm,roi_name,angle,distance,ENTROPY_FEATURE_KEY);
				add_descriptorToFeature(glcm,roi_name,angle,distance,HOMOGENEITY_FEATURE_KEY);

			}
		}
    }

    private void add_descriptorToFeature(GLCMTextureDescriptors glcm, String roi_name, int angle, int distance, String feature_key){

		//encoding starts
		String[] angle_distance_pair = new String[3];
		Double[] feature_value = new Double[2];
		Pair<String[],Double[]> one_descriptor = new Pair<>(angle_distance_pair,feature_value);
		Pair<String,Pair<String[],Double[]>> one_roi_texture_descriptors = new Pair<>("",one_descriptor);

		angle_distance_pair[0] = feature_key;
		angle_distance_pair[1] = Integer.toString(angle);
		angle_distance_pair[2] = Integer.toString(distance);

		// particular feature encoding
		one_descriptor.first = angle_distance_pair;

		if (feature_key==ASM_FEATURE_KEY){
			one_descriptor.second[0] = glcm.getAngular2ndMoment();
			one_descriptor.second[1] = 0.0;
		}
		else if(feature_key==CORRELATION_FEATURE_KEY){
			one_descriptor.second[0] = glcm.getCorrelation();
			one_descriptor.second[1] = 0.0;
		}
		else if(feature_key==CONTRAST_FEATURE_KEY){
			one_descriptor.second[0] = glcm.getContrast();
			one_descriptor.second[1] = 0.0;
		}
		else if(feature_key==DISSIMILARITY_FEATURE_KEY){
			one_descriptor.second[0] = glcm.getDissimilarity();
			one_descriptor.second[1] = 0.0;
		}
		else if(feature_key==ENERGY_FEATURE_KEY){
			one_descriptor.second[0] = glcm.getEnergy();
			one_descriptor.second[1] = 0.0;
		}
		else if(feature_key==ENTROPY_FEATURE_KEY){
			one_descriptor.second[0] = glcm.getEntropy();
			one_descriptor.second[1] = 0.0;
		}
		else if(feature_key==HOMOGENEITY_FEATURE_KEY){
			one_descriptor.second[0] = glcm.getHomogeneity();
			one_descriptor.second[1] = 0.0;
		}

		one_roi_texture_descriptors.first = roi_name;
		one_roi_texture_descriptors.second = one_descriptor;

		//added to feature vector
		feature_vector.add(one_roi_texture_descriptors);
	}

	/* Saves the current settings of the plugin for further use
	 * 
	 *
	 * @param prefs
	 */
	public void savePreferences(Properties prefs) {
		//prefs.put(DEGREE, Integer.toString(degree));
	}

	@Override
	public Map<String, String> getDefaultSettings() {
		//settings.put(DEGREE, Integer.toString(degree));
		return this.settings;
	}

	@Override
	public boolean reset() {
		//degree= Prefs.getInt(DEGREE, 3);
		return true;
	}

	@Override
	public boolean updateSettings(Map<String, String> settingsMap) {
		//degree=Integer.parseInt(settingsMap.get(DEGREE));
		return true;
	}

	@Override
	public void applyFilter(ImageProcessor imageProcessor, String s, List<Roi> list) {

        // if asked for GLCM descriptors of ROIs
        if(list != null &&  list.size()>0){
            for(int i=0;i<list.size();i++){
                imageProcessor.setRoi(list.get(i));
                ImageProcessor ip_roi = imageProcessor.crop();
                //utility.display_image(ip_roi);
                filter(ip_roi,list.get(i).getName());
            }
        }
		// if asked for GLCM of image, we do not have any use case where we need both at a time
		else{
            System.out.println("Image is not null");
			filter(imageProcessor,s);
		}

	}

	@Override
	public String getKey() {
		return this.FILTER_KEY;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.FILTER_NAME;
	}

	@Override
	public Image getImage(){
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isEnabled;
	}

	@Override
	public void setEnabled(boolean isEnabled) {
		// TODO Auto-generated method stub
		this.isEnabled= isEnabled;
	}

	@Override
	public int getFilterType() {
		// TODO Auto-generated method stub
		return this.TYPE;
	}

	@Override
	public ArrayList<Pair<String,Pair<String[],Double[]>>> getFeatures() {
		// TODO Auto-generated method stub
		return feature_vector;
	}

}
