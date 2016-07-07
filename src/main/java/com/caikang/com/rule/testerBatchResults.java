package com.caikang.com.rule;

import com.caikang.com.pitch.PitchEstimation;
import com.caikang.com.utils.Copyfiles;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class testerBatchResults {
	public static void main(String[] args) throws Exception {
		String dirpath = "/Users/karl/Work/database/emotionTester/a_train/test_06_17/";
		String target1 = "/Users/karl/Work/database/emotionTester/a_train/不爽/";
		String target2 = "/Users/karl/Work/database/emotionTester/a_train/无聊/";
		File file = new File(dirpath);
		String[] files = file.list();
		int count = 0;
		for(String name : files) {
			if(name.length() < 4)
				continue;
			if(!name.substring(name.length() - 4, name.length()).equals(".wav"))
				continue;
			String filename = dirpath + name;
			String targetname1 = target1 + name;
			String targetname2 = target2 + name;
			FileInputStream fis = new FileInputStream(filename);
			byte[] temp = new byte[fis.available()];
			fis.read(temp);
			fis.close();
			byte[] buffer = Arrays.copyOfRange(temp, 44, temp.length);
			PitchEstimation.process(buffer);
			GetLoudnessSeq.process(buffer, buffer.length, PitchEstimation.getPoint());
			//angry为主判断,getLoudnessmax去除音量过低的
			if(PitchEstimation.angryScore >= 0.5) {
				if(GetLoudnessSeq.getLoudnessmax() >= 1.5) {
					System.out.println(name + ": " + PitchEstimation.angryScore + " " + GetLoudnessSeq.getLoudnessmax());
					count++;
					Copyfiles.fileChannelCopy(filename, targetname1);
					continue;
				}
			}
			if(PitchEstimation.getLongestContinous() >= 70 || PitchEstimation.getSecondContinous() >= 45) {
				if(GetLoudnessSeq.getLoudnessmax() >= 2.3) {
					System.out.println(name + ": " + PitchEstimation.getLongestContinous() + " " + PitchEstimation.getSecondContinous());
					count++;
					Copyfiles.fileChannelCopy(filename, targetname2);
					continue;
				}
			}	
		}
		System.out.println(count);
	}
}
