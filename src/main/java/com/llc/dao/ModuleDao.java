package com.llc.dao;

import com.llc.model.moduleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by llc on 16/7/25.
 */
public interface ModuleDao {
    List<moduleInfo> getModuleInfo(@Param("member_id") String member_id,
                                   @Param("date") String date);

    List<String> getUsedTime(@Param("member_id") String member_id,
                                   @Param("date") String date);
}
