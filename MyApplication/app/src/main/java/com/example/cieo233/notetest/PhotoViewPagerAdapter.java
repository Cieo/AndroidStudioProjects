package com.example.cieo233.notetest;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Cieo233 on 1/25/2017.
 */

public class PhotoViewPagerAdapter extends PagerAdapter{
    private ImageFolder imageFolder;
    private Context context;

    public PhotoViewPagerAdapter(Context context,String currentFolder) {
        this.context = context;
        this.imageFolder = GlobalStorage.getInstance().getImageFolder(currentFolder);
        Log.e("what?", String.valueOf(imageFolder == null));
        Log.e("what?", currentFolder);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, container, false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
        Glide.with(context).load(imageFolder.getImageInfoList().get(position).getImageURL()).into(photoView);
        container.addView(view);
        return view;
    }



    @Override
    public int getCount() {
        return imageFolder.getFolderCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }


}
