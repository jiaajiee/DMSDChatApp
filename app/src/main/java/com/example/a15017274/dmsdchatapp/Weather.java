package com.example.a15017274.dmsdchatapp;

import java.io.Serializable;

/**
 * Created by 15017274 on 15/8/2017.
 */

public class Weather implements Serializable {
    String userName;
    String text;
    String time;

    public Weather() {

    }

    public Weather(String userName, String text, String time) {
        this.userName = userName;
        this.text = text;
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
