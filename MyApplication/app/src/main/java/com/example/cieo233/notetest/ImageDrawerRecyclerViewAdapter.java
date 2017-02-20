package com.example.cieo233.notetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class ImageDrawerRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private HashMap<String, ImageFolder> imageFolders;
    private List<String> keys;
    private Interfaces.OnImageFolderClickedListener onImageFolderClickedListener;

    public ImageDrawerRecyclerViewAdapter(Context context) {
        this.context = context;
        this.imageFolders = GlobalStorage.getInstance().getImageFolders();
        keys = new ArrayList<>(imageFolders.keySet());
    }

    public void updateDateSet(){
        this.imageFolders = GlobalStorage.getInstance().getImageFolders();
        keys = new ArrayList<>(imageFolders.keySet());
        notifyDataSetChanged();
    }

    public int getFolderViewHolderPosition(String folderName){
        return keys.indexOf(folderName);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (holder.getAdapterPosition() == GlobalStorage.getInstance().getHighLightedImageDrawerButton()){
            myViewHolder.getButton().setBackgroundResource(R.drawable.button_style_yellow);
        } else {
            myViewHolder.getButton().setBackgroundResource(R.drawable.button_style_white);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.setBadgeText(String.valueOf(imageFolders.get(keys.get(position)).size()));
        myViewHolder.setButtonText(imageFolders.get(keys.get(position)).getFolderName());
        myViewHolder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageFolderClickedListener.onFolderClicked(imageFolders.get(keys.get(position)), myViewHolder.getButton());
                GlobalStorage.getInstance().setHighLightedImageDrawerButton(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return imageFolders.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private Button button;
        private TextView badge;

        public MyViewHolder(View view) {
            super(view);
            button = (Button) view.findViewById(R.id.button);
            button.setTypeface(GlobalStorage.getFZLT(button.getContext()));
            badge = (TextView) view.findViewById(R.id.badge);
            badge.setTypeface(GlobalStorage.getRoboto(badge.getContext()));

        }

        void setButtonText(String content){
            button.setText(content);
        }

        void setBadgeText(String content){
            badge.setText(content);
        }

        public Button getButton() {
            return button;
        }

        public TextView getBadge() {
            return badge;
        }
    }

    public void setImageFolders(HashMap<String, ImageFolder> imageFolders) {
        this.imageFolders = imageFolders;
    }

    public void setOnImageFolderClickedListener(Interfaces.OnImageFolderClickedListener onImageFolderClickedListener) {
        this.onImageFolderClickedListener = onImageFolderClickedListener;
    }
}
