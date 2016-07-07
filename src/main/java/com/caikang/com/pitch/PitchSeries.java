package com.caikang.com.pitch;

import java.util.ArrayList;

public class PitchSeries {
	public static void process(byte[] buf) {
		ArrayList<Float> to = null;
		try {
			to= PitchDetection.writeFile(buf);
			for(float a : to) {
				if(a > 0)
					System.out.print((int)a + " ");
				else
					System.out.print(" . ");
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
