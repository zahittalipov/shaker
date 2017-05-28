package com.angelectro.shakerdetection.data.interactor.internal;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.angelectro.shakerdetection.R;
import com.angelectro.shakerdetection.Shaker;
import com.angelectro.shakerdetection.base.progress.ProgressView;
import com.angelectro.shakerdetection.data.model.ErrorModel;
import com.annimon.stream.Optional;
import com.annimon.stream.function.Consumer;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

public class PresentationDefaultSubscriber<T> extends DomainDefaultSubscriber<T> {

    private WeakReference<ProgressView> progressBarView;

    public PresentationDefaultSubscriber(ProgressView progressView) {
        this.progressBarView = new WeakReference<>(progressView);
    }

    public PresentationDefaultSubscriber(@NonNull WeakReference<ProgressView> progressBarView) {
        this.progressBarView = progressBarView;
    }

    public PresentationDefaultSubscriber() {
        progressBarView = new WeakReference<>(null);
    }

    public static <R> PresentationDefaultSubscriber<R> onPositive(Consumer<R> positiveConsumer) {
        return new PresentationDefaultSubscriber<R>() {
            @Override
            public void onNext(R r) {
                super.onNext(r);
                positiveConsumer.accept(r);
            }
        };
    }

    public static <R> PresentationDefaultSubscriber<R> onPositive(@Nullable ProgressView progressView, Consumer<R> positiveConsumer) {
        return new PresentationDefaultSubscriber<R>(progressView) {
            @Override
            public void onNext(R r) {
                super.onNext(r);
                positiveConsumer.accept(r);
            }
        };
    }

    public static <R> PresentationDefaultSubscriber<R> onPositiveNegative(@Nullable ProgressView progressView, Consumer<R> positiveConsumer, Consumer<ErrorModel> negativeConsumer) {
        return new PresentationDefaultSubscriber<R>(progressView) {
            @Override
            public void onNext(R r) {
                super.onNext(r);
                positiveConsumer.accept(r);
            }

            @Override
            protected void onError(ErrorModel errorTitle) {
                negativeConsumer.accept(errorTitle);
            }
        };
    }

    public WeakReference<ProgressView> getProgressBarView() {
        return progressBarView;
    }

    @CallSuper
    @Override
    public void onCompleted() {
        super.onCompleted();
        hideProgress();
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
        hideProgress();
    }

    @Override
    protected void handleError(Throwable throwable) {
        ErrorModel errorEntity = fetchError(throwable).orElseGet(() -> {
            String error = throwable.getCause() instanceof UnknownHostException
                    ? Shaker.context.getString(R.string.error_not_connected)
                    : Shaker.context.getString(R.string.general_error);
            return new ErrorModel(error, throwable);
        });

        onError(errorEntity);
        hideProgress();
        throwable.printStackTrace();
    }

    protected final void hideProgress() {
        Optional.ofNullable(progressBarView.get())
                .ifPresent(ProgressView::hideProgressBar);
    }

    @Override
    protected void onError(ErrorModel errorTitle) {
        Optional.ofNullable(progressBarView.get())
                .ifPresent(view -> view.showError(errorTitle.getMessage()));
    }

    @Override
    protected final void log(Throwable throwable) {
        //fixme
    }

    @CallSuper
    @Override
    public void onStart() {
        Optional.ofNullable(progressBarView.get())
                .ifPresent(ProgressView::showProgressBar);
    }
}
