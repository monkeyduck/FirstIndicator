package com.llc.service;

import com.llc.model.moduleInfo;

import java.util.List;

/**
 * Created by llc on 16/7/25.
 */
public interface ModuleService {
    List<moduleInfo> getModuleInfo(String member_id, String date);

    List<String> getUsedTime(String member_id, String date);

}
