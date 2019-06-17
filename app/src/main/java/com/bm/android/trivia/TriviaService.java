package com.bm.android.trivia;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TriviaService {
    @GET("/api.php")
    Single<TriviaResult> getQuizResults(
            @Query("amount") int amount,
            @Query("category") int category,
            @Query("difficulty") String difficulty,
            @Query("type") String type);
}
