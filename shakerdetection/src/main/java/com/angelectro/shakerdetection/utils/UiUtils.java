package com.angelectro.shakerdetection.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.angelectro.shakerdetection.R;

import java.util.List;

import rx.functions.Action1;

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

    public static void showError(Context context, String message) {
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText(R.string.text_button_ok)
                .onPositive((dialog, which) -> {
                })
                .show();
    }


    public static <T> MaterialDialog showSingleChoiceDialog(Context context, @StringRes int title, final List<T> channels, final Action1<T> channelAction) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .items(channels)
                .itemsCallbackSingleChoice(0, (dialog, itemView, which, text) -> {
                    channelAction.call(channels.get(which));
                    return true;
                })
                .cancelable(false)
                .positiveText(R.string.text_button_ok)
                .show();
    }

    public static void showToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
