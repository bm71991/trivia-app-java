package com.bm.android.trivia.user_access;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bm.android.trivia.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;

public class LoginSuccessFragment extends Fragment {
    private Button mStartGameButton;
    private Button mLogoutButton;
    private LoginSuccessFragmentCallback mCallback;
    private FirebaseAuth mAuth;

    public interface LoginSuccessFragmentCallback  {
        void onSetupNewGame();
        void onLogoutUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (LoginSuccessFragmentCallback) context;
    }

    public static LoginSuccessFragment newInstance()  {
        return new LoginSuccessFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.login_success_fragment, container, false);
        mStartGameButton = view.findViewById(R.id.start_game);
        mLogoutButton = view.findViewById(R.id.log_out);

        mStartGameButton.setOnClickListener(v -> mCallback.onSetupNewGame());
        mLogoutButton.setOnClickListener(v -> mCallback.onLogoutUser());
        return view;
    }
}
