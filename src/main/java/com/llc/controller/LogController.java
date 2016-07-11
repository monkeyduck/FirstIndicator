package com.llc.controller;

import com.caikang.com.pitch.MainLabel;
import com.llc.service.LogService;
import com.llc.utils.DownloadFileUtil;
import com.llc.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.joda.time.DateTime;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by llc on 16/6/20.
 */
@Controller
@RequestMapping("/log")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Resource(name="LogService")
    private LogService logService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/queryLogs")
    public ModelAndView queryLog(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam("member_id") String member_id, @RequestParam("time") String time)
    {
        ModelAndView modelAndView = new ModelAndView();
        List<com.llc.model.Log> logList = logService.queryLog(member_id, time);
        modelAndView.addObject("logList", logList);
        modelAndView.setViewName("queryLogs");
        return modelAndView;
    }

    @RequestMapping("/showLog")
    public ModelAndView searchLogById(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam("id") int id){
        ModelAndView modelAndView = new ModelAndView();
        com.llc.model.Log log = logService.getLog(id);
        modelAndView.addObject("log", log);
        modelAndView.setViewName("showLog");
        return modelAndView;
    }

    @RequestMapping("/download_result")
    public ModelAndView downloadResult(HttpServletResponse response) throws IOException{
        String basePath = "/home/llc/LogAnalysis/py/";
        String targetName = "svm_test.predict";
        DownloadFileUtil.pushFile(targetName, basePath + targetName, response);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("analyseLog");
        return modelAndView;
    }

    @RequestMapping("/downloadAbnormal")
    public ModelAndView downloadAbnormalLog(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam("moduleName") String moduleName) throws IOException{
        ModelAndView modelAndView = new ModelAndView();
        String basePath = "/home/llc/abnormalLog/";
        String targetName = "abnormal_" + moduleName + ".log";
        DownloadFileUtil.pushFile(targetName, basePath + targetName, response);
        modelAndView.setViewName("downloadAbnormalLog");
        return modelAndView;
    }

    @RequestMapping("/analyseLog")
    public ModelAndView analyseLog(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("analyseLog");
        return modelAndView;
    }

    @RequestMapping(value="/upload", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ModelAndView checkAndAnalyse(@RequestParam("file")MultipartFile multipartFile) throws
            IOException, InterruptedException {
        File file = Utils.convert(multipartFile);
        Utils.writeToTxt(file);

        DateTime datetime = new DateTime();
        String dateSuffix = datetime.toString("yyyyMMddhhmmss");

        MainLabel mainLabel = new MainLabel();
        mainLabel.downloadAllAudioWav(file);
        mainLabel.createAudioLabel();
        mainLabel.runPython(dateSuffix);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("analyseLog");
//        File result = new File("/Users/linchuan/IdeaProjects/LogWebService/py/svm_test.predict");
        File result = new File("/home/llc/LogAnalysis/py/svm_test_"+dateSuffix+".predict");
        int count = 0;
        while (!result.exists()){
            if (count == 50){
                logger.error("svm_test.predict not found! Exit :1");
                break;
            }
            logger.info("Waiting test.predict");
            try {
                // thread to sleep for 1000 milliseconds
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
            count += 1;

        }
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(result));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line="";
        String re="";
        while ((line = bufferedReader.readLine())!=null){
            re += line+"\n";
        }
        modelAndView.addObject("message", re);
        return modelAndView;
    }


}
