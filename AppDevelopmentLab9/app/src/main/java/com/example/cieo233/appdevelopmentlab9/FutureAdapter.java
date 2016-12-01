package com.example.cieo233.appdevelopmentlab9;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Cieo233 on 12/1/2016.
 */

class FutureAdapter extends RecyclerView.Adapter {
    private List<String> future;
    private Context context;

    void setFuture(List<String> future) {
        this.future = future;
    }

    FutureAdapter(List<String> future, Context context) {
        this.future = future;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FutureHolder(LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FutureHolder futureHolder = (FutureHolder) holder;
        String[] contents = future.get(position).split("\n");
        futureHolder.getFuture_data().setText((contents[0].split(" "))[0]);
        futureHolder.getFuture_weather().setText((contents[0].split(" "))[1]);
        futureHolder.getFuture_low_high().setText(contents[1]);
    }

    @Override
    public int getItemCount() {
        return future == null ? 0 : future.size();
    }

    private class FutureHolder extends RecyclerView.ViewHolder {
        private TextView future_data, future_weather, future_low_high;

        FutureHolder(View view) {
            super(view);
            future_data = (TextView) view.findViewById(R.id.date);
            future_weather = (TextView) view.findViewById(R.id.weather);
            future_low_high = (TextView) view.findViewById(R.id.item_low_high);
        }

        TextView getFuture_data() {
            return future_data;
        }

        TextView getFuture_weather() {
            return future_weather;
        }

        TextView getFuture_low_high() {
            return future_low_high;
        }
    }
}
