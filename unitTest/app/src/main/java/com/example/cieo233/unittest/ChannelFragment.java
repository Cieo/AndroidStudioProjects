package com.example.cieo233.unittest;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.bottomdialog.BottomDialog;

/**
 * Created by Cieo233 on 12/7/2016.
 */

public class ChannelFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, Interface.RecyclerViewCheckboxClickListener {
    @BindView(R.id.channel_list)
    RecyclerView channel_list;
    @BindView(R.id.btnAddChannel)
    ImageView btn_add_channel;
    @BindView(R.id.fragmentChannelDate)
    TextView fragmentChannelDate;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private ChannelAdapter channelAdapter;
    BottomDialog mBottomDialog;
    Handler createChannelHandler, exitChannelHandler, joinChannelHandler, fragmentChannelHandler, getChannelHandler;
    int result;
    final int DONE = 3;
    String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View fragment_channel = inflater.inflate(R.layout.fragment_channel, container, false);
        ButterKnife.bind(this, fragment_channel);
        setResponse();
        init();
        return fragment_channel;
    }

    void setResponse() {
        btn_add_channel.setOnClickListener(this);
    }

    void init() {
        Calendar today = Calendar.getInstance();
        fragmentChannelDate.setText(month[today.get(Calendar.MONTH)] + " " + today.get(Calendar.DAY_OF_MONTH) + " " + today.get(Calendar.YEAR));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        result = 0;
        channelAdapter = new ChannelAdapter(getContext(), CurrentUser.getInstance().getAllChannels(),this);
        channel_list.setLayoutManager(new LinearLayoutManager(getContext()));
        channel_list.setAdapter(channelAdapter);
        createChannelHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case StateCode.OK:
                        CodoAPI.getChannels(getChannelHandler);
                        Log.e("CreateChannelHandler","Create success");
                        break;
                    case StateCode.CHANNEL_NAME_DUPLICATE_ERROR:
                        Log.e("CreateChannelHandler","Create fail");
                        channelAdapter.setChannels(CurrentUser.getInstance().getAllChannels());
                        channelAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        joinChannelHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case StateCode.OK:
                        Log.e("JoinChannelHandler","Join success");
                        CodoAPI.getChannels(getChannelHandler);
                        break;
                    case StateCode.JOIN_OR_EXIT_CHANNEL_FAIL:
                        Log.e("JoinChannelHandler","Join fail");
                        channelAdapter.setChannels(CurrentUser.getInstance().getAllChannels());
                        channelAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        exitChannelHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case StateCode.OK:
                        Log.e("ExitChannelHandler","Exit success");
                        CodoAPI.getChannels(getChannelHandler);
                        break;
                    case StateCode.JOIN_OR_EXIT_CHANNEL_FAIL:
                        Log.e("ExitChannelHandler","Exit fail");
                        channelAdapter.setChannels(CurrentUser.getInstance().getAllChannels());
                        channelAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        getChannelHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case StateCode.OK:
                        result += 1;
                        if (result == DONE){
                            result = 0;
                            swipeRefreshLayout.setRefreshing(false);
                            channelAdapter.setChannels(CurrentUser.getInstance().getAllChannels());
                            channelAdapter.notifyDataSetChanged();
                        }
                        break;
                    case StateCode.TOKEN_INVALID:
                        break;
                }
                return false;
            }
        });
        fragmentChannelHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case DONE:
                        mBottomDialog.dismiss();
                        break;
                }
                return false;
            }
        });

        if (GeneralUtils.isNetConnected(getContext())){
            CodoAPI.getChannels(getChannelHandler);
        }
    }


    void showBottomDialog() {
        mBottomDialog = BottomDialog.create(getActivity().getSupportFragmentManager()).setViewListener(new BottomDialog.ViewListener() {
            MaterialEditText new_channel;
            ImageView create;

            @Override
            public void bindView(View v) {
                new_channel = (MaterialEditText) v.findViewById(R.id.channelName);
                create = (ImageView) v.findViewById(R.id.btnCreate);
                create.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Channel newChannel = new Channel(new_channel.getText().toString());
                        CodoAPI.createChannel(newChannel,createChannelHandler);
                        fragmentChannelHandler.sendEmptyMessage(DONE);
                    }
                });

            }
        }).setLayoutRes(R.layout.dialog_add_channel).setDimAmount(0.2f);
        mBottomDialog.show();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddChannel:
                showBottomDialog();
                break;
        }
    }

    @Override
    public void onRefresh() {
        CodoAPI.getChannels(getChannelHandler);
    }

    @Override
    public void recyclerViewCheckboxChecked(Object data) {
        Channel joinChannel = (Channel) data;
        if (joinChannel.getType() != 2){
            Log.e("ChannelChecked",joinChannel.getName());
        CodoAPI.joinChannel(joinChannel,joinChannelHandler);
        }
    }

    @Override
    public void recyclerViewCheckboxUnchecked(Object data) {
        Channel exitChannel = (Channel) data;
        if (exitChannel.getType() != 0){
            Log.e("ChannelUnchecked",((Channel) data).getName());
        CodoAPI.exitChannel(exitChannel,exitChannelHandler);
        }
    }
}
