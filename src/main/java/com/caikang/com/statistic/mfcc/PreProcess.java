package com.caikang.com.statistic.mfcc;

import java.util.Arrays;

public class PreProcess {

	float[] originalSignal;// initial extracted PCM,
	public int noOfFrames;// calculated total no of frames
	int samplePerFrame;// how many samples in one frame
	int framedArrayLength;// how many samples in framed array
	private float[] framedSignal;
	float[] hammingWindow;
	int samplingRate;

	public float[] getWindowedSignal(float[] originalSignal, int samplePerFrame, int samplingRate) {
		this.originalSignal = originalSignal;
		this.samplingRate = samplingRate;
		framedSignal = new float[samplePerFrame];
		framedSignal = Arrays.copyOf(originalSignal, samplePerFrame);
		doWindowing();
		return framedSignal;
	}

	private void doWindowing() {
		hammingWindow = new float[samplePerFrame + 1];
		for (int i = 1; i <= samplePerFrame; i++) 
			hammingWindow[i] = (float) (0.54 - 0.46 * (Math.cos(2 * Math.PI * i / samplePerFrame)));
		for (int j = 0; j < samplePerFrame; j++) {
			framedSignal[j] = framedSignal[j] * hammingWindow[j + 1];
		}
	}
}
