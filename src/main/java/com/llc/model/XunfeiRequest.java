package com.llc.model;

/**
 * Created by zoey on 16/11/9.
 */
public class XunfeiRequest {
    public XunfeiRequest(int member_count, String date, int avg, int first_quarter, int third_quarter, int max, int min, String version, String member_type){
        this.date = date;
        this.member_count = member_count;
        this.max = max;
        this.min = min;
        this.avg = avg;
        this.first_quarter = first_quarter;
        this.third_quarter = third_quarter;
        this.version = version;
        this.member_type = member_type;
    }

    public XunfeiRequest(){}

    private String date;

    private long request_count;

    private String member_id;

    private String member_type;

    private String version;

    private int max;

    private int min;

    private int avg;

    private int first_quarter;

    private int third_quarter;

    private int member_count;

    private long total_request;

    public void setDate(String date){ this.date = date;}

    public String getDate() {return date;}

    public void setRequest_count() {this.request_count=request_count;}

    public long getRequest_count() {return request_count;}

    public void setMember_id(String member_id) { this.member_id = member_id;}

    public String getMember_id(){
        return member_id;
    }

    public String getMember_type() { return member_type;}

    public void setMember_type(String member_type) { this.member_type = member_type;}

    public void setVersion(String version) {this.version = version;}

    public String getVersion() { return version;}

    public void setAvg(int avg){this.avg=avg;}

    public int getAvg() { return avg;}

    public void setFirst_quarter(int first_quarter) {this.first_quarter=first_quarter;}

    public int getFirst_quarter() {return first_quarter;}

    public void setThird_quarter(int third_quarter) { this.third_quarter = third_quarter;}

    public int getThird_quarter() { return third_quarter;}

    public void setMax(int max) {this.max = max;}

    public int getMax() { return max;}

    public void setMin(int min){this.min = min;}

    public int getMin(){return min;}

    public int getMember_count() { return member_count; }

    public void setMember_count(int member_count) { this.member_count = member_count; }

    public long getTotal_request() { return total_request; }

    public void setTotal_request(long total_request) { this.total_request = total_request; }
}
