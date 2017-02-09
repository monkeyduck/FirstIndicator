package com.llc.service;

import com.llc.model.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by llc on 16/6/22.
 */
public interface LogService {

    public List<Log> getLog(String tableName);

    public List<Log> getDiff(@Param("tableNameA") String tableNameA,
                             @Param("tableNameB") String tableNameB);
}
