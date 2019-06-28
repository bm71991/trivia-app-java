package com.bm.android.trivia;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bm.android.trivia.game.BestPlayersFragment;
import com.bm.android.trivia.game.GameFragment;
import com.bm.android.trivia.game.SetupFragment;
import com.bm.android.trivia.game.SummaryFragment;
import com.bm.android.trivia.user_access.LoginFragment;
import com.bm.android.trivia.user_access.LoginSuccessFragment;
import com.bm.android.trivia.user_access.SignupFragment;
import com.bm.android.trivia.user_access.SignupSuccessFragment;
import com.bm.android.trivia.user_access.WelcomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TriviaActivity extends AppCompatActivity implements
        SetupFragment.SetupFragmentCallback,
        GameFragment.GameFragmentCallback,
        SummaryFragment.SummaryFragmentCallback,
        WelcomeFragment.WelcomeFragmentCallback,
        SignupFragment.SignupFragmentCallback,
        SignupSuccessFragment.SignupSuccessFragmentCallback,
        LoginFragment.LoginFragmentCallback,
        LoginSuccessFragment.LoginSuccessFragmentCallback   {

    private FragmentManager fm;
    private FirebaseAuth mAuth;
    private ActionBar mActionBar;

    protected void addFirstFragment(Fragment fragment)   {
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    private void replaceFragment(Fragment fragment)  {
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void addPreviousToBackStack(Fragment fragment) {
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        mAuth = FirebaseAuth.getInstance();
        fm = getSupportFragmentManager();
        mActionBar = getSupportActionBar();

        // UI of Fragment already inflated in Fragment
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        /* returns null if this is the first time onCreate is being called */
        if (fragment == null)   {
            if (mAuth.getCurrentUser() == null) {
                /* if user is not logged in, go to Welcome Page */
                addFirstFragment(WelcomeFragment.newInstance());
                mActionBar.setTitle(R.string.welcome);
            } else {
                /* if user is logged in, go to game setup page */
                addFirstFragment(SetupFragment.newInstance());
                setPlayerNameInActionbar();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)    {
        MenuInflater inflater = getMenuInflater();

        if (mAuth.getCurrentUser() != null) {
            inflater.inflate(R.menu.trivia_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_log_out):
                onLogoutUser();
            break;
            case (R.id.menu_top_players):
                onSelectTopThreeItem();
            break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /* Game callbacks */
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
        setPlayerNameInActionbar();
        invalidateOptionsMenu();
    }

    /*AUTH callbacks */
    /* used in SignupSuccessFragment */
    @Override
    public void onSelectLogin() {
        replaceFragment(LoginFragment.newInstance());
    }

    /* used in WelcomeFragment */
    @Override
    public void onSelectSignup() {
        replaceFragment(SignupFragment.newInstance());
    }

    /* used in SignupFragment */
    @Override
    public void onSignupSuccess()   {
        replaceFragment(SignupSuccessFragment.newInstance());
    }

    /* used in LoginFragment */
    @Override
    public void onLoginSuccess()    {
        mActionBar.setTitle(R.string.actionbar_login_success);
        replaceFragment(LoginSuccessFragment.newInstance());
    }
    /* end of callbacks */

    /* callbacks for selecting menu items */
    private void onSelectTopThreeItem() {
        addPreviousToBackStack(BestPlayersFragment.newInstance());
    }

    /* Used in this Activity (menuItem) and in LoginSuccessFragment */
    public void onLogoutUser()    {
        mAuth.signOut();
        toWelcomeFragment();
        mActionBar.setTitle(R.string.welcome);
        invalidateOptionsMenu();
    }

    private void toWelcomeFragment() {
        replaceFragment(WelcomeFragment.newInstance());
    }

    private void setPlayerNameInActionbar()  {
        String usernameTitleString = getString(R.string.game_actionbar_title,
                mAuth.getCurrentUser().getDisplayName());
        mActionBar.setTitle(usernameTitleString);
    }
}

