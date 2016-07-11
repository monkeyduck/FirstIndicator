package com.llc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by llc on 16/6/20.
 */
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static final String endpoint = "cn-beijing.sls.aliyuncs.com"; // 选择与上面步骤创建Project所属区域匹配的

    public static final String accessKeyId = "Kywj58hCJKOSQTSk"; // 使用你的阿里云访问秘钥AccessKeyId

    public static final String accessKeySecret = "tnQmCz77jZdxr5OBBqKb4lWx4fRziC"; // 使用你的阿里云访问秘钥AccessKeySecret

    public static final String project = "xiaole-log"; // 上面步骤创建的项目名称

    public static final String logstore = "logstore"; // 上面步骤创建的日志库名称

    public static File convert(MultipartFile file) throws IOException {

        String name = file.getOriginalFilename();
        File readFile = new File(name);
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(readFile), "gb18030");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        logger.info("Original sentence: "+line);
        bufferedReader.close();
        inputStreamReader.close();
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(new File(name)));
        FileCopyUtils.copy(file.getInputStream(), stream);
        stream.close();

        File inputFile = new File(name);
        return inputFile;
//        BufferedReader reader = null;
//
//        String output = "";
//        try {
//            reader = new BufferedReader(new FileReader(inputFile));
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//
//            }

//        File convFile = new File(file.getOriginalFilename());
//        boolean b = convFile.createNewFile();
//        if (!b) {
//            logger.info(file.getOriginalFilename() + " can't trans form to file");
//        }
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(file.getBytes());
//        logger.info("Read contents: " + file.getBytes());
//        fos.close();
//        return convFile;
    }

    public static void writeToTxt(File file) throws IOException{
        File wfile = new File("/home/llc/LogAnalysis/py/labeled.txt");
        wfile.createNewFile();
        FileWriter fileWriter = new FileWriter(wfile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "gb18030");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line="";
        while ((line=bufferedReader.readLine())!=null){
            logger.info("read one sentence: "+line);
            bufferedWriter.write(line+"\n");
        }
        bufferedReader.close();
        bufferedWriter.close();
        fileWriter.close();
        inputStreamReader.close();
    }
}
