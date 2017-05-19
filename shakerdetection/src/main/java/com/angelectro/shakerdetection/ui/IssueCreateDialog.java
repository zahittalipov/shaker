package com.angelectro.shakerdetection.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.angelectro.shakerdetection.R;

/**
 * Created by Загит Талипов on 28.03.2017.
 */

public class IssueCreateDialog extends DialogFragment {

    ImageView mImageView;
    private Bitmap mBitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_issue, null);
        mImageView = (ImageView) inflate.findViewById(R.id.screenshot_image);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setView(inflate)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        mImageView.setImageDrawable(new BitmapDrawable(mBitmap));
        return builder.create();
    }

    public void show(Activity activity, Bitmap bitmap) {
        mBitmap = bitmap;
        this.show(activity.getFragmentManager(), "");
    }
}
