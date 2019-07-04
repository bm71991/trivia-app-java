package com.bm.android.trivia.user_access;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.bm.android.trivia.game.FirestoreRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private MutableLiveData<Boolean> successfulDeletingAccount;
    private MutableLiveData<String> errorDeletingAccount;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private boolean queryFlag;
    private boolean deletingAccountFlag;
    private FirestoreRepository mFirestoreRepository;

    public UserAccessViewModel(@NonNull Application application) {
        super(application);
        initLoginLiveData();
        initSignupLiveData();
        initDeleteAccountLiveData();
        mFirestoreRepository = new FirestoreRepository();
    }

    public void initLoginLiveData() {
        queryFlag = false;
        isEmailVerified = new MutableLiveData<>();
        hadErrorSigningIn = new MutableLiveData<>();
    }

    public void initSignupLiveData()   {
        queryFlag = false;
        isSuccessfulSigningUp = new MutableLiveData<>();
        hadErrorSigningUp = new MutableLiveData<>();
    }

    public void initDeleteAccountLiveData() {
        deletingAccountFlag = false;
        successfulDeletingAccount = new MutableLiveData<>();
        errorDeletingAccount = new MutableLiveData<>();
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
                    if (task.isSuccessful()){
                        //add documents to firestore
                        addDbRecords();
                    } else {
                        hadErrorSigningUp.setValue(task.getException().getMessage());
                    }
                });
    }

    private void addDbRecords() {
        mFirestoreRepository.addUserToDb()
                .addOnSuccessListener(aVoid -> {
                    mAuth.signOut();
                    isSuccessfulSigningUp.setValue(true);
                })
                .addOnFailureListener(e -> {
                    mAuth.signOut();
                    hadErrorSigningUp.setValue("could not add user to the database");
                });
    }

    public void deleteAccount(String email, String password)    {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();

        // Prompt the user to re-provide their sign-in credentials
        mCurrentUser.reauthenticate(credential)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())    {
                    //delete user auth data from firebase
                    deleteDbRecords();

                } else {
                    task.getException().toString();
                    errorDeletingAccount.setValue(task.getException().getMessage());
                }
            }
        });
    }

    private void deleteDbRecords()  {
        //delete user-related documents from firestore db
        mFirestoreRepository.removeUserFromDb()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //delete user from firebase db
                        mAuth.getCurrentUser().delete();
                        mAuth.signOut();
                        successfulDeletingAccount.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        errorDeletingAccount.setValue(e.getMessage());
                    }
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

    public LiveData<Boolean> getSuccessfulDeletingAccount() {
        return successfulDeletingAccount;
    }

    public LiveData<String> getErrorDeletingAccount() {
        return errorDeletingAccount;
    }

    public boolean isDeletingAccount()  {
        return deletingAccountFlag;
    }

    public void setDeletingAccountFlag(boolean bool)    {
        deletingAccountFlag = bool;
    }
}
