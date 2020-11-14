package com.projekt.tdp028.utility;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

public class LatchThread extends Thread {

    private final CountDownLatch latch;
    private final OnLatchDoneListener onLatchDoneListener;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LatchThread(CountDownLatch latch, OnLatchDoneListener onLatchDoneListener)  {
        this.latch = latch;
        this.onLatchDoneListener = onLatchDoneListener;
    }

    @Override
    public void run() {

        try {
            latch.await();
            Runnable mainRunnable = new Runnable() {
                @Override
                public void run() {
                    onLatchDoneListener.onLatchDone();
                }
            };
            mainHandler.post(mainRunnable);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public interface OnLatchDoneListener {
        public void onLatchDone();
    }
}
