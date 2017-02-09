package com.llc.dao;

import com.llc.model.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by llc on 16/6/22.
 */
public interface LogDao {

    public List<Log> getLog(@Param("tableName")String tableName);

    public List<Log> getDiff(@Param("tableNameA") String tableNameA,
                             @Param("tableNameB") String tableNameB);
}
