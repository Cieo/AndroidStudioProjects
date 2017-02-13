package com.example.cieo233.notetest;

import android.view.View;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class Interfaces {
    public interface OnFolderClickedListener{
        void onFolderClicked(ImageFolder clickedFolder);
    }

    public interface OnImageClickedListener{
        void onImageClicked(ImageInfo url, View checkBox);
    }

    public interface OnNoteFolderClickedListener{
        void onFolderClicked(NoteFolder clickedFolder);
    }

    public interface OnNoteClickedListener{
        void onNoteClicked(NoteInfo clickedNote, View checkBox);
        void onAddOneClicked();
    }

}
