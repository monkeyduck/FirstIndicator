package com.llc.model;

/**
 * Created by llc on 16/7/14.
 */
public class FirstIndicator {
    private int dailyActive;

    private int newUserNum;

    private int bugNum;

    private float avgUsedTime;

    public String getDisplaytime() {
        return displaytime;
    }

    public void setDisplaytime(String displaytime) {
        this.displaytime = displaytime;
    }

    private String displaytime;

    public int getDailyActive() {
        return dailyActive;
    }

    public void setDailyActive(int dailyActive) {
        this.dailyActive = dailyActive;
    }

    public int getDailyNewUser() {
        return newUserNum;
    }

    public void setDailyNewUser(int dailyNewUser) {
        this.newUserNum = dailyNewUser;
    }

    public int getBugNum() {
        return bugNum;
    }

    public void setBugNum(int bugNum) {
        this.bugNum = bugNum;
    }

    public double getAvgUsedTime() {
        return avgUsedTime;
    }

    public void setAvgUsedTime(float avgUsedTime) {
        this.avgUsedTime = avgUsedTime;
    }
}
