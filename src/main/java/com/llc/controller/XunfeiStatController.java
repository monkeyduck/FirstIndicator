package com.llc.controller;

import com.llc.model.XunfeiRequest;
import com.llc.service.XunfeiService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zoey on 16/11/9.
 */

@Controller
@RequestMapping("/stat")
public class XunfeiStatController {
    @Resource(name = "XunfeiService")
    private XunfeiService xfService;

    private static List<String> userTypes = new ArrayList<String>();

    private static Map<String, String> userTypeName = new HashMap<String, String>();

    private static Map<String, String> userShowTypeName = new HashMap<String, String>();

    static {
        userTypes.add("all");
        userTypes.add("real");
        userTypes.add("formal");
        userTypes.add("indoor");
        userTypes.add("market");
        userTypes.add("innerTest");
        userTypes.add("gray");
        userTypes.add("vip");
        userTypeName.put("real", "0");
        userTypeName.put("formal", "0");
        userTypeName.put("indoor", "1");
        userTypeName.put("market", "2");
        userTypeName.put("innerTest", "3");
        userTypeName.put("gray", "4");
        userTypeName.put("vip", "5");
        userShowTypeName.put("all", "全部用户");
        userShowTypeName.put("real", "真实用户");
        userShowTypeName.put("formal", "正式用户");
        userShowTypeName.put("indoor", "入户用户");
        userShowTypeName.put("market", "市场用户");
        userShowTypeName.put("innerTest", "公司内测");
        userShowTypeName.put("gray", "灰度用户");
        userShowTypeName.put("vip", "VIP用户");
    }

