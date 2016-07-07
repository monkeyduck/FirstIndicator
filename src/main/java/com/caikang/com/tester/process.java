package com.caikang.com.tester;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.List;

public class process {
	public static void main(String[] args) {
		List<String[]> list = ExcelReader.readExcel("/Users/karl/Work/database/emotionTester/a_train/20160626-情绪训练集/20160619.xls");
		String res;
		for(int i = 0; i < list.size(); i++) {
			res = downloadFromUrl(list.get(i)[1],"/Users/karl/Work/database/emotionTester/a_train/20160626-情绪训练集/audio/", list.get(i)[0]);  
			System.out.println(res);
		}
	}
	
	public static String downloadFromUrl(String url,String dir, String index) {  
		try {  
			URL httpurl = new URL(url);  
			String fileName = index + ".wav";  
			System.out.println(fileName);  
			File f = new File(dir + fileName);  
			FileUtils.copyURLToFile(httpurl, f);  
		} catch (Exception e) {  
			e.printStackTrace();  
			return "Fault!";  
		}   
		return "Successful!";  
	}  
}
