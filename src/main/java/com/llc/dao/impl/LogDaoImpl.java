package com.llc.dao.impl;

import com.llc.dao.LogDao;
import com.llc.model.Log;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by llc on 16/6/22.
 */
public class LogDaoImpl implements LogDao {

    public Log getLog(int id){return null;}

    public List<Log> queryLog(String member_id, String time) {
        return null;
    }

    public void addLog(String log_source, int log_time, String log_topic, String time, String device_id, String ip, String member_id, String log_level, String modtrans, String content) {

    }
}
