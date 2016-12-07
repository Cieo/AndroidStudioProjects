package com.example.cieo233.unittest;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Cieo233 on 12/7/2016.
 */

public class ChannelFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.channel_list)
    RecyclerView channel_list;
    @BindView(R.id.btn_add_channel)
    FloatingActionButton btn_add_channel;
    private Handler handler;
    private ChannelAdapter channelAdapter;

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

    void setResponse(){
        btn_add_channel.setOnClickListener(this);
    }

    void init(){
        channelAdapter = new ChannelAdapter(getContext(),CurrentUser.getInstance().getChannels());
        channel_list.setLayoutManager(new LinearLayoutManager(getContext()));
        channel_list.setAdapter(channelAdapter);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        channelAdapter.setChannels(CurrentUser.getInstance().getChannels());
                        channelAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        showToast("用户身份无效");
                        break;
                    case 2:
                        showToast("未知错误");
                        break;
                    case 8:
                        getAllChannel();
                        break;
                }
            }
        };
    }

    void showToast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }

    void getAllChannel(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/channel").newBuilder();
        url_builder.addEncodedQueryParameter("token",CurrentUser.getInstance().getToken());
        Request request = new Request.Builder()
                .url(url_builder.build())
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
                    if (jsonObject.getInt("ret") == StateCode.TOKEN_INVALID) {
                        result = 1;
                    } else if (jsonObject.getInt("ret") == StateCode.OK) {
                        result = 3;
                        CurrentUser.getInstance().setChannels((List<Channel>) new Gson().fromJson(jsonObject.getString("channels"),new TypeToken<List<Channel>>(){}.getType()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(result);
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_channel:
                break;
        }
    }
}
