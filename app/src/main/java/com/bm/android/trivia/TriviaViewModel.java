package com.bm.android.trivia;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TriviaViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<TriviaQuestion>> mQuestions;
    private String TAG = "TriviaViewModel";

    public TriviaViewModel(@NonNull Application application) {
        super(application);
        mQuestions = new MutableLiveData<>();
    }

    public LiveData<ArrayList<TriviaQuestion>> getQuestions() {
        return mQuestions;

    }

    public void clearQuestions()   {
        mQuestions = new MutableLiveData<>();
    }

    public void loadQuestions() {
        String url = "https://opentdb.com/";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        TriviaService service = retrofit.create(TriviaService.class);
        Single<TriviaResult> testObservable = service.getQuizResults(
                5, 11,"easy", "multiple");
        testObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TriviaResult>() {
                    @Override
                    public void onSubscribe(Disposable d)   {

                    }
                    @Override
                    public void onSuccess(TriviaResult triviaResult) {
                        mQuestions.postValue(triviaResult.getQuestions());
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }
}
