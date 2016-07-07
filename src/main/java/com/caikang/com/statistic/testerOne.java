package com.caikang.com.statistic;

import com.caikang.com.pitch.PitchEstimation;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class testerOne {
	public static void main(String[] args) throws Exception {
		String filename = "/Users/karl/Work/database/emotionTester/a_train/1.wav";
		FileInputStream fis = new FileInputStream(filename);
		byte[] temp = new byte[fis.available()];
		fis.read(temp);
		fis.close();
		byte[] buffer = Arrays.copyOfRange(temp, 44, temp.length);
		PitchEstimation.process(buffer);
		System.out.println(PitchEstimation.getFullContent());
		System.out.println(PitchEstimation.getPoint());
		double[] mfcc = GetMfccSeq.getmfcc(buffer, buffer.length, PitchEstimation.getPoint());
		ArrayList<double[]> list = GetMfccSeq.getSeq();
		System.out.println(Arrays.toString(mfcc));
		for(double[] da : list) {
			System.out.println(Arrays.toString(da));
		}
	}
}
