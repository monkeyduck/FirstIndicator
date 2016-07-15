package com.llc.controller;

import com.llc.model.FirstIndicator;
import com.llc.service.StatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by llc on 16/7/12.
 */
@Controller
@RequestMapping("/stat")
public class StatController {
    @Resource(name="StatService")
    private StatService statService;

    @RequestMapping("/first")
    public ModelAndView displayFirstIndicator(HttpServletRequest request,
                                              HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        FirstIndicator indicator = statService.getStatistic();
        modelAndView.addObject("firstStat", indicator);
        modelAndView.setViewName("first");
        return modelAndView;
    }

}
