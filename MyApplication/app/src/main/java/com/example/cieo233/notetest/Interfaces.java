package com.example.cieo233.notetest;

import android.view.View;
import android.widget.Button;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class Interfaces {
    public interface OnImageFolderClickedListener {
        void onFolderClicked(ImageFolder clickedFolder, Button button);
    }

    public interface OnImageClickedListener{
        void onImageClicked(ImageInfo url, View checkBox);
    }

    public interface OnNoteFolderClickedListener{
        void onFolderClicked(NoteFolder clickedFolder, Button button);
    }

    public interface OnNoteClickedListener{
        void onNoteClicked(NoteInfo clickedNote, View checkBox);
        void onAddOneClicked();
    }

}
