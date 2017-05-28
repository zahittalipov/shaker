package com.angelectro.shakerdetection.data.interactor.internal;

import android.support.annotation.CallSuper;

import com.angelectro.shakerdetection.data.model.ErrorModel;
import com.angelectro.shakerdetection.utils.ResponseUtils;
import com.annimon.stream.Optional;
import com.google.gson.Gson;


public abstract class DomainDefaultSubscriber<T> extends rx.Subscriber<T> {


    Gson gson;

    @CallSuper
    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(T t) {

    }

    protected final Optional<ErrorModel> fetchError(Throwable throwable) {
        return ResponseUtils.fetchResponseError(throwable)
                .flatMap(body -> Optional.empty());
    }

    @Override
    public final void onError(Throwable throwable) {
        log(throwable);
        handleError(throwable);
    }

    protected abstract void handleError(Throwable throwable);

    protected abstract void onError(ErrorModel errorTitle);

    protected abstract void log(Throwable throwable);
}