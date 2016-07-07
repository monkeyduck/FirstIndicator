package com.caikang.com.rule;

import com.caikang.com.pitch.PitchEstimation;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class testerBatch {
	public static void main(String[] args) throws Exception {
		String dirpath = "/Users/karl/Work/database/emotionTester/a_train/train_06_17/";
		//String dirpath = "/Users/karl/Work/database/ageTester/a_train/";
		File file = new File(dirpath);
		String[] files = file.list();
		int count = 0;
		int ca = 0, cb = 0;
		for(String name : files) {
			if(name.length() < 4)
				continue;
			if(!name.substring(name.length() - 4, name.length()).equals(".wav"))
				continue;
			String filename = dirpath + name;
			FileInputStream fis = new FileInputStream(filename);
			byte[] temp = new byte[fis.available()];
			fis.read(temp);
			fis.close();
			byte[] buffer = Arrays.copyOfRange(temp, 44, temp.length);
			PitchEstimation.process(buffer);
			GetLoudnessSeq.process(buffer, buffer.length, PitchEstimation.getPoint());
			//angry为主判断,getLoudnessmax去除音量过低的
			if(PitchEstimation.angryScore == 1.0) {
				if(GetLoudnessSeq.getLoudnessmax() >= 1.5) {
					System.out.println(name + ": " + PitchEstimation.angryScore + " " + GetLoudnessSeq.getLoudnessmax());
					count++;
					ca++;
					continue;
				}
			}
			if(PitchEstimation.getLongestContinous() >= 70 || PitchEstimation.getSecondContinous() >= 50) {
				if(GetLoudnessSeq.getLoudnessmax() >= 2.3) {
					System.out.println(name + ": " + PitchEstimation.getLongestContinous() + " " + PitchEstimation.getSecondContinous());
					count++;
					cb++;
					continue;
				}
			}	
		}
		System.out.println(count);
		System.out.println(ca + " " + cb);
	}
}
