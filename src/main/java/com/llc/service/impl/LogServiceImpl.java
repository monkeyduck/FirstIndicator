package com.llc.service.impl;

import com.llc.dao.LogDao;
import com.llc.model.Log;
import com.llc.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by llc on 16/6/20.
 */
@Service("LogService")
public class LogServiceImpl implements LogService {

    @Resource
    private LogDao logDao;

    public Log getLog(int id) {
        return this.logDao.getLog(id);
    }

    public void addLog(String log_source, int log_time, String log_topic, String time, String device_id,
                       String ip, String member_id, String log_level, String modtrans, String content){
        this.logDao.addLog(log_source, log_time,  log_topic,  time,  device_id,
                 ip,  member_id,  log_level,  modtrans,  content);
    }

    public List<Log> queryLog(String member_id, String time) {
        return logDao.queryLog(member_id, time);
    }
}
