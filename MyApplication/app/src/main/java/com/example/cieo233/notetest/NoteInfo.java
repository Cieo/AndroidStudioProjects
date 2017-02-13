package com.example.cieo233.notetest;

/**
 * Created by Cieo233 on 2/11/2017.
 */

public class NoteInfo {
    private String noteName, noteCreateTime, noteContent, noteBelongTo, noteMark;

    public NoteInfo(String noteMark, String noteBelongTo, String noteCreateTime, String noteName) {
        this.noteMark = noteMark;
        this.noteBelongTo = noteBelongTo;
        this.noteCreateTime = noteCreateTime;
        this.noteName = noteName;
    }

    public NoteInfo(String noteName, String noteCreateTime, String noteContent, String noteBelongTo, String noteMark) {
        this.noteName = noteName;
        this.noteCreateTime = noteCreateTime;
        this.noteContent = noteContent;
        this.noteBelongTo = noteBelongTo;
        this.noteMark = noteMark;
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

    public String getNoteMark() {
        return noteMark;
    }

    public void setNoteMark(String noteMark) {
        this.noteMark = noteMark;
    }

    @Override
    public String toString() {
        return String.format("noteName=%s,noteCreateTime=%s,noteContent=%s,noteBelongto=%s,noteMark=%s",noteName,noteCreateTime,noteContent,noteBelongTo,noteMark);
    }
}
