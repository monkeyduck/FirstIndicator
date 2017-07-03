package com.llc.service.impl;

import com.llc.dao.StatDao;
import com.llc.model.FirstIndicator;
import com.llc.service.StatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by llc on 16/7/14.
 */
@Service("StatService")
public class StatServiceImpl implements StatService{
    @Resource
    StatDao statDao;

    public List<FirstIndicator> getStatistic(String member_type, String version){
        return this.statDao.getStatistic(member_type, version);
    }

    @Override
    public List<FirstIndicator> getBeiwaStatistic(String member_type, String version) {
        return this.statDao.getBeiwaStatistic(member_type, version);
    }

    public List<FirstIndicator> getStatisticByVersion(String version){
        return this.statDao.getStatisticByVersion(version);
    }

    public List<FirstIndicator> getStatisticByDate(String member_type, String version){
        return this.statDao.getStatisticByDate(member_type, version);
    }

    @Override
    public List<FirstIndicator> getBeiwaStatisticByDate(String member_type, String version) {
        return this.statDao.getBeiwaStatisticByDate(member_type, version);
    }

    public List<String> getVersionList(){
        return this.statDao.getVersionList();
    }

    @Override
    public List<String> getBeiwaVersionList() {
        return statDao.getBeiwaVersionList();
    }

    public Map<String,String> getChartData(String column){
        return this.statDao.getChartData(column);
    }

    public List<FirstIndicator> getStatisticByVersionByDate(String version){
        return this.statDao.getStatisticByVersionByDate(version);
    }

    public List<FirstIndicator> getStatisticByUserType(String userType){
        return this.statDao.getStatisticByUserType(userType);
    }

    public List<FirstIndicator> getStatisticByUserTypeByDate(String userType){
        return this.statDao.getStatisticByUserTypeByDate(userType);
    }

    @Override
    public List<Integer> getTodayDailyActive(String date) {
        return statDao.getTodayDailyActive(date);
    }

    @Override
    public List<Integer> getBeiwaTodayDailyActive(String date) {
        return statDao.getBeiwaTodayDailyActive(date);
    }

    @Override
    public List<Integer> getBeiwaTodayHourlyActive(String date) {
        return statDao.getBeiwaTodayHourlyActive(date);
    }

    @Override
    public List<Integer> getTodayNewUser(String date) {
        return statDao.getTodayNewUser(date);
    }

    @Override
    public List<Integer> getBeiwaTodayNewUser(String date) {
        return statDao.getBeiwaTodayNewUser(date);
    }

    @Override
    public List<Float> getOneDayRetention(String date) {
        return statDao.getOneDayRetention(date);
    }

    @Override
    public List<Float> getThreeDayRetention(String date) {
        return statDao.getThreeDayRetention(date);
    }

    @Override
    public List<Float> getSevenDayRetention(String date) {
        return statDao.getSevenDayRetention(date);
    }


}
