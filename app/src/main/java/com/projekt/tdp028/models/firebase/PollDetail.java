package com.projekt.tdp028.models.firebase;

import java.util.List;

public class PollDetail {
    String id;
    String ownerId;
    String text;
    List<PollOption> pollOptions;

    public PollDetail() {}

    public PollDetail(String id, String ownerId, String text, List<PollOption> pollOptions) {
        this.id = id;
        this.ownerId = ownerId;
        this.text = text;
        this.pollOptions = pollOptions;
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(List<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
    }
}
