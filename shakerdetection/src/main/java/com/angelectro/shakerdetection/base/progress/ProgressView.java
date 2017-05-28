package com.angelectro.shakerdetection.base.progress;

public interface ProgressView {
    void showProgressBar();

    void hideProgressBar();

    void showError(String error);
}
