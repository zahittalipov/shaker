package com.angelectro.shakerdetection.feature.detector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class ShakeDetector {

    private static final int THRESHOLD = 13;
    private static final int SHAKES_COUNT = 5;
    private static final int SHAKES_PERIOD = 1;

    public static void createAndStart(Context context, Action1 actionShake) {
        getSensorEvenObservable(context)
                .map(sensorEvent -> new EventX(sensorEvent.timestamp, sensorEvent.values[0]))
                .filter(xEvent -> Math.abs(xEvent.getValue()) > THRESHOLD)
                .buffer(2, 1)
                .filter(buf -> buf.get(0).getValue() * buf.get(1).getValue() < 0)
                .map(buf -> buf.get(1).getTime() / 1000000000f)
                .buffer(SHAKES_COUNT, 1)
                .filter(buf -> buf.get(SHAKES_COUNT - 1) - buf.get(0) < SHAKES_PERIOD)
                .throttleFirst(SHAKES_PERIOD, TimeUnit.SECONDS)
                .subscribe(actionShake);
    }


    private static Observable<SensorEvent> getSensorEvenObservable(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
        if (sensorList == null || sensorList.isEmpty()) {
            throw new IllegalStateException("Device has no linear acceleration sensor");
        }

        return createSensorObservable(sensorManager, sensorList.get(0));
    }

    private static Observable<SensorEvent> createSensorObservable(@NonNull SensorManager sensorManager,
                                                                  @NonNull Sensor sensor) {
        return Observable.create(subscriber -> {
            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    subscriber.onNext(sensorEvent);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
        });
    }

}
