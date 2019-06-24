package com.bm.android.trivia.user_access;

import com.bm.android.trivia.BaseActivity;

public class UserAccessActivity extends BaseActivity implements
        WelcomeFragment.WelcomeFragmentCallback,
        SignupFragment.SignupFragmentCallback,
        SignupSuccessFragment.SignupSuccessFragmentInterface,
        LoginFragment.LoginFragmentInterface
        /* LoginSuccessFragment.LoginSuccessFragmentInterface */ {

    @Override
    protected void addFirstFragment() {
        fm.beginTransaction()
                .add(FRAGMENT_CONTAINER_ID, WelcomeFragment.newInstance())
                .commit();
    }

    @Override
    public void onSelectLogin() {
        replaceFragment(LoginFragment.newInstance());
    }

    @Override
    public void onSelectSignup() {
        replaceFragment(SignupFragment.newInstance());
    }

    @Override
    public void onSignupSuccess()   {
        replaceFragment(SignupSuccessFragment.newInstance());
    }

    @Override
    public void onLoginSuccess()    {
        replaceFragment(LoginSuccessFragment.newInstance());
    }

    @Override
    public void onSelectMainPage()  {
        replaceFragment(WelcomeFragment.newInstance());
    }
}
