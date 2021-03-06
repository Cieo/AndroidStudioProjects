package com.example.cieo233.unittest;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 12/4/2016.
 */

public class ReminderAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Reminder> reminders;
    private String[] color = {"#FF8BC322", "#FF03A9F4", "#FFE91E63"};
    private Interface.RecyclerViewClickListener recyclerViewClickListener;

    Comparator<Reminder> comparator;

    public ReminderAdapter(Context context, List<Reminder> reminders, Interface.RecyclerViewClickListener recyclerViewClickListener) {
        this.context = context;
        this.reminders = reminders;
        this.recyclerViewClickListener = recyclerViewClickListener;

        comparator = new Comparator<Reminder>() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.CHINA);

            @Override
            public int compare(Reminder reminder, Reminder t1) {
                try {
                    if (simpleDateFormat.parse(reminder.getDue()).before(simpleDateFormat.parse(t1.getDue()))) {
                        return -1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        };
        sortReminders();
    }

    private void sortReminders() {
        List<Reminder> tempReminders = new ArrayList<>();
        for (Reminder i : reminders) {
            if (i.getDue() != null) {
                tempReminders.add(i);
            }
        }
        for (Reminder i : tempReminders) {
            reminders.remove(i);
        }
        Collections.sort(tempReminders, comparator);
        reminders.addAll(tempReminders);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        sortReminders();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReminderHolder(LayoutInflater.from(context).inflate(R.layout.reminder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReminderHolder mHolder = (ReminderHolder) holder;
        Reminder mReminder = reminders.get(position);

        mHolder.getReminderContent().setText(mReminder.getContent());
        mHolder.getReminderTitle().setText(mReminder.getTitle());
        mHolder.getReminderPriority().setBackgroundColor(Color.parseColor(color[mReminder.getPriority()]));

        if (mReminder.getDue() != null) {
            String[] splits = mReminder.getDue().split(" ");
            mHolder.getReminderDueDate().setText(splits[0].substring(5));
            mHolder.getReminderDueTime().setText(splits[1].substring(0, 5));
        } else {
            mHolder.getReminderDueDate().setText("");
            mHolder.getReminderDueTime().setText("");
        }

    }

    @Override
    public int getItemCount() {
        return reminders == null ? 0 : reminders.size();
    }

    class ReminderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reminderDueDate)
        TextView reminderDueDate;
        @BindView(R.id.reminderDueTime)
        TextView reminderDueTime;
        @BindView(R.id.reminderTitle)
        TextView reminderTitle;
        @BindView(R.id.reminderContent)
        TextView reminderContent;
        @BindView(R.id.reminderPriority)
        TextView reminderPriority;

        public ReminderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickListener.recyclerViewListClicked(reminders.get(getLayoutPosition()));
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    recyclerViewClickListener.recyclerViewListLongClicked(reminders.get(getAdapterPosition()));
                    return true;
                }
            });
        }

        public TextView getReminderDueDate() {
            return reminderDueDate;
        }

        public TextView getReminderDueTime() {
            return reminderDueTime;
        }

        public TextView getReminderTitle() {
            return reminderTitle;
        }

        public TextView getReminderContent() {
            return reminderContent;
        }

        public TextView getReminderPriority() {
            return reminderPriority;
        }
    }

}
