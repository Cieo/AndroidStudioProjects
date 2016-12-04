package com.example.cieo233.unittest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
 * Created by Cieo233 on 12/4/2016.
 */

public class ReminderFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.reminder_list)
    RecyclerView reminder_list;
    @BindView(R.id.frament_reminder_date)
    TextView fragment_reminder_date;
    @BindView(R.id.btn_add_reminder)
    FloatingActionButton btn_add_reminder;
    private Handler handler;
    private List<Reminder> reminders;
    private ReminderAdapter reminderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View fragment_reminder = inflater.inflate(R.layout.fragment_reminder,container,false);
        ButterKnife.bind(this,fragment_reminder);
        setResponse();
        init();
        getReminder();


        return fragment_reminder;
    }

    void init(){
        reminders = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(getContext(),reminders);
        reminder_list.setLayoutManager(new LinearLayoutManager(getContext()));
        reminder_list.setAdapter(reminderAdapter);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        reminderAdapter.setReminders(reminders);
                        reminderAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        };
    }

    void getReminder(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/reminder").newBuilder();
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
                        result = 0;
                        reminders = new Gson().fromJson(jsonObject.getString("reminders"),new TypeToken<List<Reminder>>(){}.getType());
                        Log.e("WOCAO", reminders.get(2).getChannel().getName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(result);
            }

        });
    }

    void setResponse(){
        btn_add_reminder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_reminder:
                Log.e("WOCAO","add_reminder, wait to be done");
                break;
        }
    }
}
