package com.llc.service.impl;

import com.llc.dao.ModuleDao;
import com.llc.model.moduleInfo;
import com.llc.service.ModuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by llc on 16/7/25.
 */
@Service("ModuleService")
public class ModuleServiceImpl implements ModuleService{
    @Resource
    private ModuleDao moduleDao;

    public List<moduleInfo> getModuleInfo(String member_id, String date){
        return moduleDao.getModuleInfo(member_id,date);
    }

    public List<String> getUsedTime(String member_id, String date) {
        return moduleDao.getUsedTime(member_id, date);
    }

}
