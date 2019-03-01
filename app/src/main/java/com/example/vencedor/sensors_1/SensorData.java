package com.example.vencedor.sensors_1;

import android.hardware.Sensor;

import java.util.Arrays;

public class SensorData {
    int accuracy;
    long timestamp;
    Sensor sensor;
    float[] data;

    public SensorData(int accuracy, long timestamp, Sensor sensor, float[] data) {
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        this.sensor = sensor;
        this.data = data;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "accuracy=" + accuracy +
                ", timestamp=" + timestamp +
                ", sensor=" + sensor +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
