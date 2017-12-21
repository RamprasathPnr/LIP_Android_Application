package com.omneagate.lip.Service;

import java.util.HashMap;

/**
 * Created by Sathya Rathinavelu on 5/8/2016.
 */
public interface ServiceResponseListener {
    public void onSuccessResponse(String url,
                                  HashMap<String, Object> requestParams, String response);

    public void onError(String url, HashMap<String, Object> requestParams,
                        String errorMessage, Boolean isVolleyError);

    public void onNetWorkFailure(String url);
}
