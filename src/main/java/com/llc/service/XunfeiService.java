package com.llc.service;

import com.llc.model.XunfeiRequest;

import java.util.List;
import java.util.Map;

public interface XunfeiService {
    List<String> getVersionListFromRequest(String start_date, String end_date);

    List<String> getVersionListFromRecLatency(String start_date, String end_date);

    List<String> getVersionListFromTTSLatency(String start_date, String end_date);

    Map<String,String> getChartData(String column);

    List<XunfeiRequest> getXfRequestByUserByVersion(String member_type, String version, String start_date, String end_date);

    List<XunfeiRequest> getXfRequestDetail(String date);

    List<XunfeiRequest> getXfRealRequestDetail(String date);

    List<XunfeiRequest> getXfRequestDetailByUser(String member_type, String date);

    List<XunfeiRequest> getXfRequestDetailByVersion(String version, String date);

    List<XunfeiRequest> getXfRealRequestDetailByVersion(String version, String date);

    List<XunfeiRequest> getXfRequestDetailByUserByVersion(String member_type, String version, String date);

    List<XunfeiRequest> getXfRecLatency(String date);

    List<XunfeiRequest> getXfRealRecLatency(String date);

    List<XunfeiRequest> getXfRecLatencyByUser(String member_type, String date);

    List<XunfeiRequest> getXfRecLatencyByVersion(String version, String date);

    List<XunfeiRequest> getXfRealRecLatencyByVersion(String version, String date);

    List<XunfeiRequest> getXfRecLatencyByUserByVersion(String member_type, String version, String date);

    List<XunfeiRequest> getXfTTSLatency(String date);

    List<XunfeiRequest> getXfRealTTSLatency(String date);

    List<XunfeiRequest> getXfTTSLatencyByUser(String member_type, String date);

    List<XunfeiRequest> getXfTTSLatencyByVersion(String version, String date);

    List<XunfeiRequest> getXfRealTTSLatencyByVersion(String version, String date);

    List<XunfeiRequest> getXfTTSLatencyByUserByVersion(String member_type, String version, String date);

    List<XunfeiRequest> getDailyXfRecLatencyByUserByVersion(String member_type, String version, String start_date, String end_date);

    List<XunfeiRequest> getDailyXfTTSLatencyByUserByVersion(String member_type, String version, String start_date, String end_date);

    List<XunfeiRequest> getXfRecLatencyTime(String date);

    List<XunfeiRequest> getXfRealRecLatencyTime(String date);

    List<XunfeiRequest> getXfRecLatencyTimeByUser(String member_type, String date);

    List<XunfeiRequest> getXfRecLatencyTimeByVersion(String version, String date);

    List<XunfeiRequest> getXfRealRecLatencyTimeByVersion(String version, String date);

    List<XunfeiRequest> getXfRecLatencyTimeByUserByVersion(String member_type, String version, String date);

    List<XunfeiRequest> getXfTTSLatencyTime(String date);

    List<XunfeiRequest> getXfRealTTSLatencyTime(String date);

    List<XunfeiRequest> getXfTTSLatencyTimeByUser(String member_type, String date);

    List<XunfeiRequest> getXfTTSLatencyTimeByVersion(String version, String date);

    List<XunfeiRequest> getXfRealTTSLatencyTimeByVersion(String version, String date);

    List<XunfeiRequest> getXfTTSLatencyTimeByUserByVersion(String member_type, String version, String date);

    List<XunfeiRequest> getHourlyXfRecLatencyByUserByVersion(String member_type, String version, String start_date, String end_date);

    List<XunfeiRequest> getHourlyXfTTSLatencyByUserByVersion(String member_type, String version, String start_date, String end_date);

}
