package com.projekt.tdp028.models.firebase;

public class PollOverview {
    String pollId;
    String creator;

    String text;

    public PollOverview() {

    }
    public PollOverview(String pollId, String creator, String text) {
        this.pollId = pollId;
        this.creator = creator;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getPollId() {
        return pollId;
    }

    public String getCreator() { return creator; }

    public void setCreator(String creatorId) { this.creator = creatorId; }

}