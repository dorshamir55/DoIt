package com.example.doit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Statistic implements Serializable {
    private String title;
    private List<StatisticElement> competitors;

    public Statistic(String title) {
        this.title = title;
        this.competitors = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<StatisticElement> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<StatisticElement> competitors) {
        this.competitors = competitors;
    }
}
