package com.example.limaoi.gameone.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by limaoi on 2017/6/28.
 * E-mail：autismlm20@vip.qq.com
 */

public class Person extends BmobUser {

    private String nickname;
    private String signature;
    private String sex;
    private String address;
    private String pic;
    private String userType;
    private String certified;


    public String getCertified() {
        return certified;
    }

    public void setCertified(String certified) {
        this.certified = certified;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }


    public String getAddress() {
        return address;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
