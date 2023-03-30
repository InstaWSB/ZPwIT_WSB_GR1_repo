package com.zpwit_wsb_gr1_project.model;

public class HomeModel {

    private String userName, timestamp, profileImage, postImage, uid, comments, description;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class HomeModel {

    private String userName, timestamp, profileImage, postImage, uid;
 main


    private int likeCount;

    public HomeModel() {

    }

    public HomeModel(String userName, String timestamp, String profileImage, String postImage, String uid, String comments, String description, int likeCount) {
    public HomeModel(String userName, String timestamp, String profileImage, String postImage, String uid, int likeCount) {
main
        this.userName = userName;
        this.timestamp = timestamp;
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.uid = uid;
        this.comments = comments;
        this.description = description;
        this.likeCount = likeCount;
    }


        this.likeCount = likeCount;
    }

main
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

main
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
