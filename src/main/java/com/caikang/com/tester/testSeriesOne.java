package com.caikang.com.tester;

import com.pitch.PitchSeries;

import java.io.FileInputStream;
import java.util.Arrays;

public class testSeriesOne {
	public static void main(String[] args) throws Exception {
		String dirpath = "/Users/karl/Work/database/emotionTester/a_train/20160626-情绪训练集/audio/";
		String filename = dirpath + 835 + ".wav";
		FileInputStream fis = new FileInputStream(filename);
		byte[] temp = new byte[fis.available()];
		fis.read(temp);
		fis.close();
		byte[] buffer = Arrays.copyOfRange(temp, 44, temp.length);
		PitchSeries.process(buffer);
	}
}
