package com.example.cieo233.notetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter{
    private ImageFolder imageFolder;
    private Context context;
    private Interfaces.OnImageClickedListener onImageClickedListener;

    public ImageRecyclerViewAdapter(Context context) {
        this.context = context;
    }


    void swap(int srcPosition, int targetPosition){

    }

    void setImageFolder(String folderName){
        if (folderName == "allImage"){
            ImageFolder allImageFolder = new ImageFolder("allImage");
            for (ImageFolder item : GlobalStorage.getInstance().getImageFolders().values()){
                allImageFolder.getImageInfoList().addAll(item.getImageInfoList());
            }
            this.imageFolder = allImageFolder;
        } else {
            this.imageFolder = GlobalStorage.getInstance().getImageFolders().get(folderName);
        }
    }




    public void setOnImageClickedListener(Interfaces.OnImageClickedListener onImageClickedListener) {
        this.onImageClickedListener = onImageClickedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.image_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        Glide.with(context).load(imageFolder.getImageInfoList().get(position).getImageURL()).into(myViewHolder.getImageView());
        myViewHolder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickedListener.onImageClicked(imageFolder.getImageInfoList().get(position),myViewHolder.getCheckBox());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageFolder.getImageInfoList().size();
    }

    public void updateDateset(String currentFolder) {
        imageFolder = GlobalStorage.getInstance().getImageFolder(currentFolder);
        notifyDataSetChanged();
    }

    public void remove(int position){
        imageFolder.getImageInfoList().remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView, checkBox;
        private RelativeLayout imageItem;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageItem = (RelativeLayout) itemView.findViewById(R.id.imageItem);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            checkBox = (ImageView) itemView.findViewById(R.id.checkBox);
        }

        public View getImageItem() {
            return imageItem;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public ImageView getCheckBox() {
            return checkBox;
        }
    }



}
