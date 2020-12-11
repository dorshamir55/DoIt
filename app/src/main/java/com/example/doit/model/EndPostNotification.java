package com.example.doit.model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class EndPostNotification {

    private String postedUserId;
    private String questionText;
    private Date endingPostDate;  // ending post date

    @PrimaryKey
    @NonNull
    private String id; // Document id (EXCLUDED)

    public EndPostNotification(String postedUserId, String questionText, Date endingPostDate) {
        this.postedUserId = postedUserId;
        this.questionText = questionText;
        this.endingPostDate = endingPostDate;
    }

    public <T extends EndPostNotification> T withId(String id) {
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

    public String getPostedUserId() {
        return postedUserId;
    }

    public void setPostedUserId(String postedUserId) {
        this.postedUserId = postedUserId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Date getEndingPostDate() {
        return endingPostDate;
    }

    public void setEndingPostDate(Date endingPostDate) {
        this.endingPostDate = endingPostDate;
    }
}
