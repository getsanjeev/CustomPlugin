package com.customplugin.activeseg;


import activeSegmentation.IFilter;
import com.customplugin.activeseg.filter_core.LegendreMoments_elm;
import ij.IJ;
import ij.Prefs;
import ij.process.ImageProcessor;
import ijaux.scale.Pair;

import java.awt.*;
import java.util.*;


/**
 */


public class Legendre_filter_ implements IFilter {

	public static boolean debug=IJ.debugMode;
	public final static String DEGREE = "Degree";

	private  int degree = Prefs.getInt(DEGREE, 5);
	private boolean isEnabled=true;
	private ArrayList<Pair<Pair<Integer, Integer>, Double>> moment_vector = new ArrayList<>();

	/* NEW VARIABLES*/

	/** A string key identifying this factory. */
	private final  String FILTER_KEY = "LM";

	/** The pretty name of the target detector. */
	private final String FILTER_NAME = "Legendre Moment Filter";

	private final int TYPE=1;
	// 1 Means Segmentation
	// 2 Means Classification

	private Map< String, String > settings= new HashMap<String, String>();

	@Override
	public void applyFilter(ImageProcessor image, String filterPath) {
	    filter(image);
	}

	private void filter(ImageProcessor ip){
        double moment_matrix[][] = new LegendreMoments_elm(degree,degree).extractLegendreMoment(ip);
        Pair<Integer,Integer> order = new Pair<>(0,0);
        Pair<Pair<Integer,Integer>,Double> one_moment = new Pair<>(order,0.0);
        for(int i=0;i<=degree;i++){
            for(int j=0;j<=degree;j++){
                order.first = i;
                order.second = j;
                one_moment.first = order;
                one_moment.second = moment_matrix[i][j];
                moment_vector.add(one_moment);
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
	public ArrayList<Pair<Pair<Integer, Integer>, Double>> getFeatures() {
		// TODO Auto-generated method stub
		return moment_vector;
	}

}
