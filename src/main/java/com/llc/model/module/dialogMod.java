package com.llc.model.module;

import net.sf.json.JSONObject;

/**
 * Created by llc on 16/7/25.
 */
public class dialogMod extends BaseMod {

    public dialogMod(String content, String usedTime){
        super(content, usedTime);
        JSONObject json = JSONObject.fromObject(content);
        this.module = "dialog";
        this.name = json.getString("topic");
        if (name.equals("") || name.startsWith("file")){
            throw new NullPointerException("topic is null or starts with file");
        }
        this.num = 1;
    }

    @Override
    public void merge(BaseMod cur){
        super.merge(cur);
        this.name += ","+cur.getName();
        this.num++;
    }
}
