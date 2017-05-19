package com.angelectro.shaker;

import android.app.Application;
import android.graphics.Shader;

import com.angelectro.shakerdetection.Shaker;



public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Shaker.initialize(this);
    }
}
