package com.llc.controller;

import com.llc.model.FirstIndicator;
import com.llc.service.StatService;
import net.sf.json.JSONObject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by llc on 16/7/12.
 */
@Controller
//@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/stat")
public class StatController {
    @Resource(name="StatService")
    private StatService statService;

    static{

    }

    private Logger logger = LoggerFactory.getLogger(StatController.class);

    private List<List<String>> getChartData(List<FirstIndicator> indicators){
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i=0;i<11;i++){
            chartData.add(new ArrayList<String>());
        }
        for (FirstIndicator fi: indicators){
            chartData.get(0).add(""+fi.getTotalUserNum());
            chartData.get(1).add(""+fi.getNewUserNum());
            chartData.get(2).add(""+fi.getDailyActive());
            chartData.get(3).add(""+fi.getValidDailyActive());
            chartData.get(4).add(""+fi.getAvgUsedTimePerUser());
            chartData.get(5).add(""+fi.getValidAvgUsedTimePerUser());
            chartData.get(6).add(""+fi.getRetention());
            chartData.get(7).add(""+fi.getRetention_3());
            chartData.get(8).add(""+fi.getRetention_7());
            chartData.get(9).add(""+fi.getBugNum());
            chartData.get(10).add(String.format("'%s'",fi.getDisplayTime()));
        }
        return chartData;
    }


    @RequestMapping("/first")
    public ModelAndView crossingFilter(@RequestParam(value="member_type",defaultValue = "real") String member_type,
                                       @RequestParam(value="version",defaultValue = "") String version,
                                       @RequestParam(value="type",defaultValue = "hour") String type){
        ModelAndView modelAndView = new ModelAndView();

        // 增加一级指标用户类型，和贝瓦类型不同
        List<String> userTypes = new ArrayList<>();
        Map<String, String> userTypeName = new HashMap<String, String>();
        userTypes.add("real");
        userTypes.add("indoor");
        userTypes.add("market");
        userTypes.add("innerTest");
        userTypes.add("gray");
        userTypes.add("vip");
        userTypeName.put("real", "真实用户");
        userTypeName.put("indoor","入户用户");
        userTypeName.put("market","市场用户");
        userTypeName.put("innerTest","公司内测");
        userTypeName.put("gray","灰度用户");
        userTypeName.put("vip","VIP用户");

        List<FirstIndicator> indicators;
        if (type.equals("date")){
            indicators = statService.getStatisticByDate(member_type,version);
        }else{
            indicators = statService.getStatistic(member_type,version);
        }
        List<String> versions = statService.getVersionList();
        List<List<String>> chartData = getChartData(indicators);
        List<String> compare = calCompare(type, indicators);

        modelAndView.addObject("firstStatList", indicators);
        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("userTypeList", userTypes);
        modelAndView.addObject("typeName", userTypeName);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("compare", compare);
        modelAndView.addObject("utype", member_type);
        modelAndView.addObject("ver", version);
        modelAndView.setViewName("first");
        return modelAndView;
    }

    @RequestMapping("/sdk-first")
    public ModelAndView crossingFilterSDK(@RequestParam(value="member_type",defaultValue = "real") String member_type,
                                       @RequestParam(value="version",defaultValue = "") String version,
                                       @RequestParam(value="type",defaultValue = "hour") String type){
        ModelAndView modelAndView = new ModelAndView();

        List<String> userTypes = new ArrayList<>();
        Map<String, String> userTypeName = new HashMap<String, String>();
        userTypes.add("real");
        userTypes.add("innerTest");
        userTypeName.put("real", "真实用户");
        userTypeName.put("innerTest","公司内测");


        List<FirstIndicator> indicators;
        if (type.equals("date")){
            indicators = statService.getBeiwaStatisticByDate(member_type,version);
        }else{
            indicators = statService.getBeiwaStatistic(member_type,version);
        }
        List<String> versions = statService.getBeiwaVersionList();
        List<List<String>> chartData = getChartData(indicators);
        List<String> compare = calCompare(type, indicators);


        modelAndView.addObject("firstStatList", indicators);
        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("userTypeList", userTypes);
        modelAndView.addObject("typeName", userTypeName);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("compare", compare);
        modelAndView.addObject("utype", member_type);
        modelAndView.addObject("ver", version);
        modelAndView.setViewName("sdk-first");
        return modelAndView;
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

    @RequestMapping("/dailyActive")
    @ResponseBody
    public JSONObject getDailyActive() {
        DateTime dt = new DateTime();
        String date = dt.toString("yyyy-MM-dd");
        date += "%";
        List<Integer> xiaoleDaily = statService.getTodayDailyActive(date);
        List<Integer> beiwaDaily = statService.getBeiwaTodayDailyActive(date);
        Map<String, Map<String, Integer>> dailyMap = new HashMap<>();
        dailyMap.put("xiaole", list2Map(xiaoleDaily));
        dailyMap.put("beiwa", list2Map(beiwaDaily));
        JSONObject jsonResult = JSONObject.fromObject(dailyMap);
        return jsonResult;
    }

    @RequestMapping("/hourlyActive")
    @ResponseBody
    public JSONObject getHourlyActive() {
        DateTime dt = new DateTime();
        String date = dt.toString("yyyy-MM-dd");
        List<Integer> beiwaHourly = statService.getBeiwaTodayHourlyActive(date);
        Map<String, Map<String, Integer>> hourlyMap = new HashMap<>();
        hourlyMap.put("xiaole", new HashMap<>());
        hourlyMap.put("beiwa", list2Map(beiwaHourly));
        JSONObject jsonResult = JSONObject.fromObject(hourlyMap);
        return jsonResult;
    }

    @RequestMapping("/newUser")
    @ResponseBody
    public JSONObject getNewUser() {
        DateTime dt = new DateTime();
        String date = dt.toString("yyyy-MM-dd");
        date += "%";
        List<Integer> xiaoleNewUser = decreaseList(statService.getTodayNewUser(date));
        List<Integer> beiwaNewUser = decreaseList(statService.getBeiwaTodayNewUser(date));

        Map<String, Map<String, Integer>> newUserMap = new HashMap<>();
        newUserMap.put("xiaole", list2Map(xiaoleNewUser));
        newUserMap.put("beiwa", list2Map(beiwaNewUser));
        JSONObject jsonResult = JSONObject.fromObject(newUserMap);
        return jsonResult;
    }

    private Map<String, Integer> list2Map(List<Integer> list) {
        int length = list.size();
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < length; ++i) {
            result.put(""+i, list.get(i));
        }
        return result;
    }

    private List<Integer> decreaseList(List<Integer> list) {
        List<Integer> newUserHour = new ArrayList<>();
        if (list.size() > 0) {
            newUserHour.add(list.get(0));
            for (int i = 1; i < list.size(); ++i) {
                newUserHour.add(list.get(i) - list.get(i - 1));
            }
        }
        return newUserHour;
    }

}
