package com.example.cieo233.appdevelopmentlab9;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_search;
    private EditText input_city;
    private ListView tips;
    private TextView city, update_time, tempture, high_low, humidity, air_quality, windspeed;
    private RecyclerView future_weather;
    private Handler handler;
    private Weather weather;
    private TipsAdapter tipsAdapter;
    private FutureAdapter futureAdapter;

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
        tipsAdapter = new TipsAdapter(new ArrayList<String>(), this);
        tips.setAdapter(tipsAdapter);
        futureAdapter = new FutureAdapter(new ArrayList<String>(),this);
        future_weather.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        future_weather.setAdapter(futureAdapter);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        city.setText(weather.getCity());
                        update_time.setText(weather.getUpdateTime()+"更新");
                        tempture.setText(weather.getTempture());
                        high_low.setText(weather.getLowHigh());
                        humidity.setText(weather.getHumidity());
                        air_quality.setText(weather.getAirQuality());
                        windspeed.setText(weather.getWindSpeed());
                        tipsAdapter.setTips(weather.getTips());
                        tipsAdapter.notifyDataSetChanged();
                        futureAdapter.setFuture(weather.getFuture());
                        futureAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        showToast("免费用户不能使用高速访问");
                        break;
                    case 3:
                        showToast("查询结果为空");
                        break;
                    case 24:
                        showToast("免费用户24小时内访问超过规定数量");
                        break;
                }
            }
        };
    }

    void setResponse() {
        btn_search.setOnClickListener(this);
    }

    String praseXML(String body) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        StringBuilder builder = new StringBuilder();
        xpp.setInput(new StringReader(body));
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                eventType = xpp.next();
                if (eventType == XmlPullParser.TEXT) {
                    builder.append(xpp.getText() + "\n");
                }
            }
            eventType = xpp.next();
        }
        return builder.toString();
    }

    @Override
    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str_city = input_city.getText().toString();
                String http_request = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theCityCode=" + str_city + "&theUserID=";
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
                        String body = builder.toString();
                        if (body.contains("免费用户24小时内访问超过规定数量")){
                            handler.sendEmptyMessage(24);
                            return;
                        } else if (body.contains("免费用户不能使用高速访问")){
                            handler.sendEmptyMessage(2);
                        } else if (body.contains("查询结果为空")){
                            handler.sendEmptyMessage(3);
                        } else {
                            weather = new Weather(praseXML(body).split("\n"));
                            handler.sendEmptyMessage(1);
                        }
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void showToast(String content){
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }

}

class Weather {
    private String[] weather;

    public Weather(String[] weather) {
        this.weather = weather;
    }

    public String getCity() {
        return weather[3];
    }

    public String getUpdateTime() {
        return weather[5].split(" ")[1];
    }

    public String getTempture() {
        return weather[6].substring(weather[6].indexOf("气温：") + 3, weather[6].indexOf("；风向"));
    }

    public String getLowHigh() {
        return weather[16];
    }

    public String getHumidity() {
        return weather[6].substring(weather[6].indexOf("湿度："));
    }

    public String getAirQuality() {
        return weather[7].substring(weather[7].indexOf("空气质量："));
    }

    public String getWindSpeed() {
        return weather[6].substring(weather[6].indexOf("风力：") + 3, weather[6].indexOf("；湿度"));
    }

    public List<String> getTips() {
        List<String> tips = new ArrayList<>();
        tips.add(weather[8]);
        tips.add(weather[9]);
        tips.add(weather[10]);
        tips.add(weather[11]);
        tips.add(weather[12]);
        tips.add(weather[13]);
        return tips;
    }

    public List<String> getFuture() {
        List<String> future = new ArrayList<>();
        future.add(weather[15] + "\n" + weather[16]);
        future.add(weather[20] + "\n" + weather[21]);
        future.add(weather[25] + "\n" + weather[26]);
        future.add(weather[30] + "\n" + weather[31]);
        future.add(weather[35] + "\n" + weather[36]);
        return future;
    }
}