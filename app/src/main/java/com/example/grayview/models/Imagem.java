package com.example.grayview.models;

import com.google.gson.annotations.SerializedName;

public class Imagem {
    @SerializedName("previewURL")
    private String previewURL;

    @SerializedName("webformatURL")
    private String webFormatURL;

    @SerializedName("largeImageURL")
    private String largeImageURL;

    @SerializedName("id")
    private String id;

    @SerializedName("downloads")
    private int dowloads;

    @SerializedName("likes")
    private int likes;

    @SerializedName("user")
    private String userName;

    @SerializedName("pageURL")
    private String pageURL;

    @SerializedName("userImageURL")
    private String userImage;


    public String getPreviewURL() { return previewURL; }
    public String getWebFormatURL() { return webFormatURL; }
    public String getUserName() { return userName; }
    public String getPageURL() { return pageURL; }
    public int getLikes() { return likes; }
    public int getDownloads() { return dowloads; }
    public String getUserImage() {return userImage;}
    public String getId() {return id;}

    public String getLargeImageURL() {return largeImageURL;}
}
