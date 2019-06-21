package com.bm.android.trivia.api_call;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private final static String TRIVIA_URL  = "https://opentdb.com/";

    private static ApiClient apiClient = null;

    public static ApiClient getInstance()   {
        if (apiClient == null)   {
            apiClient = new ApiClient();
        }
        return apiClient;
    }

    public Retrofit getTriviaClient()   {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TRIVIA_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }
}