    private List<List<String>> getXfRequest(List<XunfeiRequest> xfRequests) {
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i = 0; i < 3; i++) {
            chartData.add(new ArrayList<String>());
        }
        int n = xfRequests.size();
        for (int i = n-1;i>=0;i--) {
            XunfeiRequest fi = xfRequests.get(i);
            chartData.get(0).add("" + fi.getMember_count());
            chartData.get(1).add("" + fi.getTotal_request());
            chartData.get(2).add(String.format("'%s'", fi.getDate()));
        }
        return chartData;
    }

    private List<List<String>> getXfRequestDetail(List<XunfeiRequest> xfRequests) {
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i = 0; i < 3; i++) {
            chartData.add(new ArrayList<String>());
        }
        for (XunfeiRequest fi : xfRequests) {
            chartData.get(0).add(String.format("'%s'", fi.getMember_id()));
            chartData.get(1).add("" + fi.getRequest_count());
            chartData.get(2).add(String.format("'%s'", fi.getDate()));
        }
        return chartData;
    }

    private List<List<String>> getXfDailyLatency(List<XunfeiRequest> xfLatencys, String type) {
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i = 0; i < 7; i++) {
            chartData.add(new ArrayList<String>());
        }
        int n = xfLatencys.size();
        for (int i = n-1;i>=0;i--) {
            int tmp = i;
            if(type.equals("hourly"))
                tmp = n-1-i;
            XunfeiRequest fi = xfLatencys.get(tmp);
            chartData.get(0).add("" + fi.getMember_count());
            chartData.get(1).add("" + fi.getAvg());
            chartData.get(2).add("" + fi.getMax());
            chartData.get(3).add("" + fi.getMin());
            chartData.get(4).add("" + fi.getFirst_quarter());
            chartData.get(5).add("" + fi.getThird_quarter());
            chartData.get(6).add(String.format("'%s'", fi.getDate()));
        }
        return chartData;
    }

    @RequestMapping("/xfRequestCount")
    public ModelAndView showXfRequestCount(@RequestParam(value="member_type",defaultValue = "all") String member_type,
                                           @RequestParam(value="version",defaultValue = "") String version,
                                           @RequestParam(value="start_date",defaultValue = "") String start_date,
                                           @RequestParam(value="end_date",defaultValue = "") String end_date){

        if(end_date.equals("")){
            DateTime dt = new DateTime();
            DateTime last = dt.minusDays(1);
            end_date = last.toString("yyyy-MM-dd");
            dt = dt.minusMonths(1);
            start_date = dt.toString("yyyy-MM-dd");
        }

        ModelAndView modelAndView = new ModelAndView();
        List<XunfeiRequest> xf = new ArrayList<XunfeiRequest>();

        xf = xfService.getXfRequestByUserByVersion((member_type), version, start_date, end_date);

        List<List<String>> chartData = getXfRequest(xf);
        List<String> versions = xfService.getVersionListFromRequest(start_date,end_date);

        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("xfRequestList", xf);
        modelAndView.addObject("userTypeList", userTypes);
        modelAndView.addObject("typeName", userShowTypeName);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("start_date", start_date);
        modelAndView.addObject("end_date", end_date);
        modelAndView.addObject("utype", member_type);
        modelAndView.addObject("ver", version);
        modelAndView.setViewName("xfRequestCount");

        System.out.println(member_type);

        return modelAndView;
    }

    @RequestMapping("/xfRequestDetail")
    public ModelAndView showxfRequestDetail(@RequestParam(value="member_type",defaultValue = "all") String member_type,
                                           @RequestParam(value="version",defaultValue = "") String version,
                                           @RequestParam(value="date",defaultValue = "2016-11-24") String date){
        ModelAndView modelAndView = new ModelAndView();
        List<XunfeiRequest> xf = new ArrayList<XunfeiRequest>();

        if(member_type.equals("all")) {
            if(version.equals("")) {
                xf = xfService.getXfRequestDetail( date);
            }
            else{
                xf = xfService.getXfRequestDetailByVersion(version, date);
            }
        }
        else{
            if(member_type.equals("real")){
                if(version.equals("")) {
                    xf = xfService.getXfRealRequestDetail( date);
                }
                else{
                    xf = xfService.getXfRealRequestDetailByVersion(version, date);
                }
            }
            else{
                if(version.equals("")) {
                    xf = xfService.getXfRequestDetailByUser(userTypeName.get(member_type), date);
                }
                else{
                    xf = xfService.getXfRequestDetailByUserByVersion(userTypeName.get(member_type),version, date);
                }
            }
        }

        List<List<String>> chartData = getXfRequestDetail(xf);

        modelAndView.addObject("xfRequestList", xf);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("date", date);
        modelAndView.setViewName("xfRequestDetail");

        System.out.println(member_type);

        return modelAndView;
    }

    @RequestMapping("/xfIndex")
    public String index(){
        return "xfIndex";
    }

    @RequestMapping("/xfRecLatency")
    public ModelAndView showXfRecLatency(@RequestParam(value="member_type",defaultValue = "all") String member_type,
                                           @RequestParam(value="version",defaultValue = "") String version,
                                           @RequestParam(value="date",defaultValue = "2016-11-24") String date,
                                            @RequestParam(value="type",defaultValue = "date") String type){
        ModelAndView modelAndView = new ModelAndView();
        List<XunfeiRequest> xf = new ArrayList<XunfeiRequest>();

        if(type.equals("hour")){
            if (member_type.equals("all")) {
                if (version.equals("")) {
                    xf = xfService.getXfRecLatencyTime(date);
                } else {
                    xf = xfService.getXfRecLatencyTimeByVersion(version, date);
                }
            } else {
                if (member_type.equals("real")) {
                    if (version.equals("")) {
                        xf = xfService.getXfRealRecLatencyTime(date);
                    } else {
                        xf = xfService.getXfRealRecLatencyTimeByVersion(version, date);
                    }
                } else {
                    if (version.equals("")) {
                        xf = xfService.getXfRecLatencyTimeByUser(userTypeName.get(member_type), date);
                    } else {
                        xf = xfService.getXfRecLatencyTimeByUserByVersion(userTypeName.get(member_type), version, date);
                    }
                }
            }
        }
        else {
            if (member_type.equals("all")) {
                if (version.equals("")) {
                    xf = xfService.getXfRecLatency(date);
                } else {
                    xf = xfService.getXfRecLatencyByVersion(version, date);
                }
            } else {
                if (member_type.equals("real")) {
                    if (version.equals("")) {
                        xf = xfService.getXfRealRecLatency(date);
                    } else {
                        xf = xfService.getXfRealRecLatencyByVersion(version, date);
                    }
                } else {
                    if (version.equals("")) {
                        xf = xfService.getXfRecLatencyByUser(userTypeName.get(member_type), date);
                    } else {
                        xf = xfService.getXfRecLatencyByUserByVersion(userTypeName.get(member_type), version, date);
                    }
                }
            }
        }

        modelAndView.addObject("xfRecLatencyList", xf);
        modelAndView.addObject("date", date);
        modelAndView.setViewName("xfRecLatency");

        System.out.println(member_type);

        return modelAndView;
    }

    @RequestMapping("/xfTTSLatency")
    public ModelAndView showXfTTSLatency(@RequestParam(value="member_type",defaultValue = "all") String member_type,
                                           @RequestParam(value="version",defaultValue = "") String version,
                                           @RequestParam(value="date",defaultValue = "2016-11-24") String date,
                                            @RequestParam(value="type",defaultValue = "date") String type){
        ModelAndView modelAndView = new ModelAndView();
        List<XunfeiRequest> xf = new ArrayList<XunfeiRequest>();

        if(type.equals("hour")){
            if (member_type.equals("all")) {
                if (version.equals("")) {
                    xf = xfService.getXfTTSLatencyTime(date);
                } else {
                    xf = xfService.getXfTTSLatencyTimeByVersion(version, date);
                }
            } else {
                if (member_type.equals("real")) {
                    if (version.equals("")) {
                        xf = xfService.getXfRealTTSLatencyTime(date);
                    } else {
                        xf = xfService.getXfRealTTSLatencyTimeByVersion(version, date);
                    }
                } else {
                    if (version.equals("")) {
                        xf = xfService.getXfTTSLatencyTimeByUser(userTypeName.get(member_type), date);
                    } else {
                        xf = xfService.getXfTTSLatencyTimeByUserByVersion(userTypeName.get(member_type), version, date);
                    }
                }
            }
        }
        else{
            if (member_type.equals("all")) {
                if (version.equals("")) {
                    xf = xfService.getXfTTSLatency(date);
                } else {
                    xf = xfService.getXfTTSLatencyByVersion(version, date);
                }
            } else {
                if (member_type.equals("real")) {
                    if (version.equals("")) {
                        xf = xfService.getXfRealTTSLatency(date);
                    } else {
                        xf = xfService.getXfRealTTSLatencyByVersion(version, date);
                    }
                } else {
                    if (version.equals("")) {
                        xf = xfService.getXfTTSLatencyByUser(userTypeName.get(member_type), date);
                    } else {
                        xf = xfService.getXfTTSLatencyByUserByVersion(userTypeName.get(member_type), version, date);
                    }
                }
            }
        }

        modelAndView.addObject("xfTTSLatencyList", xf);
        modelAndView.addObject("date", date);
        modelAndView.setViewName("xfTTSLatency");

        System.out.println(member_type);

        return modelAndView;
    }

    @RequestMapping("/xfDailyRecLatency")
    public ModelAndView showXfDailyRecLatency(@RequestParam(value="member_type",defaultValue = "all") String member_type,
                                         @RequestParam(value="version",defaultValue = "") String version,
                                         @RequestParam(value="start_date",defaultValue = "") String start_date,
                                         @RequestParam(value="end_date",defaultValue = "") String end_date){
        ModelAndView modelAndView = new ModelAndView();
        List<XunfeiRequest> xf = new ArrayList<XunfeiRequest>();

        if(end_date.equals("")){
            DateTime dt = new DateTime();
            DateTime last = dt.minusDays(1);
            end_date = last.toString("yyyy-MM-dd");
            dt = dt.minusMonths(1);
            start_date = dt.toString("yyyy-MM-dd");
        }

        if(start_date.equals(end_date)){
            xf = xfService.getHourlyXfRecLatencyByUserByVersion((member_type), version, start_date, end_date);

            Map<String, Integer> hourMap = new HashMap<>();
            List<XunfeiRequest> xfnew = new ArrayList<XunfeiRequest>();
            for(int i=0;i<xf.size();i++){
                XunfeiRequest cur = xf.get(i);
                hourMap.put(cur.getDate(),i );
            }

            for(int t = 0;t<24;t++){
                String hour = start_date + " " +  String.format("%02d", t) + ":00:00";
                if(!hourMap.containsKey(hour)){
                    XunfeiRequest cur = new XunfeiRequest(0, hour, 0, 0, 0, 0, 0, "", "");
                    xfnew.add(cur);
                }
                else{
                    xfnew.add(xf.get(hourMap.get(hour)));
                }
            }
            xf = xfnew;
        }
        else {
            xf = xfService.getDailyXfRecLatencyByUserByVersion((member_type), version, start_date, end_date);
        }

        List<List<String>> chartData = new ArrayList<>();
        if(start_date.equals(end_date)) {
            chartData = getXfDailyLatency(xf, "hourly");
        }
        else{
            chartData = getXfDailyLatency(xf, "daily");
        }
        List<String> versions = xfService.getVersionListFromRecLatency(start_date,end_date);

        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("xfRecLatencyList", xf);
        modelAndView.addObject("userTypeList", userTypes);
        modelAndView.addObject("typeName", userShowTypeName);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("start_date", start_date);
        modelAndView.addObject("end_date", end_date);
        modelAndView.addObject("utype", member_type);
        modelAndView.addObject("ver", version);
        modelAndView.setViewName("xfDailyRecLatency");

        System.out.println(member_type);

        return modelAndView;
    }

    @RequestMapping("/xfDailyTTSLatency")
    public ModelAndView showXfDailyTTSLatency(@RequestParam(value="member_type",defaultValue = "all") String member_type,
                                         @RequestParam(value="version",defaultValue = "") String version,
                                         @RequestParam(value="start_date",defaultValue = "") String start_date,
                                         @RequestParam(value="end_date",defaultValue = "") String end_date){
        ModelAndView modelAndView = new ModelAndView();
        List<XunfeiRequest> xf = new ArrayList<XunfeiRequest>();

        if(end_date.equals("")){
            DateTime dt = new DateTime();
            DateTime last = dt.minusDays(1);
            end_date = last.toString("yyyy-MM-dd");
            dt = dt.minusMonths(1);
            start_date = dt.toString("yyyy-MM-dd");
        }

        if(start_date.equals(end_date)) {
            xf = xfService.getHourlyXfTTSLatencyByUserByVersion((member_type), version, start_date, end_date);
            Map<String, Integer> hourMap = new HashMap<>();
            List<XunfeiRequest> xfnew = new ArrayList<XunfeiRequest>();
            for(int i=0;i<xf.size();i++){
                XunfeiRequest cur = xf.get(i);
                hourMap.put(cur.getDate(),i );
            }

            for(int t = 0;t<24;t++){
                String hour = start_date + " " +  String.format("%02d", t) + ":00:00";
                if(!hourMap.containsKey(hour)){
                    XunfeiRequest cur = new XunfeiRequest(0, hour, 0, 0, 0, 0, 0, "", "");
                    xfnew.add(cur);
                }
                else{
                    xfnew.add(xf.get(hourMap.get(hour)));
                }
            }
            xf = xfnew;
        }
        else {
            xf = xfService.getDailyXfTTSLatencyByUserByVersion((member_type), version, start_date, end_date);
        }

        List<List<String>> chartData = new ArrayList<>();
        if(start_date.equals(end_date)) {
            chartData = getXfDailyLatency(xf, "hourly");
        }
        else{
            chartData = getXfDailyLatency(xf, "daily");
        }
        List<String> versions = xfService.getVersionListFromTTSLatency(start_date,end_date);

        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("xfTTSLatencyList", xf);
        modelAndView.addObject("userTypeList", userTypes);
        modelAndView.addObject("typeName", userShowTypeName);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("start_date", start_date);
        modelAndView.addObject("end_date", end_date);
        modelAndView.addObject("utype", member_type);
        modelAndView.addObject("ver", version);
        modelAndView.setViewName("xfDailyTTSLatency");

        System.out.println(member_type);

        return modelAndView;
    }
}

