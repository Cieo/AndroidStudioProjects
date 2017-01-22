package com.example.cieo233.notetest;

/**
 * Created by Cieo233 on 1/21/2017.
 */

public class ImageInfo {
    private String imageURL, imageFolder;

    public ImageInfo(String imageURL, String imageFolder) {
        this.imageURL = imageURL;
        this.imageFolder = imageFolder;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageFolder() {
        return imageFolder;
    }

    public void setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
    }
}
