An Android application written in Java which allows registered users to play a trivia game. The difficulty and subject matter of the quiz can be configured by the player. The quiz is organized into three packages: user_access, game and api_call. 

**user_access** - uses the Firebase authentication API to provide account creation, login, and deletion. These methods are organized into a repository class which are only called at the ViewModel level (UserAccessViewModel). This ensures that network calls in progress during an orientation change will not be interrupted. UserAccessViewModel handles the success or failure of the tasks these methods return by propagating the result to the Fragment level via MutableLiveData/LiveData. After the account has been created on the Firebase authentication level, a document is added to each of the collections in Firestore pertaining to perfect scores: tv, books, film and music. In keep with NoSQL best practices, I tried to keep the database structure as denormalized/flat as possible. 

**game and api_call** - game.SetupFragment allows the user to select the category and difficulty of the quiz. These preferences are saved in the GameViewModel so that they can be used in the API call. GameViewModel.loadQuestions() calls api_call.WebServiceRespository.getTriviaResult(category, difficulty), which uses a retrofit instance to call the trivia API with the parameters entered by the user. loadQuestions() subscribes to the observable returned by getTriviaResult(). The returned JSON is mapped to an api_call.TriviaResult POJO using GSON. TriviaResult is a container for a list of TriviaQuestion objects, which contain all relevant information about each question.
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Once onSuccess is called by the subscriber, ArrayList<TriviaQuestion> is set as the new value for the MutableLiveData field in GameViewModel, which then propagates to the LiveData in GameFragment. Since this LiveData is being observed for changes, it triggers the method playGame(). Data regarding the current game are stored in GameViewModel so that it can persist over configuration changes. If the user gets a perfect score, GameViewModel.incrementPerfectScoreCount() is called. This method is a wrapper for a repository call to update the document which is recording how many perfect scores that particular player has. The player can choose to play another game, which results in the application redirecting to SetupFragment to restart the whole process.
<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;BestPlayersFragment is similar in layout to SetupFragment, but it enables the user to see what players had the highest number of perfect scores for a particular difficulty and category. BestPlayersViewModel makes the appropriate query by calling FirestoreRepository.getBestPlayers. This result is mapped to a list of BestPlayer objects, which contain the email address and number of wins for each top player. This data is then displayed in BestPlayersDialogFragment. 
