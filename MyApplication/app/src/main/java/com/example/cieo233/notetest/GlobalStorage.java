package com.example.cieo233.notetest;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Cieo233 on 1/22/2017.
 */

public class GlobalStorage {
    private static GlobalStorage globalStorage;
    private HashMap<String, ImageFolder> imageFolders;
    private HashMap<String, NoteFolder> noteFolders;
    private List<View> selectedImageViewCheckBox;
    private List<ImageInfo> selectedImageInfo;
    private List<NoteInfo> selectedNoteInfo;
    private List<View> selectedNoteThumbnailCheckBox;


    private static Typeface FZLT;
    private static Typeface Roboto;

    private GlobalStorage(){
        imageFolders = new HashMap<>();
        noteFolders = new HashMap<>();
        selectedImageViewCheckBox = new ArrayList<>();
        selectedImageInfo = new ArrayList<>();
        selectedNoteInfo = new ArrayList<>();
        selectedNoteThumbnailCheckBox = new ArrayList<>();
    }

    public static Typeface getFZLT(Context context) {
        if (FZLT == null){
            FZLT = Typeface.createFromAsset(context.getAssets(),"FZLTHJW.TTF");
        }
        return FZLT;
    }

    public static Typeface getRoboto(Context context) {
        if (Roboto == null){
            Roboto = Typeface.createFromAsset(context.getAssets(),"Roboto-Regular.ttf");
        }
        return Roboto;
    }

    static synchronized GlobalStorage getInstance(){
        if (globalStorage == null){
            globalStorage = new GlobalStorage();

        }
        return globalStorage;
    }

    public List<NoteInfo> getSelectedNoteInfo() {
        return selectedNoteInfo;
    }

    public List<View> getSelectedNoteThumbnailCheckBox() {
        return selectedNoteThumbnailCheckBox;
    }

    public HashMap<String, ImageFolder> getImageFolders() {
        return imageFolders;
    }

    public ImageFolder getImageFolder(String folderName){
        if (Objects.equals(folderName, "allImage")){
            ImageFolder allImageFolder = new ImageFolder("allImage");
            for (ImageFolder item : GlobalStorage.getInstance().getImageFolders().values()){
                allImageFolder.getImageInfoList().addAll(item.getImageInfoList());
            }
            return allImageFolder;
        } else {
            return imageFolders.get(folderName);
        }
    }

    public NoteFolder getNoteFolder(String folderName){
        if (Objects.equals(folderName, "allNote")){
            NoteFolder allNoteFolder = new NoteFolder(new ArrayList<NoteInfo>(),"allNote");
            for (NoteFolder item : GlobalStorage.getInstance().getNoteFolders().values()){
                allNoteFolder.getNoteInfoList().addAll(item.getNoteInfoList());
            }
            return allNoteFolder;
        } else {
            return noteFolders.get(folderName);
        }
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

    public void clearSeletecdNote(){
        for (View checkBox : this.selectedNoteThumbnailCheckBox){
            checkBox.setVisibility(View.GONE);
        }
        this.selectedNoteInfo.clear();
        this.selectedNoteThumbnailCheckBox.clear();
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

    public void deleteSelectedNote(Context context){
        NoteDatabaseHelper noteDatabaseHelper = new NoteDatabaseHelper(context,"note",null,1);
        for (NoteInfo item : selectedNoteInfo){
            noteDatabaseHelper.delete(item.getNoteMark());
            noteFolders.get(item.getNoteBelongTo()).getNoteInfoList().remove(item);
        }
        clearSeletecdNote();
    }

    public String getFolderCount(String folderName){
        if (Objects.equals(folderName, "allImage")){
            ImageFolder allImageFolder = new ImageFolder("allImage");
            for (ImageFolder item : GlobalStorage.getInstance().getImageFolders().values()){
                allImageFolder.getImageInfoList().addAll(item.getImageInfoList());
            }
            return String.valueOf(allImageFolder.getFolderCount());
        }else {
            return String.valueOf(imageFolders.get(folderName).getFolderCount());
        }
    }

    public String getNoteFolderCount(String folderName){
        if (Objects.equals(folderName, "allNote")){
            NoteFolder allNoteFolder = new NoteFolder(new ArrayList<NoteInfo>(),"allNote");
            for (NoteFolder item : GlobalStorage.getInstance().getNoteFolders().values()){
                allNoteFolder.getNoteInfoList().addAll(item.getNoteInfoList());
            }
            return String.valueOf(allNoteFolder.getFolderCount());
        }else {
            return String.valueOf(noteFolders.get(folderName).getFolderCount());
        }
    }


    public void getImageFromContentProvider(Context context) {
        imageFolders.clear();

        ContentResolver contentResolver = context.getContentResolver();
        String path = "/storage/emulated/0/";
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.DATA + " like ?", new String[]{"%" + path + "%"}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String imageURL = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String folderImageIn = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (imageFolders.containsKey(folderImageIn)) {
                    imageFolders.get(folderImageIn).getImageInfoList().add(new ImageInfo(imageURL, folderImageIn));
                } else {
                    GlobalStorage.getInstance().getImageFolders().put(folderImageIn, new ImageFolder(folderImageIn));
                    GlobalStorage.getInstance().getImageFolders().get(folderImageIn).getImageInfoList().add(new ImageInfo(imageURL, folderImageIn));
                }
            }
        }
    }

    public void getNoteFromDataBase(Context context){
        NoteDatabaseHelper noteDatabaseHelper = new NoteDatabaseHelper(context, "note",null,1);
        Cursor cursor = noteDatabaseHelper.select();
        while (cursor.moveToNext()){
            NoteInfo noteInfo = new NoteInfo(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(0));
            Log.e("testDatabase",noteInfo.toString());
            if (noteFolders.containsKey(noteInfo.getNoteBelongTo())){
                noteFolders.get(noteInfo.getNoteBelongTo()).getNoteInfoList().add(noteInfo);
            } else {
                List<NoteInfo> noteInfos = new ArrayList<>();
                noteInfos.add(noteInfo);
                NoteFolder noteFolder = new NoteFolder(noteInfos, noteInfo.getNoteBelongTo());
                noteFolders.put(noteInfo.getNoteBelongTo(),noteFolder);
            }
        }
        cursor.close();
    }

    public HashMap<String, NoteFolder> getNoteFolders() {
        return noteFolders;
    }
}
