package com.llc.controller;

import com.caikang.com.pitch.MainLabel;
import com.llc.service.LogService;
import com.llc.service.UserService;
import com.llc.utils.CipherUtil;
import com.llc.utils.DownloadFileUtil;
import com.llc.utils.Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    /**
     * 跳转至登录页
     * @param request
     * @return
     */
    @RequestMapping("/tologin")
    public String tologin(HttpServletRequest request, HttpServletResponse response, Model model){
        logger.debug("来自IP[" + request.getRemoteHost() + "]的访问");
        System.out.println("来自IP[" + request.getRemoteHost() + "]的访问");
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

    @RequestMapping("/record")
    public String record(){
        return "record";
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


}
