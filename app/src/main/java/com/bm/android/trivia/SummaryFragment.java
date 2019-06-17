package com.bm.android.trivia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SummaryFragment extends Fragment {
    private SummaryFragmentCallback mCallback;
    private Button mRestartButton;

    /*Hosting activity must implement - see onAttach*/
    public interface SummaryFragmentCallback    {
        void onSetupNewGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.summary_fragment, container, false);
        mRestartButton = view.findViewById(R.id.restart_button);

        mRestartButton.setOnClickListener(v -> mCallback.onSetupNewGame());
        return view;
    }

    @Override
    public void onAttach(Context context)   {
        super.onAttach(context);
        mCallback = (SummaryFragment.SummaryFragmentCallback) context;
    }

    @Override
    public void onDetach()   {
        super.onDetach();
        mCallback = null;
    }

    public static SummaryFragment newInstance()   {
        return new SummaryFragment();
    }


}
