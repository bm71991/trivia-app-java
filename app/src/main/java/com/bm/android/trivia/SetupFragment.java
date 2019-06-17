package com.bm.android.trivia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SetupFragment extends Fragment {
    private SetupFragmentCallback mCallback;
    private Button submitButton;

    /*Hosting activity must implement - see onAttach*/
    public interface SetupFragmentCallback {
        void onStartGame();
    }

    public static SetupFragment newInstance()  {
        return new SetupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.setup_fragment, container, false);
        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> mCallback.onStartGame());
        return view;
    }

    @Override
    public void onResume()  {
        super.onResume();

    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (SetupFragmentCallback) context;
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }
}
