package com.projekt.tdp028.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.CountDownLatch;

public class LatchedTaskCompleteListener implements OnCompleteListener {
    private final OnDoneLatchedTaskCompleteListener listener;
    private final CountDownLatch latch;

    public LatchedTaskCompleteListener(CountDownLatch latch, OnDoneLatchedTaskCompleteListener listener) {
        this.latch = latch;
        this.listener = listener;
    }

    public LatchedTaskCompleteListener(CountDownLatch latch) {
        this.latch = latch;
        this.listener = new OnDoneLatchedTaskCompleteListener() {
            @Override
            public void onDone() {}
        };
    }

    @Override
    public void onComplete(@NonNull Task task) {
        Log.d("LATCH", "######## LATCH DOWN");
        listener.onDone();
        latch.countDown();
    }

    public interface OnDoneLatchedTaskCompleteListener {
        public void onDone();
    }
}