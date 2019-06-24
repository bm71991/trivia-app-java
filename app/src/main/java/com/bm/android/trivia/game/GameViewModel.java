package com.bm.android.trivia.game;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.bm.android.trivia.api_call.TriviaQuestion;
import com.bm.android.trivia.api_call.TriviaResult;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*Used by GameFragment and for communication between SetupFragment and GameFragment */
public class GameViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<TriviaQuestion>> mQuestions;
    private String category;
    private int correctAnswerCount;
    private MutableLiveData<Integer> currentQuestionIndex;
    private String difficulty;
    private WebServiceRepository mWebServiceRepository;
    private boolean hasCalledLoadQuestions;

    public GameViewModel(@NonNull Application application) {
        super(application);
        mQuestions = new MutableLiveData<>();
        currentQuestionIndex = new MutableLiveData<>();
        currentQuestionIndex.setValue(0);
        correctAnswerCount = 0;
        hasCalledLoadQuestions = false;
        mWebServiceRepository = new WebServiceRepository();
    }

    public boolean hasCalledLoadQuestions() {
        return hasCalledLoadQuestions;
    }

    public void loadQuestionsHasBeenCalled() {
        hasCalledLoadQuestions = true;
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
        hasCalledLoadQuestions = false;
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
        category = categoryName;
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
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }
}
