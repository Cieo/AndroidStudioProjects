package com.example.cieo233.appdevelopmentlab9;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_search;
    private EditText input_city;
    private ListView tips;
    private TextView city, update_time, tempture, high_low, humidity, air_quality, windspeed;
    private RecyclerView future_weather;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setResponse();
    }

    void init() {
        btn_search = (Button) findViewById(R.id.btn_search);
        input_city = (EditText) findViewById(R.id.input_city);
        tips = (ListView) findViewById(R.id.tips);
        city = (TextView) findViewById(R.id.city);
        update_time = (TextView) findViewById(R.id.update_time);
        tempture = (TextView) findViewById(R.id.tempture);
        high_low = (TextView) findViewById(R.id.low_high);
        humidity = (TextView) findViewById(R.id.humidity);
        air_quality = (TextView) findViewById(R.id.airQuality);
        windspeed = (TextView) findViewById(R.id.windSpeed);
        future_weather = (RecyclerView) findViewById(R.id.future_weather);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

            }
        };
    }

    void setResponse() {
        btn_search.setOnClickListener(this);
    }

    void praseXML(String body){
        Log.e("WOCAO",body);
    }

    @Override
    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("WOCAO","Thread start!");
                String str_city = city.getText().toString();
                String http_request = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theCityCode=" + str_city+"&theUserID=";
                try {
                    HttpURLConnection http = (HttpURLConnection) new URL(http_request).openConnection();
                    http.setRequestMethod("GET");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                    if (http.getResponseCode() == 200) {
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line + "\n");
                        }
                        praseXML(builder.toString());
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
