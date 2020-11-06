package com.example.doit.remote;

import androidx.lifecycle.LiveData;

import com.example.doit.model.Answer;
import com.example.doit.model.AnswerInQuestion;
import com.example.doit.model.Consumer;
import com.example.doit.model.NewQuestion;
import com.example.doit.model.QuestionPostData;

import java.util.Date;
import java.util.List;

public interface IMainRemoteDataSource {
    public void fetchQuestionsPosts(Date fromDate, Consumer<List<QuestionPostData>> consumer);
    public void removePost(String id, Runnable onFinish);
    public void fetchAllQuestions(Consumer<List<NewQuestion>> consumerList);
    public void fetchAnswers(Consumer<List<Answer>> consumerList, List<AnswerInQuestion> answerInQuestions);
}
