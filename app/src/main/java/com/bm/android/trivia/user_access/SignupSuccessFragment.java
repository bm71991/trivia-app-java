package com.bm.android.trivia.user_access;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.android.trivia.R;

import androidx.fragment.app.Fragment;

public class SignupSuccessFragment extends Fragment {
    public interface SignupSuccessFragmentInterface   {
        void onSelectLogin();
        void onSelectMainPage();
    }

    public static SignupSuccessFragment newInstance()  {
        return new SignupSuccessFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.signup_success_fragment, container, false);

        return view;
    }
}