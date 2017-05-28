package com.angelectro.shakerdetection.data.preferences;

import android.support.annotation.Nullable;

import com.annimon.stream.Optional;

public interface ObjectPref<T> {

    T get();

    default Optional<T> getOptional() {
        return Optional.ofNullable(get());
    }

    void set(@Nullable T value);
}
