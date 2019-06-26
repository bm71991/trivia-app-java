package com.bm.android.trivia.user_access;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bm.android.trivia.R;

import androidx.fragment.app.Fragment;

public class SignupSuccessFragment extends Fragment {
    private Button loginButton;
    private SignupSuccessFragmentCallback mCallback;

    public interface SignupSuccessFragmentCallback {
        void onSelectLogin();
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (SignupSuccessFragmentCallback) context;
    }

    public static SignupSuccessFragment newInstance()  {
        return new SignupSuccessFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.signup_success_fragment, container, false);

        loginButton = view.findViewById(R.id.back_to_login_button);
        loginButton.setOnClickListener(v -> {
            mCallback.onSelectLogin();
        });
        return view;
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }
}