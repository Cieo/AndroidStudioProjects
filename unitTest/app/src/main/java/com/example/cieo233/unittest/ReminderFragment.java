package com.example.cieo233.unittest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.bottomdialog.BottomDialog;

/**
 * Created by Cieo233 on 12/4/2016.
 */

public class ReminderFragment extends Fragment implements View.OnClickListener, Interface.RecyclerViewClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.reminder_list)
    RecyclerView reminderList;
    @BindView(R.id.frament_reminder_date)
    TextView fragment_reminder_date;
    @BindView(R.id.btn_add_reminder)
    ImageView btnAddReminder;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private Handler createReminderHandler;
    private Handler showAllReminderHandler;
    private Handler reminderFragmentHandler;
    private Handler deleteReminderHandler;
    private Handler updateReminderHandler;
    private ReminderAdapter reminderAdapter;
    private BottomDialog mBottomDialog;
    private final int Done = 0;
    private final int KEEPCHANGE = 0;
    private final int DROPCHANGE = 1;
    BroadcastReceiver syncReciver;
    String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View fragment_reminder = inflater.inflate(R.layout.fragment_reminder, container, false);
        ButterKnife.bind(this, fragment_reminder);
        init();
        setResponse();
        return fragment_reminder;
    }

    void init() {
        Calendar today = Calendar.getInstance();
        fragment_reminder_date.setText(month[today.get(Calendar.MONTH)] + " " + today.get(Calendar.DAY_OF_MONTH) + " " + today.get(Calendar.YEAR));

        reminderAdapter = new ReminderAdapter(getContext(), CurrentUser.getInstance().getReminders(), this);
        reminderList.setLayoutManager(new LinearLayoutManager(getContext()));
        reminderList.setAdapter(reminderAdapter);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);
        createReminderHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case StateCode.OK:
                        CodoAPI.getReminders(showAllReminderHandler);
                        Log.e("CreateReminder", "备忘录创建成功");
                        break;
                    case StateCode.PARAMETER_EMPTY:
                        Log.e("CreateReminder", "备忘录参数错误");
                        CodoAPI.getReminders(showAllReminderHandler);
                        break;
                }
                return false;
            }
        });
        showAllReminderHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case StateCode.OK:
                        Log.e("ShowAllRminder", "获取备忘录成功");
                        reminderAdapter.setReminders(CurrentUser.getInstance().getReminders());
                        reminderAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                        break;
                    case StateCode.TOKEN_INVALID:
                        Log.e("ShowAllReminder", "获取备忘录失败");
                        break;
                }
                return false;
            }
        });
        reminderFragmentHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case Done:
                        mBottomDialog.dismiss();
                        break;
                }
                return false;
            }
        });
        deleteReminderHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case StateCode.OK:
                        Log.e("DeleteReminder", "删除成功");
                        break;
                    case StateCode.TOKEN_INVALID:
                        Log.e("DeleteReminder", "删除失败");
                        CodoAPI.getReminders(showAllReminderHandler);
                        break;
                    case StateCode.PERMISSION_DENY:
                        Log.e("DeleteReminder", "删除失败");
                        CodoAPI.getReminders(showAllReminderHandler);
                        break;
                }
                return false;
            }
        });
        updateReminderHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case StateCode.OK:
                        Log.e("UpdateReminder", "更新成功");
                        break;
                    case StateCode.URLIDINVALID:
                        CodoAPI.getReminders(showAllReminderHandler);
                        Log.e("UpdateReminder", "更新失败");
                        break;
                    case StateCode.TOKEN_INVALID:
                        CodoAPI.getReminders(showAllReminderHandler);
                        Log.e("UpdateReminder", "更新失败");
                }
                return false;
            }
        });
        syncReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                reminderAdapter.setReminders(CurrentUser.getInstance().getReminders());
                reminderAdapter.notifyDataSetChanged();
                Log.e("TestSync", "备忘录同步完成");
            }
        };
        IntentFilter intentFilter = new IntentFilter("Cieo.SyncReminderComplete");
        getContext().registerReceiver(syncReciver, intentFilter);

        if (GeneralUtils.isNetConnected(getContext())){
            CodoAPI.getReminders(showAllReminderHandler);
        }
    }


    void setResponse() {
        btnAddReminder.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_reminder:
                showBottomDialog();
                break;
        }
    }

    void showBottomDialog() {
        mBottomDialog = BottomDialog.create(getActivity().getSupportFragmentManager()).setViewListener(new BottomDialog.ViewListener() {
            String reminderDue;
            ImageView dateTime, channel, create;
            Channel newChannel;
            MaterialEditText newTitle, newContent;
            TextView showDate, showChannel;
            HorizontalScrollView showChannelScroll;

            SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
                @Override
                public void onCancelled() {
                }

                @Override
                public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                                    int hourOfDay, int minute,
                                                    SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                                    String recurrenceRule) {

                    Calendar targetDate = selectedDate.getStartDate();
                    targetDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    targetDate.set(Calendar.MINUTE, minute);
                    updateDateTime(targetDate);
                }
            };


            void updateDateTime(Calendar targetDate) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA);
                reminderDue = dateFormat.format(targetDate.getTime());
                showDate.setVisibility(View.VISIBLE);
                showDate.setText(reminderDue.substring(5, 11));
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
                dateTime = (ImageView) v.findViewById(R.id.iconDate);
                dateTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSublimePicker();
                    }
                });

                newTitle = (MaterialEditText) v.findViewById(R.id.title);
                newContent = (MaterialEditText) v.findViewById(R.id.content);

                showChannel = (TextView) v.findViewById(R.id.showChannel);
                showDate = (TextView) v.findViewById(R.id.showDate);
                showChannelScroll = (HorizontalScrollView) v.findViewById(R.id.showChannelScroll);

                channel = (ImageView) v.findViewById(R.id.iconChannel);
                channel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        final List<Channel> channels = CurrentUser.getInstance().getCreatorChannels();
                        String[] shownChannels = new String[channels.size() + 1];
                        shownChannels[0] = "own";
                        for (int i = 1; i < shownChannels.length; i++) {
                            shownChannels[i] = channels.get(i - 1).getName();
                        }
                        //    设置一个下拉的列表选择项
                        builder.setItems(shownChannels, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showChannelScroll.setVisibility(View.VISIBLE);
                                if (which == 0) {
                                    newChannel = null;
                                    showChannel.setText("Own");
                                    return;
                                }
                                newChannel = channels.get(which - 1);
                                showChannel.setText(newChannel.getName());
                            }
                        });
                        builder.show();
                    }
                });

                create = (ImageView) v.findViewById(R.id.iconSend);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int type;
                        if (newChannel == null) {
                            type = 1;
                        } else {
                            type = 0;
                        }
                        Reminder newReminder = new Reminder(newChannel, newTitle.getText().toString(), newContent.getText().toString(), reminderDue, 1, type);
                        CodoAPI.createReminder(newReminder, createReminderHandler);

                        reminderFragmentHandler.sendEmptyMessage(Done);

                    }
                });

            }
        }).setLayoutRes(R.layout.dialog_add_reminder).setDimAmount(0.2f);
        mBottomDialog.show();
    }


    @Override
    public void recyclerViewListClicked(Object data) {
        Reminder selectedReminder = (Reminder) data;
        Log.e("TestInterface", selectedReminder.getTitle());
        Intent intent = new Intent(getContext(), ReminderDetailActivity.class);
        intent.putExtra("Data", selectedReminder);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case KEEPCHANGE:
                Reminder dataIn = (Reminder) data.getSerializableExtra("DataIn");
                Reminder dataOut = (Reminder) data.getSerializableExtra("DataOut");
                for (Reminder i : CurrentUser.getInstance().getReminders()) {
                    if (i.getId() == dataIn.getId()) {
                        CurrentUser.getInstance().getReminders().set(CurrentUser.getInstance().getReminders().indexOf(i), dataOut);
                    }
                }
                reminderAdapter.setReminders(CurrentUser.getInstance().getReminders());
                reminderAdapter.notifyDataSetChanged();
                CodoAPI.updateReminder(dataOut, updateReminderHandler);
                break;
            case DROPCHANGE:
                break;
        }
    }

    @Override
    public void recyclerViewListLongClicked(final Object data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("确认删除?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeReminder(data);
            }
        }).create().show();
    }

    void removeReminder(Object data) {
        CurrentUser.getInstance().getReminders().remove(data);
        reminderAdapter.setReminders(CurrentUser.getInstance().getReminders());
        reminderAdapter.notifyDataSetChanged();
        CodoAPI.deleteReminder((Reminder) data, deleteReminderHandler);
    }

    @Override
    public void onRefresh() {
        CodoAPI.getReminders(showAllReminderHandler);
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(syncReciver);
        super.onDestroy();
    }
}
