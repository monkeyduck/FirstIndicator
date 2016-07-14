package com.llc.model;

/**
 * Created by llc on 16/7/14.
 */
public class FirstIndicator {
    private int dailyActive;

    private int dailyNewUser;

    private int fatalBugNum;

    private float avgUsedTime;

    public int getDailyActive() {
        return dailyActive;
    }

    public void setDailyActive(int dailyActive) {
        this.dailyActive = dailyActive;
    }

    public int getDailyNewUser() {
        return dailyNewUser;
    }

    public void setDailyNewUser(int dailyNewUser) {
        this.dailyNewUser = dailyNewUser;
    }

    public int getFatalBugNum() {
        return fatalBugNum;
    }

    public void setFatalBugNum(int fatalBugNum) {
        this.fatalBugNum = fatalBugNum;
    }

    public double getAvgUsedTime() {
        return avgUsedTime;
    }

    public void setAvgUsedTime(float avgUsedTime) {
        this.avgUsedTime = avgUsedTime;
    }
}
