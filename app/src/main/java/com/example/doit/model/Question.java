package com.example.doit.model;

import androidx.room.Embedded;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties  // For Firebase deserialization
public class Question {
    private String questionID;
    @Embedded(prefix = "question_")
    private QuestionLanguage en;
    @Embedded(prefix = "question_")
    private QuestionLanguage he;

    public Question() {

    }

    public Question(String questionID, QuestionLanguage en, QuestionLanguage he) {
        this.questionID = questionID;
        this.en = en;
        this.he = he;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
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
}
