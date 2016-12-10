package com.example.cieo233.unittest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

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
 * Created by Cieo233 on 12/5/2016.
 */

public class AddRminderActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.reminder_detail_title)
    EditText reminder_create_title;
    @BindView(R.id.reminder_detail_content)
    EditText reminder_create_content;
    @BindView(R.id.reminder_detail_remark)
    EditText reminder_create_remark;
    @BindView(R.id.reminder_detail_channel)
    EditText reminder_create_channel;
    @BindView(R.id.reminder_detail_priority)
    EditText reminder_create_priority;
    @BindView(R.id.reminder_create_submit)
    Button reminder_create_submit;
    @BindView(R.id.reminder_detail_date)
    TextView reminder_create_date;
    private Handler handler;
    ProgressDialog progressDialog;
    String[] return_value = {"创建成功", "频道ID错误", "未知错误"};
    String title, content, due, priority, type, channel_id;
    private SelectedDate mSelectedDate;
    int mHour, mMinute;


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

    void updateDateTime(){
        reminder_create_date.setText(String.valueOf(mSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH))+" " + String.valueOf(mHour));
    }

    void showSublimePicker(){
        SublimeOptions options = new SublimeOptions();
        options.setCanPickDateRange(false);
        options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER|SublimeOptions.ACTIVATE_TIME_PICKER);
        SublimePickerFragment pickerFrag = new SublimePickerFragment();
        pickerFrag.setCallback(mFragmentCallback);
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", options);
        pickerFrag.setArguments(bundle);

        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        pickerFrag.show(getSupportFragmentManager(),"");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        ButterKnife.bind(this);
        init();
        setResponse();
    }

    void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                showToast(return_value[msg.what]);
                switch (msg.what) {
                    case 0:
                        setResult(1);
                        finish();
                        break;
                }
            }
        };
    }


    void setResponse(){
        reminder_create_submit.setOnClickListener(this);
        reminder_create_date.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        Log.e("WOCAO","inclick");
        switch (view.getId()){
            case R.id.reminder_create_submit:
                postReminder();
                break;
        }
    }


    void postReminder(){
        if (valid()) {
            progressDialog.show();
            OkHttpClient mOkHttpClient = new OkHttpClient();
            HttpUrl.Builder url_builder = HttpUrl.parse("http://api.sysu.space/api/reminder").newBuilder();
            url_builder.addEncodedQueryParameter("token",CurrentUser.getInstance().getUser().getToken());
            RequestBody formBody = new FormBody.Builder()
                    .add(Reminder.TITLE,title)
                    .add(Reminder.CONTENT,content)
                    .add(Reminder.DUE,due)
                    .add(Reminder.PRIORITY,priority)
                    .add(Reminder.TYPE,type)
                    .add(Reminder.CHANNEL_ID,channel_id)
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
                            result = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(result);
                }

            });
        }
    }

    boolean valid(){
        title = reminder_create_title.getText().toString();
        content = reminder_create_content.getText().toString();
        due = "1991-23-99 99:99:99";
        priority = reminder_create_priority.getText().toString();
        type = "1";
        channel_id = reminder_create_channel.getText().toString();
        return true;
    }

    void showToast(String content){
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }
}
