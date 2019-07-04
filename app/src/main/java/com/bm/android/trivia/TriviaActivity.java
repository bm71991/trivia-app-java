package com.bm.android.trivia;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bm.android.trivia.game.BestPlayersDialog;
import com.bm.android.trivia.game.BestPlayersFragment;
import com.bm.android.trivia.game.BestPlayersPickerFragment;
import com.bm.android.trivia.game.GameFragment;
import com.bm.android.trivia.game.SetupFragment;
import com.bm.android.trivia.game.SummaryFragment;
import com.bm.android.trivia.game.viewmodels.BestPlayersViewModel;
import com.bm.android.trivia.game.viewmodels.GameViewModel;
import com.bm.android.trivia.game.viewmodels.SetupViewModel;
import com.bm.android.trivia.game.viewmodels.SummaryViewModel;
import com.bm.android.trivia.user_access.DeleteAccountFragment;
import com.bm.android.trivia.user_access.DeletedAccountFragment;
import com.bm.android.trivia.user_access.LoginFragment;
import com.bm.android.trivia.user_access.LoginSuccessFragment;
import com.bm.android.trivia.user_access.SignupFragment;
import com.bm.android.trivia.user_access.SignupSuccessFragment;
import com.bm.android.trivia.user_access.UserAccessViewModel;
import com.bm.android.trivia.user_access.WelcomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

public class TriviaActivity extends AppCompatActivity implements
        SetupFragment.SetupFragmentCallback,
        GameFragment.GameFragmentCallback,
        SummaryFragment.SummaryFragmentCallback,
        WelcomeFragment.WelcomeFragmentCallback,
        SignupFragment.SignupFragmentCallback,
        SignupSuccessFragment.SignupSuccessFragmentCallback,
        LoginFragment.LoginFragmentCallback,
        LoginSuccessFragment.LoginSuccessFragmentCallback,
        BestPlayersFragment.BestPlayersFragmentCallback,
        DeletedAccountFragment.DeletedAccountCallback,
        DeleteAccountFragment.DeleteAccountCallback {

    private FragmentManager fm;
    private FirebaseAuth mAuth;
    private ActionBar mActionBar;
    private SetupViewModel setupVm;
    private BestPlayersViewModel bestPlayersVm;
    private UserAccessViewModel userAccessVm;
    private GameViewModel gameVm;
    private SummaryViewModel summaryVm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        mAuth = FirebaseAuth.getInstance();
        fm = getSupportFragmentManager();
        mActionBar = getSupportActionBar();

        setupVm = ViewModelProviders.of(this).get(SetupViewModel.class);
        bestPlayersVm = ViewModelProviders.of(this)
                .get(BestPlayersViewModel.class);
        userAccessVm =  ViewModelProviders.of(this)
                .get(UserAccessViewModel.class);
        gameVm = ViewModelProviders.of(this)
                .get(GameViewModel.class);
        summaryVm = ViewModelProviders.of(this)
                .get(SummaryViewModel.class);

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
            case (R.id.menu_delete_account):
                onSelectDeleteAccount();
            break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /* Game callbacks */
    /*SetupFragment Callbacks*/
    public void onStartGame()  {
        gameVm.resetGame();
        replaceFragment(GameFragment.newInstance());
    }

    /*GameFragment Callbacks*/
    public void onFinishGame()  {
        replaceFragment(SummaryFragment.newInstance());
    }

    /*SummaryFragment/LoginSuccessFragment Callback*/
    public void onSetupNewGame()    {
        setupVm.clearDialogType();
        setupVm.resetChosenOptions();
        summaryVm.resetFinalScore();

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
        userAccessVm.initSignupLiveData();
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
        userAccessVm.initLoginLiveData();
        replaceFragment(LoginSuccessFragment.newInstance());
    }

    /*used in BestPlayersFragment */
    @Override
    public void onStartPicker() {
        String PICKER_DIALOG_TAG = "pickerDialogTag";
        BestPlayersPickerFragment pickerDialog = BestPlayersPickerFragment.newInstance();
        pickerDialog.show(fm, PICKER_DIALOG_TAG);
    }

    @Override
    public void onStartBestPlayersDialog()  {
        String BEST_PLAYERS_DIALOG_TAG = "bestPlayersDialogTag";
        bestPlayersVm.initBestPlayersViewModel();
        BestPlayersDialog bestPlayersDialog = BestPlayersDialog.newInstance();
        bestPlayersDialog.show(fm, BEST_PLAYERS_DIALOG_TAG);
    }

    /*used in DeleteAccountFragment*/
    @Override
    public void onDeleteAccount()   {
        replaceFragment(DeletedAccountFragment.newInstance());
    }
    /* end of callbacks */

    /* for selecting menu items */
    private void onSelectTopThreeItem() {
        //clear previous viewmodel state if it exists
        bestPlayersVm.clearDialogType();
        bestPlayersVm.resetChosenOptions();
        addPreviousToBackStack(BestPlayersFragment.newInstance());
    }

    /* Used in this Activity (menuItem) and in LoginSuccessFragment */
    public void onLogoutUser()    {
        mAuth.signOut();
        toWelcomeFragment();
        mActionBar.setTitle(R.string.welcome);
        invalidateOptionsMenu();
    }

    public void toWelcomeFragment() {
        replaceFragment(WelcomeFragment.newInstance());
    }

    private void setPlayerNameInActionbar()  {
        String usernameTitleString = getString(R.string.game_actionbar_title,
                mAuth.getCurrentUser().getDisplayName());
        mActionBar.setTitle(usernameTitleString);
    }

    private void onSelectDeleteAccount()    {
        userAccessVm.initDeleteAccountLiveData();
        addPreviousToBackStack(DeleteAccountFragment.newInstance());
    }
}

