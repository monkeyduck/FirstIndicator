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

    public List<FirstIndicator> getStatistic(){
        return this.statDao.getStatistic();
    }

    public List<FirstIndicator> getStatisticByVersion(String version){
        return this.statDao.getStatisticByVersion(version);
    }

    public List<FirstIndicator> getStatisticByDate(){
        return this.statDao.getStatisticByDate();
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

}
