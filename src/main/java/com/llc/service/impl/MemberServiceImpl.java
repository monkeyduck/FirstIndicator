package com.llc.service.impl;

import com.llc.dao.MemberDao;
import com.llc.model.Member;
import com.llc.service.MemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zoey on 16/10/28.
 */
@Service("MemberService")
public class MemberServiceImpl implements MemberService{
    @Resource
    private MemberDao memberDao;

    public List<Member> getMember(String member_type, String version, String date){
        return this.memberDao.getMember(member_type, version, date);
    }

    public List<Member> getMemberByUserType(String userType, String date){
        return this.memberDao.getMemberByUserType(userType, date);
    }

    public Map<String,String> getChartData(String column){
        return this.memberDao.getChartData(column);
    }

    public List<String> getVersionList(){
        return this.memberDao.getVersionList();
    }

    public List<Member> getDailyNewMember(String member_type, String version, String date){
        return this.memberDao.getDailyNewMember(member_type, version, date);
    }

    public List<Member> getDailyNewMemberByUserType(String userType, String date){
        return this.memberDao.getDailyNewMemberByUserType(userType, date);
    }

    public List<Member> getDailyNewMemberByDate(String member_type, String version, String date){
        return this.memberDao.getDailyNewMemberByDate(member_type, version, date);
    }

    public List<Member> getDailyNewMemberByUserTypeByDate(String userType, String date){
        return this.memberDao.getDailyNewMemberByUserTypeByDate(userType, date);
    }

    public List<Member> getDailyActiveTime(String member_type, String version, String date){
        return this.memberDao.getDailyActiveTime(member_type, version, date);
    }

    public List<Member> getDailyActiveTimeByUserType(String member_type, String date){
        return this.memberDao.getDailyActiveTimeByUserType(member_type, date);
    }

    public List<Member> getValidDailyActiveTime(String member_type, String version, String date){
        return this.memberDao.getValidDailyActiveTime(member_type, version, date);
    }

    public List<Member> getValidDailyActiveTimeByUserType(String member_type, String date){
        return this.memberDao.getValidDailyActiveTimeByUserType(member_type, date);
    }

    public List<Member> getAllMemberCount(String member_type, String start_date, String end_date){
        return this.memberDao.getAllMemberCount(member_type, start_date, end_date);
    }

    public List<Member> getValidMemberCount(String member_type, String start_date, String end_date){
        return this.memberDao.getValidMemberCount(member_type, start_date, end_date);
    }

    public List<Member> getAllRealMemberCount(String start_date, String end_date){
        return this.memberDao.getAllRealMemberCount(start_date, end_date);
    }

    public List<Member> getValidRealMemberCount(String start_date, String end_date){
        return this.memberDao.getValidRealMemberCount(start_date, end_date);
    }
}
