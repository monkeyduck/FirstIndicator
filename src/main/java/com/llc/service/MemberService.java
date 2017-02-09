package com.llc.service;

import com.llc.model.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    List<String> getVersionList();

    Map<String,String> getChartData(String column);

    List<Member> getMember(String member_type, String version, String date);

    List<Member> getMemberByUserType(String userType, String date);

    List<Member> getDailyNewMember(String member_type, String version, String date);

    List<Member> getDailyNewMemberByUserType(String userType, String date);

    List<Member> getDailyNewMemberByDate(String member_type, String version, String date);

    List<Member> getDailyNewMemberByUserTypeByDate(String userType, String date);

    List<Member> getDailyActiveTime(String member_type, String version, String date);

    List<Member> getDailyActiveTimeByUserType(String userType, String date);

    List<Member> getValidDailyActiveTime(String member_type, String version, String date);

    List<Member> getValidDailyActiveTimeByUserType(String userType, String date);

    List<Member> getAllMemberCount(String userType, String start_date, String end_date);

    List<Member> getValidMemberCount(String member_type, String start_date, String end_date);

    List<Member> getAllRealMemberCount(String start_date, String end_date);

    List<Member> getValidRealMemberCount(String start_date, String end_date);
}
