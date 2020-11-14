package com.projekt.tdp028.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projekt.tdp028.Constants.FirebaseKeys;
import com.projekt.tdp028.models.firebase.PollDetail;
import com.projekt.tdp028.models.firebase.PollOption;
import com.projekt.tdp028.utility.LatchThread;
import com.projekt.tdp028.utility.LatchedTaskCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static android.content.Context.MODE_PRIVATE;

public class PollDialogViewModel extends ViewModel {

    private DatabaseReference mDatabase;
    private String pollId;
    private String userId;
    public final PollDialogViewModel.OnDataUpdateListener listener;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            PollDetail pollDetail = dataSnapshot.getValue(PollDetail.class);

            Iterable<DataSnapshot> pollOptionSnapshots = dataSnapshot.child(FirebaseKeys.POLL_OPTIONS).getChildren();
            List<PollOption> pollOptions = new ArrayList<>();

            for (DataSnapshot snapshot: pollOptionSnapshots) {
                pollOptions.add(snapshot.getValue(PollOption.class));
            }


            pollDetail.setPollOptions(pollOptions);
            listener.didReceivePostData(pollDetail);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    public PollDialogViewModel(String pollId, String userId, PollDialogViewModel.OnDataUpdateListener listener) {
        this.pollId      = pollId;
        this.listener    = listener;
        this.userId      = userId;
        this.mDatabase   = FirebaseDatabase.getInstance().getReference();
    }

    public void setUserChoice(String optionId) {
        CountDownLatch latch = new CountDownLatch(3);
        Thread thread = new LatchThread(latch, new LatchThread.OnLatchDoneListener() {
            @Override
            public void onLatchDone() {
                listener.onSetChoiceCompleted();
            }
        });
        thread.start();

        Task pollPickUserTask = mDatabase.child(FirebaseKeys.POLL_PICK_USER).child(pollId).child(optionId).child(userId).setValue(true);
        Task userPollPickTask = mDatabase.child(FirebaseKeys.USER_POLL_PICK).child(pollId).child(userId).setValue(optionId);

        incrementPollOptionPicks(optionId, latch);

        pollPickUserTask.addOnCompleteListener(new LatchedTaskCompleteListener(latch));
        userPollPickTask.addOnCompleteListener(new LatchedTaskCompleteListener(latch));
    }

    private void incrementPollOptionPicks(String pollOptionId, CountDownLatch latch) {
        Query pickCountQuery  = mDatabase.child(FirebaseKeys.POLL_DETAIL)
                .child(pollId)
                .child(FirebaseKeys.POLL_OPTIONS)
                .child(pollOptionId)
                .child(FirebaseKeys.POLL_OPTION_PICKS);

        pickCountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int picks = dataSnapshot.getValue(int.class);

                Task pickCountTask = mDatabase.child(FirebaseKeys.POLL_DETAIL)
                                              .child(pollId)
                                              .child(FirebaseKeys.POLL_OPTIONS)
                                              .child(pollOptionId)
                                              .child(FirebaseKeys.POLL_OPTION_PICKS)
                                              .setValue(picks+1);
                pickCountTask.addOnCompleteListener(new LatchedTaskCompleteListener(latch));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }

    public void findUserChoice() {
        Query query = mDatabase.child(FirebaseKeys.USER_POLL_PICK).child(pollId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    listener.onUserChoiceFound(dataSnapshot.child(userId).getValue(String.class));
                } else {
                    listener.onUserChoiceFound(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void fetchData() {
        Query query = mDatabase.child(FirebaseKeys.POLL_DETAIL).child(pollId);
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    public interface OnDataUpdateListener {
        public void didReceivePostData(PollDetail pollDetail); // byt det här mot observers
        public void onSetChoiceCompleted();
        public void onUserChoiceFound(@Nullable String pollOptionId);
    }
}
