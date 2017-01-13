package com.example.cieo233.unittest;

import android.content.Intent;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aitangba.swipeback.SwipeBackActivity;
import com.google.gson.Gson;
import com.jmpergar.awesometext.AwesomeTextHandler;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.bottomdialog.BottomDialog;

/**
 * Created by Cieo233 on 12/5/2016.
 */

public class ReminderDetailActivity extends SwipeBackActivity implements Interface.AwesomeTextChannelClickListener, OnDateSelectedListener {

    @BindView(R.id.calendar)
    MaterialCalendarView calendarView;
    @BindView(R.id.swipeSelector)
    SwipeSelector swipeSelector;
    @BindView(R.id.title)
    MaterialEditText title;
    @BindView(R.id.channel)
    TextView channel;
    @BindView(R.id.content)
    MaterialEditText content;
    @BindView(R.id.contentText)
    TextView contentText;
    @BindView(R.id.remark)
    MaterialEditText remark;

    Reminder inReminder;
    Channel inChannel;
    Reminder newReminder;
    private static final String HASHTAG_PATTERN = "(##!##.*?##!##)";
    AwesomeTextHandler awesomeEditTextHandler;
    CalendarDay[] checkDoubleClick;
    BottomDialog mBottomDialog;
    String selectedHour, selectedMinute, selectedSecond;
    Handler reminderDetailHandler;
    private final int DONE = 0;
    private final int KEEPCHANGE = 0;
    private final int DROPCHANGE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);
        ButterKnife.bind(this);
        init();
        setContent();
    }

    void init() {
        checkDoubleClick = new CalendarDay[3];
        inReminder = (Reminder) getIntent().getSerializableExtra("Data");
        inChannel = inReminder.getChannel();
        newReminder = new Reminder();
        calendarView.state().edit().setFirstDayOfWeek(Calendar.SUNDAY).setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        calendarView.setOnDateChangedListener(this);
        swipeSelector.setItems(
                new SwipeItem(0, "Priority : Low", null),
                new SwipeItem(1, "Priority : Normal", null),
                new SwipeItem(2, "Priority : High", null)
        );
        awesomeEditTextHandler = new AwesomeTextHandler();
        awesomeEditTextHandler
                .addViewSpanRenderer(HASHTAG_PATTERN, new HashTagSpanRenderer(this))
                .setView(channel);
        reminderDetailHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case DONE:
                        mBottomDialog.dismiss();
                        break;
                }
                return false;
            }
        });
    }

    void setContent() {
        title.setText(inReminder.getTitle());
        String strTitle;
        if (inChannel != null) {
            strTitle = "##!##" + inChannel.getName() + "##!##";
        } else {
            strTitle = "##!##" + "own" + "##!##";
        }
        awesomeEditTextHandler.setText(strTitle);
        swipeSelector.selectItemAt(inReminder.getPriority());
        content.setText(inReminder.getContent());
        contentText.setText(inReminder.getContent());
        remark.setText(inReminder.getRemark());
        if (inReminder.getType() == 1||(inReminder.getType()==0 && checkCreator(inChannel))) {
            content.setVisibility(View.VISIBLE);
            contentText.setVisibility(View.GONE);
            remark.setVisibility(View.GONE);
        }
        else {
            content.setVisibility(View.GONE);
            contentText.setVisibility(View.VISIBLE);
            remark.setVisibility(View.VISIBLE);
        }

        if (inReminder.getDue() != null) {
            selectedMinute = inReminder.getDue().substring(14, 16);
            selectedHour = inReminder.getDue().substring(11, 13);
            selectedSecond = inReminder.getDue().substring(17);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.CHINA);
            try {
                calendarView.setSelectedDate(simpleDateFormat.parse(inReminder.getDue()));
                checkDoubleClick[0] = calendarView.getSelectedDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            selectedHour = "00";
            selectedMinute = "00";
            selectedSecond = "00.0";
        }
    }

    boolean checkChange() {
        if (!Objects.equals(inReminder.getChannel(), newReminder.getChannel())) {
            Log.e("TestCheckChange", "DifferentChannel");
            return true;
        }
        if (!(inReminder.getPriority() == newReminder.getPriority())) {
            Log.e("TestCheckChange", "DifferentPriority");
            return true;
        }
        if (inReminder.getDue() != null && newReminder.getDue() != null) {
            if (!(Objects.equals(inReminder.getDue(), newReminder.getDue()))) {
                Log.e("TestCheckChange", "DifferentDue");
                Log.e("TestCheckChange", inReminder.getDue());
                Log.e("TestCheckChange", newReminder.getDue());
                return true;
            }
        }
        if (inReminder.getDue() == null && newReminder.getDue() != null) {
            Log.e("TestCheckChange", "DifferentDueNew");
            return true;
        }
        if (!(Objects.equals(inReminder.getTitle(), newReminder.getTitle()))) {
            Log.e("TestCheckChange", "DifferentTitle");
            return true;
        }
        if (!(Objects.equals(inReminder.getContent(), newReminder.getContent()))) {
            if (inReminder.getContent() == null && Objects.equals(newReminder.getContent(), "")) {
                return false;
            }
            Log.e("TestCheckChange", "DifferentContent");
            return true;
        }
        if (inReminder.getRemark() != null && newReminder.getRemark() != null) {
            if (!(Objects.equals(inReminder.getRemark(), newReminder.getRemark()))) {
                Log.e("TestCheckChange", "DifferentRemark");
                Log.e("TestCheckChange", inReminder.getRemark());
                Log.e("TestCheckChange", newReminder.getRemark());
                return true;
            }
        }
        if (inReminder.getRemark() == null && newReminder.getRemark() != null) {
            if (Objects.equals(newReminder.getRemark(), "")) {
                return false;
            }
            Log.e("TestCheckChange", "DifferentRemarkNew");
            Log.e("TestCheckChange", newReminder.getRemark());

            return true;
        }

        return false;
    }


    @Override
    public void finish() {
        CalendarDay selectedDate = calendarView.getSelectedDate();
        String strDue = null;
        if (selectedDate != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            strDue = simpleDateFormat.format(selectedDate.getDate()) + " " + selectedHour + ":" + selectedMinute + ":" + selectedSecond;
            Log.e("TestOnBackPressed", strDue);
        }
        newReminder.setDue(strDue);
        newReminder.setPriority((Integer) swipeSelector.getSelectedItem().value);
        newReminder.setChannel(inChannel);
        newReminder.setTitle(title.getText().toString());
        newReminder.setContent(content.getText().toString());
        newReminder.setCreatorID(inReminder.getCreatorID());
        newReminder.setId(inReminder.getId());
        newReminder.setLast_update(inReminder.getLast_update());
        newReminder.setRemark(remark.getText().toString());
        newReminder.setState(inReminder.getState());
        newReminder.setType(inReminder.getType());
        if (checkChange()) {
            Intent intent = new Intent();
            intent.putExtra("DataOut", newReminder);
            intent.putExtra("DataIn", inReminder);
            setResult(KEEPCHANGE, intent);
        } else {
            setResult(DROPCHANGE);
        }
        super.finish();
    }

    boolean checkCreator(Channel toBeCheck){
        List<Channel> channels = CurrentUser.getInstance().getCreatorChannels();
        for (Channel channel : channels){
            if (Objects.equals(channel.getName(), toBeCheck.getName()) && channel.getId() == toBeCheck.getId()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void awesomeTextChannelClicked(Object data) {

        Log.e("ChannelClicked", "ChannelClicked");
//        Channel selectedChannel = (Channel) data;
//        String strTitle;
//        if (selectedChannel != null){
//            strTitle = "##!##" + selectedChannel.getName() + "##!##";
//        } else {
//            strTitle = "##!##" + "own" + "##!##";
//        }
//        awesomeEditTextHandler.setText(strTitle);
//        Log.e("TestAweSomeText",channel.getText().toString());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        checkDoubleClick[2] = checkDoubleClick[1];
        checkDoubleClick[1] = checkDoubleClick[0];
        checkDoubleClick[0] = date;
        if (checkDoubleClick[1] != null) {
            if (checkDoubleClick[0].equals(checkDoubleClick[1]) && checkDoubleClick[1].equals(checkDoubleClick[2])) {
                Log.e("TestCalendar", "Triple Clicked");
                widget.clearSelection();
                checkDoubleClick[0] = null;
                checkDoubleClick[1] = null;
                checkDoubleClick[2] = null;
            } else if (checkDoubleClick[0].equals(checkDoubleClick[1])) {
                Log.e("TestCalendar", "Double Clicked");
                showBottomDialog();
            }
        }
    }

    void showBottomDialog() {
        mBottomDialog = BottomDialog.create(getSupportFragmentManager()).setViewListener(new BottomDialog.ViewListener() {
            WheelPicker hourSelector, minuteSelector;
            ImageView check;

            @Override
            public void bindView(View v) {
                hourSelector = (WheelPicker) v.findViewById(R.id.hourSelector);
                minuteSelector = (WheelPicker) v.findViewById(R.id.minuteSelector);
                check = (ImageView) v.findViewById(R.id.check);
                final List<String> hours = new ArrayList<String>();
                final List<String> minutes = new ArrayList<String>();
                for (int i = 0; i < 24; i++) {
                    if (i < 10) {
                        hours.add("0" + String.valueOf(i));
                    } else {
                        hours.add(String.valueOf(i));
                    }
                }
                for (int i = 0; i < 60; i++) {
                    if (i < 10) {
                        minutes.add("0" + String.valueOf(i));
                    } else {
                        minutes.add(String.valueOf(i));
                    }
                }
                hourSelector.setData(hours);
                hourSelector.setSelectedItemPosition(Integer.parseInt(selectedHour));
                minuteSelector.setData(minutes);
                minuteSelector.setSelectedItemPosition(Integer.parseInt(selectedMinute));
                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedHour = hours.get(hourSelector.getCurrentItemPosition());
                        selectedMinute = minutes.get(minuteSelector.getCurrentItemPosition());
                        reminderDetailHandler.sendEmptyMessage(DONE);
                    }
                });
            }
        }).setLayoutRes(R.layout.dialog_select_time).setDimAmount(0.2f);
        mBottomDialog.show();
    }
}
