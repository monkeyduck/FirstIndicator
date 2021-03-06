package com.llc.model;

import com.llc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by llc on 16/7/14.
 */
public class FirstIndicator {

    private int totalUserNum;

    private int hourlyActive;

    private int dailyActive;

    private int validDailyActive;

    private int newUserNum;

    private int bugNum;

    private float retention;

    private float retention_3;

    private float retention_7;

    private String member_type;

    private String version;

    private float avgUsedTimePerUser;

    private float validAvgUsedTimePerUser;

    private String displayTime;

    public void setTotalUserNum(int totalUserNum) {
        this.totalUserNum = totalUserNum;
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

    public int getHourlyActive() {
        return hourlyActive;
    }

    public void setHourlyActive(int hourlyActive) {
        this.hourlyActive = hourlyActive;
    }


    public float getRetention_3() {
        return retention_3;
    }

    public void setRetention_3(float retention_3) {
        this.retention_3 = retention_3;
    }

    public void setRetention_7(float retention_7) {
        this.retention_7 = retention_7;
    }

    public float getRetention_7() {
        return retention_7;
    }

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

    public int getDailyActive() {
        return dailyActive;
    }

    public void setDailyActive(int dailyActive) {
        this.dailyActive = dailyActive;
    }

    public int getNewUserNum() {
        return newUserNum;
    }

    public void setNewUserNum(int newUserNum) {
        this.newUserNum = newUserNum;
    }

    public int getBugNum() {
        return bugNum;
    }

    public void setBugNum(int bugNum) {
        this.bugNum = bugNum;
    }

    public int getValidDailyActive() {
        return validDailyActive;
    }

    public void setValidDailyActive(int validDailyActive) {
        this.validDailyActive = validDailyActive;
    }

    public float getValidAvgUsedTimePerUser() {
        return validAvgUsedTimePerUser;
    }

    public void setValidAvgUsedTimePerUser(float validAvgUsedTimePerUser) {
        this.validAvgUsedTimePerUser = validAvgUsedTimePerUser;
    }

    public float getAvgUsedTimePerUser() {
        return avgUsedTimePerUser;
    }

    public void setAvgUsedTimePerUser(float avgUsedTimePerUser) {
        this.avgUsedTimePerUser = avgUsedTimePerUser;
    }

    public String getMember_type() {
        return member_type;
    }

    public void setMember_type(String member_type) {
        this.member_type = member_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> calCompare(FirstIndicator last, String type){
        List<String> compare = new ArrayList<String>();
        float f;
        compare.add(type.equals("date")?"周同比":"日同比");
        f = 100*Utils.devide(this.getTotalUserNum()-last.getTotalUserNum(),last.getTotalUserNum());
        compare.add(String.format("%.2f%%", f));
        f = 100*Utils.devide(this.getNewUserNum()-last.getNewUserNum(),last.getNewUserNum());
        compare.add(String.format("%.2f%%", f));
        f = 100*Utils.devide(this.getDailyActive()-last.getDailyActive(),last.getDailyActive());
        compare.add(String.format("%.2f%%", f));
        f = 100*Utils.devide(this.getValidDailyActive()-last.getValidDailyActive(),last.getValidDailyActive());
        compare.add(String.format("%.2f%%", f));
        f = 100*Utils.devide(this.getAvgUsedTimePerUser()-last.getAvgUsedTimePerUser(),
                last.getAvgUsedTimePerUser());
        compare.add(String.format("%.2f%%", f));
        f = 100*Utils.devide(this.getValidAvgUsedTimePerUser()-last.getValidAvgUsedTimePerUser(),
                last.getValidAvgUsedTimePerUser());
        compare.add(String.format("%.2f%%", f));
        f = 100*Utils.devide(this.getRetention()-last.getRetention(),last.getRetention());
        compare.add(String.format("%.2f%%", f));
        f = 100*Utils.devide(this.getRetention_3()-last.getRetention_3(),last.getRetention_3());
        compare.add(String.format("%.2f%%", f));
        f = 100*Utils.devide(this.getRetention_7()-last.getRetention_7(),last.getRetention_7());
        compare.add(String.format("%.2f%%", f));
        f = 100*Utils.devide(this.getBugNum()-last.getBugNum(),last.getBugNum());
        compare.add(String.format("%.2f%%", f));
        return compare;
    }
}
