package com.customplugin.activeseg.filters;


import activeSegmentation.IFilter;
import com.customplugin.activeseg.filter_core.LegendreMoments_elm;
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

	private  int degree = Prefs.getInt(DEGREE, 5);
	private boolean isEnabled=true;

	/*List< Pair<String, T: Pair<Int[], Double>> with
	int[0] m
	int[1] n*/

	private ArrayList<Pair<String,Pair<Integer[],Double>>> moment_vector = new ArrayList<>();

	/* NEW VARIABLES*/

	/** A string key identifying this factory. */
	private final  String FILTER_KEY = "LM";

	/** The pretty name of the target detector. */
	private final String FILTER_NAME = "Legendre Moment Filter";

	private final int TYPE=1;
	// 1 Means Segmentation
	// 2 Means Classification

	private Map<String, String> settings= new HashMap<String, String>();


	public void filter(ImageProcessor ip,String roi_name){
        double moment_matrix[][] = new LegendreMoments_elm(degree,degree).extractLegendreMoment(ip);
        Integer[] order_index = new Integer[2];
        Pair<Integer[],Double> order = new Pair<>(order_index,0.0);
		Pair<String,Pair<Integer[],Double>> one_roi_moment = new Pair<>("",order);
        for(int i=0;i<=degree;i++){
            for(int j=0;j<=degree;j++){
            	order_index[0] = i;
            	order_index[1] = j;
            	order.first = order_index;
            	order.second = moment_matrix[i][j];
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
		degree= Prefs.getInt(DEGREE, 5);
		return true;
	}

	@Override
	public boolean updateSettings(Map<String, String> settingsMap) {
		degree=Integer.parseInt(settingsMap.get(DEGREE));
		return true;
	}

	@Override
	public void applyFilter(ImageProcessor imageProcessor, String s, List<Roi> list) {

		// if asked for moment of image, we do not have any use case where we need both at a time
		if(imageProcessor!=null){
			filter(imageProcessor,s);
		}
		// if asked for moment of ROIs
		else {
			if(list.size()<1){
				for(int i=0;i<list.size();i++){
					filter(list.get(i).getImage().getProcessor(),list.get(i).getName());
				}
			}
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
	public ArrayList<Pair<String,Pair<Integer[],Double>>> getFeatures() {
		// TODO Auto-generated method stub
		return moment_vector;
	}

}
