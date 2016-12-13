package com.example.cieo233.unittest;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.jmpergar.awesometext.AwesomeTextHandler;

import java.util.List;

class HashTagSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer, AwesomeTextHandler.ViewSpanClickListener {

    private final static int textSizeInDips = 18;
    private final static int backgroundResource = R.drawable.title_sytle;
    private Interface.AwesomeTextChannelClickListener awesomeTextChannelClickListener;

    public HashTagSpanRenderer(Interface.AwesomeTextChannelClickListener awesomeTextChannelClickListener) {
        this.awesomeTextChannelClickListener = awesomeTextChannelClickListener;
    }

    @Override
    public View getView(String text, Context context) {
        TextView view = new TextView(context);
        view.setText(text.substring(5,text.indexOf("##!##",4)));
        view.setTextSize(dipsToPixels(context, textSizeInDips));
        view.setBackgroundResource(backgroundResource);
        view.setTextColor(Color.parseColor("#ffffff"));
        return view;
    }

    @Override
    public void onClick(String text, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                if (which == 0) {
                    awesomeTextChannelClickListener.awesomeTextChannelClicked(null);
                    return;
                }
                awesomeTextChannelClickListener.awesomeTextChannelClicked(channels.get(which - 1));
            }
        });
        builder.show();
    }

    private int dipsToPixels(Context ctx, float dips) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dips * scale + 0.5f);
    }
}
