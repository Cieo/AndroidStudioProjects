package com.example.cieo233.appdevelopmentlab3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Cieo233 on 10/8/2016.
 */

public class OperationAdapter extends BaseAdapter {

    private String[] operations;
    private Context context;

    public OperationAdapter(String[] operations, Context context) {
        this.operations = operations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return operations.length;
    }

    @Override
    public Object getItem(int i) {
        return operations[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mViewHolder mViewHolder;
        if (view == null){
            mViewHolder = new mViewHolder();
            view = View.inflate(context,R.layout.operation_item,null);
            mViewHolder.operation = (TextView) view.findViewById(R.id.operation);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (OperationAdapter.mViewHolder) view.getTag();
        }
        mViewHolder.operation.setText(operations[i]);
        return view;
    }

    class mViewHolder{
        TextView operation;
    }
}
