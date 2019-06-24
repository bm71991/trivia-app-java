package com.bm.android.trivia.game;

import com.bm.android.trivia.BaseActivity;

public class TriviaActivity extends BaseActivity implements
        SetupFragment.SetupFragmentCallback,
        GameFragment.GameFragmentCallback,
        SummaryFragment.SummaryFragmentCallback {

    @Override
    protected void addFirstFragment()   {
        fm.beginTransaction()
                .add(FRAGMENT_CONTAINER_ID, SetupFragment.newInstance())
                .commit();
    }

    /*SetupFragment Callbacks*/
    public void onStartGame()  {
        replaceFragment(GameFragment.newInstance());
    }

    /*GameFragment Callbacks*/
    public void onFinishGame()  {
        replaceFragment(SummaryFragment.newInstance());
    }

    /*SummaryFragment Callbacks*/
    public void onSetupNewGame()    {
        replaceFragment(SetupFragment.newInstance());
    }
}

