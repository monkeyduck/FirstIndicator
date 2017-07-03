package com.llc.dao;

import com.llc.model.FirstIndicator;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by llc on 16/7/14.
 */
public interface StatDao {

    List<FirstIndicator> getStatistic(@Param("member_type") String member_type,
                                      @Param("version") String version);

    List<FirstIndicator> getBeiwaStatistic(@Param("member_type") String member_type,
                                      @Param("version") String version);

    List<FirstIndicator> getStatisticByVersion(String version);

    List<FirstIndicator> getStatisticByDate(@Param("member_type") String member_type,
                                            @Param("version") String version);

    List<FirstIndicator> getBeiwaStatisticByDate(@Param("member_type") String member_type,
                                            @Param("version") String version);

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
