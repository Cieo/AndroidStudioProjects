package com.example.cieo233.unittest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import com.jmpergar.awesometext.AwesomeTextHandler;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 12/5/2016.
 */

public class ReminderDetailActivity extends AppCompatActivity implements View.OnClickListener {

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

    Reminder inReminder;
    Channel inChannel;
    private static final String HASHTAG_PATTERN = "(##!##.*?##!##)";
    AwesomeTextHandler awesomeEditTextHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);
        ButterKnife.bind(this);
        init();
        setContent();
    }

    void init(){
        inReminder = (Reminder) getIntent().getSerializableExtra("Data");
        inChannel = inReminder.getChannel();
        calendarView.state().edit().setFirstDayOfWeek(Calendar.SUNDAY).setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        swipeSelector.setItems(
                new SwipeItem(0, "Priority : Low", null),
                new SwipeItem(1, "Priority : Normal", null),
                new SwipeItem(2, "Priority : High", null)
        );
        awesomeEditTextHandler = new AwesomeTextHandler();
        awesomeEditTextHandler
                .addViewSpanRenderer(HASHTAG_PATTERN, new HashTagSpanRenderer())
                .setView(channel);
    }

    void setContent(){
        title.setText(inReminder.getTitle());
        String strTitle;
        if (inChannel != null){
            strTitle = new StringBuilder().append("##!##").append(inChannel.getName()).append("##!##").toString();
        } else {
            strTitle = new StringBuilder().append("##!##").append("own").append("##!##").toString();
        }
        awesomeEditTextHandler.setText(strTitle);
        swipeSelector.selectItemAt(inReminder.getPriority());
        content.setText(inReminder.getContent());

        if (inReminder.getDue() != null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.CHINA);
            try {
                calendarView.setSelectedDate(simpleDateFormat.parse(inReminder.getDue()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
