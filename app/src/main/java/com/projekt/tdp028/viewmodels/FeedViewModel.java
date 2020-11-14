package com.projekt.tdp028.viewmodels;

import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projekt.tdp028.Constants.FirebaseKeys;
import com.projekt.tdp028.models.firebase.PollOverview;

import java.util.ArrayList;
import java.util.List;

public class FeedViewModel extends PollViewModel {

    public FeedViewModel(OnDataUpdateListener listener, Context context) {
        super(listener, context);
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
            List<PollOverview> overviews = new ArrayList<>();

            for (DataSnapshot snapshot: snapshots) {
                 overviews.add(0, snapshot.getValue(PollOverview.class));
            }

            pollOverviews = overviews;
            listener.onPollOverviewDataReceived(pollOverviews);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };


    public void updateData() {}

    public void fetchData() {
        Query query = mDatabase.child(FirebaseKeys.POLL_OVERVIEW);
        query.addValueEventListener(valueEventListener);
    }

}
