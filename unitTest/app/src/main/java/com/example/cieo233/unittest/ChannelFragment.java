package com.example.cieo233.unittest;

import android.content.DialogInterface;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.bottomdialog.BottomDialog;
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

public class ChannelFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.channel_list)
    RecyclerView channel_list;
    @BindView(R.id.btn_add_channel)
    FloatingActionButton btn_add_channel;
    private Handler handler;
    private ChannelAdapter channelAdapter;
    BottomDialog mBottomDialog;
    String new_name;

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

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        channelAdapter.setChannels(CurrentUser.getInstance().getUnsubscribeChannels());
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
                    case 9:
                        mBottomDialog.dismiss();
                }
            }
        };
        channelAdapter = new ChannelAdapter(getContext(), CurrentUser.getInstance().getUnsubscribeChannels());
        channelAdapter.setHandler(handler);
        channel_list.setLayoutManager(new LinearLayoutManager(getContext()));
        channel_list.setAdapter(channelAdapter);
    }

    void showToast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }

    void showBottomDialog() {
        mBottomDialog = BottomDialog.create(getActivity().getSupportFragmentManager()).setViewListener(new BottomDialog.ViewListener() {
            EditText new_channel;
            Button create;

            @Override
            public void bindView(View v) {
                new_channel = (EditText) v.findViewById(R.id.name);
                create = (Button) v.findViewById(R.id.create);
                create.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        new_name = new_channel.getText().toString();
                        Log.e("WOCAO", new_name);
                        postChannel();
                        handler.sendEmptyMessage(9);
                    }
                });

            }
        }).setLayoutRes(R.layout.dialog_add_channel).setDimAmount(0.2f);
        mBottomDialog.show();
    }

    void getAllChannel() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/channel").newBuilder();
        url_builder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
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
                        result = 0;
                        CurrentUser.getInstance().setUnsubscribeChannels((List<Channel>) new Gson().fromJson(jsonObject.getString("channels"), new TypeToken<List<Channel>>() {
                        }.getType()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(result);
            }

        });
    }

    void postChannel() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/channel").newBuilder();
        url_builder.addEncodedQueryParameter("token", CurrentUser.getInstance().getUser().getToken());
        RequestBody formBody = new FormBody.Builder()
                .add("name", new_name)
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_channel:
                showBottomDialog();
                break;
        }
    }
}
