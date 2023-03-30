package com.zpwit_wsb_gr1_project.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PostImageModel {

    private String imageUrl, id, description;
    private String imageUrl, id;
main

    @ServerTimestamp
    private Date timestamp;

    public PostImageModel() {
    }


    public PostImageModel(String imageUrl, String id, String description, Date timestamp) {
        this.imageUrl = imageUrl;
        this.id = id;
        this.description = description;
    public PostImageModel(String imageUrl, String id, Date timestamp) {
        this.imageUrl = imageUrl;
        this.id = id;
 main
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

main
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
