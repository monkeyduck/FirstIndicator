package com.llc.model.module;

import net.sf.json.JSONObject;
import org.joda.time.DateTime;

/**
 * Created by llc on 16/7/25.
 */
public class BaseMod {
    protected String fromTime;

    protected String toTime;

    protected String module;

    protected int usedTime;

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
        DateTime dt = new DateTime(Long.parseLong(json.getString("fromTime")));
        this.fromTime = dt.toString("yyyy-MM-dd HH:mm:ss");
        dt = new DateTime(Long.parseLong(json.getString("toTime")));
        this.toTime = dt.toString("yyyy-MM-dd HH:mm:ss");
        this.usedTime = Integer.parseInt(usedTime)/1000/60+1;

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

    public int getUsedTime() {
        return usedTime;
    }

    public int getNum() {
        return num;
    }

    public void merge(BaseMod cur){
        this.toTime = cur.getToTime();
        int curUsed = cur.getUsedTime();
        this.usedTime = usedTime+curUsed;
    }
}
