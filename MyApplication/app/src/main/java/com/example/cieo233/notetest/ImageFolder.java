package com.example.cieo233.notetest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class ImageFolder {
    private List<ImageInfo> imageInfoList;
    private String folderName;

    public ImageFolder() {
        imageInfoList = new ArrayList<>();
    }

    public ImageFolder(String folderName) {
        this.folderName = folderName;
        imageInfoList = new ArrayList<>();
    }

    public List<ImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    public void setImageInfoList(List<ImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int size() {
        return imageInfoList.size();
    }

    public void add(ImageInfo imageInfo){
        imageInfoList.add(imageInfo);
    }

    public void remove(ImageInfo imageInfo){
        imageInfoList.remove(imageInfo);
    }

    public ImageInfo get(int position){
        return imageInfoList.get(position);
    }

    public void addAll(List<ImageInfo> list){
        imageInfoList.addAll(list);
    }

    public void remove(int position){
        imageInfoList.remove(position);
    }

    public int indexOf(ImageInfo imageInfo){
        return imageInfoList.indexOf(imageInfo);
    }
}
