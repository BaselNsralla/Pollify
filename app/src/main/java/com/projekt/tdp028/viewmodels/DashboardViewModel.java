package com.projekt.tdp028.viewmodels;

import android.content.Context;
import android.util.Log;

import com.projekt.tdp028.utility.AsyncModelListParser;
import com.projekt.tdp028.utility.Network;
import com.projekt.tdp028.models.Post;

import java.util.List;

public class DashboardViewModel {

    private final Context context;
    public final OnDataUpdateListener listener;
    private Network network = Network.getInstance();

    public DashboardViewModel(OnDataUpdateListener listener, Context c) {
        this.listener = listener;
        this.context  = c;
    }

    public void update() {
        network.<Post>getStuffWithVolley(context, Post.class ,new AsyncModelListParser.AsyncModelListParserListener() {
            @Override
            public <M> void onFinishModelListParsing(List<M> model) {
                Log.d("Finished parsing", "FINISHED PARSING");
                List<Post> a = (List<Post>) model;
                listener.didReceivePostData(a);
            }

        });
    }

    public interface OnDataUpdateListener {
        public void didReceivePostData(List<Post> data); // byt det här mot observers
    }
}
