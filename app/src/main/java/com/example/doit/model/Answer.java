package com.example.doit.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties  // For Firebase deserialization
public class Answer {
    private String answerID;
    private AnswerLanguage en;
    private AnswerLanguage he;

    public Answer() {

    }

    public Answer(String answerID, AnswerLanguage en, AnswerLanguage he) {
        this.answerID = answerID;
        this.en = en;
        this.he = he;
    }

    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public AnswerLanguage getEn() {
        return en;
    }

    public void setEn(AnswerLanguage en) {
        this.en = en;
    }

    public AnswerLanguage getHe() {
        return he;
    }

    public void setHe(AnswerLanguage he) {
        this.he = he;
    }
}

