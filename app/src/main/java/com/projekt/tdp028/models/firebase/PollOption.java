package com.projekt.tdp028.models.firebase;

public class PollOption {
    String id;
    String text;
    int picks;

    public PollOption(String id, String text, int picks) {
        this.id = id;
        this.text = text;
        this.picks = picks;
    }

    public PollOption(String id, String text) {
        this.id = id;
        this.text = text;
        this.picks = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPicks() { return picks; }

    public void setPicks(int picks) {
        this.picks = picks;
    }

    public PollOption() {}
}
