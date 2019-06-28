package com.bm.android.trivia.game.viewmodels;

import android.app.Application;

import com.bm.android.trivia.game.viewmodels.SetupViewModel;

public class BestPlayersViewModel extends SetupViewModel {

    public BestPlayersViewModel(Application app) {
        super(app);
    }

    @Override
    public String [] getCategoryOptions()   {
        return new String[] {
                "Books",
                "Film",
                "TV",
                "Music",
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
}
