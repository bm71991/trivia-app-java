package com.bm.android.trivia.user_access;

import android.app.Application;
import android.widget.Toast;

import com.bm.android.trivia.game.FirestoreRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class UserAccessViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isEmailVerified;
    private MutableLiveData<String> hadErrorSigningIn;
    private MutableLiveData<Boolean> isSuccessfulSigningUp;
    private MutableLiveData<String> hadErrorSigningUp;
//    private MutableLiveData<Boolean> addedUserToDatabase;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private boolean queryFlag;
    private FirestoreRepository mFirestoreRepository;

    public UserAccessViewModel(@NonNull Application application) {
        super(application);
        initLoginLiveData();
        initSignupLiveData();
        queryFlag = false;
        mFirestoreRepository = new FirestoreRepository();
    }

    public void initLoginLiveData() {
        isEmailVerified = new MutableLiveData<>();
        hadErrorSigningIn = new MutableLiveData<>();
    }

    public void initSignupLiveData()   {
        isSuccessfulSigningUp = new MutableLiveData<>();
        hadErrorSigningUp = new MutableLiveData<>();
//        addedUserToDatabase = new MutableLiveData<>();
    }

    public boolean isQuerying() {
        return queryFlag;
    }

    public void setQueryFlag(boolean bool)   {
        queryFlag = bool;
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())    {
                            /* if username/password is correct && they are email verified:*/
                            if (mAuth.getCurrentUser().isEmailVerified())   {
                                isEmailVerified.setValue(true);
                            }   else {
                                /*if username/password is correct && not email verified:*/
                                mAuth.signOut();
                                isEmailVerified.setValue(false);
                            }
                            /* every other error */
                        } else  {
                            hadErrorSigningIn.setValue(task.getException().getMessage());
                        }
                    }
                });
    }

    public void signUp(String email, String password, String username)  {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())    {
                        mAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(task1 -> {
                                    setUserName(username);
                                });
                    } else {
                        hadErrorSigningUp.setValue(task.getException().getMessage());
                    }
                });
    }

    private void setUserName(String username)  {
        UserProfileChangeRequest usernameUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();
        mAuth.getCurrentUser().updateProfile(usernameUpdate)
                .addOnCompleteListener(task -> {
                      mFirestoreRepository.addUserToDb(hadErrorSigningUp, isSuccessfulSigningUp);
                });
    }

    public LiveData<Boolean> getIsEmailVerified() {
        return isEmailVerified;
    }

    public LiveData<String> getHadErrorSigningIn() {
        return hadErrorSigningIn;
    }

    public LiveData<Boolean> getIsSuccessfulSigningUp() {
        return isSuccessfulSigningUp;
    }

    public LiveData<String> getHadErrorSigningUp()   {
        return hadErrorSigningUp;
    }
}
