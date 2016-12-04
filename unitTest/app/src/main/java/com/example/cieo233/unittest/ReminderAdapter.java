package com.example.cieo233.unittest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 12/4/2016.
 */

public class ReminderAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Reminder> reminders;
    private Reminder mReminder;
    private ReminderHolder mHolder;

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReminderHolder(LayoutInflater.from(context).inflate(R.layout.reminder_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mHolder = (ReminderHolder) holder;
        mReminder = reminders.get(position);
        mHolder.getReminder_description().setText(mReminder.getContent());
        mHolder.getReminder_time().setText(mReminder.getDue());
        mHolder.getReminder_title().setText(mReminder.getTitle());
    }

    @Override
    public int getItemCount() {
        return reminders == null?0:reminders.size();
    }

    class ReminderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reminder_time)
        TextView reminder_time;
        @BindView(R.id.reminder_title)
        TextView reminder_title;
        @BindView(R.id.reminder_description)
        TextView reminder_description;
        @BindView(R.id.reminder_type)
        ImageView reminder_type;

        public ReminderHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public TextView getReminder_time() {
            return reminder_time;
        }

        public TextView getReminder_title() {
            return reminder_title;
        }

        public TextView getReminder_description() {
            return reminder_description;
        }

        public ImageView getReminder_type() {
            return reminder_type;
        }
    }

}
