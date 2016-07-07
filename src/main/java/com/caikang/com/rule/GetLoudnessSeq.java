package com.caikang.com.rule;

import java.util.ArrayList;

public class GetLoudnessSeq {
	static ArrayList<Double> list;
	static double loudnessmax;
	static double loudnessmean;
	static int winlen = 256;
	
	public static ArrayList<Double> getSeq(){
		return list;
	}
	public static double getLoudnessmax(){
		return loudnessmax;
	}
	public static double getLoudnessmean(){
		return loudnessmean;
	}
	public static void process(double[] buffer, ArrayList<Integer> point) {
		loudnessmean = 0;
		loudnessmax = 0;
		list = new ArrayList<Double>();
		double[] dp = new double[buffer.length];
		dp[0] = buffer[0] * buffer[0];
		for(int i = 1; i < buffer.length; i++) 
			dp[i] = dp[i-1] + buffer[i] * buffer[i];
		for(int d : point) {		
			int start = (int)(winlen/2) * d;
			int end = start + winlen - 1;
			if(end > buffer.length)
				continue;
			double loudness = dp[end] - dp[start];
			list.add(loudness);
			//System.out.print(loudness + " ");
			loudnessmean += loudness;
			if(loudnessmax < loudness)
				loudnessmax = loudness;
		}
		loudnessmean = loudnessmean * 1.0 / point.size();
	}
	public static void process(byte[] wavbyte, int nbytes, ArrayList<Integer> point) {
		double[] buffer = convertByteToDoubleArray(wavbyte, nbytes);
		process(buffer, point);
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
