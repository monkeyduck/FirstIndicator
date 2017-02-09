package com.llc.service.impl;

import com.llc.dao.LogDao;
import com.llc.model.Log;
import com.llc.service.LogService;
import org.apache.ibatis.annotations.Param;
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

    @Override
    public List<Log> getLog(String tableName) {
        return logDao.getLog(tableName);
    }

    @Override
    public List<Log> getDiff(@Param("tableNameA") String tableNameA,
                             @Param("tableNameB") String tableNameB) {
        return logDao.getDiff(tableNameA, tableNameB);
    }

}
