package com.customplugin.activeseg.filters;


import activeSegmentation.IFilter;
import com.customplugin.activeseg.filter_core.LegendreMoments_elm;
import com.customplugin.activeseg.filter_core.utility;
import ij.IJ;
import ij.Prefs;
import ij.gui.Roi;
import ij.process.ImageProcessor;
import ijaux.scale.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 */


public class Legendre_filter_ implements IFilter {

	public static boolean debug=IJ.debugMode;
	public final static String DEGREE = "Degree";

	private  int degree = Prefs.getInt(DEGREE, 3);
	private boolean isEnabled=true;

	/*List< Pair<String, T: Pair<Int[], Double>> with
	int[0] m
	int[1] n*/
	private ArrayList<Pair<String,Pair<String[],Double[]>>> moment_vector = new ArrayList<>();

	//private Pair<String,Pair<String[],Double>> moment_vector = new Pair<String,Pair<String[],Double>>[33];

	/* NEW VARIABLES*/

	/** A string key identifying this factory. */
	private final  String FILTER_KEY = "LM";
	public static final String LM_FEATURE_KEY = "LM";

	/** The pretty name of the target detector. */
	private final String FILTER_NAME = "Legendre Moment Filter";

	private final int TYPE=1;
	// 1 Means Segmentation
	// 2 Means Classification

	private Map<String, String> settings= new HashMap<>();


	public void filter(ImageProcessor ip,String roi_name){
        double moment_matrix[][] = new LegendreMoments_elm(degree,degree).extractLegendreMoment(ip);
        for(int i=0;i<=degree;i++){
            for(int j=0;j<=degree;j++){
                String[] order_index = new String[3];
                Double[] moment_values = new Double[2];
                Pair<String[],Double[]> order = new Pair<>(order_index,moment_values);
                Pair<String,Pair<String[],Double[]>> one_roi_moment = new Pair<>("",order);
                order_index[0] = LM_FEATURE_KEY;
            	order_index[1] = Integer.toString(i);
            	order_index[2] = Integer.toString(j);
            	order.first = order_index;
            	order.second[0] = moment_matrix[i][j];
				order.second[1] = 0.0;
            	one_roi_moment.first = roi_name;
            	one_roi_moment.second = order;
                moment_vector.add(one_roi_moment);
            }
        }
    }

	/* Saves the current settings of the plugin for further use
	 * 
	 *
	 * @param prefs
	 */
	public void savePreferences(Properties prefs) {
		prefs.put(DEGREE, Integer.toString(degree));
	}

	@Override
	public Map<String, String> getDefaultSettings() {
		settings.put(DEGREE, Integer.toString(degree));
		return this.settings;
	}

	@Override
	public boolean reset() {
		degree= Prefs.getInt(DEGREE, 3);
		return true;
	}

	@Override
	public boolean updateSettings(Map<String, String> settingsMap) {
		degree=Integer.parseInt(settingsMap.get(DEGREE));
		return true;
	}

	@Override
	public void applyFilter(ImageProcessor imageProcessor, String s, List<Roi> list) {
        // if asked for moment of ROIs
        if(list != null && list.size()>0){
            for(int i=0;i<list.size();i++){
                imageProcessor.setRoi(list.get(i));
                ImageProcessor ip_roi = imageProcessor.crop();
                //utility.display_image(ip_roi);
                filter(ip_roi,list.get(i).getName());
            }
        }

		// if asked for moment of image, we do not have any use case where we need both at a time
		else{
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
		return moment_vector;
	}

}
