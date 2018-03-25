package com.example.limaoi.gameone.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by limaoi on 2017/12/14.
 * E-mail：autismlm20@vip.qq.com
 */

public class Video extends BmobObject {

    private String objectVideoId;
    private String userId;
    private String nickname;
    private String title; //标题
    private String videoUrl;
    private String videoPicUrl;
    private String changeTime;
    private String headPictureUrl;
    private Integer likeCount;
    private Integer commentCount;
    private Integer playCount;
    private String label;
    private String userType;


    public Video() {

    }

    public Video(String title, String videoUrl, String videoPicUrl, String nickname, String headPictureUrl, Integer likeCount, Integer commentCount,String userType) {
        this.title = title;
        this.videoUrl = videoUrl;
        this.videoPicUrl = videoPicUrl;
        this.nickname = nickname;
        this.headPictureUrl = headPictureUrl;
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

    public void setLabel(String label) {
        this.label = label;
    }

    public void setObjectVideoId(String objectVideoId) {
        this.objectVideoId = objectVideoId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setVideoPicUrl(String videoPicUrl) {
        this.videoPicUrl = videoPicUrl;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public String getObjectVideoId() {
        return objectVideoId;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoPicUrl() {
        return videoPicUrl;
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

    public String getLabel() {
        return label;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public Integer getPlayCount() {
        return playCount;
    }
}
