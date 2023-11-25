package com.example.har;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DataCollection {

    private MainActivity mainActivity;

    public DataCollection(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public DataCollection(){}

    public void updateSheet(float x, float y, float z, String sheet) {

        // Url for the google sheet. API script has already been generated.
        String url = "https://script.google.com/macros/s/AKfycbz9D6IGTMB29t76cW6_nlP_mhF7yoZJKUIjdPH_mHurQ3M5cjDdsPBFureweo72CzmM/exec";


        // Dependency required: implementation 'com.android.volley:volley:1.2.1'implementation 'com.android.volley:volley:1.2.1'
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // On successful data entry
                Toast.makeText(mainActivity, "Response: " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // On an error response
                Toast.makeText(mainActivity, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Generating parameters to send values to the API
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "create");
                params.put("x", String.valueOf(x));
                params.put("y", String.valueOf(y));
                params.put("z", String.valueOf(z));
                params.put("sheet", sheet);
                return params;
            }

        };
        int socketTimeOut = 5000; // 5 sec. to wait until retry the entry.
        // Default retry policy. Not necessary for our use.
        RetryPolicy rp = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(rp);
        RequestQueue queue = Volley.newRequestQueue(mainActivity);  // Creating a new request queue that will contain all the requests for data entry
        queue.add(request); // Add the request
    }

    }


