package com.caikang.com.statistic.mfcc;

public class FeatureExtract {
	private float[] framedSignal;
	/**
	 * how many mfcc coefficients per frame
	 */
	private int numCepstra = 12;

	private double[] featureVector;
	private double[] mfccFeature;
	private double energyVal;
	private FeatureVector fv;
	private MFCC mfcc;
	private Energy en;

	// FeatureVector fv;
	/**
	 * constructor of feature extract
	 * 
	 * @param framedSignal
	 *            2-D audio signal obtained after framing
	 * @param samplePerFrame
	 *            number of samples per frame
	 */
	public FeatureExtract(float[] framedSignal, int samplingRate, int samplePerFrame) {
		this.framedSignal = framedSignal;
		mfcc = new MFCC(samplePerFrame, samplingRate, numCepstra);
		en = new Energy(samplePerFrame);
		fv = new FeatureVector();
		mfccFeature = new double[numCepstra + 1];
		featureVector = new double[numCepstra + 1];
	}

	public FeatureVector getFeatureVector() {
		return fv;
	}

	/**
	 * generates feature vector by combining mfcc, and its delta and delta
	 * deltas also contains energy and its deltas
	 */
	public void makeMfccFeatureVector() {
		calculateMFCC();
		doCepstralMeanNormalization();
		// energy
		energyVal = en.calcEnergy(framedSignal);
		for (int j = 0; j < numCepstra; j++) {
			featureVector[j] = mfccFeature[j];
		}
		featureVector[numCepstra] = energyVal;
		fv.setFeatureVector(featureVector);
		System.gc();
	}

	/**
	 * calculates MFCC coefficients of each frame
	 */
	private void calculateMFCC() {
		// for each frame i, make mfcc from current framed signal
		mfccFeature = mfcc.doMFCC(framedSignal);// 2D data
	}

	/**
	 * performs cepstral mean substraction. <br>
	 * it removes channel effect...
	 */
	private void doCepstralMeanNormalization() {
		double sum;
		double mean;
		double mCeps[] = new double[numCepstra - 1];// same size
		// as mfcc
		// 1.loop through each mfcc coeff
		for (int i = 0; i < numCepstra - 1; i++) {
			// calculate mean
			sum = 0.0;
			sum += mfccFeature[i];// ith coeff of all frame
			mean = sum;
			// subtract
			mCeps[i] = mfccFeature[i] - mean;
		}
	}
}
