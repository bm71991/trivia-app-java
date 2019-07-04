package com.bm.android.trivia.game.viewmodels;

import android.app.Application;
import android.util.Log;

import com.bm.android.trivia.game.BestPlayer;
import com.bm.android.trivia.game.FirestoreRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BestPlayersViewModel extends SetupViewModel {
    private boolean isLoadingResults;
    private boolean hasCalledDb;
    private FirestoreRepository mFirestoreRepository;
    private MutableLiveData<List<BestPlayer>> bestPlayersMutableLiveData;
    private List<BestPlayer> mBestPlayers;

    public BestPlayersViewModel(Application app) {
        super(app);
        initBestPlayersViewModel();
        mFirestoreRepository = new FirestoreRepository();
    }

    // make init data method - call in trivia activity before BestPlayersFragment is instantiated
    public void initBestPlayersViewModel()  {
        isLoadingResults = false;
        hasCalledDb = false;
        bestPlayersMutableLiveData = new MutableLiveData<>();
        mBestPlayers = new ArrayList<>();
    }

    public void setIsLoadingResults(boolean bool)   {
        isLoadingResults = bool;
    }

    public LiveData<List<BestPlayer>> getBestPlayersLiveData()   {
        return bestPlayersMutableLiveData;
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

    public void storeBestPlayers(List<BestPlayer> bestPlayerData)   {
        List<BestPlayer> parsedBestPlayers = parseBestPlayers(bestPlayerData);
        mBestPlayers.clear();
        mBestPlayers.addAll(parsedBestPlayers);
    }

    public List<BestPlayer> getBestPlayers()    {
        return mBestPlayers;
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
                bestPlayersMutableLiveData);
    }

    public List<BestPlayer> parseBestPlayers(List<BestPlayer> playerData)  {
        boolean noMorePlayersWithWins = false;
        List<BestPlayer> parsedBestPlayers;
        int playerIndex = 0;

        while (playerIndex < playerData.size() && !noMorePlayersWithWins)
        {
            int winCount = getPlayerWinCount(playerData.get(playerIndex),
                    getDifficultyStringChosen());
            Log.i("test", "wincount = " + winCount);
            if (winCount == 0)  {
                noMorePlayersWithWins = true;
            } else {
                playerIndex++;
            }
        }
        /* Get a sublist of playerData which only has nonzero win counts (subList's first
        argument is inclusive, the second is inclusive
         */
        Log.i("test", "playerIndex = " + playerIndex);
        parsedBestPlayers = playerData.subList(0, playerIndex);
        return parsedBestPlayers;
    }

    public static int getPlayerWinCount(BestPlayer playerToDisplay, String difficulty)   {
        int winCount = -1;
        switch (difficulty)    {
            case ("easy"):
                winCount = playerToDisplay.easyCount;
                break;
            case ("medium"):
                winCount = playerToDisplay.mediumCount;
                break;
            case ("hard"):
                winCount = playerToDisplay.hardCount;
                break;
        }
        return winCount;
    }
}
