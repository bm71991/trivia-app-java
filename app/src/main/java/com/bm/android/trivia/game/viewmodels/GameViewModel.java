package com.bm.android.trivia.game.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.bm.android.trivia.api_call.TriviaQuestion;
import com.bm.android.trivia.api_call.TriviaResult;
import com.bm.android.trivia.api_call.WebServiceRepository;
import com.bm.android.trivia.game.FirestoreRepository;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GameViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<TriviaQuestion>> mQuestions;
    private String category;
    private int correctAnswerCount;
    private MutableLiveData<Integer> currentQuestionIndex;
    private String difficulty;
    private WebServiceRepository mWebServiceRepository;
    private FirestoreRepository mFirestoreRepository;
    private boolean hasCalledLoadQuestions;
    private MutableLiveData<Boolean> incrementPerfectScoreWasSuccessful;
    private boolean mCallingDbFlag;

    public GameViewModel(@NonNull Application application) {
        super(application);
        mQuestions = new MutableLiveData<>();
        currentQuestionIndex = new MutableLiveData<>();
        currentQuestionIndex.setValue(0);
        correctAnswerCount = 0;

        hasCalledLoadQuestions = false;
        mCallingDbFlag = false;

        mWebServiceRepository = new WebServiceRepository();
        incrementPerfectScoreWasSuccessful = new MutableLiveData<>();
        mFirestoreRepository = new FirestoreRepository();
    }

    public void resetGame() {
        mQuestions = new MutableLiveData<>();
        currentQuestionIndex.setValue(0);
        correctAnswerCount = 0;
        hasCalledLoadQuestions = false;
        incrementPerfectScoreWasSuccessful = new MutableLiveData<>();
    }

    public boolean isCallingDb() {
        return mCallingDbFlag;
    }

    public boolean isPerfectScore(int numberOfQuestions) {
        return correctAnswerCount / numberOfQuestions == 1;
    }

    public void incrementPerfectScoreCount(String category, String difficulty)  {
        mFirestoreRepository.incrementPerfectScoreCount(category, difficulty,
                incrementPerfectScoreWasSuccessful);
    }

    public void setDbCallFlag(boolean bool)    {
        mCallingDbFlag = bool;
    }

    public LiveData<Boolean> getIncrementScoreCountStatus()  {
        return incrementPerfectScoreWasSuccessful;
    }

    public boolean hasCalledLoadQuestions() {
        return hasCalledLoadQuestions;
    }

    public void loadQuestionsHasBeenCalled() {
        hasCalledLoadQuestions = true;
    }

    public void incrementCorrectAnswerCount() {
        correctAnswerCount++;
    }

    public int getCorrectAnswerCount()  {
        return correctAnswerCount;
    }

    public LiveData<ArrayList<TriviaQuestion>> getQuestionsLiveData() {
        return mQuestions;
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setCategory(String categoryName)    {
        category = categoryName;
    }
    public String getCategory() {
        return category;
    }

    public void loadQuestions() {
       Single<TriviaResult> apiObservable =
               mWebServiceRepository.getTriviaResult(category, difficulty);

        apiObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TriviaResult>() {
                    @Override
                    public void onSubscribe(Disposable d)   {

                    }
                    @Override
                    public void onSuccess(TriviaResult triviaResult) {
                        mQuestions.postValue(triviaResult.getQuestions());
                        Log.i("test", mQuestions + "");
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }
}
