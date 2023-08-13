package com.example.cafeteria.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.R;
import com.example.cafeteria.adapters.OrdenesAdapter;
import com.example.cafeteria.adapters.PlatillosOrdenAdapter;
import com.example.cafeteria.models.Orden;
import com.example.cafeteria.models.OrdenPlatillo;
import com.example.cafeteria.utils.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetalleOrdenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_orden);

        Intent intent = getIntent();
        int id = intent.getIntExtra("idOrden", 0);

        TextView editText = findViewById(R.id.textoa);
        editText.setText("OLIS: " + id);
        listarDetalleOrden(id);
    }

    public void listarDetalleOrden(int idOrden) {
        String url = Constantes.URL_BASE + "orden.php?tipoConsulta=detalleOrden&idOrden=" + idOrden;
        ArrayList<OrdenPlatillo> platillosOrden = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                Log.d("TAG", "onResponse: " + jsonObject.getInt("id")  + " --> " + jsonObject.getString("platillo"));

                                platillosOrden.add(new OrdenPlatillo(jsonObject.getInt("id"), jsonObject.getInt("mesa_id"),
                                        jsonObject.getString("estatus"), jsonObject.getString("platillo"),
                                        jsonObject.getDouble("precio"), jsonObject.getInt("cantidad_platillo"),
                                        jsonObject.getString("nombre_mesero"), jsonObject.getDouble("total")));
                            }
                            construirRecycler(platillosOrden);
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
    }

    private void construirRecycler(ArrayList<OrdenPlatillo> listaOrdenesPlatillo) {
        RecyclerView recyclerView = findViewById(R.id.platillosOrdenReciclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PlatillosOrdenAdapter ordenesAdapter = new PlatillosOrdenAdapter(listaOrdenesPlatillo);
        recyclerView.setAdapter(ordenesAdapter);
    }
}