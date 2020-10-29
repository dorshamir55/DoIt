package com.example.doit.model;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties  // For Firebase deserialization
@Entity(tableName = QuestionPostData.TABLE_NAME)
public class QuestionPostData {
    public static final String TABLE_NAME = "questions_post";

    @PrimaryKey
    @NonNull
    private String id; // Document id (EXCLUDED)

    private String postedUserId; // User document id which is also the authentication id.

    private HashMap<Integer, String> question;

    private HashMap<Integer, String> answers;

    @ServerTimestamp
    private Date updateDate;  // update (also created) date - from Firebase

    private boolean isRemoved;

    public QuestionPostData() {

    }

    public QuestionPostData(String postedUserId, HashMap<Integer, String> question, HashMap<Integer, String> answers, Date updateDate, boolean isRemoved) {
        this.postedUserId = postedUserId;
        this.question = question;
        this.answers = answers;
        this.updateDate = updateDate;
        this.isRemoved = isRemoved;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getPostedUserId() {
        return postedUserId;
    }

    public void setPostedUserId(String postedUserId) {
        this.postedUserId = postedUserId;
    }

    public HashMap<Integer, String> getQuestion() {
        return question;
    }

    public void setQuestion(HashMap<Integer, String> question) {
        this.question = question;
    }

    public HashMap<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<Integer, String> answers) {
        this.answers = answers;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }
}
