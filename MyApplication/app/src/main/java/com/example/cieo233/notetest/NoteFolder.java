package com.example.cieo233.notetest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Cieo233 on 2/11/2017.
 */

public class NoteFolder {
    private List<NoteInfo> noteInfoList;
    private String folderName;

    public NoteFolder(List<NoteInfo> noteInfoList, String folderName) {
        this.noteInfoList = noteInfoList;
        this.folderName = folderName;
    }

    public NoteFolder(String folderName) {
        noteInfoList = new ArrayList<>();
        this.folderName = folderName;
    }

    public List<NoteInfo> getNoteInfoList() {
        return noteInfoList;
    }

    public void setNoteInfoList(List<NoteInfo> noteInfoList) {
        this.noteInfoList = noteInfoList;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int size() {
        return noteInfoList.size();
    }

    public void add(NoteInfo noteInfo){
        noteInfoList.add(noteInfo);
    }

    public void remove(NoteInfo noteInfo){
        for(NoteInfo info : noteInfoList){
            if (Objects.equals(info.getNoteID(), noteInfo.getNoteID())){
                noteInfoList.remove(info);
            }
        }
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
