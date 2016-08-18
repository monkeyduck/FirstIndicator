package com.caikang.com.pitch;

import com.caikang.com.rule.GetLoudnessSeq;
import com.caikang.com.utils.HttpTookit;
import com.caikang.com.utils.OSSHelper;
import com.emotion.pitch.EmotionDiagram;
import com.tester.Diagram;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainLabel {
	private ArrayList<String> lines;
	private ArrayList<String> audioList;
    private ArrayList<String> audioKeyList;
    private static final Logger logger = LoggerFactory.getLogger(MainLabel.class);

    public MainLabel(){
        lines = new ArrayList<String>();
        audioList = new ArrayList<String>();
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
			if(PitchEstimation.angryScore >= 1.0 && GetLoudnessSeq.getLoudnessmax() >= 3.0)
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
    public void downloadAudioWavBatch(){
        int idx = 0;
        int maxS = 50;
        List<List<String>> multiThreadList = new ArrayList<List<String>>();
        for (int i=0;i<maxS;++i){
            multiThreadList.add(new ArrayList<String>());
        }
        for (String audioKey: audioKeyList){
            multiThreadList.get(idx%maxS).add(audioKey);
            ++idx;
        }
        for (List<String> l: multiThreadList) {
            Thread thread = new Thread(() -> {
                OSSHelper oss = new OSSHelper();
                l.stream().forEach(s -> {
                    try {
                        oss.download(s);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                });
//                oss.getOssClient().shutdown();
            });
            thread.run();
        }
    }

	public void downloadAllAudioWav(File rfile) throws IOException{
        logger.info("Start to download audios...");
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
        downloadAudioWavBatch();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        logger.info("Finished downloading audios");
    }

	public ArrayList<Integer> createAudioLabel() throws IOException{
        logger.info("Start to create audio label");
        ArrayList<Integer> labelList = new ArrayList<Integer>();
        File file = new File("/home/llc/LogAnalysis/py/annotation.txt");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (String audioRecord: audioList){
            int label = getLabel(audioRecord);
            labelList.add(label);
            bufferedWriter.write(""+label+"\n");
        }
        logger.info("Finished creating audio labels");
        bufferedWriter.close();
        fileWriter.close();
		return labelList;
	}

    public void createNewLabel() throws IOException {
        List<String> result = new ArrayList<String>();
        File file = new File("/home/llc/LogAnalysis/emotion/audio.txt");
        logger.info("Start to analyse audio...");
        for (String audioRecord: audioList){
            try{
                int label = EmotionDiagram.getLabel(audioRecord);
                String str = ""+label+"\t";
                label = Diagram.getOutput(audioRecord);
                str += label;
                result.add(str);
            }catch (Exception e){
                logger.error(e.getMessage());
            }

        }
        logger.info("Finished analysing audio");
        FileUtils.writeLines(file, result);
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
