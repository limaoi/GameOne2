package com.example.limaoi.gameone.bean;

/**
 * Created by limaoi on 2017/7/14.
 * E-mail：autismlm20@vip.qq.com
 */

public class Info {

    public Info(String title, String value) {
        this.title = title;
        this.value = value;
    }

    private String title;

    private String value;

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
