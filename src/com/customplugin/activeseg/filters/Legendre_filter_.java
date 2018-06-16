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
        for(int i=0;i<=degree;i++){
            for(int j=0;j<=degree;j++){
                Integer[] order_index = new Integer[2];
                Pair<Integer[],Double> order = new Pair<>(order_index,0.0);
                Pair<String,Pair<Integer[],Double>> one_roi_moment = new Pair<>("",order);
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
        System.out.println("okay in applyfilter");
        int x1,x3,y1,y3,x_diff,y_diff;

        // if asked for moment of ROIs
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                x1 = list.get(i).getPolygon().xpoints[0];
                y1 = list.get(i).getPolygon().ypoints[0];
                x3 = list.get(i).getPolygon().xpoints[2];
                y3 = list.get(i).getPolygon().ypoints[2];
                x_diff = x3-x1;
                y_diff = y3-y1;
                System.out.println(x1+" "+y1+" "+x_diff+" "+y_diff);
                //imageProcessor.setRoi(x1,y1,x_diff,y_diff);
                imageProcessor.setRoi(list.get(i));
                ImageProcessor ip_roi = imageProcessor.crop();
                System.out.println("THE ROI IS "+list.get(i).getName());
                utility.display_image(ip_roi);
                filter(ip_roi,list.get(i).getName());
                System.out.println("Size of list is "+ moment_vector.size());
            }
        }

		// if asked for moment of image, we do not have any use case where we need both at a time
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
	public ArrayList<Pair<String,Pair<Integer[],Double>>> getFeatures() {
		// TODO Auto-generated method stub
		return moment_vector;
	}

}
