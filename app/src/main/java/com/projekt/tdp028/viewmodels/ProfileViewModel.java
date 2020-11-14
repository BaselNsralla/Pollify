package com.projekt.tdp028.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projekt.tdp028.Constants.FirebaseKeys;
import com.projekt.tdp028.models.firebase.PollOverview;
import com.projekt.tdp028.utility.LatchThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ProfileViewModel extends PollViewModel {
    private final OnProfileDataListener profileDataListener;

    public ProfileViewModel(OnDataUpdateListener dataUpdateListener, OnProfileDataListener profileDataListener, Context context) {
        super(dataUpdateListener, context);
        this.profileDataListener = profileDataListener;
    }


    public void fetchData(String ownerId) {
        Query ownedPollIdsQuery = mDatabase.child(FirebaseKeys.USERS).child(ownerId).child(FirebaseKeys.USER_CREATED_POLLS);

        ownedPollIdsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                List<String> ids = new ArrayList<>();

                for (DataSnapshot snapshot: snapshots) {
                    String pollId = snapshot.getKey();
                    ids.add(0,pollId);

                }
                getOverviews(ids);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


        Query imageQuery = mDatabase.child(FirebaseKeys.USERS).child(ownerId).child(FirebaseKeys.IMAGE);
        imageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageUrlString = dataSnapshot.getValue(String.class);
                profileDataListener.onImageReceived(imageUrlString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOverviews(List<String> ids) {

        List<PollOverview> overviews = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(ids.size());

        LatchThread latchThread = new LatchThread(latch, new LatchThread.OnLatchDoneListener() {
            @Override
            public void onLatchDone() {
                pollOverviews = overviews;
                listener.onPollOverviewDataReceived(pollOverviews);
            }
        });

        latchThread.start();
        for (String id: ids) {
            mDatabase.child(FirebaseKeys.POLL_OVERVIEW).child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    PollOverview pollOverview = dataSnapshot.getValue(PollOverview.class);
                    overviews.add(pollOverview);
                    latch.countDown();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public interface OnProfileDataListener {
        public void onImageReceived(String imageUrlString);
    }

}
