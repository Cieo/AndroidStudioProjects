package com.example.cieo233.unittest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.ArrayList;
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
    private ReminderAdapter reminderAdapter;
    private String new_title, new_content, new_due, new_priority, new_type, new_channel_id;
    private BottomDialog mBottomDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View fragment_reminder = inflater.inflate(R.layout.fragment_reminder, container, false);
        ButterKnife.bind(this, fragment_reminder);
        setResponse();
        init();
        return fragment_reminder;
    }

    void init() {
        reminderAdapter = new ReminderAdapter(getContext(), CurrentUser.getInstance().getReminders());
        reminder_list.setLayoutManager(new LinearLayoutManager(getContext()));
        reminder_list.setAdapter(reminderAdapter);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        reminderAdapter.setReminders(CurrentUser.getInstance().getReminders());
                        reminderAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        showToast("用户身份无效");
                        break;
                    case 2:
                        showToast("未知错误");
                        break;
                    case 8:
                        getAllReminder();
                        break;
                    case 9:
                        mBottomDialog.dismiss();
                        break;
                }
            }
        };
    }

    void getReminder() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/reminder").newBuilder();
        url_builder.addEncodedQueryParameter("token", CurrentUser.getInstance().getToken());
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
                        CurrentUser.getInstance().setReminders((List<Reminder>) new Gson().fromJson(jsonObject.getString("reminders"), new TypeToken<List<Reminder>>() {
                        }.getType()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(result);
            }

        });
    }

    void setResponse() {
        btn_add_reminder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_reminder:
                Log.e("WOCAO", "add_reminder, wait to be done");
                showBottomDialog();
//                startActivityForResult(new Intent(getContext(), AddRminderActivity.class), 1);
                break;
        }
    }

    void showBottomDialog() {
        mBottomDialog = BottomDialog.create(getActivity().getSupportFragmentManager()).setViewListener(new BottomDialog.ViewListener() {

            TextView date_time, channel;
            Button create;
            private SelectedDate mSelectedDate;
            int mHour, mMinute;
            Channel mChannel;
            SwipeSelector swipeSelector;

            SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
                @Override
                public void onCancelled() {
                }

                @Override
                public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                                    int hourOfDay, int minute,
                                                    SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                                    String recurrenceRule) {

                    mSelectedDate = selectedDate;
                    mHour = hourOfDay;
                    mMinute = minute;

                    updateDateTime();
                }
            };


            void updateDateTime() {
                date_time.setText(String.valueOf(mSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH)) + " " + String.valueOf(mHour));
            }

            void showSublimePicker() {
                SublimeOptions options = new SublimeOptions();
                options.setCanPickDateRange(false);
                options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER | SublimeOptions.ACTIVATE_TIME_PICKER);
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", options);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getActivity().getSupportFragmentManager(), "");
            }

            @Override
            public void bindView(View v) {
                date_time = (TextView) v.findViewById(R.id.date_time);
                date_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSublimePicker();
                    }
                });
                swipeSelector = (SwipeSelector) v.findViewById(R.id.priority);
                swipeSelector.setItems(new SwipeItem(0, "Low",null),
                        new SwipeItem(1, "Normal", null),
                        new SwipeItem(2, "High", null));
                channel = (TextView) v.findViewById(R.id.channel);
                channel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        //    指定下拉列表的显示数据

                        final List<Channel> channels = CurrentUser.getInstance().getChannels();
                        String[] shownChannels = new String[channels.size()+1];
                        shownChannels[0] = "own";
                        for (int i  = 1; i < shownChannels.length; i ++) {
                            shownChannels[i] = channels.get(i-1).getName();
                        }
                        //    设置一个下拉的列表选择项
                        builder.setItems(shownChannels, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0){
                                    mChannel = null;
                                    return;
                                }
                                mChannel = channels.get(which);
                            }
                        });
                        builder.show();
                    }
                });
                create = (Button) v.findViewById(R.id.create);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new_title = "just try try title";
                        new_content = "just try try content";
                        new_due = "1234-12-34 12:34:56";
                        new_priority = String.valueOf(swipeSelector.getSelectedItem().value);
                        if (mChannel == null){
                            new_type = "1";
                            new_channel_id = "null";
                        }
                        else {
                            new_type = "0";
                            new_channel_id = String.valueOf(mChannel.getId());
                        }
                        postReminder();
                        handler.sendEmptyMessage(9);
                    }
                });

            }
        }).setLayoutRes(R.layout.dialog_add_reminder).setDimAmount(0.2f);
        mBottomDialog.show();
    }

    void getAllReminder(){
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
                        CurrentUser.getInstance().setReminders((List<Reminder>) new Gson().fromJson(jsonObject.getString("reminders"),new TypeToken<List<Reminder>>(){}.getType()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(result);
            }

        });
    }


    void postReminder(){
            OkHttpClient mOkHttpClient = new OkHttpClient();
            HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/reminder").newBuilder();
            url_builder.addEncodedQueryParameter("token",CurrentUser.getInstance().getToken());
            RequestBody formBody = new FormBody.Builder()
                    .add(Reminder.TITLE,new_title)
                    .add(Reminder.CONTENT,new_content)
                    .add(Reminder.DUE,new_due)
                    .add(Reminder.PRIORITY,new_priority)
                    .add(Reminder.TYPE,new_type)
                    .add(Reminder.CHANNEL_ID,new_channel_id)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getReminder();
        Log.e("WOCAO", "msg from on activity result");
    }

    void showToast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }
}
