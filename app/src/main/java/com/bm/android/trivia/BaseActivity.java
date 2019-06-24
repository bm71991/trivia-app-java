package com.bm.android.trivia;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class BaseActivity extends AppCompatActivity {
    protected FragmentManager fm;
    protected final int FRAGMENT_CONTAINER_ID = R.id.fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();
        setContentView(R.layout.fragment_container);

        // UI of Fragment already inflated in Fragment - returns null if
        //activity is just instantiated.
        Fragment fragment = fm.findFragmentById(FRAGMENT_CONTAINER_ID);

        if (fragment == null)   {
            addFirstFragment();
        }
    }

    protected abstract void addFirstFragment();

    protected void replaceFragment(Fragment fragment)  {
        fm.beginTransaction()
                .replace(FRAGMENT_CONTAINER_ID, fragment)
                .commit();
    }
}
