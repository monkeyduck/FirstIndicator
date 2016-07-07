package com.caikang.com.pitch;

import com.caikang.com.rule.GetLoudnessSeq;
import com.caikang.com.utils.OSSHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainLabel {
	private ArrayList<String> lines;
	private ArrayList<String> audioList;
    private OSSHelper ossHelper = new OSSHelper();
    private static final Logger logger = LoggerFactory.getLogger(MainLabel.class);

	public static int getLabel(String filename){
		FileInputStream fis;
		int label = 0;
		try {
			fis = new FileInputStream(filename);
			byte[] temp = new byte[fis.available()];
			fis.read(temp);
			fis.close();
			byte[] buffer = Arrays.copyOfRange(temp, 44, temp.length);
			PitchEstimation.process(buffer);
			GetLoudnessSeq.process(buffer, buffer.length, PitchEstimation.getPoint());
			if(PitchEstimation.angryScore >= 1.0 && GetLoudnessSeq.getLoudnessmax() >= 2.5)
				label = 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return label;
	}

//	public static void main(String args[]) throws IOException {
//		createAudioLabel();
//    }
    public boolean downloadOneAudioWav(String memberId, String recordId){
        try{
            ossHelper.download(memberId, recordId);

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

	public void downloadAllAudioWav(File rfile) throws IOException{
		lines = new ArrayList<String>();
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(rfile));
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line = "";
		while ((line=bufferedReader.readLine())!=null){
			lines.add(line);
			String segs[] = line.split("\t");
			String mem_id = segs[0];
			String record_id = segs[4].split("/")[segs[4].split("/").length-1];
			String audioPath = "audio/"+mem_id+"/"+record_id;
            downloadOneAudioWav(mem_id, record_id);
			audioList.add(audioPath);
		}
	}

	public ArrayList<Integer> createAudioLabel() throws IOException{
        ArrayList<Integer> labelList = new ArrayList<Integer>();
        File file = new File("annotation.txt");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (String audioRecord: audioList){
            int label = getLabel(audioRecord);
            labelList.add(label);
            bufferedWriter.write(""+label+"\n");
            System.out.println(label);
        }
		return labelList;
	}

    public void runPython() throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec("python Combine.py");
        proc.waitFor();
    }
}
