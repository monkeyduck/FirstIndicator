package com.caikang.com.pitch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PitchEstimation {
	private static double lowfreqratio;
	private static boolean isSalient_20_280;
	private static boolean isSalient_20_270;
	private static boolean isSalient_12_280;
	private static boolean isSalient_15;
	private static int speechnum;
	private static int speechcount;
	private static int audiocount;
	private static String fullcontent;
	private static int firstpos;
	private static List<Clip> list;
	private static ArrayList<Integer> point;
	private static int longestContinous;
	private static int secondContinous;
	
	public static double angryScore;
	public static double angryfreqratio;
	public static int countAngry;
	public static double getLowf() {
		return lowfreqratio;
	}
	public static int getSpeechNum() {
		return speechnum;
	}
	public static int getFirstpos() {
		return firstpos;
	}
	public static int getSpeechCount() {
		return speechcount;
	}
	public static int getAudioCount() {
		return audiocount;
	}
	public static boolean isSalient_20_280() {
		return isSalient_20_280;
	}
	public static boolean isSalient_20_270() {
		return isSalient_20_270;
	}
	public static boolean isSalient_12_280() {
		return isSalient_12_280;
	}
	public static boolean isSalient_15() {
		return isSalient_15;
	}
	public static int getLongestContinous() {
		return longestContinous;
	}
	public static int getSecondContinous() {
		return secondContinous;
	}
	public static String getFullContent() {
		return fullcontent;
	}
	public static ArrayList<Integer> getPoint() {
		return point;
	}
	public static void print() {
		System.out.println("Clip:");
		for(Clip clip: list) {
			System.out.println(clip.getNum() + " " + clip.getMean());
		}
	}
	public static void process(byte[] buf){
		fullcontent = "";
		firstpos = -1;
		list = new ArrayList<Clip> ();
		point = new ArrayList<Integer> ();
		angryScore = 0;
		longestContinous = 0;
		secondContinous = 0;
		countAngry = 0;
		angryfreqratio = 0;
		int continousParameter = 5;
		int totalnum = 0;
		int sum = 0;
		int countspeech=0;
		int num = 0;
		int lowfreqnum = 0;
		int tempfirstpos = -1;
		int countContinous = 0;
		
		ArrayList<Float> to = null;
		try {
			to= PitchDetection.writeFile(buf);
			audiocount = to.size();
			int change = 0;
			for(int j = 0; j < to.size(); j++){
				if(getSpeech(to.get(j)) == 1) {
					countspeech = countspeech + 1;
					if(tempfirstpos == -1)
						tempfirstpos = j;
				}
				else if(to.get(j) > Constants.fu) //Human upper limitation
					continue;
				if(to.get(j) > 0){
					change = 0;
					int tmp = Math.round((to.get(j)));
					if(tmp < Constants.mu)
						lowfreqnum++;
					else if(tmp > Constants.mu)
						point.add(j);
					if(tmp > 455)
						countAngry++;
					sum += tmp;
					fullcontent += ((String.valueOf(tmp)) + " ");
					num++;
					totalnum++;
					countContinous++;
				}
				else{
					if(change == continousParameter && num >= 2) {
						int mean = (Math.round(sum)/num);
						Clip c = new Clip(num, mean);
						list.add(c);
						if(firstpos == -1)
							firstpos = tempfirstpos;
						num = 0;
						sum = 0;
					}
					else if(change == continousParameter) {
						num = 0;
						tempfirstpos = -1;
					}
					change++;
					fullcontent+=(" . ");
					if(countContinous > longestContinous) {
						if(longestContinous > 0)
							secondContinous = longestContinous;
						longestContinous = countContinous;
					}
					else if(countContinous > secondContinous) 
						secondContinous = countContinous;
					countContinous = 0;
				}	
			}
			if(num != 0){
				int mean = (Math.round(sum)/num);
				Clip c = new Clip(num, mean);
				list.add(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(totalnum > 0) {
			lowfreqratio = (double)(lowfreqnum * 1.0)/ totalnum;
			angryfreqratio = (double)(countAngry * 1.0)/ totalnum;
		}
		speechnum = list.size();
		speechcount = countspeech;
		isSalient_20_280 = func_isSalient(list, 20, 280);
		isSalient_20_270 = func_isSalient(list, 20, 270);
		isSalient_12_280 = func_isSalient(list, 12, 280);
		isSalient_15 = func_isSalient(list, 12, 90);
		if(speechcount == 0)
			angryScore = 0;
		else if(angryfreqratio >= 0.32) {
			angryScore = 1.0;
		}
	}
	private static class Clip {
		int num, mean;
		Clip(int num, int mean) {
			this.num = num;
			this.mean = mean;
		}
		int getNum() {
			return num;
		}
		int getMean() {
			return mean;
		}
	}
	private static boolean func_isSalient(List<Clip> list, int numt, int meant) {
		for(Clip c : list) {
			if(c.getNum() >= numt && c.getMean() > meant) 
				return true;
		}
		return false;
	}
	/*
	private static int Median(ArrayList<Integer> to)
    {
        Collections.sort(to);
        if(to.size()==0){
        	return 0;
        }
        if (to.size() % 2 == 1)
    	return to.get((to.size()+1)/2-1);
        else
        {
    	int lower = to.get(to.size()/2-1);
    	int upper = to.get(to.size()/2);
     
    	return (int) Math.round((lower + upper) / 2.0);
        }	
    }
	*/
	/** speech estimation algorithm */
	private static int getSpeech(double pitch) {
		// uncertain
		int speech = 0;	
		
		if (pitch > Constants.ml && pitch < Constants.fu) {
			speech = 1;	
		}
		else {
			speech = 0;	
		}
		return speech;
	}
	
	
}
