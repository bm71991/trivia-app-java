package com.bm.android.trivia.user_access;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bm.android.trivia.R;
import androidx.fragment.app.Fragment;

public class WelcomeFragment extends Fragment {
    private Button signupButton;
    private Button loginButton;
    private WelcomeFragmentCallback mCallback;

    public interface WelcomeFragmentCallback {
        void onSelectSignup();
        void onSelectLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.welcome_fragment, container, false);
        signupButton = view.findViewById(R.id.sign_up);
        loginButton = view.findViewById(R.id.log_in);

        signupButton.setOnClickListener(v -> mCallback.onSelectSignup());

        loginButton.setOnClickListener(v -> mCallback.onSelectLogin());
        return view;
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (WelcomeFragmentCallback) context;
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }

    public static WelcomeFragment newInstance()  {
        return new WelcomeFragment();
    }
}
