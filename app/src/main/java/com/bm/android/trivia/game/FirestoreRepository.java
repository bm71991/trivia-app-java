package com.bm.android.trivia.game;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class FirestoreRepository    {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private MutableLiveData<Integer> userDocumentCreationCount;
    private final ArrayList<String> perfectScoresCollections = new ArrayList<>(
            Arrays.asList(
            "booksPerfectScores",
            "filmPerfectScores",
            "TVPerfectScores",
            "musicPerfectScores"
            ));

    public FirestoreRepository()    {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userDocumentCreationCount = new MutableLiveData<>();
    }

    public void addUserToDb(MutableLiveData<Boolean> addedUserToDatabase,
                            MutableLiveData<Boolean> isSuccessfulSigningUp)   {
        String userEmail = mAuth.getCurrentUser().getEmail();
        userDocumentCreationCount.setValue(0);

        for (String collectionName : perfectScoresCollections)  {
            DocumentReference newDocument = db.collection(collectionName)
                    .document(userEmail);

            newDocument.set(new initialPerfectScoreData()).addOnCompleteListener(task -> {
                if (task.isSuccessful())    {
                    incrementDocumentCreationCount();
                    if (allDocumentsAreCreated())   {
                        addedUserToDatabase.setValue(true);
                        isSuccessfulSigningUp.setValue(true);
                        userDocumentCreationCount = new MutableLiveData<>();
                        mAuth.signOut();
                    }
                } else {
                    addedUserToDatabase.setValue(false);
                    userDocumentCreationCount = new MutableLiveData<>();
                    mAuth.signOut();
                }
            });
        }
    }

    private void incrementDocumentCreationCount()  {
        Integer previousCreationCount = userDocumentCreationCount.getValue();
        userDocumentCreationCount.setValue(previousCreationCount + 1);
    }

    private boolean allDocumentsAreCreated() {
        return userDocumentCreationCount.getValue() == perfectScoresCollections.size();
    }

    public void incrementPerfectScoreCount(String category, String difficulty,
                                           MutableLiveData<Boolean> dbOperationWasSuccessful)  {
        String collectionName = category + "PerfectScores";
        String categoryKey = difficulty + "Count";
        db.collection(collectionName).document(mAuth.getCurrentUser().getEmail())
                .update(categoryKey, FieldValue.increment(1)).addOnCompleteListener(dbOperation -> {
                    if (dbOperation.isSuccessful())    {
                        dbOperationWasSuccessful.setValue(true);
                    } else {
                        dbOperationWasSuccessful.setValue(false);
                    }
                });
    }

    private class initialPerfectScoreData    {
        public int easyCount;
        public int mediumCount;
        public int hardCount;

        initialPerfectScoreData()    {
            easyCount = 0;
            mediumCount = 0;
            hardCount = 0;
        }
    }
}
