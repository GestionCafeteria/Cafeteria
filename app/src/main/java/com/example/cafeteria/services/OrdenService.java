package com.example.cafeteria.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.models.Orden;
import com.example.cafeteria.utils.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrdenService {

    Context context;

    public OrdenService(Context context) {
        this.context = context;
    }

    public ArrayList<Orden> listarOrdenesPorMeseroEstatus(int mesero, int estado) {
        String url = Constantes.URL_BASE + "orden.php?mesero=" + mesero + "&estado=" + estado;
        ArrayList<Orden> ordenes = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                Log.d("TAG", "onResponse: " + jsonObject.getInt("id")  + " --> " + jsonObject.getString("total"));

                                ordenes.add(new Orden(jsonObject.getInt("id"), jsonObject.getInt("mesa_id"),
                                        jsonObject.getInt("mesero_id"), jsonObject.getString("nombre_mesero"),
                                        jsonObject.getDouble("total"), jsonObject.getInt("estado")));
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", "Error en la respuesta del servidor: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("onErrorResponse", "ERROR: " + error.getMessage());
                    }
                });

        queue.add(jsonArrayRequest);
        Log.d("RETURN", "listarOrdenesPorMeseroEstatus: ");
        return ordenes;
    }
}
