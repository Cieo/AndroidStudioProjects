package com.example.cieo233.notetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 1/19/2017.
 */

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter{
    private NoteFolder noteFolder;
    private Context context;
    private Interfaces.OnNoteClickedListener onNoteClickedListener;

    public NoteRecyclerViewAdapter(Context context) {
        this.context = context;
    }


    void setNoteFolder(String folderName){
        if (folderName == "allNote"){
            NoteFolder allNoteFolder = new NoteFolder(new ArrayList<NoteInfo>(),"allNote");
            for (NoteFolder item : GlobalStorage.getInstance().getNoteFolders().values()){
                allNoteFolder.getNoteInfoList().addAll(item.getNoteInfoList());
            }
            this.noteFolder = allNoteFolder;
        } else {
            this.noteFolder = GlobalStorage.getInstance().getNoteFolders().get(folderName);
        }
    }


    public void setOnNoteClickedListener(Interfaces.OnNoteClickedListener onNoteClickedListener) {
        this.onNoteClickedListener = onNoteClickedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.add_one_thumbnail_item,parent,false));
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.note_thumbnail_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (position == 0){
            myViewHolder.getNoteThumbnail().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNoteClickedListener != null){
                        onNoteClickedListener.onAddOneClicked();
                    }
                }
            });
        }else {
            final NoteInfo noteInfo = noteFolder.getNoteInfoList().get(position-1);
            myViewHolder.getNoteThumbnail().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNoteClickedListener!= null){
                        onNoteClickedListener.onNoteClicked(noteInfo, myViewHolder.getCheckBox());
                    }
                }
            });
            myViewHolder.getThumbnailTitle().setText(noteInfo.getNoteName());
            myViewHolder.getThumbnailCreateTime().setText(noteInfo.getNoteCreateTime());
            myViewHolder.getThumbnailContent().setText(noteInfo.getNoteContent());
            //need set image
        }

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.getAdapterPosition()!=0){
            Log.e("testAttachedToWindow", String.valueOf(holder.getAdapterPosition()-1));
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            if (GlobalStorage.getInstance().getSelectedNoteInfo().contains(noteFolder.getNoteInfoList().get(holder.getAdapterPosition()-1))){
                myViewHolder.getCheckBox().setVisibility(View.VISIBLE);
            }else {
                myViewHolder.getCheckBox().setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position==0?0:1;
    }

    @Override
    public int getItemCount() {
        return noteFolder==null?1:noteFolder.getNoteInfoList().size()+1;
    }

    public NoteFolder getNoteFolder() {
        return noteFolder;
    }

    public void updateDateset(String currentFolder) {
        Log.e("TestUpdateDataset", currentFolder);
        noteFolder = GlobalStorage.getInstance().getNoteFolder(currentFolder);
        notifyDataSetChanged();
    }

    public void remove(int position){

        noteFolder.getNoteInfoList().remove(position-1);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,noteFolder.getFolderCount());
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout noteThumbnail;
        private TextView thumbnailTitle, thumbnailCreateTime, thumbnailContent;
        private ImageView imageView1, imageView2, imageView3, checkBox;
        private List<ImageView> smallImage;
        public MyViewHolder(View itemView) {
            super(itemView);
            noteThumbnail = (RelativeLayout) itemView.findViewById(R.id.noteThumbnail);
            thumbnailTitle = (TextView) itemView.findViewById(R.id.noteThumbnailTitle);
            thumbnailCreateTime = (TextView) itemView.findViewById(R.id.noteThumbnailCreateTime);
            thumbnailContent = (TextView) itemView.findViewById(R.id.noteThumbnailContent);
            imageView1 = (ImageView) itemView.findViewById(R.id.noteThumbnailImage1);
            imageView2 = (ImageView) itemView.findViewById(R.id.noteThumbnailImage2);
            imageView3 = (ImageView) itemView.findViewById(R.id.noteThumbnailImage3);
            checkBox = (ImageView) itemView.findViewById(R.id.noteThumbnailCheckBox);
            smallImage = new ArrayList<>();
            smallImage.add(imageView1);
            smallImage.add(imageView2);
            smallImage.add(imageView3);
        }

        public ImageView getCheckBox() {
            return checkBox;
        }

        public RelativeLayout getNoteThumbnail() {
            return noteThumbnail;
        }

        public TextView getThumbnailTitle() {
            return thumbnailTitle;
        }

        public TextView getThumbnailCreateTime() {
            return thumbnailCreateTime;
        }

        public TextView getThumbnailContent() {
            return thumbnailContent;
        }

        public ImageView getImageView1() {
            return imageView1;
        }

        public ImageView getImageView2() {
            return imageView2;
        }

        public ImageView getImageView3() {
            return imageView3;
        }

        public List<ImageView> getSmallImage() {
            return smallImage;
        }
    }

}
