package com.example.cieo233.notetest;

/**
 * Created by Cieo233 on 2/11/2017.
 */

public class NoteInfo {
    private String noteName, noteCreateTime, noteContent, noteBelongTo, noteID;

    public NoteInfo(String noteContent, String noteBelongTo, String noteCreateTime, String noteName) {
        this.noteContent = noteContent;
        this.noteBelongTo = noteBelongTo;
        this.noteCreateTime = noteCreateTime;
        this.noteName = noteName;
    }

    public NoteInfo(String noteName, String noteCreateTime, String noteContent, String noteBelongTo, String noteID) {
        this.noteName = noteName;
        this.noteCreateTime = noteCreateTime;
        this.noteContent = noteContent;
        this.noteBelongTo = noteBelongTo;
        this.noteID = noteID;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteCreateTime() {
        return noteCreateTime;
    }

    public void setNoteCreateTime(String noteCreateTime) {
        this.noteCreateTime = noteCreateTime;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteBelongTo() {
        return noteBelongTo;
    }

    public void setNoteBelongTo(String noteBelongTo) {
        this.noteBelongTo = noteBelongTo;
    }

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    @Override
    public String toString() {
        return String.format("noteName=%s,noteCreateTime=%s,noteContent=%s,noteBelongto=%s,noteID=%s",noteName,noteCreateTime,noteContent,noteBelongTo, noteID);
    }
}
