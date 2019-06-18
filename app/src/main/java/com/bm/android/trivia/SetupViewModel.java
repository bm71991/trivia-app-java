package com.bm.android.trivia;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

/*Primarily used for communication between SetupFragment and PickerFragment*/
public class SetupViewModel extends AndroidViewModel {
    private String dialogType;
    private MutableLiveData<String> categoryChosen;
    private MutableLiveData<String> difficultyChosen;
    private final String[] categoryOptions = new String[] {
        "Books",
        "Film",
        "TV",
        "Music"
    };
    private final String[] difficultyOptions = new String[] {
        "easy",
        "medium",
        "hard"
    };

    public SetupViewModel(@NonNull Application application) {
        super(application);
        dialogType = "";
        categoryChosen = new MutableLiveData<>();
        difficultyChosen = new MutableLiveData<>();
    }

    public String getCategoryString(int index)    {
        return categoryOptions[index];
    }

    public void resetChosenOptions() {
        difficultyChosen = new MutableLiveData<>();
        categoryChosen = new MutableLiveData<>();
    }

    public String getDifficultyString(int index)    {
        return difficultyOptions[index];
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
        return categoryOptions;
    }

    public String[] getDifficultyOptions() {
        return difficultyOptions;
    }
}
