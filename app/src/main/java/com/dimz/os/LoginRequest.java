package com.dimz.os;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dimz OS on 16-06-2017.
 * Custom Request to login
 */

public class LoginRequest extends StringRequest {
    private static final String LOGIN_URL = "http://dimzos.000webhostapp.com/login.php";
    private Map<String, String> parameters;



    public LoginRequest(String username, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_URL, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }


}
