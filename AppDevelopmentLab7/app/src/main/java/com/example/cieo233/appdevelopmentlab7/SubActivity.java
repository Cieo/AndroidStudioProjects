package com.example.cieo233.appdevelopmentlab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class SubActivity extends AppCompatActivity{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_sub);
  }



  @Override
  public void onBackPressed() {
      Intent i = new Intent(Intent.ACTION_MAIN);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      i.addCategory(Intent.CATEGORY_HOME);
      startActivity(i);
  }
}
