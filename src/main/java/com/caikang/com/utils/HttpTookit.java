package com.caikang.com.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.impl.client.HttpClients;


/**
 * Created by llc on 16/7/22.
 */
public class HttpTookit {

    private final static Logger logger = LoggerFactory.getLogger(HttpTookit.class);


    // HTTP GET request
    public void sendGet() throws Exception {

        String url = "http://www.google.com/search?q=developer";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }

    // HTTP POST request
    public String sendPost(String filePath) throws Exception {
        String url = "http://api-web.emokit.com:802/wechatemo/WxVoiceWAV.do";
        // 待上传的文件
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()){
            logger.error((filePath + ":不是有效文件路径"));
        }
        // 响应内容
        String respContent = null;
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        StringBuilder content = new StringBuilder();
        try {
            httpClient = HttpClients.createDefault();
            // 创建POST请求
            httpPost = new HttpPost(url);
            FileBody bin = new FileBody(file);
            StringBody key = new StringBody("98cd22f6f90141f8f1876dd2f5a27ea5",
                    ContentType.create("text/plain", Consts.UTF_8));
            StringBody platid = new StringBody("h5_tag",ContentType.create("text/plain", Consts.UTF_8));
            StringBody uid = new StringBody("uid", ContentType.create("text/plain", Consts.UTF_8));
            StringBody appid = new StringBody("100001", ContentType.create("text/plain", Consts.UTF_8));

            // 创建请求实体
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("file", bin)//相当于<input type="file" name="media"/>
                    .addPart("appid", appid)
                    .addPart("key", key)
                    .addPart("platid", platid)//相当于<input type="text" name="name" value=name>
                    .addPart("uid", uid)
                    .build();
            httpPost.setEntity(reqEntity);
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            // 获取响应内容
            respContent = parseRepsonse(content, response, "POST");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("resp：" + respContent);
        logger.info("------------------------------HttpClient POST结束-------------------------------");
        return respContent;
    }

    /**
     * 获取响应内容，针对MimeType为text/plan、text/json格式
     *
     * @param content
     *            响应内容
     * @param response
     *            HttpResponse对象
     * @param method
     *            请求方式 GET|POST
     * @return 转为UTF-8的字符串
     * @throws ParseException
     * @throws IOException
     * @author Jie
     * @date 2015-2-28
     */
    public static String parseRepsonse(StringBuilder content, HttpResponse response, String method) throws ParseException, IOException {
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();// 响应码
        String reasonPhrase = statusLine.getReasonPhrase();// 响应信息
        if (statusCode == 200) {// 请求成功
            HttpEntity entity = response.getEntity();
            logger.info("MineType:" + entity.getContentType().getValue());
            content.append(EntityUtils.toString(entity));
        } else {
            logger.error(method + "：code[" + statusCode + "],desc[" + reasonPhrase + "]");
        }
        return content.toString();
    }

    public static void main(String[] args) throws Exception {

        HttpTookit http = new HttpTookit();

        System.out.println("\nTesting 2 - Send Http POST request");
        String filePath = "/Users/linchuan/Downloads/audio0621/wav/2252.wav";
        http.sendPost(filePath);

    }
}
