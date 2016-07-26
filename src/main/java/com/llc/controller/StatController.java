package com.llc.controller;

import com.llc.model.FirstIndicator;
import com.llc.service.StatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llc on 16/7/12.
 */
@Controller
@RequestMapping("/stat")
public class StatController {
    @Resource(name="StatService")
    private StatService statService;

    private Logger logger = LoggerFactory.getLogger(StatController.class);

    @RequestMapping("/first")
    public ModelAndView displayFirstIndicator(HttpServletRequest request,
                                              HttpServletResponse response,
                                              @RequestParam(value = "type",required = false) String type){
        ModelAndView modelAndView = new ModelAndView();
        List<FirstIndicator> indicators;
        List<String> versions = statService.getVersionList();
        if (type==null){
            type = "time";
        }
        if (type.equals("date")){
            indicators = statService.getStatisticByDate();
        }else{
            indicators = statService.getStatistic();
        }
        List<String> compare = calCompare(type, indicators);
        List<List<String>> chartData = getChartData(indicators);
        modelAndView.addObject("firstStatList", indicators);
        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("compare", compare);
        modelAndView.addObject("type", type);
        modelAndView.setViewName("first");
        return modelAndView;
    }

    private List<List<String>> getChartData(List<FirstIndicator> indicators){
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i=0;i<7;i++){
            chartData.add(new ArrayList<String>());
        }
        for (FirstIndicator fi: indicators){
            chartData.get(0).add(""+fi.getTotalUserNum());
            chartData.get(1).add(""+fi.getNewUserNum());
            chartData.get(2).add(""+fi.getDailyActive());
            chartData.get(3).add(""+fi.getAvgUsedTimePerUser());
            chartData.get(4).add(""+fi.getRetention());
            chartData.get(5).add(""+fi.getBugNum());
            chartData.get(6).add(String.format("'%s'",fi.getDisplayTime()));
        }
        return chartData;
    }

    @RequestMapping("/version")
    public ModelAndView displayByVersion(@RequestParam(value = "version") String version,
                                         @RequestParam(value = "type") String type){
        ModelAndView mv = new ModelAndView();
        List<FirstIndicator> indicators;
        if (version.equals("all")){
            if (type.equals("date")){
                indicators = statService.getStatisticByDate();
            }else{
                indicators = statService.getStatistic();
            }
        }
        else{
            if (type.equals("date")){
                indicators = statService.getStatisticByVersionByDate(version);
            }
            else{
                indicators = statService.getStatisticByVersion(version);
            }
        }
        List<String> versions = statService.getVersionList();
        List<List<String>> chartData = getChartData(indicators);
        List<String> compare = calCompare(type, indicators);

        mv.addObject("chartData", chartData);
        mv.addObject("firstStatList", indicators);
        mv.addObject("versionList", versions);
        mv.addObject("ver", version);
        mv.addObject("compare", compare);
        mv.addObject("type", type);
        mv.setViewName("first");
        return mv;
    }

    public List<String> calCompare(String type, List<FirstIndicator> indicators){
        int len = indicators.size();
        List<String> compare = new ArrayList<String>();
        List<Integer> indexs = new ArrayList<Integer>();
        if (len > 0){
            FirstIndicator cur = indicators.get(len-1);
            int gap = type.equals("date")?7:24;
            boolean first = true;
            for (int i=len-1;i>=0;i=i-gap){
                if (first){
                    first = false;
                    if (i-gap>=0){
                        compare = cur.calCompare(indicators.get(i-gap), type);
                    }
                }
                indexs.add(i);
            }
        }
        return compare;
    }

}
