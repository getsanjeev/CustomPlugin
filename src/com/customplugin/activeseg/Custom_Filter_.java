package com.customplugin.activeseg;


import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.Prefs;

import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ijaux.scale.GScaleSpace;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

















import activeSegmentation.IFilter;


/**
 */


public class Custom_Filter_ implements IFilter {


	public static boolean debug=IJ.debugMode;
	public final static String SIGMA="LOG_sigma", LEN="G_len",MAX_LEN="G_MAX", ISSEP="G_SEP", SCNORM="G_SCNORM";

	private  int sz= Prefs.getInt(LEN, 2);
	private  int max_sz= Prefs.getInt(MAX_LEN, 8);
	public  boolean sep= Prefs.getBoolean(ISSEP, true);

	public  boolean scnorm= Prefs.getBoolean(SCNORM, false);


	private boolean isEnabled=true;



	/* NEW VARIABLES*/

	/** A string key identifying this factory. */
	private final  String FILTER_KEY = "CustomFilter";

	/** The pretty name of the target detector. */
	private final String FILTER_NAME = "Custom Filter";

	private final int TYPE=1;
	// 1 Means Segmentation
	// 2 Means Classification
	private Map< String, String > settings= new HashMap<String, String>();




	@Override
	public void applyFilter(ImageProcessor image, String filterPath) {

	

	}

	






	/* Saves the current settings of the plugin for further use
	 * 
	 *
	 * @param prefs
	 */
	public void savePreferences(Properties prefs) {
		prefs.put(LEN, Integer.toString(sz));
		prefs.put(ISSEP, Boolean.toString(sep));
		prefs.put(SCNORM, Boolean.toString(scnorm));

	}

	@Override
	public Map<String, String> getDefaultSettings() {

		settings.put(LEN, Integer.toString(sz));
		settings.put(MAX_LEN, Integer.toString(max_sz));
		settings.put(ISSEP, Boolean.toString(sep));
		settings.put(SCNORM, Boolean.toString(scnorm));

		return this.settings;
	}

	@Override
	public boolean reset() {
		sz= Prefs.getInt(LEN, 2);
		max_sz= Prefs.getInt(MAX_LEN, 8);
		sep= Prefs.getBoolean(ISSEP, true);
		return true;
	}


	@Override
	public boolean updateSettings(Map<String, String> settingsMap) {
		sz=Integer.parseInt(settingsMap.get(LEN));
		max_sz=Integer.parseInt(settingsMap.get(MAX_LEN));
		sep=Boolean.parseBoolean(settingsMap.get(ISSEP));
		scnorm=Boolean.parseBoolean(settingsMap.get(SCNORM));

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
	public <T> T getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}



}
