package com.example.cieo233.notetest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class ImageFolder {
    private List<ImageInfo> imageInfoList;
    private String folderName, folderPath;

    public ImageFolder() {
        imageInfoList = new ArrayList<>();
    }

    public ImageFolder(String folderName) {
        this.folderName = folderName;
        imageInfoList = new ArrayList<>();
    }

    public ImageFolder(String folderName, String folderPath) {
        this.folderName = folderName;
        this.folderPath = folderPath;
        imageInfoList = new ArrayList<>();
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
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
        ImageInfo targetInfo = new ImageInfo("nul","nul");
        for(ImageInfo info : imageInfoList){
            if (Objects.equals(info.getImageURL(), imageInfo.getImageURL())){
                targetInfo = info;
                break;
            }
        }
        imageInfoList.remove(targetInfo);
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
