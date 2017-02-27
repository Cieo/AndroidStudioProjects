package com.example.cieo233.notetest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.transitionseverywhere.Slide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private int highLightedNoteDrawerButton;
    private int highLightedImageDrawerButton;

    private GlobalStorage(){
        imageFolders = new HashMap<>();
        noteFolders = new HashMap<>();
        selectedImageViewCheckBox = new ArrayList<>();
        selectedImageInfo = new ArrayList<>();
        selectedNoteInfo = new ArrayList<>();
        selectedNoteThumbnailCheckBox = new ArrayList<>();
        highLightedImageDrawerButton = -1;
        highLightedNoteDrawerButton = -1;
    }

    public int getHighLightedNoteDrawerButton() {
        return highLightedNoteDrawerButton;
    }

    public void setHighLightedNoteDrawerButton(int highLightedNoteDrawerButton) {
        this.highLightedNoteDrawerButton = highLightedNoteDrawerButton;
    }

    public int getHighLightedImageDrawerButton() {
        return highLightedImageDrawerButton;
    }

    public void setHighLightedImageDrawerButton(int highLightedImageDrawerButton) {
        this.highLightedImageDrawerButton = highLightedImageDrawerButton;
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
        String path = "/storage/emulated/0/";
        if (Objects.equals(folderName, "allImage")){
            ImageFolder allImageFolder = new ImageFolder("allImage", path);
            for (ImageFolder item : GlobalStorage.getInstance().getImageFolders().values()){
                allImageFolder.addAll(item.getImageInfoList());
            }
            return allImageFolder;
        } else {
            return imageFolders.get(folderName);
        }
    }

    public NoteFolder getNoteFolder(String folderName){
        if (Objects.equals(folderName, "allNote")){
            NoteFolder allNoteFolder = new NoteFolder("allNote");
            for (NoteFolder item : GlobalStorage.getInstance().getNoteFolders().values()){
                allNoteFolder.addAll(item.getNoteInfoList());
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

    public void clearSelectedImage(){
        for (View checkBox : this.selectedImageViewCheckBox){
            checkBox.setVisibility(View.GONE);
        }
        this.selectedImageInfo.clear();
        this.selectedImageViewCheckBox.clear();
    }

    public void clearSelectedNote(){
        for (View checkBox : this.selectedNoteThumbnailCheckBox){
            checkBox.setVisibility(View.GONE);
        }
        this.selectedNoteInfo.clear();
        this.selectedNoteThumbnailCheckBox.clear();
    }

    public void deleteSelectedImage(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        for (ImageInfo selectedItem : this.selectedImageInfo){
            File file = new File(selectedItem.getImageURL());
            if (file.exists()){
                file.delete();
                contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.Media.DATA+"=?",new String[]{selectedItem.getImageURL()});
            }
            imageFolders.get(selectedItem.getImageFolder()).remove(selectedItem);
        }
        clearSelectedImage();
    }

    public void deleteSelectedNote(Context context){
        SlideNoteDatabaseHelper slideNoteDatabaseHelper = new SlideNoteDatabaseHelper(context,"note",null,1);
        for (NoteInfo item : selectedNoteInfo){
            slideNoteDatabaseHelper.deleteNote(item.getNoteID());
            noteFolders.get(item.getNoteBelongTo()).remove(item);
        }
        clearSelectedNote();
    }

    public String getImageFolderSize(String folderName){
        if (Objects.equals(folderName, "allImage")){
            String path = "/storage/emulated/0/";
            ImageFolder allImageFolder = new ImageFolder("allImage", path);
            for (ImageFolder item : GlobalStorage.getInstance().getImageFolders().values()){
                allImageFolder.addAll(item.getImageInfoList());
            }
            return String.valueOf(allImageFolder.size());
        }else {
            return String.valueOf(imageFolders.get(folderName).size());
        }
    }

    public String getNoteFolderSize(String folderName){
        if (Objects.equals(folderName, "allNote")){
            NoteFolder allNoteFolder = new NoteFolder("allNote");
            for (NoteFolder item : GlobalStorage.getInstance().getNoteFolders().values()){
                allNoteFolder.addAll(item.getNoteInfoList());
            }
            return String.valueOf(allNoteFolder.size());
        }else {
            return String.valueOf(noteFolders.get(folderName).size());
        }
    }


    public void getImageFromContentProvider(Context context) {
        imageFolders.clear();
        SlideNoteDatabaseHelper databaseHelper = new SlideNoteDatabaseHelper(context,"note",null,1);
        Cursor otherPath = databaseHelper.selectAllImageFolder();
        while (otherPath.moveToNext()){
            String path = otherPath.getString(1);
            File file = new File(path);
            if (!file.exists()){
                file.mkdirs();
            }
            String name = path.substring(path.lastIndexOf("/")+1);
            if (!imageFolders.containsKey(name)){
                imageFolders.put(name,new ImageFolder(name,path));
            }
        }
        ContentResolver contentResolver = context.getContentResolver();
        String path = "/storage/emulated/0/";
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.DATA + " like ?", new String[]{"%" + path + "%"}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String imageURL = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String folderImageIn = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (imageFolders.containsKey(folderImageIn)) {
                    imageFolders.get(folderImageIn).add(new ImageInfo(imageURL, folderImageIn));

                } else {
                    imageFolders.put(folderImageIn, new ImageFolder(folderImageIn, imageURL.substring(0,imageURL.lastIndexOf("/"))));
                    imageFolders.get(folderImageIn).add(new ImageInfo(imageURL, folderImageIn));
                }
            }
        }
        cursor.close();
        otherPath.close();
    }

    public void moveImageToOtherFolder(Context context, ImageFolder targetFolder){
        String newPath = targetFolder.getFolderPath();
        for (ImageInfo info : selectedImageInfo){
            String oldURL = info.getImageURL();
            String newURL = newPath+oldURL.substring(oldURL.lastIndexOf("/"));
            Log.e("TestOldURL",oldURL);
            Log.e("TestNewURL",newURL);
            File oldFile = new File(info.getImageURL());
            File newFile = new File(newURL);
            imageFolders.get(targetFolder.getFolderName()).add(new ImageInfo(newURL,targetFolder.getFolderName()));
            try {
                copy(oldFile,newFile);
                MediaScannerConnection.scanFile(context,new String[]{newURL},null,null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        deleteSelectedImage(context);
    }

    public void moveNoteToOtherFolder(Context context, NoteFolder targetFolder){
        SlideNoteDatabaseHelper slideNoteDatabaseHelper = new SlideNoteDatabaseHelper(context,"note",null,1);
        for (NoteInfo info : selectedNoteInfo){
            noteFolders.get(info.getNoteBelongTo()).remove(info);
            info.setNoteBelongTo(targetFolder.getFolderName());
            slideNoteDatabaseHelper.updateNote(info);
            noteFolders.get(targetFolder.getFolderName()).add(info);
        }
        clearSelectedNote();
    }


    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    public void getNoteFromDataBase(Context context){
        noteFolders.clear();
        SlideNoteDatabaseHelper slideNoteDatabaseHelper = new SlideNoteDatabaseHelper(context, "note",null,1);
        Cursor cursor = slideNoteDatabaseHelper.selectAllNote();
        Cursor otherPath = slideNoteDatabaseHelper.selectAllNoteFolder();
        while (otherPath.moveToNext()){
            String path = otherPath.getString(1);
            if (!noteFolders.containsKey(path)){
                noteFolders.put(path,new NoteFolder(path));
            }
        }
        while (cursor.moveToNext()){
            NoteInfo noteInfo = new NoteInfo(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(0));
            Log.e("testDatabase",noteInfo.toString());
            if (noteFolders.containsKey(noteInfo.getNoteBelongTo())){
                noteFolders.get(noteInfo.getNoteBelongTo()).add(noteInfo);
            } else {
                List<NoteInfo> noteInfos = new ArrayList<>();
                noteInfos.add(noteInfo);
                NoteFolder noteFolder = new NoteFolder(noteInfos, noteInfo.getNoteBelongTo());
                noteFolders.put(noteInfo.getNoteBelongTo(),noteFolder);
            }
        }
        cursor.close();
        otherPath.close();
    }

    public HashMap<String, NoteFolder> getNoteFolders() {
        return noteFolders;
    }
}
