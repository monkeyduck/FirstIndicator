package com.llc.model.module;

import net.sf.json.JSONObject;

/**
 * Created by llc on 16/7/25.
 */
public class BaseMod {
    protected String fromTime;

    protected String toTime;

    protected String module;

    protected String usedTime;

    protected String name;

    public String getName() {
        return name;
    }

    protected int num;

    public BaseMod(String content, String usedTime){
        if (content==null){
            throw new NullPointerException("content is null");
        }
        JSONObject json = JSONObject.fromObject(content);
        this.fromTime = json.getString("fromTime");
        this.toTime = json.getString("toTime");
        this.usedTime = usedTime;

    }

    public void addNum(int n){
        num++;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public String getModule() {
        return module;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public int getNum() {
        return num;
    }

    public void merge(BaseMod cur){
        this.toTime = cur.getToTime();
        long used = Long.parseLong(usedTime);
        long curUsed = Long.parseLong(cur.getUsedTime());
        this.usedTime = Long.toString(used+curUsed);
    }
}
