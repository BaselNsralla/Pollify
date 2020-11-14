package com.projekt.tdp028.viewmodels;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.projekt.tdp028.Constants.FirebaseKeys;
import com.projekt.tdp028.models.firebase.PollOverview;

import java.util.ArrayList;
import java.util.List;

public abstract class PollViewModel extends ViewModel {
    protected final Context context;
    public final PollViewModel.OnDataUpdateListener listener;
    protected DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    protected List<PollOverview> pollOverviews = new ArrayList<PollOverview>();

    public PollViewModel(PollViewModel.OnDataUpdateListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    public String getPollId(int index) {
        return pollOverviews.get(index).getPollId();
    }

    public void updateImage(Uri uri, String userId) {
        Task task = mDatabase.child(FirebaseKeys.USERS).child(userId).child(FirebaseKeys.IMAGE).setValue(uri.toString());
    }

    public interface OnDataUpdateListener {
        public void onPollOverviewDataReceived(List<PollOverview> data); // byt det här mot observers

    }
}
