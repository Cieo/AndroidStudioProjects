package com.example.cieo233.unittest;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Cieo233 on 12/10/2016.
 */

public class GeneralUtils {
    public static void showToast(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
