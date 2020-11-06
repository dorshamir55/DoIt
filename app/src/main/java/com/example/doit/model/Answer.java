package com.example.doit.model;

import androidx.room.Embedded;

import com.esotericsoftware.kryo.NotNull;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties  // For Firebase deserialization
public class Answer implements Serializable {
    public static final String TABLE_NAME = "answers";

    @NotNull
    private String id;
    @Embedded
    private AnswerLanguage en;
    @Embedded
    private AnswerLanguage he;

    public Answer() {

    }

    public Answer(AnswerLanguage en, AnswerLanguage he) {
        this.en = en;
        this.he = he;
    }

    public Answer(String id, AnswerLanguage en, AnswerLanguage he) {
        this.id = id;
        this.en = en;
        this.he = he;
    }

    public <T extends Answer> T withId(String id) {
        this.id = id;
        return (T)this;
    }

    @Exclude
    public String getAnswerID() {
        return id;
    }
    @Exclude
    public void setAnswerID(String id) {
        this.id = id;
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

