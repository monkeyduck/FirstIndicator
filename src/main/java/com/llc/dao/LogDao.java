package com.llc.dao;

import com.llc.model.Log;

import java.util.List;

/**
 * Created by llc on 16/6/22.
 */
public interface LogDao {

    public Log getLog(int id);

    public List<Log> queryLog(String member_id, String time);

    public void addLog(String log_source, int log_time, String log_topic, String time, String device_id,
                       String ip, String member_id, String log_level, String modtrans, String content);
}
