package com.llc.model;

/**
 * Created by llc on 16/7/14.
 */
public class FirstIndicator {

    private int totalUserNum;

    private int dailyActive;

    private int newUserNum;

    private int bugNum;

    private float retention;

    public float getRetention() {
        return retention;
    }

    public String getValueColumn(int column){
        if (column == 1){
            return ""+totalUserNum;
        }
        else if (column == 2){
            return ""+dailyActive;
        }
        else if (column == 3){
            return "";
        }
        return "";
    }

    public void setRetention(float retention) {
        this.retention = retention;
    }

    private float avgUsedTimePerUser;

    private String displayTime;

    public void setTotalUserNum(int totalUserNum) {
        this.totalUserNum = totalUserNum;
    }

    public int getNewUserNum() {
        return newUserNum;
    }

    public void setNewUserNum(int newUserNum) {
        this.newUserNum = newUserNum;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public int getTotalUserNum() {
        return totalUserNum;
    }

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

    public float getAvgUsedTimePerUser() {
        return avgUsedTimePerUser;
    }

    public void setAvgUsedTimePerUser(float avgUsedTimePerUser) {
        this.avgUsedTimePerUser = avgUsedTimePerUser;
    }
}
