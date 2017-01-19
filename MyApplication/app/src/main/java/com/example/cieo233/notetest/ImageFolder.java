package com.example.cieo233.notetest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class ImageFolder {
    private List<String> urlList;
    private String folderName;

    public ImageFolder() {
        urlList = new ArrayList<>();
    }

    public ImageFolder(String folderName) {
        this.folderName = folderName;
        urlList = new ArrayList<>();
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getFolderCount() {
        return urlList.size();
    }

}
