package com.example.cieo233.unittest;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cieo233.unittest.R;
import com.jmpergar.awesometext.AwesomeTextHandler;

class HashTagSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer, AwesomeTextHandler.ViewSpanClickListener {

    private final static int textSizeInDips = 18;
    private final static int backgroundResource = R.drawable.title_sytle;

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
        Toast.makeText(context, "Hello " + text, Toast.LENGTH_SHORT).show();
    }

    private int dipsToPixels(Context ctx, float dips) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        int px = (int) (dips * scale + 0.5f);
        return px;
    }
}
