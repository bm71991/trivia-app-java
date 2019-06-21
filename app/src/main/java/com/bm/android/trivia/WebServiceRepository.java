package com.bm.android.trivia;

import com.bm.android.trivia.api_call.ApiClient;
import com.bm.android.trivia.api_call.TriviaResult;
import com.bm.android.trivia.api_call.TriviaService;

import io.reactivex.Single;

public class WebServiceRepository {
    private final int QUESTION_AMOUNT = 5;
    private final String QUESTION_TYPE = "multiple";
    private final ApiClient apiClient;

    public WebServiceRepository()   {
        apiClient = ApiClient.getInstance();
    }

    public Single<TriviaResult> getTriviaResult(String category, String difficulty)    {
        int categoryNumber = mapCategoryNameToInt(category);
        TriviaService service = apiClient.getTriviaClient().create(TriviaService.class);
        return service.getQuizResults
                (QUESTION_AMOUNT, categoryNumber, difficulty, QUESTION_TYPE);
    }

    /*mapping of category names to numbers which are used in the trivia API call */
    private int mapCategoryNameToInt(String categoryName)   {
        switch (categoryName)   {
            case "Books":
                return 10;
            case "Film":
                return 11;
            case "Music":
                return 12;
            case "TV":
                return 14;
            default:
                /* -1 means some sort of error occured */
                return -1;
        }
    }
}

