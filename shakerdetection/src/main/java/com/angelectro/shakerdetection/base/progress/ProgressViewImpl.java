package com.angelectro.shakerdetection.base.progress;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.utils.UiUtils;

public class ProgressViewImpl extends FrameLayout implements ProgressView {


    protected FrameLayout mProgressContainer;

    public ProgressViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            mProgressContainer = (FrameLayout) getRootView().findViewById(R.id.progressContainer);
        }
    }

    @Override
    public void showProgressBar() {
        mProgressContainer.setVisibility(VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressContainer.setVisibility(GONE);
    }

    @Override
    public void showError(String error) {
        UiUtils.showError(getContext(), error);
    }
}
