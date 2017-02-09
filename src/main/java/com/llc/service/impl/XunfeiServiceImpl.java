package com.llc.service.impl;

import com.llc.dao.XunfeiDao;
import com.llc.model.XunfeiRequest;
import com.llc.service.XunfeiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zoey on 16/11/16.
 */
@Service("XunfeiService")
public class XunfeiServiceImpl implements XunfeiService {
    @Resource
    XunfeiDao xfDao;

    public List<XunfeiRequest> getXfRequestByUserByVersion(String member_type, String version, String start_date, String end_date){
        return this.xfDao.getXfRequestByUserByVersion(member_type, version, start_date, end_date);
    }

    public List<XunfeiRequest> getXfRequestDetail(String date){
        return this.xfDao.getXfRequestDetail(date);
    }

    public List<XunfeiRequest> getXfRequestDetailByVersion(String version, String date){
        return this.xfDao.getXfRequestDetailByVersion(version, date);
    }

    public List<XunfeiRequest> getXfRealRequestDetail(String date){
        return this.xfDao.getXfRealRequestDetail(date);
    }

    public List<XunfeiRequest> getXfRealRequestDetailByVersion(String version, String date){
        return this.xfDao.getXfRealRequestDetailByVersion(version, date);
    }

    public List<XunfeiRequest> getXfRequestDetailByUser(String member_type, String date){
        return this.xfDao.getXfRequestDetailByUser(member_type, date);
    }

    public List<XunfeiRequest> getXfRequestDetailByUserByVersion(String member_type, String version, String date){
        return this.xfDao.getXfRequestDetailByUserByVersion(member_type, version, date);
    }

    public List<XunfeiRequest> getXfRecLatency(String date){
        return this.xfDao.getXfRecLatency(date);
    }

    public List<XunfeiRequest> getXfRecLatencyByVersion(String version, String date){
        return this.xfDao.getXfRecLatencyByVersion(version, date);
    }

    public List<XunfeiRequest> getXfRealRecLatency(String date){
        return this.xfDao.getXfRealRecLatency(date);
    }

    public List<XunfeiRequest> getXfRealRecLatencyByVersion(String version, String date){
        return this.xfDao.getXfRealRecLatencyByVersion(version, date);
    }

    public List<XunfeiRequest> getXfRecLatencyByUser(String member_type, String date){
        return this.xfDao.getXfRecLatencyByUser(member_type, date);
    }

    public List<XunfeiRequest> getXfRecLatencyByUserByVersion(String member_type, String version, String date){
        return this.xfDao.getXfRecLatencyByUserByVersion(member_type, version, date);
    }

    public List<XunfeiRequest> getXfTTSLatency(String date){
        return this.xfDao.getXfTTSLatency(date);
    }

    public List<XunfeiRequest> getXfTTSLatencyByVersion(String version, String date){
        return this.xfDao.getXfTTSLatencyByVersion(version, date);
    }

    public List<XunfeiRequest> getXfRealTTSLatency(String date){
        return this.xfDao.getXfRealTTSLatency(date);
    }

    public List<XunfeiRequest> getXfRealTTSLatencyByVersion(String version, String date){
        return this.xfDao.getXfRealTTSLatencyByVersion(version, date);
    }

    public List<XunfeiRequest> getXfTTSLatencyByUser(String member_type, String date){
        return this.xfDao.getXfTTSLatencyByUser(member_type, date);
    }

    public List<XunfeiRequest> getXfTTSLatencyByUserByVersion(String member_type, String version, String date){
        return this.xfDao.getXfTTSLatencyByUserByVersion(member_type, version, date);
    }

    public Map<String,String> getChartData(String column){
        return this.xfDao.getChartData(column);
    }

    public List<String> getVersionListFromRequest(String start_date, String end_date){
        return this.xfDao.getVersionListFromRequest(start_date,end_date);
    }

    public List<String> getVersionListFromRecLatency(String start_date, String end_date){
        return this.xfDao.getVersionListFromRecLatency(start_date,end_date);
    }

    public List<String> getVersionListFromTTSLatency(String start_date, String end_date){
        return this.xfDao.getVersionListFromTTSLatency(start_date,end_date);
    }

    public List<XunfeiRequest> getDailyXfRecLatencyByUserByVersion(String member_type, String version, String start_date, String end_date){
        return this.xfDao.getDailyXfRecLatencyByUserByVersion(member_type, version, start_date, end_date);
    }

    public List<XunfeiRequest> getDailyXfTTSLatencyByUserByVersion(String member_type, String version, String start_date, String end_date){
        return this.xfDao.getDailyXfTTSLatencyByUserByVersion(member_type, version, start_date, end_date);
    }

    public List<XunfeiRequest> getHourlyXfRecLatencyByUserByVersion(String member_type, String version, String start_date, String end_date){
        return this.xfDao.getHourlyXfRecLatencyByUserByVersion(member_type, version, start_date, end_date);
    }

    public List<XunfeiRequest> getHourlyXfTTSLatencyByUserByVersion(String member_type, String version, String start_date, String end_date){
        return this.xfDao.getHourlyXfTTSLatencyByUserByVersion(member_type, version, start_date, end_date);
    }

    public List<XunfeiRequest> getXfRecLatencyTime(String date){
        return this.xfDao.getXfRecLatencyTime(date);
    }

    public List<XunfeiRequest> getXfRecLatencyTimeByVersion(String version, String date){
        return this.xfDao.getXfRecLatencyTimeByVersion(version, date);
    }

    public List<XunfeiRequest> getXfRealRecLatencyTime(String date){
        return this.xfDao.getXfRealRecLatencyTime(date);
    }

    public List<XunfeiRequest> getXfRealRecLatencyTimeByVersion(String version, String date){
        return this.xfDao.getXfRealRecLatencyTimeByVersion(version, date);
    }

    public List<XunfeiRequest> getXfRecLatencyTimeByUser(String member_type, String date){
        return this.xfDao.getXfRecLatencyTimeByUser(member_type, date);
    }

    public List<XunfeiRequest> getXfRecLatencyTimeByUserByVersion(String member_type, String version, String date){
        return this.xfDao.getXfRecLatencyTimeByUserByVersion(member_type, version, date);
    }

    public List<XunfeiRequest> getXfTTSLatencyTime(String date){
        return this.xfDao.getXfTTSLatencyTime(date);
    }

    public List<XunfeiRequest> getXfTTSLatencyTimeByVersion(String version, String date){
        return this.xfDao.getXfTTSLatencyTimeByVersion(version, date);
    }

    public List<XunfeiRequest> getXfRealTTSLatencyTime(String date){
        return this.xfDao.getXfRealTTSLatencyTime(date);
    }

    public List<XunfeiRequest> getXfRealTTSLatencyTimeByVersion(String version, String date){
        return this.xfDao.getXfRealTTSLatencyTimeByVersion(version, date);
    }

    public List<XunfeiRequest> getXfTTSLatencyTimeByUser(String member_type, String date){
        return this.xfDao.getXfTTSLatencyTimeByUser(member_type, date);
    }

    public List<XunfeiRequest> getXfTTSLatencyTimeByUserByVersion(String member_type, String version, String date){
        return this.xfDao.getXfTTSLatencyTimeByUserByVersion(member_type, version, date);
    }

}

