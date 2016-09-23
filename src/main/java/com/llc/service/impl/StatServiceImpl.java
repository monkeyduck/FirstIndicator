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

    public List<FirstIndicator> getStatisticByVersion(String version){
        return this.statDao.getStatisticByVersion(version);
    }

    public List<FirstIndicator> getStatisticByDate(String member_type, String version){
        return this.statDao.getStatisticByDate(member_type, version);
    }

    public List<String> getVersionList(){
        return this.statDao.getVersionList();
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


}
