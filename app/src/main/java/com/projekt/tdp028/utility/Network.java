package com.projekt.tdp028.utility;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class Network {

    private static Network instance = null;

    public static Network getInstance() {
        if (instance == null) {
            instance = new Network();
        }
        return instance;
    }

    public String[] getData() {
        String[] strs = {"omg", "omgigeb", "asdasdads",   "ixidGAFhjf",
                "uPCyCNvMdO",
                "txsbGFVCdp",
                "cFSZjQzSZK",
                "QcZddFSsMf",
                "tRCLALKvIf",
                "VTeQUnilRR",
                "pTPImorLuf",
        };
        return strs;
    }


    public String getStuff(String url) {
        StringBuffer buffer = new StringBuffer();

        try {
            URL the_url = new URL(url);

            BufferedReader in = new BufferedReader(new InputStreamReader(the_url.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                buffer.append(inputLine);
            }

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();


    }

    // Anpassat till rest backend
    public <M> void getStuffWithVolley(Context context, final Class<M> dataclass, final AsyncModelListParser.AsyncModelListParserListener pl
            ) {
        String url = "http://10.0.2.2:3000/getall";

        RequestQueue queue = Volley.newRequestQueue(context); // det brukar räcka med en kö
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DID PERFORM", "PERFORMER");
                AsyncTask<String, Void, List<M>> threaded_parser = new AsyncModelListParser<M>(dataclass, pl);
                threaded_parser.execute(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("netwocrk", error.getMessage());
            }
        });
        queue.add(stringRequest);
    }



}

