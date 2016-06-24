package com.llc.controller;

import com.llc.service.LogService;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by llc on 16/6/20.
 */
@Controller
@RequestMapping("/log")
public class LogController {

    private static final Log logger = LogFactory.getLog(LogController.class);

    @Resource(name="LogService")
    private LogService logService;

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
    public ModelAndView searchLogById(HttpServletRequest request, HttpServletRequest response,
                                      @RequestParam("id") int id){
        ModelAndView modelAndView = new ModelAndView();
        com.llc.model.Log log = logService.getLog(id);
        modelAndView.addObject("log", log);
        modelAndView.setViewName("showLog");
        return modelAndView;
    }


}
