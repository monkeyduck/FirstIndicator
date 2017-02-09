package com.llc.dao;

import com.llc.model.XunfeiRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface XunfeiDao {
    Map<String,String> getChartData(String column);

    List<String> getVersionListFromRequest(@Param("start_date") String start_date, @Param("end_date") String end_date);

    List<String> getVersionListFromRecLatency(@Param("start_date") String start_date, @Param("end_date") String end_date);

    List<String> getVersionListFromTTSLatency(@Param("start_date") String start_date, @Param("end_date") String end_date);

    List<XunfeiRequest> getXfRequestByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("start_date") String start_date, @Param("end_date") String end_date);

    List<XunfeiRequest> getXfRequestDetail(@Param("date") String date);

    List<XunfeiRequest> getXfRealRequestDetail(@Param("date") String date);

    List<XunfeiRequest> getXfRequestDetailByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfRealRequestDetailByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfRequestDetailByUser(@Param("member_type") String member_type, @Param("date") String date);

    List<XunfeiRequest> getXfRequestDetailByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfRecLatency(@Param("date") String date);

    List<XunfeiRequest> getXfRealRecLatency(@Param("date") String date);

    List<XunfeiRequest> getXfRecLatencyByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfRealRecLatencyByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfRecLatencyByUser(@Param("member_type") String member_type, @Param("date") String date);

    List<XunfeiRequest> getXfRecLatencyByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfTTSLatency(@Param("date") String date);

    List<XunfeiRequest> getXfTTSLatencyByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfRealTTSLatency(@Param("date") String date);

    List<XunfeiRequest> getXfRealTTSLatencyByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfTTSLatencyByUser(@Param("member_type") String member_type, @Param("date") String date);

    List<XunfeiRequest> getXfTTSLatencyByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getDailyXfRecLatencyByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("start_date") String start_date, @Param("end_date") String end_date);

    List<XunfeiRequest> getDailyXfTTSLatencyByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("start_date") String start_date, @Param("end_date") String end_date);

    List<XunfeiRequest> getXfRecLatencyTime(@Param("date") String date);

    List<XunfeiRequest> getXfRealRecLatencyTime(@Param("date") String date);

    List<XunfeiRequest> getXfRecLatencyTimeByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfRealRecLatencyTimeByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfRecLatencyTimeByUser(@Param("member_type") String member_type, @Param("date") String date);

    List<XunfeiRequest> getXfRecLatencyTimeByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfTTSLatencyTime(@Param("date") String date);

    List<XunfeiRequest> getXfTTSLatencyTimeByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfRealTTSLatencyTime(@Param("date") String date);

    List<XunfeiRequest> getXfRealTTSLatencyTimeByVersion(@Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getXfTTSLatencyTimeByUser(@Param("member_type") String member_type, @Param("date") String date);

    List<XunfeiRequest> getXfTTSLatencyTimeByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("date") String date);

    List<XunfeiRequest> getHourlyXfRecLatencyByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("start_date") String start_date, @Param("end_date") String end_date);

    List<XunfeiRequest> getHourlyXfTTSLatencyByUserByVersion(@Param("member_type") String member_type, @Param("version") String version, @Param("start_date") String start_date, @Param("end_date") String end_date);

}
