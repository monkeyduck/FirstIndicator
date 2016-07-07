package com.caikang.com.statistic.mfcc;


public class Energy {

	/**
	 *
	 */
	private int samplePerFrame;

	/**
	 * 
	 * @param samplePerFrame
	 */
	public Energy(int samplePerFrame) {
		this.samplePerFrame = samplePerFrame;
	}

	/**
	 * 
	 * @param framedSignal
	 * @return energy of given PCM frame
	 */
	public double calcEnergy(float[] framedSignal) {
		double energyValue;
		float sum = 0;
		for (int j = 0; j < samplePerFrame; j++) {
			// sum the square
			sum += Math.pow(framedSignal[j], 2);
		}
		// find log
		energyValue = Math.log(sum);
		return energyValue;
	}
}
