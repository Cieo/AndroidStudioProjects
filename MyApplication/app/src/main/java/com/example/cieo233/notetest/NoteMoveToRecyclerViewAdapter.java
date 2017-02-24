package com.example.cieo233.notetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cieo233 on 2/23/2017.
 */

public class NoteMoveToRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private HashMap<String, NoteFolder> noteFolders;
    private List<String> keys;
    private Interfaces.OnNoteMoveToFolderClickedListener onNoteMoveToFolderClickedListener;

    public NoteMoveToRecyclerViewAdapter(Context context) {
        this.context = context;
        this.noteFolders = GlobalStorage.getInstance().getNoteFolders();
        keys = new ArrayList<>(noteFolders.keySet());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.move_to_item,null,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return noteFolders.size() + 1;
    }


    public Interfaces.OnNoteMoveToFolderClickedListener getOnNoteMoveToFolderClickedListener() {
        return onNoteMoveToFolderClickedListener;
    }

    public void setOnNoteMoveToFolderClickedListener(Interfaces.OnNoteMoveToFolderClickedListener onNoteMoveToFolderClickedListener) {
        this.onNoteMoveToFolderClickedListener = onNoteMoveToFolderClickedListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView albumThumbnail;
        private TextView albumName, albumSize;
        private LinearLayout folderItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            albumThumbnail = (ImageView) itemView.findViewById(R.id.albumThumbnail);
            albumName = (TextView) itemView.findViewById(R.id.albumName);
            albumSize = (TextView) itemView.findViewById(R.id.albumSize);
            folderItem = (LinearLayout) itemView.findViewById(R.id.folderItem);
        }

        public TextView getAlbumSize() {
            return albumSize;
        }

        public void setAlbumSize(TextView albumSize) {
            this.albumSize = albumSize;
        }

        public LinearLayout getFolderItem() {
            return folderItem;
        }

        public void setFolderItem(LinearLayout folderItem) {
            this.folderItem = folderItem;
        }

        public ImageView getAlbumThumbnail() {
            return albumThumbnail;
        }

        public void setAlbumThumbnail(ImageView albumThumbnail) {
            this.albumThumbnail = albumThumbnail;
        }

        public TextView getAlbumName() {
            return albumName;
        }

        public void setAlbumName(TextView albumName) {
            this.albumName = albumName;
        }
    }
}
