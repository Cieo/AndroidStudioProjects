package com.example.cieo233.appdevelopmentlab10;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    private ImageView compass;
    private TextView degree, latitude, longitude, address, shake;
    private Geocoder geocoder;

    private int shakeCount, shakeCheck;

    private float fromDegree, toDegree, showDegree;

    private SensorManager sensorManager;
    private Sensor magnetometer;
    private Sensor accelerometer;

    private LocationManager locationManager;
    private LocationProvider gpsProvider;
    private LocationProvider netProvider;


    private float[] accelerometerValues = new float[3];
    private float[] magnetometerValues = new float[3];
    private float[] values = new float[3];
    private float[] r = new float[9];

    private boolean hasMagnet, hasAccelerate, hasLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setResponse();
    }

    void setResponse() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.e("WOCAO,GPS", "GPS pass");
        if (gpsProvider != null) {
            Log.e("WOCAO,GPS", "GPS not null");
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                showLocation(location);
            } else {
                Log.e("WOCAO,GPS", "LastLocation null");
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, this);
        }
        if (netProvider != null) {
            Log.e("WOCAO,Network", "Network not null");
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                showLocation(location);
            } else {
                Log.e("WOCAO,Network", "LastLocation null");
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1, this);
        }

    }

    void showLocation(Location location) {
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            address.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        latitude.setText("纬度：" + String.valueOf(location.getLatitude()));
        longitude.setText("经度：" + String.valueOf(location.getLongitude()));
    }

    void init() {
        shakeCheck = 0;
        shakeCount = 0;
        fromDegree = 0;
        toDegree = 0;
        showDegree = 0;
        hasAccelerate = false;
        hasLocation = false;
        hasMagnet = false;
        geocoder = new Geocoder(this, Locale.CHINA);
        compass = (ImageView) findViewById(R.id.compass);
        degree = (TextView) findViewById(R.id.degree);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        address = (TextView) findViewById(R.id.address);
        shake = (TextView) findViewById(R.id.shake);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        netProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        shake.setText("摇一摇次数：" + String.valueOf(shakeCount));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = sensorEvent.values.clone();
            hasAccelerate = true;
            float power[] = sensorEvent.values.clone();
            if ((Math.abs(power[0]) +  Math.abs(power[1]) + Math.abs(power[2])) > 100){
                shakeCheck += 1;
            }
            if (shakeCheck == 2){
                shakeCheck = 0;
                shakeCount += 1;
                shake.setText("摇一摇次数：" + String.valueOf(shakeCount));
            }
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerValues = sensorEvent.values.clone();
            hasMagnet = true;
        }

        if (hasMagnet && hasAccelerate) {
            SensorManager.getRotationMatrix(r, null, accelerometerValues, magnetometerValues);
            SensorManager.getOrientation(r, values);
            toDegree = ((float) Math.toDegrees(values[0]) + 360) % 360;
            showDegree = (float) Math.toDegrees(values[0]);
            if (Math.abs(toDegree - fromDegree) > 3) {
                RotateAnimation rotateAnimation = new RotateAnimation(-fromDegree, -toDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(200);
                rotateAnimation.setFillAfter(true);
                compass.startAnimation(rotateAnimation);
                degree.setText("旋转角度：" + String.valueOf(-showDegree));

            }
            fromDegree = toDegree;
            hasMagnet = false;
            hasAccelerate = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
