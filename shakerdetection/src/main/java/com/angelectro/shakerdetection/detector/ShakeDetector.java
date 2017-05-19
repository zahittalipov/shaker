package com.angelectro.shakerdetection.detector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import java.util.ArrayList;
import java.util.Iterator;

public class ShakeDetector implements SensorEventListener {
    private static final float DEFAULT_THRESHOLD_ACCELERATION = 2.0F;
    private static final int DEFAULT_THRESHOLD_SHAKE_NUMBER = 3;
    private static final int INTERVAL = 200;
    private static SensorManager mSensorManager;
    private static ShakeDetector mSensorEventListener;
    private ShakeDetector.OnShakeListener mShakeListener;
    private ArrayList<ShakeDetector.SensorBundle> mSensorBundles;
    private Object mLock;
    private float mThresholdAcceleration;
    private int mThresholdShakeNumber;

    public static boolean create(Context context, ShakeDetector.OnShakeListener listener) {
        if(context == null) {
            throw new IllegalArgumentException("Context must not be null");
        } else {
            if(mSensorManager == null) {
                mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
            }

            mSensorEventListener = new ShakeDetector(listener);
            return mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(1), 1);
        }
    }

    public static boolean start() {
        return mSensorManager != null && mSensorEventListener != null?mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(1), 1):false;
    }

    public static void stop() {
        if(mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }

    }

    public static void destroy() {
        mSensorManager = null;
        mSensorEventListener = null;
    }

    public static void updateConfiguration(float sensibility, int shakeNumber) {
        mSensorEventListener.setConfiguration(sensibility, shakeNumber);
    }

    private ShakeDetector(ShakeDetector.OnShakeListener listener) {
        if(listener == null) {
            throw new IllegalArgumentException("Shake listener must not be null");
        } else {
            this.mShakeListener = listener;
            this.mSensorBundles = new ArrayList();
            this.mLock = new Object();
            this.mThresholdAcceleration = 2.0F;
            this.mThresholdShakeNumber = 3;
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        ShakeDetector.SensorBundle sensorBundle = new ShakeDetector.SensorBundle(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], sensorEvent.timestamp);
        Object var3 = this.mLock;
        synchronized(this.mLock) {
            if(this.mSensorBundles.size() == 0) {
                this.mSensorBundles.add(sensorBundle);
            } else if(sensorBundle.getTimestamp() - ((ShakeDetector.SensorBundle)this.mSensorBundles.get(this.mSensorBundles.size() - 1)).getTimestamp() > 200L) {
                this.mSensorBundles.add(sensorBundle);
            }
        }

        this.performCheck();
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private void setConfiguration(float sensibility, int shakeNumber) {
        this.mThresholdAcceleration = sensibility;
        this.mThresholdShakeNumber = shakeNumber;
        Object var3 = this.mLock;
        synchronized(this.mLock) {
            this.mSensorBundles.clear();
        }
    }

    private void performCheck() {
        Object var1 = this.mLock;
        synchronized(this.mLock) {
            int[] vector = new int[]{0, 0, 0};
            int[][] matrix = new int[][]{{0, 0}, {0, 0}, {0, 0}};
            Iterator arr$ = this.mSensorBundles.iterator();

            while(arr$.hasNext()) {
                ShakeDetector.SensorBundle len$ = (ShakeDetector.SensorBundle)arr$.next();
                if(len$.getXAcc() > this.mThresholdAcceleration && vector[0] < 1) {
                    vector[0] = 1;
                    ++matrix[0][0];
                }

                if(len$.getXAcc() < -this.mThresholdAcceleration && vector[0] > -1) {
                    vector[0] = -1;
                    ++matrix[0][1];
                }

                if(len$.getYAcc() > this.mThresholdAcceleration && vector[1] < 1) {
                    vector[1] = 1;
                    ++matrix[1][0];
                }

                if(len$.getYAcc() < -this.mThresholdAcceleration && vector[1] > -1) {
                    vector[1] = -1;
                    ++matrix[1][1];
                }

                if(len$.getZAcc() > this.mThresholdAcceleration && vector[2] < 1) {
                    vector[2] = 1;
                    ++matrix[2][0];
                }

                if(len$.getZAcc() < -this.mThresholdAcceleration && vector[2] > -1) {
                    vector[2] = -1;
                    ++matrix[2][1];
                }
            }

            int[][] var14 = matrix;
            int var15 = matrix.length;

            for(int i$ = 0; i$ < var15; ++i$) {
                int[] axis = var14[i$];
                int[] arr$1 = axis;
                int len$1 = axis.length;

                for(int i$1 = 0; i$1 < len$1; ++i$1) {
                    int direction = arr$1[i$1];
                    if(direction < this.mThresholdShakeNumber) {
                        return;
                    }
                }
            }

            this.mShakeListener.OnShake();
            this.mSensorBundles.clear();
        }
    }

    private class SensorBundle {
        private final float mXAcc;
        private final float mYAcc;
        private final float mZAcc;
        private final long mTimestamp;

        public SensorBundle(float XAcc, float YAcc, float ZAcc, long timestamp) {
            this.mXAcc = XAcc;
            this.mYAcc = YAcc;
            this.mZAcc = ZAcc;
            this.mTimestamp = timestamp;
        }

        public float getXAcc() {
            return this.mXAcc;
        }

        public float getYAcc() {
            return this.mYAcc;
        }

        public float getZAcc() {
            return this.mZAcc;
        }

        public long getTimestamp() {
            return this.mTimestamp;
        }

        public String toString() {
            return "SensorBundle{mXAcc=" + this.mXAcc + ", mYAcc=" + this.mYAcc + ", mZAcc=" + this.mZAcc + ", mTimestamp=" + this.mTimestamp + '}';
        }
    }

    public interface OnShakeListener {
        void OnShake();
    }
}
