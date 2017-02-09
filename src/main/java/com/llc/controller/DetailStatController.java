package com.llc.controller;

import com.llc.model.Member;
import com.llc.service.MemberService;
import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zoey on 16/10/28.
 */

@Controller
@RequestMapping("/stat")
public class DetailStatController {

    @Resource(name = "MemberService")
    private MemberService memberService;

    private static List<String> userTypes = new ArrayList<String>();

    private static List<String> validTypes = new ArrayList<String>();

    private static Map<String, String> userTypeName = new HashMap<String, String>();

    private static Map<String, String> userShowTypeName = new HashMap<String, String>();

    private static Map<String, String> validTypeName = new HashMap<String, String>();

    static {
        validTypes.add("all");
        validTypes.add("true");
        validTypeName.put("all","全部用户");
        validTypeName.put("true","活跃用户");
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
        userShowTypeName.put("real", "真实用户");
        userShowTypeName.put("formal", "正式用户");
        userShowTypeName.put("indoor", "入户用户");
        userShowTypeName.put("market", "市场用户");
        userShowTypeName.put("innerTest", "公司内测");
        userShowTypeName.put("gray", "灰度用户");
        userShowTypeName.put("vip", "VIP用户");
    }

    private List<List<String>> getMemberDetail(List<Member> members) {
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i = 0; i < 9; i++) {
            chartData.add(new ArrayList<String>());
        }
        for (Member fi : members) {
            chartData.get(0).add(String.format("'%s'", fi.getMember_id()));
            chartData.get(1).add(String.format("'%s'", fi.getDevice_id()));
            chartData.get(2).add(String.format("'%s'", fi.getNick_name()));
            chartData.get(3).add(String.format("'%s'", fi.getReal_name()));
            chartData.get(4).add(String.format("'%s'", fi.getGender()));
            chartData.get(5).add("" + fi.getAge());
            chartData.get(6).add(String.format("'%s'", fi.getSignup_time()));
            chartData.get(7).add(String.format("'%s'", fi.getMember_type()));
            chartData.get(8).add(String.format("'%s'", fi.getVersion()));
        }
        return chartData;
    }

    private List<List<String>> getDailyNewMemberDetail(List<Member> members) {
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i = 0; i < 9; i++) {
            chartData.add(new ArrayList<String>());
        }
        for (Member fi : members) {
            chartData.get(0).add(String.format("'%s'", fi.getDate()));
            chartData.get(1).add(String.format("'%s'", fi.getMember_id()));
            chartData.get(1).add(String.format("'%s'", fi.getDevice_id()));
            chartData.get(2).add(String.format("'%s'", fi.getNick_name()));
            chartData.get(3).add(String.format("'%s'", fi.getReal_name()));
            chartData.get(4).add(String.format("'%s'", fi.getGender()));
            chartData.get(5).add("" + fi.getAge());
            chartData.get(6).add(String.format("'%s'", fi.getSignup_time()));
            chartData.get(7).add(String.format("'%s'", fi.getMember_type()));
            chartData.get(8).add(String.format("'%s'", fi.getVersion()));
        }
        return chartData;
    }

    private List<List<String>> getDailyActiveTime(List<Member> members) {
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i = 0; i < 10; i++) {
            chartData.add(new ArrayList<String>());
        }
        for (Member fi : members) {
            chartData.get(0).add(String.format("'%s'", fi.getDate()));
            chartData.get(1).add(String.format("'%s'", fi.getMember_id()));
            chartData.get(2).add(String.format("'%s'", fi.getUsedTime()));
            chartData.get(3).add(String.format("'%s'", fi.getVersion()));
            chartData.get(4).add(String.format("'%s'", fi.getIsValid()));
            chartData.get(5).add(String.format("'%s'", fi.getMember_type()));
            chartData.get(6).add(String.format("'%s'", fi.getDevice_id()));
            chartData.get(7).add(String.format("'%s'", fi.getGender()));
            chartData.get(8).add("" + fi.getAge());
            chartData.get(9).add(String.format("'%s'", fi.getSignup_time()));
        }
        return chartData;
    }

    private List<List<String>> getValidDailyActiveTime(List<Member> members) {
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i = 0; i < 9; i++) {
            chartData.add(new ArrayList<String>());
        }
        for (Member fi : members) {
            chartData.get(0).add(String.format("'%s'", fi.getDate()));
            chartData.get(1).add(String.format("'%s'", fi.getMember_id()));
            chartData.get(2).add(String.format("'%s'", fi.getUsedTime()));
            chartData.get(3).add(String.format("'%s'", fi.getVersion()));
            chartData.get(4).add(String.format("'%s'", fi.getMember_type()));
            chartData.get(5).add(String.format("'%s'", fi.getDevice_id()));
            chartData.get(6).add(String.format("'%s'", fi.getGender()));
            chartData.get(7).add("" + fi.getAge());
            chartData.get(8).add(String.format("'%s'", fi.getSignup_time()));
        }
        return chartData;
    }

    private List<List<String>> getMemberCount(List<Member> members) {
        List<List<String>> chartData = new ArrayList<List<String>>();
        for (int i = 0; i < 3; i++) {
            chartData.add(new ArrayList<String>());
        }
        for (Member fi : members) {
            chartData.get(0).add(String.format("'%s'", fi.getDate()));
            chartData.get(1).add(String.format("'%s'", fi.getVersion()));
            chartData.get(2).add("" + fi.getMember_count());
        }
        return chartData;
    }

    @RequestMapping("/member")
    public ModelAndView showMemberDetail(@RequestParam(value = "member_type", defaultValue = "real") String member_type,
                                         @RequestParam(value = "version", defaultValue = "") String version,
                                         @RequestParam(value = "type", defaultValue = "hour") String type,
                                         @RequestParam(value = "date", defaultValue = "") String date) {
        ModelAndView modelAndView = new ModelAndView();
        List<Member> members;
        if (version.equals("")) {
            if(member_type.equals("real")){
                members = ListUtils.union(memberService.getMemberByUserType("0",date), memberService.getMemberByUserType("4",date));
            }
            else {
                members = memberService.getMemberByUserType(userTypeName.get(member_type), date);
            }
        } else {
            if(member_type.equals("real")){
                members = ListUtils.union(memberService.getMember("0",version, date), memberService.getMember("4",version, date));
            }
            else {
                members = memberService.getMember(userTypeName.get(member_type), version, date);
            }
        }
        System.out.println(members.size());

        List<List<String>> chartData = getMemberDetail(members);
        List<String> versions = memberService.getVersionList();

        modelAndView.addObject("memberList", members);
        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("member_type", member_type);
        modelAndView.addObject("version", version);
        modelAndView.setViewName("member");

        System.out.println(version);
        System.out.println(userTypeName.get(member_type));

        return modelAndView;
    }

    @RequestMapping("/dailyNewMember")
    public ModelAndView showDailyNewMemberDetail(@RequestParam(value = "member_type", defaultValue = "real") String member_type,
                                                 @RequestParam(value = "version", defaultValue = "") String version,
                                                 @RequestParam(value = "type", defaultValue = "hour") String type,
                                                 @RequestParam(value = "date", defaultValue = "") String date) {
        ModelAndView modelAndView = new ModelAndView();
        List<Member> members;
        if(type.equals("hour")) {
            if (version.equals("")) {
                if (member_type.equals("real")) {
                    members = ListUtils.union(memberService.getDailyNewMemberByUserType("0", date), memberService.getDailyNewMemberByUserType("4", date));
                } else {
                    members = memberService.getDailyNewMemberByUserType(userTypeName.get(member_type), date);
                }
            } else {
                if (member_type.equals("real")) {
                    members = ListUtils.union(memberService.getDailyNewMember("0", version, date), memberService.getDailyNewMember("4", version, date));
                } else {
                    members = memberService.getDailyNewMember(userTypeName.get(member_type), version, date);
                }
            }
        }
        else{
            if (version.equals("")) {
                if (member_type.equals("real")) {
                    members = ListUtils.union(memberService.getDailyNewMemberByUserTypeByDate("0", date), memberService.getDailyNewMemberByUserTypeByDate("4", date));
                } else {
                    members = memberService.getDailyNewMemberByUserTypeByDate(userTypeName.get(member_type), date);
                }
            } else {
                if (member_type.equals("real")) {
                    members = ListUtils.union(memberService.getDailyNewMemberByDate("0", version, date), memberService.getDailyNewMemberByDate("4", version, date));
                } else {
                    members = memberService.getDailyNewMemberByDate(userTypeName.get(member_type), version, date);
                }
            }
        }

        List<List<String>> chartData = getDailyNewMemberDetail(members);
        List<String> versions = memberService.getVersionList();

        modelAndView.addObject("memberList", members);
        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("member_type", member_type);
        modelAndView.addObject("version", version);
        modelAndView.setViewName("dailyNewMember");

        System.out.println(version);
        System.out.println(userTypeName.get(member_type));

        return modelAndView;
    }

    @RequestMapping("/dailyActiveTime")
    public ModelAndView showDailyActiveTime(@RequestParam(value="member_type",defaultValue = "real") String member_type,
                                                 @RequestParam(value="version",defaultValue = "") String version,
                                                 @RequestParam(value="type",defaultValue = "hour") String type,
                                                 @RequestParam(value="date",defaultValue = "date") String date){
        ModelAndView modelAndView = new ModelAndView();
        List<Member> members;
        if(version.equals("")){
            if(member_type.equals("real")){
                members = ListUtils.union(memberService.getDailyActiveTimeByUserType("0",date), memberService.getDailyActiveTimeByUserType("4",date));
            }
            else {
                members = memberService.getDailyActiveTimeByUserType(userTypeName.get(member_type), date);
            }
        }
        else {
            if(member_type.equals("real")){
                members = ListUtils.union(memberService.getDailyActiveTime("0",version, date), memberService.getDailyActiveTime("4",version, date));
            }
            else {
                members = memberService.getDailyActiveTime(userTypeName.get(member_type), version, date);
            }
        }
        List<List<String>> chartData = getDailyActiveTime(members);
        List<String> versions = memberService.getVersionList();

        modelAndView.addObject("memberList", members);
        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("member_type", member_type);
        modelAndView.addObject("version", version);
        modelAndView.setViewName("dailyActiveTime");

        System.out.println(version);
        System.out.println(userTypeName.get(member_type));

        return modelAndView;
    }

    @RequestMapping("/validDailyActiveTime")
    public ModelAndView showValidDailyActiveTime(@RequestParam(value="member_type",defaultValue = "real") String member_type,
                                                 @RequestParam(value="version",defaultValue = "") String version,
                                                 @RequestParam(value="type",defaultValue = "hour") String type,
                                                 @RequestParam(value="date",defaultValue = "date") String date){
        ModelAndView modelAndView = new ModelAndView();
        List<Member> members;

        if(version.equals("")){
            if(member_type.equals("real")){
                members = ListUtils.union(memberService.getValidDailyActiveTimeByUserType("0",date), memberService.getValidDailyActiveTimeByUserType("4",date));
            }
            else {
                members = memberService.getValidDailyActiveTimeByUserType(userTypeName.get(member_type), date);
            }
        }
        else {
            if(member_type.equals("real")){
                members = ListUtils.union(memberService.getValidDailyActiveTime("0",version, date), memberService.getValidDailyActiveTime("4",version, date));
            }
            else {
                members = memberService.getValidDailyActiveTime(userTypeName.get(member_type), version, date);
            }
        }

        List<List<String>> chartData = getValidDailyActiveTime(members);
        List<String> versions = memberService.getVersionList();

        modelAndView.addObject("memberList", members);
        modelAndView.addObject("versionList", versions);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("member_type", member_type);
        modelAndView.addObject("version", version);
        modelAndView.setViewName("validDailyActiveTime");

        System.out.println(version);
        System.out.println(userTypeName.get(member_type));

        return modelAndView;
    }

    @RequestMapping("/memberCount")
    public ModelAndView showMemberCount(@RequestParam(value="isValid",defaultValue = "all") String isValid,
                                        @RequestParam(value="member_type",defaultValue = "real") String member_type,
                                        @RequestParam(value="start_date",defaultValue = "2016-09-01") String start_date_str,
                                        @RequestParam(value="end_date",defaultValue = "2016-11-07") String end_date_str){
        ModelAndView modelAndView = new ModelAndView();
        List<Member> members = new ArrayList<Member>();

        if(isValid.equals("all")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String cur_date_str = start_date_str;
            try {
                Date cur_date = sdf.parse(start_date_str);
                Date end_date = sdf.parse(end_date_str);
                while (cur_date.before(end_date) || cur_date.equals(end_date)) {
                    String cur_time = sdf.format(cur_date);
                    if (member_type.equals("real")) {
                        members.addAll(memberService.getAllRealMemberCount(start_date_str, cur_time));
                    } else {
                        members.addAll(memberService.getAllMemberCount(userTypeName.get(member_type), start_date_str, cur_time));
                    }
                    Calendar rightNow = Calendar.getInstance();
                    rightNow.setTime(cur_date);
                    rightNow.add(Calendar.DAY_OF_YEAR,1);
                    cur_date=rightNow.getTime();
                }
            }
            catch(ParseException e){
                e.printStackTrace();
            }
        }
        else {
            if(member_type.equals("real")) {
                members = memberService.getValidRealMemberCount(start_date_str, end_date_str);
            }
            else {
                members = memberService.getValidMemberCount(userTypeName.get(member_type), start_date_str, end_date_str);
            }
        }

        List<List<String>> chartData = getMemberCount(members);

        modelAndView.addObject("memberList", members);
        modelAndView.addObject("isValidList", validTypes);
        modelAndView.addObject("userTypeList", userTypes);
        modelAndView.addObject("typeName", userShowTypeName);
        modelAndView.addObject("validTypeName", validTypeName);
        modelAndView.addObject("chartData", chartData);
        modelAndView.addObject("start", start_date_str);
        modelAndView.addObject("end", end_date_str);
        modelAndView.addObject("utype", member_type);
        modelAndView.addObject("is", isValid);
        modelAndView.setViewName("memberCount");

        System.out.println(member_type);

        return modelAndView;
    }
}