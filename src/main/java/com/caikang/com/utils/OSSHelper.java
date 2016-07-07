package com.caikang.com.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;

import java.io.File;

/**
 * Created by llc on 16/7/7.
 */
public class OSSHelper {
    private String endpoint = "http://oss-cn-beijing-internal.aliyuncs.com";
    private String accessKeyId = "Kywj58hCJKOSQTSk";
    private String accessKeySecret = "tnQmCz77jZdxr5OBBqKb4lWx4fRziC";
    private String bucketName = "record-resource";

    public void download(String memberId, String recordId){
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.getObject(new GetObjectRequest(bucketName, memberId+"/"+recordId), new File("audio/"+memberId+"/"+recordId));
    }


}
