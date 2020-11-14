package com.projekt.tdp028.models.firebase;

import java.util.List;
import java.util.Map;

public class User {
    String uid;
    List<String> created_polls;
    Map<String, String> answered_polls;

    public User(String uid, List<String> created_polls, Map<String, String> answered_polls) {
        this.uid = uid;
        this.created_polls = created_polls;
        this.answered_polls = answered_polls;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getCreated_polls() {
        return created_polls;
    }

    public void setCreated_polls(List<String> created_polls) {
        this.created_polls = created_polls;
    }

    public Map<String, String> getAnswered_polls() {
        return answered_polls;
    }

    public void setAnswered_polls(Map<String, String> answered_polls) {
        this.answered_polls = answered_polls;
    }
}
