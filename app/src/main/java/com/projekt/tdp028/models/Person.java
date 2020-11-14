package com.projekt.tdp028.models;

import java.util.ArrayList;

public class Person {
    String name;
    String email;
    Job job;
    ArrayList<String> kids;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public ArrayList<String> getKids() {
        return kids;
    }

    public void setKids(ArrayList<String> kids) {
        this.kids = kids;
    }
}
