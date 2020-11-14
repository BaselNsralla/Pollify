package com.projekt.tdp028.utility;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AsyncModelListParser<M> extends AsyncTask<String, Void, List<M>> {

    AsyncModelListParserListener listener;

    Class<M> dataclass;
    public AsyncModelListParser(Class<M> dataclass, AsyncModelListParserListener listener) {
        super();
        this.listener = listener;
        this.dataclass = dataclass;
    }

    @Override
    protected void onPostExecute(List<M> model_list) {
        this.listener.onFinishModelListParsing(model_list);
    }

    @Override
    protected List<M> doInBackground(String... strs) {
        Gson gson = new Gson();
        Type tft = TypeToken.getParameterized(List.class, dataclass).getType();
        ArrayList<M> d = gson.fromJson(strs[0], tft);
        List<M> m = d;
        return m;
    }

    public interface AsyncModelListParserListener {
        public <M> void onFinishModelListParsing(List<M> model);
    }

}
