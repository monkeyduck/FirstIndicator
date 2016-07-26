package com.caikang.com.pitch;

import com.aliyun.oss.OSSClient;
import com.caikang.com.rule.GetLoudnessSeq;
import com.caikang.com.utils.HttpTookit;
import com.caikang.com.utils.OSSHelper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainLabel {
	private ArrayList<String> lines;
	private ArrayList<String> audioList;
    private ArrayList<String> audioKeyList;
    private OSSHelper ossHelper;
    private static final Logger logger = LoggerFactory.getLogger(MainLabel.class);

    public MainLabel(){
        lines = new ArrayList<String>();
        audioList = new ArrayList<String>();
        ossHelper = new OSSHelper();
        audioKeyList = new ArrayList<String>();
    }

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

    public static String getEmokitLabel(String filename){
        HttpTookit tookit = new HttpTookit();
        try{
            String resp = tookit.sendPost(filename);
            System.out.println(resp);
            JSONObject json = JSONObject.fromObject(resp);
            JSONObject json2 = JSONObject.fromObject(json.getString("infovoice"));
            return json2.getString("rc_main");
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return "null";
    }

//	public static void main(String args[]) throws IOException {
//		createAudioLabel();
//    }
    public boolean downloadAudioWavBatch(){
        try{
            for (String audioKey:audioKeyList){
                // It should be asynchronous
                ossHelper.download(audioKey);
            }
            ossHelper.getOssClient().shutdown();

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return true;
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
			String record_id = segs[3].split("/")[segs[3].split("/").length-1];
			String audioPath = "/home/llc/LogAnalysis/audio/"+mem_id+"-"+record_id;
            audioKeyList.add(mem_id+"@"+record_id);
			audioList.add(audioPath);
		}
        while(!downloadAudioWavBatch());
	}

	public ArrayList<Integer> createAudioLabel() throws IOException{
        ArrayList<Integer> labelList = new ArrayList<Integer>();
        File file = new File("/home/llc/LogAnalysis/py/annotation.txt");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (String audioRecord: audioList){
            int label = getLabel(audioRecord);
//            String emoRe = getEmokitLabel(audioRecord);
            labelList.add(label);
            bufferedWriter.write(""+label+"\n");
        }
        bufferedWriter.close();
        fileWriter.close();
		return labelList;
	}

    public void runPython(String suffix) throws IOException, InterruptedException {
        try{
            logger.info("Start to run python script...");
            Process proc = Runtime.getRuntime().exec("sh /home/llc/LogAnalysis/runPython.sh "+suffix);
            proc.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
            logger.info("Python run status: "+result);
        }catch (Exception e){
            logger.error(e.getMessage());
        }


    }

}
