package com.projekt.tdp028.models;

import java.util.ArrayList;

public class Poll {

    String title;
    int chosenId;
    ArrayList<Option> options;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChosenId() {
        return chosenId;
    }

}
