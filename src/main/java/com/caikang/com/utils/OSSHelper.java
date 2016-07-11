package com.caikang.com.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by llc on 16/7/7.
 */
public class OSSHelper {
    public static OSSClient ossClient;
    private static final Logger logger = LoggerFactory.getLogger(OSSHelper.class);
    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;

    static{
        endpoint = "oss-cn-beijing.aliyuncs.com";
        accessKeyId = "Kywj58hCJKOSQTSk";
        accessKeySecret = "tnQmCz77jZdxr5OBBqKb4lWx4fRziC";
        bucketName = "record-resource";
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public OSSClient getOssClient(){
        return ossClient;
    }

    public void download(String key) throws IOException {
        // 创建OSSClient实例
        String filename = "audio/"+key;
        File wavfile = new File("/home/llc/LogAnalysis/"+filename);
        if (!wavfile.exists()){
            wavfile.createNewFile();
            //prefix
            logger.info("Start to download wav: "+key);
            ossClient.getObject(new GetObjectRequest(bucketName, key), wavfile);
            logger.info("Finished downloading wav: "+key);
        }
        logger.info(key+"has existed.");

    }


}
