package com.angelectro.shakerdetection.data.interactor.slack;

import com.angelectro.shakerdetection.data.ApiFactory;
import com.angelectro.shakerdetection.data.SlackService;
import com.angelectro.shakerdetection.data.interactor.internal.UseCase;
import com.angelectro.shakerdetection.data.model.slack.SlackFileResponse;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferences;
import com.angelectro.shakerdetection.data.preferences.ApplicationPreferencesImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;


public class FileUploadUseCase extends UseCase<SlackFileResponse> {

    public static final String JPEG_FORMAT = ".jpeg";
    public static final String TEXT_FORMAT = ".txt";
    private static String MULTIPART_FORM_DATA = "multipart/form-data";
    private byte[] data;
    private String format;
    private SlackService mSlackService;
    private ApplicationPreferences mPreferences;
    public FileUploadUseCase() {
        mPreferences = new ApplicationPreferencesImpl();
        mSlackService = ApiFactory.getSlackService(mPreferences);
    }

    public FileUploadUseCase setData(byte[] data, String format) {
        this.data = data;
        this.format = format;
        return this;
    }

    @Override
    protected Observable<SlackFileResponse> buildUseCaseObservable() {
        String filename = UUID.randomUUID().toString() + format;
        RequestBody file = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), data);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("file\"; filename=\"" + filename + "\"", file);
        return mSlackService.upload(mPreferences.getSlackAuthModel().get().getToken(), map, filename, "test", "text");
    }
}
