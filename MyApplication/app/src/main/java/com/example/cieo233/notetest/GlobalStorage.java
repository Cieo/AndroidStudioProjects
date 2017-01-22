package com.example.cieo233.notetest;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cieo233 on 1/22/2017.
 */

public class GlobalStorage {
    private static GlobalStorage globalStorage;
    private HashMap<String, ImageFolder> imageFolders;
    private List<View> selectedImageViewCheckBox;
    private List<ImageInfo> selectedImageInfo;



    private GlobalStorage(){
        imageFolders = new HashMap<>();
        selectedImageViewCheckBox = new ArrayList<>();
        selectedImageInfo = new ArrayList<>();
    }


    static synchronized GlobalStorage getInstance(){
        if (globalStorage == null){
            globalStorage = new GlobalStorage();
        }
        return globalStorage;
    }

    public HashMap<String, ImageFolder> getImageFolders() {
        return imageFolders;
    }

    public void setImageFolders(HashMap<String, ImageFolder> imageFolders) {
        this.imageFolders = imageFolders;
    }

    public List<View> getSelectedImageViewCheckBox() {
        return selectedImageViewCheckBox;
    }

    public void setSelectedImageViewCheckBox(List<View> selectedImageViewCheckBox) {
        this.selectedImageViewCheckBox = selectedImageViewCheckBox;
    }

    public List<ImageInfo> getSelectedImageInfo() {
        return selectedImageInfo;
    }

    public void setSelectedImageInfo(List<ImageInfo> selectedImageInfo) {
        this.selectedImageInfo = selectedImageInfo;
    }

    public void clearSeletecd(){
        for (View checkBox : this.selectedImageViewCheckBox){
            checkBox.setVisibility(View.GONE);
        }
        this.selectedImageInfo.clear();
        this.selectedImageViewCheckBox.clear();
    }

    public void deleteSelected(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        for (ImageInfo selectedItem : this.selectedImageInfo){
            File file = new File(selectedItem.getImageURL());
            if (file.exists()){
                file.delete();
                contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.Media.DATA+"=?",new String[]{selectedItem.getImageURL()});
            }
            imageFolders.get(selectedItem.getImageFolder()).getImageInfoList().remove(selectedItem);
        }
        clearSeletecd();
    }

    public String getFolderCount(String folderName){
        if (folderName == "allImage"){
            ImageFolder allImageFolder = new ImageFolder("allImage");
            for (ImageFolder item : GlobalStorage.getInstance().getImageFolders().values()){
                allImageFolder.getImageInfoList().addAll(item.getImageInfoList());
            }
            return String.valueOf(allImageFolder.getFolderCount());
        }else {
            return String.valueOf(imageFolders.get(folderName).getFolderCount());
        }
    }

}
