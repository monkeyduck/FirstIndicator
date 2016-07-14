package com.llc.service.impl;

import com.llc.dao.StatDao;
import com.llc.model.FirstIndicator;
import com.llc.service.StatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by llc on 16/7/14.
 */
@Service("StatService")
public class StatServiceImpl implements StatService{
    @Resource
    StatDao statDao;

    public FirstIndicator getStatistic(){
        return this.statDao.getStatistic();
    }
}
