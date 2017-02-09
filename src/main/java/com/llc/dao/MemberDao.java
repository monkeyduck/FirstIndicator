package com.llc.dao;

import com.llc.model.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    List<Member> getMember(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<Member> getMemberByUserType(@Param("member_type") String member_type, @Param("date") String date);

    Map<String,String> getChartData(String column);

    List<String> getVersionList();

    List<Member> getDailyNewMember(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<Member> getDailyNewMemberByUserType(@Param("member_type") String userType, @Param("date") String date);

    List<Member> getDailyNewMemberByDate(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<Member> getDailyNewMemberByUserTypeByDate(@Param("member_type") String userType, @Param("date") String date);

    List<Member> getDailyActiveTime(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<Member> getDailyActiveTimeByUserType(@Param("member_type") String member_type, @Param("date") String date);

    List<Member> getValidDailyActiveTime(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<Member> getValidDailyActiveTimeByUserType(@Param("member_type") String member_type, @Param("date") String date);

    List<Member> getAllMemberCount(@Param("member_type") String member_type, @Param("start_date") String start_date, @Param("end_date") String end_date);

    List<Member> getValidMemberCount(@Param("member_type") String member_type, @Param("start_date") String start_date, @Param("end_date") String end_date);

    List<Member> getAllRealMemberCount(@Param("start_date") String start_date, @Param("end_date") String end_date);

    List<Member> getValidRealMemberCount(@Param("start_date") String start_date, @Param("end_date") String end_date);
}
