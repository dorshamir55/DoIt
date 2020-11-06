package com.example.doit.model;

import com.esotericsoftware.kryo.NotNull;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties  // For Firebase deserialization
public class QuestionFireStore implements Serializable {
    public static final String TABLE_NAME = "questions";

    @NotNull
    private String id;
    private QuestionLanguage en;
    private QuestionLanguage he;
    private List<AnswerInQuestion> answersInQuestion;

    public QuestionFireStore() {

    }

    public QuestionFireStore(QuestionLanguage en, QuestionLanguage he, List<AnswerInQuestion> answersInQuestion) {
        this.en = en;
        this.he = he;
        this.answersInQuestion = answersInQuestion;
    }

    public <T extends QuestionFireStore> T withId(String id) {
        this.id = id;
        return (T)this;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public QuestionLanguage getEn() {
        return en;
    }

    public void setEn(QuestionLanguage en) {
        this.en = en;
    }

    public QuestionLanguage getHe() {
        return he;
    }

    public void setHe(QuestionLanguage he) {
        this.he = he;
    }

    public List<AnswerInQuestion> getAnswersInQuestion() {
        return answersInQuestion;
    }

    public void setAnswersInQuestion(List<AnswerInQuestion> answersInQuestion) {
        this.answersInQuestion = answersInQuestion;
    }
}
