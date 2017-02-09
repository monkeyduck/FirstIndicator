package com.llc.model;

import java.util.Calendar;

/**
 * Created by zoey on 16/10/28.
 */

public class Member {
    private String member_id;

    private String device_id;

    private String nick_name;

    private String real_name;

    private String gender;

    private String birthday;

    private String signup_time;

    private String last_signin_time;

    private String member_type;

    private String version;

    private String date;

    private String usedTime;

    private String isValid;

    private int member_count;

    public void setMember_id(String member_id) { this.member_id = member_id;}

    public String getMember_id(){
        return member_id;
    }

    public void setDevice_id(String device_id) { this.device_id = device_id;}

    public String getDevice_id(){
        return device_id;
    }

    public void setNick_name(String nick_name) { this.nick_name = nick_name;}

    public String getNick_name(){
        return nick_name;
    }

    public void setReal_name(String real_name) {this.real_name = real_name;}

    public String getReal_name(){
        return real_name;
    }

    public void setGender(String gender) { this.gender = gender;}

    public String getGender(){
//        if(gender.substring(0,1).equals("m")){
//            gender = "男";
//        }
//        else{
//            gender = "女";
//        }
        return gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public int getAge() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int age = year - Integer.parseInt(birthday.substring(0,4));
        return age;
    }

    public void setSignup_time(String signup_time) { this.signup_time = signup_time;}

    public String getSignup_time(){
        return signup_time;
    }

    public void setMember_type(String member_type) { this.member_type = member_type;}

    public String getMember_type(){
        return member_type;
    }

    public void setVersion(String version) { this.version = version;}

    public String getVersion(){
        return version;
    }

    public void setQuery_date(String date){ this.date = date;}

    public String getDate(){
        return date;
    }

    public void setUsedTime(String usedTime) { this.usedTime = usedTime;}

    public String getUsedTime() { return usedTime; }

    public void setIsValid(String isValid) { this.isValid = isValid; }

    public String getIsValid() { return isValid; }

    public void setMember_count(int member_count) { this.member_count = member_count; }

    public int getMember_count() { return member_count; }
}