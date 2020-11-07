package com.example.doit.model;

import androidx.room.Entity;

//@Entity(tableName = "answer_in_post")
public class AnswerInPost extends AnswerFireStore{
    private int votes;

    public AnswerInPost() {

    }

    public AnswerInPost(AnswerLanguage en, AnswerLanguage he) {
        super(en, he);
        this.votes = 0;
    }

    public AnswerInPost(String id, AnswerLanguage en, AnswerLanguage he) {
        super(id, en, he);
        this.votes = 0;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
