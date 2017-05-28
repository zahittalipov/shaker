package com.angelectro.shakerdetection.data.interactor.internal;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public abstract class UseCase<T> {
    private Subscription subscription = Subscriptions.empty();


    protected abstract Observable<T> buildUseCaseObservable();

    @SuppressWarnings("unchecked")
    public void execute(DomainDefaultSubscriber subscriber) {
        unsubscribe();
        subscription = buildUseCaseObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private int attemptCount = 0;

    public boolean isUnsubscribed() {
        return subscription.isUnsubscribed();
    }
}