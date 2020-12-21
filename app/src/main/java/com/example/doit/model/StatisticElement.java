package com.example.doit.model;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;

public class StatisticElement implements Serializable {
    String titleElement;
    long value;
    String stringImageUri;
    int position;

    public StatisticElement(String titleElement, long value){
        this.titleElement = titleElement;
        this.value = value;
        this.stringImageUri = null;
        this.position = 0;
    }

    public StatisticElement(String titleElement, long value, String stringImageUri, int position) {
        this.titleElement = titleElement;
        this.value = value;
        this.stringImageUri = stringImageUri;
        this.position = position;
    }

    public String getTitleElement() {
        return titleElement;
    }

    public void setTitleElement(String titleElement) {
        this.titleElement = titleElement;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getStringImageUri() {
        return stringImageUri;
    }

    public void setStringImageUri(String stringImageUri) {
        this.stringImageUri = stringImageUri;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
