package com.angelectro.shakerdetection.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.angelectro.shakerdetection.data.model.SlackAuthData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;



public class PrefUtils {


    public static class Slack {
        private static final String PREF_AUTH_DATA = "pref_auth_data";
        private static final String PREF_CHANNEL_ID = "channel_id";

        public static void saveAuthData(@NonNull Context context, @NonNull SlackAuthData data) {
            Gson gson = new Gson();
            getEditor(context).putString(PREF_AUTH_DATA, gson.toJson(data)).commit();
        }

        public static void saveChannel(@NonNull Context context, @NonNull String channelId) {
            getEditor(context).putString(PREF_CHANNEL_ID, channelId).commit();
        }

        @Nullable
        public static String getChannelId(@NonNull Context context) {
            return getPreferences(context).getString(PREF_CHANNEL_ID, null);
        }

        @Nullable
        public static SlackAuthData getAuthData(Context context) {
            String data = getPreferences(context).getString(PREF_AUTH_DATA, null);
            if (data == null)
                return null;
            Gson gson = new Gson();
            Type type = new TypeToken<SlackAuthData>() {
            }.getType();
            return gson.fromJson(data, type);
        }

        private static SharedPreferences getPreferences(Context context) {
            return context.getSharedPreferences(Slack.class.getSimpleName(), Context.MODE_PRIVATE);
        }

        private static SharedPreferences.Editor getEditor(Context context) {
            return getPreferences(context).edit();
        }
    }
}
