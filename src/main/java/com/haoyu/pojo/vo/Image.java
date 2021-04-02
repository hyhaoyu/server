package com.haoyu.pojo.vo;

public class Image {

    private String id;
    private String picUrl;

    public Image(String id, String picUrl) {
        this.id = id;
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
