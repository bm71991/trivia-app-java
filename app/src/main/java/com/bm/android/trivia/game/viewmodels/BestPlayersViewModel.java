package com.bm.android.trivia.game.viewmodels;

import android.app.Application;
import android.util.Log;

import com.bm.android.trivia.game.BestPlayer;
import com.bm.android.trivia.game.FirestoreRepository;
import com.bm.android.trivia.game.viewmodels.SetupViewModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BestPlayersViewModel extends SetupViewModel {
    private boolean isLoadingResults;
    private boolean hasCalledDb;
    private FirestoreRepository mFirestoreRepository;
    private MutableLiveData<List<BestPlayer>> bestPlayersData;

    public BestPlayersViewModel(Application app) {
        super(app);
        isLoadingResults = false;
        hasCalledDb = false;
        mFirestoreRepository = new FirestoreRepository();
        bestPlayersData = new MutableLiveData<>();
    }

    public void setIsLoadingResults(boolean bool)   {
        isLoadingResults = bool;
    }

    public LiveData<List<BestPlayer>> getBestPlayers()   {
        return bestPlayersData;
    }

    public boolean isLoadingResults()   {
        return isLoadingResults;
    }

    public void setHasCalledDb(boolean bool)    {
        hasCalledDb = bool;
    }

    public boolean hasCalledDb()    {
        return hasCalledDb;
    }

    @Override
    public String [] getCategoryOptions()   {
        return new String[] {
                "books",
                "film",
                "TV",
                "music",
                "all"
        };
    }

    @Override
    public String[] getDifficultyOptions()  {
        return new String[] {
                "easy",
                "medium",
                "hard",
                "all"
        };
    }

    public String getCategoryStringChosen() {
        return getCategoryChosen().getValue();
    }

    public String getDifficultyStringChosen()   {
        return getDifficultyChosen().getValue();
    }


    public void getTopPlayers() {
        hasCalledDb = true;
        isLoadingResults = true;
        mFirestoreRepository.getBestPlayers(getCategoryStringChosen(),
                getDifficultyStringChosen(),
                bestPlayersData);
    }
}
