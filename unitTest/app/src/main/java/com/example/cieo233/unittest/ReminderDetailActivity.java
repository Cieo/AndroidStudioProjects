package com.example.cieo233.unittest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;


import com.jmpergar.awesometext.AwesomeTextHandler;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import java.util.Calendar;

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

    private static final String HASHTAG_PATTERN = "(#Channel#[\\p{L}0-9-_]+)";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);
        ButterKnife.bind(this);
        init();
    }

    void init(){
        calendarView.state().edit().setFirstDayOfWeek(Calendar.SUNDAY).setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        swipeSelector.setItems(
                new SwipeItem(0, "Slide one", null),
                new SwipeItem(1, "Slide two", null),
                new SwipeItem(2, "Slide three", null)
        );
        AwesomeTextHandler awesomeEditTextHandler = new AwesomeTextHandler();
        awesomeEditTextHandler
                .addViewSpanRenderer(HASHTAG_PATTERN, new HashTagSpanRenderer())
                .setView(title);
    }

    @Override
    public void onClick(View view) {

    }
}
