package com.example.cieo233.unittest;

import android.content.Context;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Cieo233 on 12/7/2016.
 */

public class ChannelAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<Channel> channels;
    private Channel mChannel;
    private ChannelHolder mHolder;
    private Handler handler;

    public ChannelAdapter(Context context, List<Channel> channels) {
        this.context = context;
        this.channels = channels;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
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
        mHolder.getChannel_type().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    joinChannel();
                }
                else if (!b){
                    exitChannel();
                }
            }
        });
    }

    void joinChannel(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/channel/"+String.valueOf(mChannel.getId())).newBuilder();
        url_builder.addEncodedQueryParameter("token",CurrentUser.getInstance().getToken());
        RequestBody formBody = new FormBody.Builder()
                .add(Channel.ACTION, String.valueOf(0))
                .build();
        Log.e("WOCAO",url_builder.build().toString());
        Request request = new Request.Builder()
                .url(url_builder.build())
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                int result = 2;
                Log.e("WOCAO", json);

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("ret") == StateCode.CHANNEL_ID_ERROR) {
                        result = 1;
                    } else if (jsonObject.getInt("ret") == StateCode.OK) {
                        handler.sendEmptyMessage(8);
                        result = 0;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }

    void exitChannel(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/channel/"+String.valueOf(mChannel.getId())).newBuilder();
        url_builder.addEncodedQueryParameter("token",CurrentUser.getInstance().getToken());
        RequestBody formBody = new FormBody.Builder()
                .add(Channel.ACTION, String.valueOf(1))
                .build();
        Request request = new Request.Builder()
                .url(url_builder.build())
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                int result = 2;
                Log.e("WOCAO", json);

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("ret") == StateCode.CHANNEL_ID_ERROR) {
                        result = 1;
                    } else if (jsonObject.getInt("ret") == StateCode.OK) {
                        handler.sendEmptyMessage(8);
                        result = 0;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

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
