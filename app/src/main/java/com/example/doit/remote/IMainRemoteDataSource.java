package com.example.doit.remote;

import android.net.Uri;

import com.example.doit.model.AnswerFireStore;
import com.example.doit.model.AnswerInPost;
import com.example.doit.model.AnswerInQuestion;
import com.example.doit.model.Consumer;
import com.example.doit.model.QuestionFireStore;
import com.example.doit.model.QuestionPostData;
import com.example.doit.model.StatisticElement;
import com.example.doit.model.UserData;

import java.util.Date;
import java.util.List;

public interface IMainRemoteDataSource {
    void fetchQuestionsPosts(Date fromDate, Consumer<List<QuestionPostData>> consumer);
    void removePost(String id, Runnable onFinish);
    void fetchAllQuestions(Consumer<List<QuestionFireStore>> consumerList);
    void fetchTopQuestions(Consumer<List<QuestionFireStore>> consumerList, int topQuestion);
    void fetchTopUsersInPosts(Consumer<List<UserData>> topUsersPostsConsumer, int topUsersPosts);
    void fetchTopUsersInVotes(Consumer<List<UserData>> topUsersVotesConsumer, int topUsersVotes);
    void fetchAllUsers(Consumer<List<UserData>> consumerList);
    void fetchUsersByIds(List<AnswerInPost> answersInPost, Consumer<List<UserData>> consumerList);
    void fetchAnswers(Consumer<List<AnswerFireStore>> consumerList, List<AnswerInQuestion> answerInQuestions);
    void updateVotes(String questionId, String currentUserId, List<AnswerInPost> answersInPost, List<String> votedQuestionPostsIdList, int votedPosition, Runnable onFinish);
    void endingPostDate(String id, Runnable onFinish);
    void changeUpdatedToFalse(String id);
    void incrementAnswerWins(String questionID, List<String> winners);
    void getCurrentUserData(String uid, Consumer<UserData> userConsumer);
    void fetchAllAccountImages(Consumer<List<Uri>> uriConsumer);
    void decrementAmountOfChosenQuestionInQuestionPost(String questionID);
    void deleteQuestionPostIdFromUser(String questionPostID, String userID, List<String> postedQuestionPostsIdList, Runnable onFinish);
    void decrementVotesOfVoters(String questionPostID, List<AnswerInPost> answersInPost);
    void prepareStatistics(Consumer<List<StatisticElement>> statisticConsumer, List<StatisticElement> data);

}
