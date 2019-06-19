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

/*Used by GameFragment and for communication between SetupFragment and GameFragment */
public class GameViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<TriviaQuestion>> mQuestions;
    private String TAG = "GameViewModel";
    private int category;
    private int correctAnswerCount;
    private MutableLiveData<Integer> currentQuestionIndex;
    private String difficulty;
    private final int QUESTION_AMOUNT = 5;
    private final String QUESTION_TYPE = "multiple";

    public GameViewModel(@NonNull Application application) {
        super(application);
        mQuestions = new MutableLiveData<>();
        currentQuestionIndex = new MutableLiveData<>();
        currentQuestionIndex.setValue(0);
        correctAnswerCount = 0;
    }

    public void incrementCorrectAnswerCount()   {
        correctAnswerCount++;
    }

    public int getCorrectAnswerCount()  {
        return correctAnswerCount;
    }

    public LiveData<ArrayList<TriviaQuestion>> getQuestionsLiveData() {
        return mQuestions;
    }

    public void resetGame() {
        mQuestions = new MutableLiveData<>();
        currentQuestionIndex.setValue(0);
        correctAnswerCount = 0;
    }

    public LiveData<Integer> getCurrentQuestionIndex()    {
        return currentQuestionIndex;
    }

    public void incrementCurrentQuestionIndex() {
        int newValue = currentQuestionIndex.getValue() + 1;
        currentQuestionIndex.setValue(newValue);
    }

    public void setDifficulty(String difficulty)    {
        this.difficulty = difficulty;
    }

    public void setCategory(String categoryName)    {
        category = mapCategoryNameToInt(categoryName);
    }

    /*mapping of category names to numbers which are used in the trivia API call */
    private int mapCategoryNameToInt(String categoryName)   {
        switch (categoryName)   {
            case "Books":
                return 10;
            case "Film":
                return 11;
            case "Music":
                return 12;
            case "TV":
                return 14;
            default:
                /* -1 means some sort of error occured */
                return -1;
        }
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
        Single<TriviaResult> apiObservable = service.getQuizResults(
                QUESTION_AMOUNT, category, difficulty, QUESTION_TYPE);

        apiObservable.subscribeOn(Schedulers.io())
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
