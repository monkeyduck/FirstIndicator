package com.llc.controller;

import com.llc.model.FirstIndicator;
import com.llc.service.StatService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                                              HttpServletResponse response,
                                              @RequestParam(value = "type",required = false) String type){
        ModelAndView modelAndView = new ModelAndView();
        List<FirstIndicator> indicators;
        List<String> versions = statService.getVersionList();
        if (type==null){
            indicators = statService.getStatistic();
        }
        else{
            if (type.equals("date")){
                indicators = statService.getStatisticByDate();
            }else{
                indicators = statService.getStatistic();
            }
        }
        List<List<String>> chartData = getChartData(indicators);
        modelAndView.addObject("firstStatList", indicators);
        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("chartData", chartData);
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
        mv.addObject("chartData", chartData);
        mv.addObject("firstStatList", indicators);
        mv.addObject("versionList", versions);
        mv.setViewName("first");
        return mv;
    }

//    @RequestMapping("/chart")
//    public JSONObject displayChart(@RequestParam(value = "column") String column){
//        Map<String, String> chartData = statService.getChartData(column);
//        JSONObject json = JSONObject.fromObject(chartData);
//        return json;
//
//    }

}
