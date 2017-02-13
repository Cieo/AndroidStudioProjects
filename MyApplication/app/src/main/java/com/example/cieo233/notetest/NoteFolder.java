package com.example.cieo233.notetest;

import java.util.List;

/**
 * Created by Cieo233 on 2/11/2017.
 */

public class NoteFolder {
    private List<NoteInfo> noteInfoList;
    private String FolderName;

    public NoteFolder(List<NoteInfo> noteInfoList, String folderName) {
        this.noteInfoList = noteInfoList;
        FolderName = folderName;
    }

    public List<NoteInfo> getNoteInfoList() {
        return noteInfoList;
    }

    public void setNoteInfoList(List<NoteInfo> noteInfoList) {
        this.noteInfoList = noteInfoList;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public int getFolderCount() {
        return noteInfoList.size();
    }
}
