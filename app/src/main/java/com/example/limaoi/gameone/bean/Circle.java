package com.example.limaoi.gameone.bean;


import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by limaoi on 2017/10/18.
 * E-mail：autismlm20@vip.qq.com
 */

public class Circle extends BmobObject {

    private String objectCircleId;
    private String userId;
    private String nickname;
    private String dynamic; //动态
    private List<String> dynamicPictureUrl;
    private String changeTime;
    private String headPictureUrl;
    private Integer likeCount;
    private Integer commentCount;
    private String userType;


    public Circle() {

    }

    public Circle(String objectCircleId, String headPictureUrl, String nickname, String dynamic, List<String> dynamicPictureUrl, String changeTime, Integer likeCount, Integer commentCount, String userType) {
        this.objectCircleId = objectCircleId;
        this.headPictureUrl = headPictureUrl;
        this.nickname = nickname;
        this.dynamic = dynamic;
        this.dynamicPictureUrl = dynamicPictureUrl;
        this.changeTime = changeTime;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.userType = userType;
    }


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setObjectCircleId(String objectCircleId) {
        this.objectCircleId = objectCircleId;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public void setDynamicPictureUrl(List<String> dynamicPictureUrl) {
        this.dynamicPictureUrl = dynamicPictureUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDynamic(String dynamic) {
        this.dynamic = dynamic;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDynamic() {
        return dynamic;
    }

    public List<String> getDynamicPictureUrl() {
        return dynamicPictureUrl;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public String getObjectCircleId() {
        return objectCircleId;
    }
}
