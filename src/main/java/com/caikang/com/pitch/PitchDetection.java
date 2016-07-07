package com.caikang.com.pitch;

import java.io.*;
import java.util.ArrayList;

public class PitchDetection {

	private static PitchDetection yinInstance;
	private final double threshold = 0.15;

	private final int bufferSize;
	private final int overlapSize;
	private final float sampleRate;
	private volatile boolean running;
	private final float[] inputBuffer;
	private final float[] yinBuffer;

	private PitchDetection(float sampleRate) {
		this.sampleRate = sampleRate;
		bufferSize = 256; 
		overlapSize = bufferSize / 2;
		running = true;
		inputBuffer = new float[bufferSize];
		yinBuffer = new float[bufferSize/2];
	}

	private void difference(){
		float delta;
		for(int tau = 0; tau < yinBuffer.length; tau++) {
			yinBuffer[tau] = 0;
		}
		// yinBuffer.length
		for(int tau = 1 ; tau < yinBuffer.length ; tau++) {
			for(int j = 0 ; j < yinBuffer.length ; j++) {
				delta = inputBuffer[j] - inputBuffer[j+tau];
				yinBuffer[tau] += delta * delta;
			}
		}
	}

	private void cumulativeMeanNormalizedDifference() {
		yinBuffer[0] = 1;
		float runningSum = yinBuffer[1];
		yinBuffer[1] = 1;
		for(int tau = 2 ; tau < yinBuffer.length ; tau++) {
			runningSum += yinBuffer[tau];
			yinBuffer[tau] *= tau / runningSum;
		}
	}


	private int absoluteThreshold(){
		for(int tau = 1; tau < yinBuffer.length; tau++){
			if (yinBuffer[tau] < threshold){
				while(tau + 1 < yinBuffer.length && yinBuffer[tau+1] < yinBuffer[tau]) {
					tau++;
				}
				return tau;
			}
		}
		return -1;
	}

	private float parabolicInterpolation(int tauEstimate) {
		float s0, s1, s2;
		int x0 = (tauEstimate < 1) ? tauEstimate : tauEstimate - 1;
		int x2 = (tauEstimate + 1 < yinBuffer.length) ? tauEstimate + 1 : tauEstimate;
		if (x0 == tauEstimate)
			return (yinBuffer[tauEstimate] <= yinBuffer[x2]) ? tauEstimate : x2;
		if (x2 == tauEstimate)
			return (yinBuffer[tauEstimate] <= yinBuffer[x0]) ? tauEstimate : x0;
		s0 = yinBuffer[x0];
		s1 = yinBuffer[tauEstimate];
		s2 = yinBuffer[x2];
		return tauEstimate + 0.5f * (s2 - s0 ) / (2.0f * s1 - s2 - s0);
	}

	private float getPitch() {
		int tauEstimate = -1;
		float pitchInHertz = -1;
		difference();
		cumulativeMeanNormalizedDifference();
		tauEstimate = absoluteThreshold();
		if (tauEstimate != -1) {
			 float betterTau = parabolicInterpolation(tauEstimate);
			 
			pitchInHertz = sampleRate/betterTau;
		}
		return pitchInHertz;
	}
	
	public static ArrayList<Float> writeFile(String fileName) throws IOException {
		InputStream is = new FileInputStream(new File(fileName));
		AudioFloatInputStream afis = AudioFloatInputStream.getInputStream(is);
		return PitchDetection.processStream(afis);
	}
	
	public static ArrayList<Float> writeFile(byte[] buf) throws IOException {
		InputStream is = new ByteArrayInputStream(buf); 
		AudioFloatInputStream afis = AudioFloatInputStream.getInputStream(is);
		return PitchDetection.processStream(afis);
	}

	public static ArrayList<Float> processStream(AudioFloatInputStream afis) throws IOException {
		float sampleRate = 16000f;
		yinInstance = new PitchDetection(sampleRate);
		int bufferStepSize = yinInstance.bufferSize - yinInstance.overlapSize;

		boolean hasMoreFloats = afis.read(yinInstance.inputBuffer, 0, yinInstance.bufferSize) != -1;
		ArrayList<Float> to=new ArrayList<Float>();
		while(hasMoreFloats && yinInstance.running) {
			float pitch = yinInstance.getPitch();
			to.add(pitch);
			for(int i = 0 ; i < bufferStepSize ; i++) {
				yinInstance.inputBuffer[i] = yinInstance.inputBuffer[i+yinInstance.overlapSize];
			}
			hasMoreFloats = afis.read(yinInstance.inputBuffer, yinInstance.overlapSize, bufferStepSize) != -1;
		}
		//fos.close();
		return to;
	}

	public static void stop() {
		if (yinInstance != null)
			yinInstance.running = false;
	}

}
