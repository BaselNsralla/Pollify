package com.projekt.tdp028.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projekt.tdp028.Constants.FirebaseKeys;
import com.projekt.tdp028.models.firebase.PollDetail;
import com.projekt.tdp028.models.firebase.PollOption;
import com.projekt.tdp028.models.firebase.PollOverview;
import com.projekt.tdp028.utility.LatchThread;
import com.projekt.tdp028.utility.LatchedTaskCompleteListener;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static android.content.Context.MODE_PRIVATE;

public class MakePollViewModel extends ViewModel {
    private DatabaseReference mDatabase;
    private Application application;

    String POLL_DETAIL        = FirebaseKeys.POLL_DETAIL;
    String POLL_OPTIONS       = FirebaseKeys.POLL_OPTIONS;
    String POLL_OVERVIEW      = FirebaseKeys.POLL_OVERVIEW;
    String USERS              = FirebaseKeys.USERS;
    String USER_CREATED_POLLS = FirebaseKeys.USER_CREATED_POLLS;
    String LOCATION           = FirebaseKeys.LOCATION;

    public MakePollViewModel(Application application) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.application = application;
    }

    public void createPoll(String pollText, List<String> pollOptionsText, LatLng latlng, OnMakePollViewModelListener listener) {
        SharedPreferences sp1 = application.getSharedPreferences("Login", MODE_PRIVATE);
        String userID = sp1.getString("USER_ID", null);
        Log.d("USERId INANNA POST", userID);
        final CountDownLatch latch = new CountDownLatch(4);

        if(userID != null) {
            Thread thread = new LatchThread(latch, new LatchThread.OnLatchDoneListener() {
                @Override
                public void onLatchDone() {
                    listener.done();
                }
            });

            thread.start();

            DatabaseReference reference = mDatabase.child(POLL_DETAIL);
            String key = reference.push().getKey(); // genererar en key
            PollDetail pollDetail = new PollDetail(key,userID, pollText, new ArrayList<>());


            Task createPollTask = reference.child(key).setValue(pollDetail);
            createPollTask.addOnCompleteListener(new LatchedTaskCompleteListener(latch, new LatchedTaskCompleteListener.OnDoneLatchedTaskCompleteListener() {
                @Override
                public void onDone() {
                    insertOptions     (key, latch, pollOptionsText);
                    insertOverview    (key, latch, pollText); // TODO: vänta på options först ? nej
                    insertUserCreation(key, latch, userID);
                    insertLocation    (key, latch, latlng);
                }
            }));

        }
    }

    private void insertLocation(String pollID, CountDownLatch latch, LatLng latlng) {
        Log.d("LOCATION", latlng.latitude + " " + latlng.longitude);
        Task insertCreation = mDatabase.child(LOCATION).child(pollID).setValue(latlng);
        insertCreation.addOnCompleteListener(new LatchedTaskCompleteListener(latch));
    }

    private void insertUserCreation(String key, CountDownLatch latch, String userID) {
        Task insertCreation = mDatabase.child(USERS).child(userID).child(USER_CREATED_POLLS).child(key).setValue(true);
        insertCreation.addOnCompleteListener(new LatchedTaskCompleteListener(latch));
    }

    private void insertOverview(String pollID, CountDownLatch latch, String pollText) {
        String pollOverviewText   = pollText.length() > 40 ? pollText.substring(0, 40) : pollText;
        PollOverview pollOverview = new PollOverview(pollID, "Placeholder username", pollOverviewText);
        Task insertOverviewTask   = mDatabase.child(POLL_OVERVIEW).child(pollID).setValue(pollOverview);
        insertOverviewTask.addOnCompleteListener(new LatchedTaskCompleteListener(latch));
    }

    private void insertOptions(String pollID, CountDownLatch latch, List<String> pollOptionsTexts) {
        Map<String,Object> taskMap = new HashMap<>();

        for (int i = 0; i < pollOptionsTexts.size(); ++i) {
            String pollOptionID = mDatabase.child(POLL_DETAIL).child(pollID).child(POLL_OPTIONS).push().getKey();
            taskMap.put(pollOptionID,new PollOption(pollOptionID, pollOptionsTexts.get(i)));
        }

        Task insertPollOptionsTask = mDatabase.child(POLL_DETAIL).child(pollID).child(POLL_OPTIONS).updateChildren(taskMap);
        insertPollOptionsTask.addOnCompleteListener(new LatchedTaskCompleteListener(latch));

    }


    public interface OnMakePollViewModelListener {
        public void done();
    }
}
