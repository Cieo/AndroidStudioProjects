package com.example.cieo233.unittest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 12/7/2016.
 */

public class ChannelAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<Channel> channels;
    private Channel mChannel;
    private ChannelHolder mHolder;

    public ChannelAdapter(Context context, List<Channel> channels) {
        this.context = context;
        this.channels = channels;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChannelHolder(LayoutInflater.from(context).inflate(R.layout.channel_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mHolder = (ChannelHolder) holder;
        mChannel = channels.get(position);
        mHolder.getChannel_name().setText(mChannel.getName());
        mHolder.getChannel_id().setText(String.valueOf(mChannel.getId()));
        mHolder.getChannel_time().setText(mChannel.getLast_update());
        if (mChannel.getType() == 1){
            mHolder.getChannel_type().setChecked(true);
        }else {
            mHolder.getChannel_type().setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return channels == null ? 0 : channels.size();
    }

    class ChannelHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.channel_type)
        CheckBox channel_type;
        @BindView(R.id.channel_name)
        TextView channel_name;
        @BindView(R.id.channel_id)
        TextView channel_id;
        @BindView(R.id.channel_time)
        TextView channel_time;


        public ChannelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public CheckBox getChannel_type() {
            return channel_type;
        }

        public TextView getChannel_name() {
            return channel_name;
        }

        public TextView getChannel_id() {
            return channel_id;
        }

        public TextView getChannel_time() {
            return channel_time;
        }
    }
}
