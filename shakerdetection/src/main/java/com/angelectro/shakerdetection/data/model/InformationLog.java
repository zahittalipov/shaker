package com.angelectro.shakerdetection.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Загит Талипов on 09.04.2017.
 */

public class InformationLog implements Parcelable {

    private String mVersionName;
    private String mLogs;
    private String mAndroidVersion;
    private String mDeviceName;
    private String mPackageName;
    public static Bitmap mBitmap;

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    private String mComment;
    private String mProjectName;

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectName(String projectName) {
        mProjectName = projectName;
    }

    public String getLogs() {
        return mLogs;
    }

    private String mVersionCode;

    public String getAndroidVersion() {
        return mAndroidVersion;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public InformationLog(Bitmap bitmap, String versionCode, String versionName, String logs, String androidVersion, String deviceName, String packageName, String projectName) {
        if (mBitmap != null)
            mBitmap.recycle();
        mBitmap = bitmap;
        this.mVersionCode = versionCode;
        this.mVersionName = versionName;
        mLogs = logs;
        mAndroidVersion = androidVersion;
        mDeviceName = deviceName;
        mPackageName = packageName;
        mProjectName = projectName;
    }

    protected InformationLog(Parcel in) {
        mVersionCode = in.readString();
        mVersionName = in.readString();
        mLogs = in.readString();
        mAndroidVersion = in.readString();
        mDeviceName = in.readString();
        mPackageName = in.readString();
        mProjectName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mVersionCode);
        dest.writeString(mVersionName);
        dest.writeString(mLogs);
        dest.writeString(mAndroidVersion);
        dest.writeString(mDeviceName);
        dest.writeString(mPackageName);
        dest.writeString(mProjectName);
    }

    public static final Creator<InformationLog> CREATOR = new Creator<InformationLog>() {
        @Override
        public InformationLog createFromParcel(Parcel in) {
            return new InformationLog(in);
        }

        @Override
        public InformationLog[] newArray(int size) {
            return new InformationLog[size];
        }
    };

    public String getVersionCode() {
        return mVersionCode;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public InformationLog(Bitmap bitmap) {
        mBitmap = bitmap;
    }


    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
