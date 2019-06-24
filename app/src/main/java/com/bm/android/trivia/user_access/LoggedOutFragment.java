package com.bm.android.trivia.user_access;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.android.trivia.R;

import androidx.fragment.app.Fragment;

public class LoggedOutFragment extends Fragment {

    public static LoggedOutFragment newInstance()  {
        return new LoggedOutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.logged_out_fragment, container, false);

        return view;
    }
}

