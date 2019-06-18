package com.bm.android.trivia;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity implements
        SetupFragment.SetupFragmentCallback,
        GameFragment.GameFragmentCallback,
        SummaryFragment.SummaryFragmentCallback
{
    private FragmentManager fm;
    private final int FRAGMENT_CONTAINER_ID = R.id.fragment_container;
    private GameViewModel mGameViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        fm = getSupportFragmentManager();
        setContentView(R.layout.fragment_container);

        if (savedInstanceState != null) {
        }

        // UI of Fragment already inflated in Fragment - returns null if
        //activity is just instantiated.
        Fragment fragment = fm.findFragmentById(FRAGMENT_CONTAINER_ID);

        if (fragment == null)   {
            addFirstFragment(SetupFragment.newInstance());
        }
    }

    private void addFirstFragment(Fragment fragment)    {
        fm.beginTransaction()
                .add(FRAGMENT_CONTAINER_ID, fragment)
                .commit();
    }

    private void replaceFragment(Fragment fragment)  {
        fm.beginTransaction()
                .replace(FRAGMENT_CONTAINER_ID, fragment)
                .commit();
    }

    /*SetupFragment Callbacks*/
    public void onStartGame()  {
        replaceFragment(GameFragment.newInstance());
    }

    /*GameFragment Callbacks*/
//    public LiveData<ArrayList<TriviaQuestion>> getTriviaQuestions() {
//        return mGameViewModel.getQuestions();
//    }

    public void onFinishGame()  {
        replaceFragment(SummaryFragment.newInstance());
    }

    /*SummaryFragment Callbacks*/
    public void onSetupNewGame()    {
        mGameViewModel.clearQuestions();
        replaceFragment(SetupFragment.newInstance());
    }
}

