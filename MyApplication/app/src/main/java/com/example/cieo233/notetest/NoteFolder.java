package com.example.cieo233.notetest;

import java.util.ArrayList;
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

    public NoteFolder(String folderName) {
        noteInfoList = new ArrayList<>();
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

    public int size() {
        return noteInfoList.size();
    }

    public void add(NoteInfo noteInfo){
        noteInfoList.add(noteInfo);
    }

    public void remove(NoteInfo noteInfo){
        noteInfoList.remove(noteInfo);
    }

    public void remove(int position){
        noteInfoList.remove(position);
    }

    public NoteInfo get(int position){
        return noteInfoList.get(position);
    }

    public void addAll(List<NoteInfo> list){
        noteInfoList.addAll(list);
    }

    public int indexOf(NoteInfo noteInfo){
        return noteInfoList.indexOf(noteInfo);
    }
}
