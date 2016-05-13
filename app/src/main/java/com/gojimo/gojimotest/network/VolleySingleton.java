package com.gojimo.gojimotest.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton of Volley to make the http requests
 */
public class VolleySingleton {

    private static VolleySingleton ourInstance;
    private RequestQueue requestQueue;
    private static Context context;

    public static final String URL_QUALIFICATIONS = "https://api.gojimo.net/api/v4/qualifications";

    public static synchronized VolleySingleton getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new VolleySingleton(context.getApplicationContext());
        }
        return ourInstance;
    }

    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}