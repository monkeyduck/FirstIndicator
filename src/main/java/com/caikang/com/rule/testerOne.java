package com.caikang.com.rule;

import com.pitch.PitchEstimation;

import java.io.FileInputStream;
import java.util.Arrays;

public class testerOne {
	public static void main(String[] args) throws Exception {
		String filename = "/Users/karl/Work/database/emotionTester/a_train/train_06_17/36.wav";
		//String filename = "/Users/karl/Work/database/emotionTester/a_train/voiceNormal/6.wav";
		FileInputStream fis = new FileInputStream(filename);
		byte[] temp = new byte[fis.available()];
		fis.read(temp);
		byte[] buffer = Arrays.copyOfRange(temp, 44, temp.length);
		PitchEstimation.process(buffer);
		System.out.println(PitchEstimation.getFullContent());
		System.out.println(PitchEstimation.getPoint());
		System.out.println(PitchEstimation.getSpeechNum() + " " + PitchEstimation.getSpeechCount());
		GetLoudnessSeq.process(buffer, buffer.length, PitchEstimation.getPoint());
		System.out.println(GetLoudnessSeq.getLoudnessmax());
		System.out.println(PitchEstimation.angryScore + " " + PitchEstimation.getLongestContinous() + " " + PitchEstimation.getSecondContinous());
	}
}
