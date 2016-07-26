package com.llc.model.module;

import net.sf.json.JSONObject;

/**
 * Created by llc on 16/7/25.
 */
public class gameMod extends BaseMod{

    public gameMod(String module, String content, String usedTime){
        super(content, usedTime);
        JSONObject json = JSONObject.fromObject(content);
        this.module = module;
        this.num = Integer.parseInt(json.getString("questionNums"));
        if (num <= 0){
            throw new NullPointerException("question nums equals 0");
        }
    }

    @Override
    public void addNum(int n){
        num += n;
    }

    @Override
    public void merge(BaseMod cur){
        super.merge(cur);
        this.num += cur.getNum();
    }
}
