package com.angelectro.shakerdetection.utils;

import com.annimon.stream.Optional;

import java.io.IOException;

import retrofit2.HttpException;

public class ResponseUtils {
    public static Optional<String> fetchResponseError(Throwable throwable) {
        return Optional.of(throwable)
                .filter(ex -> ex instanceof HttpException)
                .map(ex -> ((HttpException) ex))
                .map(ex -> ((HttpException) ex).response().errorBody())
                .flatMap(errorBody -> {
                    try {
                        return Optional.ofNullable(errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Optional.empty();
                    }
                });
    }
}