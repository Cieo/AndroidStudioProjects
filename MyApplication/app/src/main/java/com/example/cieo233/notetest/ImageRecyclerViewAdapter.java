package com.example.cieo233.notetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter{
    private ImageFolder imageFolder;
    private Context context;

    public ImageRecyclerViewAdapter(Context context, ImageFolder imageFolder) {
        this.imageFolder = imageFolder;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.image_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Glide.with(context).load(imageFolder.getUrlList().get(position)).centerCrop().crossFade().into(myViewHolder.getImageView());
    }

    @Override
    public int getItemCount() {
        return imageFolder.getUrlList().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public void setImageFolder(ImageFolder imageFolder) {
        this.imageFolder = imageFolder;
    }
}
