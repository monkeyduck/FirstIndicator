package com.caikang.com.statistic;

import com.caikang.com.statistic.mfcc.FeatureExtract;
import com.caikang.com.statistic.mfcc.PreProcess;

import java.util.ArrayList;
import java.util.Arrays;

public class GetMfccSeq {
	static ArrayList<double[]> list;
	static int winlen = 256;
	static int sampleRate = 16000;
	public static ArrayList<double[]> getSeq(){
		return list;
	}
	public static double[] getmfcc(double[] buffer, ArrayList<Integer> point) {
		list = new ArrayList<double[]>();
		float[] floatArray = new float[buffer.length];
		for (int i = 0 ; i < buffer.length; i++) 
			floatArray[i] = (float) buffer[i];
		float[] signal = new float[winlen];
		for(int d : point) {		
			int start = (int)(winlen/2) * d; // key step
			int end = start + winlen;
			signal = Arrays.copyOfRange(floatArray, start, end);
			float[] windowedSignal = new PreProcess().getWindowedSignal(signal, winlen, sampleRate);
			FeatureExtract fe = new FeatureExtract(windowedSignal, sampleRate, winlen);
			fe.makeMfccFeatureVector();
			double[] mfccFeatures = fe.getFeatureVector().getFeatureVector();
			list.add(mfccFeatures);
		}
		return integration();
	}
	public static double[] getmfcc(byte[] wavbyte, int nbytes, ArrayList<Integer> point) {
		double[] buffer = convertByteToDoubleArray(wavbyte, nbytes);
		return getmfcc(buffer, point);
	}

	private static double[] integration() {
		double[] sumda = new double[13];
		for(double[] da : list) 
			for(int i = 0; i < 13; i++) 
				sumda[i] += da[i];
		for(int i = 0; i < 13; i++) 
			sumda[i] /= list.size();
		return sumda;
	}
	private static double[] convertByteToDoubleArray(byte[] wavbyte, int nbytes) {
		int bytesPerSample = 2;
		int floatScale = 65536;
		int nframes = (int)(nbytes / bytesPerSample);
		double[] buffer = new double[nframes];
		for (int f = 0 ; f < nframes ; f++) {
			int val = 0;
			int v1 = wavbyte[f * bytesPerSample];
			int v2 = wavbyte[f * bytesPerSample + 1];
			val += v1 & 0xFF;
			val += v2 << 8;
			buffer[f] = (double) val / floatScale;
		}			
		return buffer;
	}
}
