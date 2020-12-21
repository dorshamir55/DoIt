package com.example.doit.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.example.doit.model.AnswerFireStore;
import com.example.doit.model.AnswerInPost;
import com.example.doit.model.AnswerInQuestion;
import com.example.doit.model.Consumer;
import com.example.doit.model.QuestionFireStore;
import com.example.doit.model.QuestionPostData;
import com.example.doit.model.StatisticElement;
import com.example.doit.model.UserData;

import java.util.List;

public interface IMainViewModel {
    LiveData<List<QuestionPostData>> getPostsLiveData();
    LiveData<List<QuestionPostData>> getMyPostsLiveData(String userID);
    void loadAds(Runnable onFinish);
    void deletePost(QuestionPostData questionPostData);
    void getListOfQuestions(Consumer<List<QuestionFireStore>> consumerList);
    void getTopQuestions(Consumer<List<QuestionFireStore>> consumerList, int topQuestion);
    void getTopUsersInPosts(Consumer<List<UserData>> topUsersPostsConsumer, int topUsersPosts);
    void getTopUsersInVotes(Consumer<List<UserData>> topUsersVotesConsumer, int topUsersVotes);
    void getListOfAnswers(Consumer<List<AnswerFireStore>> consumerList, List<AnswerInQuestion> answerInQuestions);
    void getAllUsers(Consumer<List<UserData>> consumerList);
    void getUsersByIds(List<AnswerInPost> answersInPost, Consumer<List<UserData>> consumerList);
    void voteOnPost(String id, String currentUserId, List<AnswerInPost> answersInPost, List<String> votedQuestionPostsIdList, int votedPosition);
    void stopPosting(String id);
    void incrementAnswerWins(String questionID, List<String> winners);
    void getCurrentUserData(String uid, Consumer<UserData> userConsumer);
    void getAllAccountImages(Consumer<List<Uri>> uriConsumer);
    void decrementAmountOfChosenQuestionInQuestionPost(String questionID);
    void deleteQuestionPostIdFromUser(String questionPostID, String userID, List<String> postedQuestionPostsIdList);
    void searchMyPostsAndRun(Consumer<List<QuestionPostData>> consumerList, String userID);
    void decrementVotesOfVoters(String questionPostID, List<AnswerInPost> answersInPost);
    void prepareStatistics(Consumer<List<StatisticElement>> statisticConsumer, List<StatisticElement> data);

}

