package com.llc.controller;

import com.caikang.com.pitch.MainLabel;
import com.llc.model.Log;
import com.llc.service.LogService;
import com.llc.utils.CipherUtil;
import com.llc.utils.DownloadFileUtil;
import com.llc.utils.Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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

    /**
     * 跳转至登录页
     * @param request
     * @return
     */
    @RequestMapping("/tologin")
    public String tologin(HttpServletRequest request, HttpServletResponse response, Model model){
//        logger.debug("来自IP[" + request.getRemoteHost() + "]的访问");
        return "login";
    }

    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        System.out.println(request);
        String username = request.getParameter("username");
        String password = CipherUtil.generatePassword(request.getParameter("password"));
        System.out.println("username: "+username);
        System.out.println("password: "+password);
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        Subject currentUser = SecurityUtils.getSubject();
        try{
            System.out.println("--------------------------");
            if (!currentUser.isAuthenticated()){
                token.setRememberMe(true);
                currentUser.login(token);
            }
            mv.setViewName("index");
        } catch(UnknownAccountException uae){
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            request.setAttribute("message_login", "未知账户");
        }catch(IncorrectCredentialsException ice){
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
            request.setAttribute("message_login", "密码不正确");
        }catch(LockedAccountException lae){
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
            request.setAttribute("message_login", "账户已锁定");
        }catch(ExcessiveAttemptsException eae){
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            request.setAttribute("message_login", "用户名或密码错误次数过多");
        }catch(AuthenticationException ae){
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
            request.setAttribute("message_login", "用户名或密码不正确");
        }
        return mv;
    }


    @RequestMapping("/download_result")
    public ModelAndView downloadResult(HttpServletResponse response, @RequestParam("fileName") String suffix) throws IOException{
        String basePath = "/home/llc/LogAnalysis/py/";
        String targetName = "predict_"+suffix+".txt";
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
        file.delete();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("analyseLog");
//        File result = new File("/Users/linchuan/IdeaProjects/LogWebService/py/svm_test.predict");
        File result = new File("/home/llc/LogAnalysis/py/predict_"+dateSuffix+".txt");
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
        modelAndView.addObject("fileName", dateSuffix);
        modelAndView.addObject("message", re);
        return modelAndView;
    }

    @RequestMapping(value="/analyseAudio", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public ModelAndView analyseAudio(@RequestParam("file")MultipartFile multipartFile,
                                     HttpServletResponse response) throws
            IOException, InterruptedException {
        ModelAndView mv = new ModelAndView();
        File file = Utils.convert(multipartFile);
        MainLabel mainLabel = new MainLabel();
        mainLabel.downloadAllAudioWav(file);
        mainLabel.createNewLabel();
        mv.setViewName("analyseLog");

        String basePath = "/home/llc/LogAnalysis/emotion/";
        String targetName = "audio.txt";
        DownloadFileUtil.pushFile(targetName, basePath + targetName, response);
        return mv;
    }

    @RequestMapping(value = "/wangmeng")
    public ModelAndView wangmeng(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("wangmeng");
        return mv;
    }

    @RequestMapping(value = "/wangmengDownload")
    public ModelAndView wangmengDownload(@RequestParam("date") String date, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("wangmeng");

        String filename = "edu"+date+".txt";
        String path = "/home/llc/analysisResult/";
        try{
            DownloadFileUtil.pushFile(filename,path+filename,response);
        }catch (IOException e){
            logger.error(e.getMessage());
            mv.addObject("error","没有该日期的文件");
        }
        return mv;
    }

    @RequestMapping(value = "/compare")
    public ModelAndView compareLog(@RequestParam("date") String date) {
        String newTableName = "aibasis_stat_bak.";
        String oldTableName = "aibasis_stat.";
        String[] segs = date.split("-");
        String sdate = segs[0] + segs[1] + segs[2];
        DateTime dt = new DateTime();
        String sdt = dt.toString("yyyy-MM-dd");
        List<Log> diff;
        if (sdt.equals(date)) {
            String old = oldTableName + "cachehour";
            String neww = newTableName + "cachehour";
            diff = logService.getDiff(old, neww);
        } else {
            String old = oldTableName + "cache" + sdate;
            String neww = newTableName+ "cache" + sdate;
            diff = logService.getDiff(old, neww);
        }

        ModelAndView mv = new ModelAndView();
        mv.setViewName("compareLog");
        mv.addObject("diffList", diff);
        return mv;
    }


}
