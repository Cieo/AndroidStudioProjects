package com.example.cieo233.unittest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 12/7/2016.
 */

public class ChannelAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Channel> channels;
    private Channel mChannel;
    private ChannelHolder mHolder;
    private Interface.RecyclerViewCheckboxClickListener recyclerViewCheckboxClickListener;

    public ChannelAdapter(Context context, List<Channel> channels, Interface.RecyclerViewCheckboxClickListener recyclerViewCheckboxClickListener) {
        this.context = context;
        this.channels = channels;
        this.recyclerViewCheckboxClickListener = recyclerViewCheckboxClickListener;
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
        return new ChannelHolder(LayoutInflater.from(context).inflate(R.layout.channel_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        mHolder = (ChannelHolder) holder;
        mChannel = channels.get(position);
        mHolder.getChannelName().setText(mChannel.getName());
        mHolder.getChannelID().setText(String.valueOf(mChannel.getId()));
        mHolder.getChannelType().setTag(channels.get(position));
        String[] splits = mChannel.getLast_update().split(" ");
        mHolder.getChannelUpdateDate().setText(splits[0].substring(5));
        mHolder.getChannelUpdateTime().setText(splits[1].substring(0, 5));
        if (mChannel.getType() == 2) {
            mHolder.getChannelType().setChecked(true);
        } else {
            mHolder.getChannelType().setChecked(false);
        }

    }


    @Override
    public int getItemCount() {
        return channels == null ? 0 : channels.size();
    }

    class ChannelHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.channel_type)
        CheckBox channelType;
        @BindView(R.id.channelName)
        TextView channelName;
        @BindView(R.id.channelID)
        TextView channelID;
        @BindView(R.id.channelUpdateDate)
        TextView channelUpdateDate;
        @BindView(R.id.channelUpdateTime)
        TextView channelUpdateTime;


        ChannelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            channelType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        recyclerViewCheckboxClickListener.recyclerViewCheckboxChecked(channels.get(getLayoutPosition()));
                    } else {
                        recyclerViewCheckboxClickListener.recyclerViewCheckboxUnchecked(channels.get(getLayoutPosition()));
                    }
                }
            });
        }

        CheckBox getChannelType() {
            return channelType;
        }

        TextView getChannelName() {
            return channelName;
        }

        TextView getChannelID() {
            return channelID;
        }

        public TextView getChannelUpdateDate() {
            return channelUpdateDate;
        }

        public TextView getChannelUpdateTime() {
            return channelUpdateTime;
        }
    }
}
