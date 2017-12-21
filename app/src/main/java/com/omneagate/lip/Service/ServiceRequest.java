/*@author Sathishkumar.R 
 * 		  -Blaze dream technologies*/

package com.omneagate.lip.Service;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ServiceRequest {

    private static ServiceRequest mInstance;
    private ImageLoader mImageLoader;
    private static String TAG = ServiceRequest.class.getSimpleName();

    public static ServiceRequest getInstance() {
        if (mInstance == null) {
            mInstance = new ServiceRequest();
        }
        return mInstance;
    }

    public ImageLoader getImageLoader() {

        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(Application.getInstance().getRequestQueue(),
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public void execute(final String url,
                        final HashMap<String, Object> requestParams,
                        final ServiceResponseListener listener, final String tag, final String url_enum) {

        Log.d(TAG, "URL :" + url);
        Log.d(TAG, "Params :");

        // Get a set of the entries
        Set set = requestParams.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
        }

        StringRequest stringRequest;

        stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            listener.onSuccessResponse(url_enum, requestParams, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onNetWorkFailure(url_enum);
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("VolleyError:", error.toString());
                Boolean error_ = true;
                listener.onError(url_enum, requestParams, error.toString(), error_);
//                listener.onNetWorkFailure(url);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Application.getInstance().addToRequestQueue(stringRequest, tag);
    }


    public void execute_POST(final String url,
                             final HashMap<String, Object> requestParams,
                             final ServiceResponseListener listener, final String tag, final String url_enum) {

        Log.d(TAG, "URL :" + url);
        Log.d(TAG, "Params :");

        // Get a set of the entries
        Set set = requestParams.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }

        System.out.println("Json " + new JSONObject(requestParams));
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requestParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.onSuccessResponse(url_enum, requestParams, response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onNetWorkFailure(url_enum);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyError:", error.toString());
                        Boolean error_ = true;
                        listener.onError(url_enum, requestParams, error.toString(), error_);
//                        listener.onNetWorkFailure(url);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        Application.getInstance().addToRequestQueue(req);
    }


    public void execute_POST_(final String url,
                              final String requestParams,
                              final ServiceResponseListener listener, final String tag, final String url_enum) {

        Log.d(TAG, "URL :" + url);
        Log.d(TAG, "Params :");

        JsonObjectRequest req = null;
        try {
            req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(requestParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        listener.onSuccessResponse(url_enum, null, response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onNetWorkFailure(url_enum);
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("VolleyError:", error.toString());
                            Boolean error_ = true;
//                            listener.onError(url, requestParams, error.toString(), error_);
                            listener.onNetWorkFailure(url_enum);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        Application.getInstance().addToRequestQueue(req);
    }

    public void removeRequest(String TAG) {
        Application.getInstance().cancelPendingRequests(TAG);
    }
}
