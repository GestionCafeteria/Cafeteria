package com.example.cafeteria.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.utils.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginService {

    public void login(String username, String password) {
        String url = Constantes.URL_BASE + "usuario.php";

       /* JSONObject loginData = new JSONObject();
        try {
            loginData.put("user", username);
            loginData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, loginData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("DEBUG", response.getString("nombre"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.toString());
                    }
                });
        queue.add(jsonObjectRequest); */
    }
}
