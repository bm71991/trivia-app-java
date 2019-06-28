package com.bm.android.trivia.game;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

/*Primarily used for communication between SetupFragment and SetupPickerFragment*/
public class SetupViewModel extends AndroidViewModel {
    private String dialogType;
    private MutableLiveData<String> categoryChosen;
    private MutableLiveData<String> difficultyChosen;

    public SetupViewModel(@NonNull Application application) {
        super(application);
        dialogType = "";
        categoryChosen = new MutableLiveData<>();
        difficultyChosen = new MutableLiveData<>();
    }

    public String getCategoryString(int index)    {
        String[] categories = getCategoryOptions();
        return categories[index];
    }

    public String getDifficultyString(int index)    {
        String[] difficulties = getDifficultyOptions();
        return difficulties[index];
    }

    public void resetChosenOptions() {
        difficultyChosen = new MutableLiveData<>();
        categoryChosen = new MutableLiveData<>();
    }



    public void setCategory(String category)    {
        categoryChosen.setValue(category);
    }

    public void setDifficulty(String difficulty)    {
        difficultyChosen.setValue(difficulty);
    }

    public LiveData<String> getCategoryChosen() {
        return categoryChosen;
    }

    public LiveData<String> getDifficultyChosen() {
        return difficultyChosen;
    }

    public String getDialogType()   {
        return dialogType;
    }

    public void setDialogType(String type)  {
        dialogType = type;
    }

    public void clearDialogType()   {
        dialogType = "";
    }

    public String[] getCategoryOptions() {
        return new String[] {
                "Books",
                "Film",
                "TV",
                "Music"
        };
    }

    public String[] getDifficultyOptions() {
        return new String[] {
                "easy",
                "medium",
                "hard"
        };
    }
}
