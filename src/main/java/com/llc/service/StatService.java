package com.llc.service;

import com.llc.model.FirstIndicator;

import java.util.List;
import java.util.Map;

/**
 * Created by llc on 16/7/14.
 */
public interface StatService {

    List<FirstIndicator> getStatistic(String member_type, String version);

    List<FirstIndicator> getBeiwaStatistic(String member_type, String version);

    List<FirstIndicator> getStatisticByVersion(String version);

    List<FirstIndicator> getStatisticByDate(String member_type, String version);

    List<FirstIndicator> getBeiwaStatisticByDate(String member_type, String version);

    List<String> getVersionList();

    List<String> getBeiwaVersionList();


    Map<String,String> getChartData(String column);

    List<FirstIndicator> getStatisticByVersionByDate(String version);

    List<FirstIndicator> getStatisticByUserType(String userType);

    List<FirstIndicator> getStatisticByUserTypeByDate(String userType);

    List<Integer> getTodayDailyActive(String date);

    List<Integer> getBeiwaTodayDailyActive(String date);

    List<Integer> getBeiwaTodayHourlyActive(String date);

    List<Integer> getTodayNewUser(String date);

    List<Integer> getBeiwaTodayNewUser(String date);

    List<Float> getOneDayRetention(String date);

    List<Float> getThreeDayRetention(String date);

    List<Float> getSevenDayRetention(String date);

}
