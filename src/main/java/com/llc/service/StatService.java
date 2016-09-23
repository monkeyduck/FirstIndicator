package com.llc.service;

import com.llc.model.FirstIndicator;

import java.util.List;
import java.util.Map;

/**
 * Created by llc on 16/7/14.
 */
public interface StatService {

    List<FirstIndicator> getStatistic(String member_type, String version);

    List<FirstIndicator> getStatisticByVersion(String version);

    List<FirstIndicator> getStatisticByDate(String member_type, String version);

    List<String> getVersionList();

    Map<String,String> getChartData(String column);

    List<FirstIndicator> getStatisticByVersionByDate(String version);

    List<FirstIndicator> getStatisticByUserType(String userType);

    List<FirstIndicator> getStatisticByUserTypeByDate(String userType);

}
