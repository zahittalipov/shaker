package com.angelectro.shakerdetection.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.action.Action;
import com.angelectro.shakerdetection.data.model.Channel;

import java.util.List;

/**
 * Created by Загит Талипов on 28.03.2017.
 */

public class UiUtils {


    public static MaterialDialog showProgressDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .progress(true, 0)
                .cancelable(false)
                .content(R.string.text_please_wait).show();
    }

    public static void showAuthError(Context context) {
        new MaterialDialog.Builder(context)
                .content(R.string.text_auth_error)
                .positiveText(R.string.text_button_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    public static MaterialDialog showSingleChoiceChannelDialog(Context context, final List<Channel> channels, final Action<Channel> channelAction) {
        return new MaterialDialog.Builder(context)
                .title(R.string.select_channel)
                .items(channels)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        channelAction.call(channels.get(which));
                        return true;
                    }
                })
                .cancelable(false)
                .positiveText(R.string.text_button_ok)
                .show();
    }

    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
