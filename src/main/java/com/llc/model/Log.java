package com.llc.model;

/**
 * Created by llc on 16/6/20.
 */


public class Log {
    //primary key
    private int id;

    private String content;

    private String time;

    private String ip;

    private String log_level;

    private String member_id;

    private String device_id;

    private String modtrans;

    private String log_source;

    private int log_time;

    private String log_topic;

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getIp() {
        return ip;
    }

    public String getLog_level() {
        return log_level;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLog_level(String log_level) {
        this.log_level = log_level;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setModtrans(String modtrans) {
        this.modtrans = modtrans;
    }

    public String getLog_source() {
        return log_source;
    }

    public void setLog_source(String log_source) {
        this.log_source = log_source;
    }

    public int getLog_time() {
        return log_time;
    }

    public void setLog_time(int log_time) {
        this.log_time = log_time;
    }

    public String getLog_topic() {
        return log_topic;
    }

    public void setLog_topic(String log_topic) {
        this.log_topic = log_topic;
    }

    public String getDevice_id() {
        return device_id;

    }

    public String getModtrans() {
        return modtrans;
    }
}
