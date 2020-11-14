/*
package com.projekt.tdp028.utility;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projekt.tdp028.models.firebase.PollOverview;

import java.util.ArrayList;
import java.util.List;

public class Firebase {
    private DatabaseReference mDatabase;
    private OnFirebaseDataListener onFirebaseDataListener;
    public Firebase(OnFirebaseDataListener listener){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        onFirebaseDataListener = listener;
    }

    public void getPollOverviews() {
        List<PollOverview> pollOverviewList = new ArrayList<>();
        //mDatabase.child("poll_overview");
        Query query = mDatabase.child("poll_overview").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                GenericTypeIndicator<List<PollOverview>> typeIndicator = new GenericTypeIndicator<List<PollOverview>>(){};
                List<PollOverview> overviews = dataSnapshot.getValue(typeIndicator);
                onFirebaseDataListener.onPollOverviewDataUpdate(overviews);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public interface OnFirebaseDataListener {
        void onPollOverviewDataUpdate(List<PollOverview> list);
    }
}

*/
