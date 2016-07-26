package com.llc.model.module;

import net.sf.json.JSONObject;

/**
 * Created by llc on 16/7/25.
 */
public class mediaMod extends BaseMod{

    public mediaMod(String module, String content, String usedTime){
        super(content, usedTime);
        JSONObject json = JSONObject.fromObject(content);
        if (json.getString("methodName").equals("usedTime") ||
            json.getString("isValid").equals("false")){
            throw new NullPointerException("invalid usage");
        }
        this.name = json.getString("mediaName");
        this.module = module;
        this.num = 1;
    }

    @Override
    public void merge(BaseMod cur){
        super.merge(cur);
        this.name += ","+cur.getName();
        this.num++;
    }
}
