package com.caikang.com.tester;

import com.pitch.PitchEstimation;
import com.rule.GetLoudnessSeq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateFinalTxt {
	public static void main(String[] args) throws Exception {
		List<String[]> list = ExcelReader.readExcel("/Users/karl/Work/database/emotionTester/a_train/20160626-情绪训练集/20160619.xls");
		List<Integer> origlist = new ArrayList<Integer>();
		List<Integer> candlist = new ArrayList<Integer>();
		List<Integer> truelist = new ArrayList<Integer>();
		String dirpath = "/Users/karl/Work/database/emotionTester/a_train/20160626-情绪训练集/audio/";
		String resultsfile = dirpath + "delete.txt";
		FileOutputStream fileoutput = new FileOutputStream(new File(resultsfile));
		int rejection = 0;
		int positive = 0;
		int negative = 0;
		int accuracy = 0;
		int recall = 0;
		int r1 = 0;
		int r2 = 0;
		int total =0;
		int totalreturn = 0;
		File file = new File(dirpath);
		String[] files = file.list();
		for(int i = 1; i <= 1482; i++) {
			String filename = dirpath + i + ".wav";
			File f = new File(filename);
			if(!f.exists()) {
				fileoutput.write("0\n".getBytes());
				continue;
			}
			FileInputStream fis = new FileInputStream(filename);
			byte[] temp = new byte[fis.available()];
			fis.read(temp);
			fis.close();
			byte[] buffer = Arrays.copyOfRange(temp, 44, temp.length);
			String label = list.get(i-1)[2];
			PitchEstimation.process(buffer);
			GetLoudnessSeq.process(buffer, buffer.length, PitchEstimation.getPoint());
			boolean problem = false;
			if(!label.isEmpty()) {
				truelist.add(i);
				System.out.print("R ");
			}
			if(PitchEstimation.countAngry >= 25) {
				candlist.add(i);
			}
			if(PitchEstimation.angryScore >= 0.5) {
				if(GetLoudnessSeq.getLoudnessmax() >= 2.5) {
					//System.out.println(i + ": " + PitchEstimation.isAngry() + " " + GetLoudnessSeq.getLoudnessmax() + " " + PitchEstimation.countAngry);
					origlist.add(i);
					System.out.print("T ");
					r1++;
					problem = true;
				}
			}
			System.out.println(i + ": " + PitchEstimation.angryScore + " " + GetLoudnessSeq.getLoudnessmax() + " " + PitchEstimation.angryfreqratio + " " + PitchEstimation.countAngry);
			if(problem)
				fileoutput.write("1\n".getBytes());
			else
				fileoutput.write("0\n".getBytes());
			/*
			else if(GetLoudnessSeq.getLoudnessmax() >= 6) {
				System.out.println(i + ": " + PitchEstimation.isAngry() + " " + GetLoudnessSeq.getLoudnessmax());
				r1++;
				problem = true;
			}
			*/
			/*
			else if(PitchEstimation.getLongestContinous() >= 75 || PitchEstimation.getSecondContinous() >= 55) {
				if(GetLoudnessSeq.getLoudnessmax() >= 2.3) {
					System.out.println(i + ": " + PitchEstimation.getLongestContinous() + " " + PitchEstimation.getSecondContinous());
					r2++;
					problem = true;
				}
			}	
			*/
			if(!label.isEmpty()) {
				negative++;
				if(problem)
					recall++;
			}
			if(label.isEmpty()) {
				positive++;
				if(!problem)
					accuracy++;
				else {
					//System.out.println(i + ": " + PitchEstimation.isAngry() + " " + GetLoudnessSeq.getLoudnessmax() + " " + PitchEstimation.countAngry);
				}
			}
			if(problem)
				totalreturn++;
			total++;
		}
		System.out.println("共返回:" + totalreturn + "\nr1:" + r1 + "\nr2:" + r2 + "\n正样本:" + positive + " 正对:"+ accuracy + "\n负样本:" + negative + " 负对:" + recall + "\n");
		fileoutput.close();
	}
}
