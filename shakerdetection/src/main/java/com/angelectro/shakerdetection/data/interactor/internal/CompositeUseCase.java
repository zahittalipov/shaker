package com.angelectro.shakerdetection.data.interactor.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.exceptions.Exceptions;

public class CompositeUseCase {
    private Set<UseCase> subscriptions;
    private volatile boolean unsubscribed;

    public CompositeUseCase() {
    }

    public void add(final UseCase s) {
        if (s.isUnsubscribed()) {
            return;
        }
        if (!unsubscribed) {
            synchronized (this) {
                if (!unsubscribed) {
                    if (subscriptions == null) {
                        subscriptions = new HashSet<>(4);
                    }
                    subscriptions.add(s);
                    return;
                }
            }
        }
        // call after leaving the synchronized block so we're not holding a lock while executing this
        s.unsubscribe();
    }

    public void clear() {
        if (!unsubscribed) {
            Collection<UseCase> unsubscribe;
            synchronized (this) {
                if (unsubscribed || subscriptions == null) {
                    return;
                } else {
                    unsubscribe = subscriptions;
                    subscriptions = null;
                }
            }
            unsubscribeFromAll(unsubscribe);
        }
    }

    private static void unsubscribeFromAll(Collection<UseCase> useCases) {
        if (useCases == null) {
            return;
        }
        List<Throwable> es = null;
        for (UseCase s : useCases) {
            try {
                s.unsubscribe();
            } catch (Throwable e) {
                if (es == null) {
                    es = new ArrayList<>();
                }
                es.add(e);
            }
        }
        Exceptions.throwIfAny(es);
    }

    public void unsubscribe() {
        if (!unsubscribed) {
            Collection<UseCase> unsubscribe;
            synchronized (this) {
                if (unsubscribed) {
                    return;
                }
                unsubscribed = true;
                unsubscribe = subscriptions;
                subscriptions = null;
            }
            // we will only get here once
            unsubscribeFromAll(unsubscribe);
        }
    }
}