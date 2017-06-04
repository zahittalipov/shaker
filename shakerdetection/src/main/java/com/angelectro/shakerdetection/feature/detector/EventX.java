package com.angelectro.shakerdetection.feature.detector;

/**
 * Created by Загит Талипов on 01.06.2017.
 */

public class EventX {
    private long time;
    private float value;

    public EventX(long time, float value) {
        this.time = time;
        this.value = value;
    }

    public long getTime() {

        return time;
    }

    public float getValue() {
        return value;
    }
}
