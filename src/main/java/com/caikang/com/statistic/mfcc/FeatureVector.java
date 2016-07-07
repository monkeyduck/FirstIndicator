package com.caikang.com.statistic.mfcc;


import java.io.Serializable;

/**
 * 
 * @author Ganesh Tiwari for storing all coeffs of spectral features<br>
 *         include mfcc + include engergy 
 */
public class FeatureVector implements Serializable {

	/**
	 * 2d array of feature vector, dimension=noOfFrame*noOfFeatures
	 */
	private double[] featureVector;// all

	public FeatureVector() {
	}

	public int getNoOfFrames() {
		return featureVector.length;
	}

	public int getNoOfFeatures() {
		return featureVector.length;
	}

	/**
	 * returns feature vector
	 * 
	 * @return
	 */
	public double[] getFeatureVector() {
		return featureVector;
	}

	/**
	 * sets the feature vector array
	 * 
	 * @param featureVector
	 */
	public void setFeatureVector(double[] featureVector) {
		this.featureVector = featureVector;
	}
}
