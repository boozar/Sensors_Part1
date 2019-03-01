package com.example.vencedor.sensors_1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView tvText;
    SensorManager sensorManager;
    Sensor sensorAccel;
    Sensor sensorLinAccel;
    Sensor sensorGravity;
    Sensor sensorLight;
    Boolean isRecordingData;

    ArrayList<SensorData> sensorData;

    StringBuilder sb = new StringBuilder();

    Timer timer;

    float[] valuesAccel = new float[3];
    float[] valuesAccelMotion = new float[3];
    float[] valuesAccelGravity = new float[3];
    float[] valuesLinAccel = new float[3];
    float[] valuesGravity = new float[3];
    float valuesLight;
    float prevStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvText = (TextView) findViewById(R.id.tvText);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorLinAccel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        isRecordingData = false;
        sensorData = new ArrayList<>();
        prevStep = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensorAccel,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener, sensorLinAccel,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener, sensorGravity,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener, sensorLight,
                SensorManager.SENSOR_DELAY_UI);

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showInfo();

                        if (Math.abs(Math.abs(valuesLinAccel[1]) - Math.abs(prevStep))>1 )
                            System.out.println(valuesLinAccel[1]);
                        prevStep = valuesLinAccel[1];
                    }
                });
            }
        };
        timer.schedule(task, 0, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
        timer.cancel();
    }

    String format(float values[]) {
        return String.format("%1$.1f\t\t%2$.1f\t\t%3$.1f", values[0], values[1],
                values[2]);
    }

    void showInfo() {
        sb.setLength(0);
        sb.append("Accelerometer: " + format(valuesAccel))
                .append("\n\nAccel motion: " + format(valuesAccelMotion))
                .append("\nAccel gravity : " + format(valuesAccelGravity))
                .append("\n\nLin accel : " + format(valuesLinAccel))
                .append("\nGravity : " + format(valuesGravity));
        tvText.setText(sb);
    }

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    for (int i = 0; i < 3; i++) {
                        valuesAccel[i] = event.values[i];
                        valuesAccelGravity[i] = (float) (0.1 * event.values[i] + 0.9 * valuesAccelGravity[i]);
                        valuesAccelMotion[i] = event.values[i] - valuesAccelGravity[i];
                    }
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    for (int i = 0; i < 3; i++) {
                        valuesLinAccel[i] = event.values[i];

                        SensorData sd = new SensorData(event.accuracy, event.timestamp, event.sensor, event.values);
                        sensorData.add(sd);
                    }
                    break;
                case Sensor.TYPE_GRAVITY:
                    for (int i = 0; i < 3; i++) {
                        valuesGravity[i] = event.values[i];
                    }
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    //TODO  Сделать сравнение "нашего" подсчёта шагов со встроенным

                    break;
                case Sensor.TYPE_LIGHT:
                    valuesLight = event.values[0];
                    //System.out.println(event.values[0]);
//                    SensorData sd = new SensorData(event.accuracy, event.timestamp, event.sensor, event.values);
//                    sensorData.add(sd);
//                    if (sd.accuracy == 3) {
//                        //tvText.setText("" + sd.data[0]);
//                        if (sd.data[0] > 1000)
//                            System.out.println("Light is on");
//                        else
//                            System.out.println("Light is off");
//                    }
                    //System.out.println(sensorData.size());
                    break;
            }

        }

    };

    public void onClickSensList(View view) {
//        String res = "";
//        for (SensorData sd : sensorData)
//            res += sd.toString() + '\n';
        System.out.println(sensorData.size());
        //tvText.setText(""+sensorData.size());
    }
}
