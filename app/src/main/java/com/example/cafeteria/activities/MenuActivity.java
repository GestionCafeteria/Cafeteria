package com.example.cafeteria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.R;
import com.example.cafeteria.adapters.PlatillosAdapter;
import com.example.cafeteria.models.Platillo;
import com.example.cafeteria.utils.Constantes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    ArrayList<Platillo> listaPlatillos;
    TextView textViewTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        textViewTotal = findViewById(R.id.totalAmountTextView);

        listaPlatillos = new ArrayList<>();

        FloatingActionButton fabButton = findViewById(R.id.fabButton);
        fabButton.setOnClickListener(view -> {
            JSONArray jsonArray = llenarOrden(listaPlatillos);
            registrarOrden(jsonArray);
        });

        listarPlatillos();
        //construirRecycler(listaPlatilos);
    }

    public void listarPlatillos() {
        String url = Constantes.URL_BASE + "platillo.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listaPlatillos.add(new Platillo(jsonObject.getInt("id"), jsonObject.getString("descripcion"), jsonObject.getDouble("precio")));
                            }
                            construirRecycler(listaPlatillos);
                        } catch (JSONException e) {
                            Log.e("JSONException", "Error en la respuesta del servidor: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("onErrorResponse", "id: " + error.getMessage() + " --> " + url);
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void construirRecycler(ArrayList<Platillo> listaPlatilos) {
        RecyclerView recyclerView = findViewById(R.id.menuReciclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PlatillosAdapter platillosAdapter = new PlatillosAdapter(listaPlatilos, textViewTotal);
        /*platillosAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClienteActivity.class);
                startActivity(intent);
            }
        });*/
        recyclerView.setAdapter(platillosAdapter);
    }

    private JSONArray llenarOrden(ArrayList<Platillo> listaPlatilos) {
        JSONArray requestData = new JSONArray();

        int total = obtenerTotalOrden(listaPlatilos);
        try {
            for (Platillo platillo : listaPlatilos) {
                if (platillo.getCantidad() != 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("mesa", Constantes.MESA_SELECCIONADA);
                    jsonObject.put("total", total);
                    jsonObject.put("platillo", platillo.getId());
                    jsonObject.put("cantidadPlatillo", platillo.getCantidad());
                    jsonObject.put("precioPlatillo", platillo.getPrecio());
                    requestData.put(jsonObject);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestData;
    }

    private int obtenerTotalOrden(ArrayList<Platillo> listaPlatilos) {
        int suma = 0;
        for (Platillo platillo : listaPlatilos) {
            if (platillo.getCantidad() != 0) {
                suma += platillo.getCantidad() * platillo.getPrecio();
            }
        }
        return suma;
    }

    public void registrarOrden(JSONArray jsonArray) {
        String url = Constantes.URL_BASE + "orden.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            String mensaje = jsonObject.getString("mensaje");

                            Toast.makeText(MenuActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            Log.e("JSONException", "ERROR: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("onErrorResponse", "onResponse: " + error.getMessage());
                    }
                });

        queue.add(jsonArrayRequest);
    }
}