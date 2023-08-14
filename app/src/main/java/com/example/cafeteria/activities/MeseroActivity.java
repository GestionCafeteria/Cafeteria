package com.example.cafeteria.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.R;
import com.example.cafeteria.adapters.MesasAdapter;
import com.example.cafeteria.adapters.OrdenesAdapter;
import com.example.cafeteria.adapters.PlatillosAdapter;
import com.example.cafeteria.models.Mesa;
import com.example.cafeteria.models.Orden;
import com.example.cafeteria.models.Platillo;
import com.example.cafeteria.models.Usuario;
import com.example.cafeteria.services.OrdenService;
import com.example.cafeteria.utils.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MeseroActivity extends AppCompatActivity {

    TextView emptyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesero);

        emptyTextView = findViewById(R.id.emptyTextView);
        Button actualizar = findViewById(R.id.btn_contrasena);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeseroActivity.this, CambioContrasenaActivity.class);
                startActivity(intent);
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int tipoConsulta = 0;
                String selectedOption = parent.getItemAtPosition(position).toString();
                switch (selectedOption) {
                    case "Pendientes":
                        tipoConsulta = 1;
                        break;
                    case "Aprobadas":
                        tipoConsulta = 2;
                        break;
                    case "En Preparación":
                        tipoConsulta = 3;
                        break;
                    case "Preparadas":
                        tipoConsulta = 4;
                        break;
                    case "Entregadas":
                        tipoConsulta = 5;
                        break;
                    case "Cerradas":
                        tipoConsulta = 6;
                        break;
                }

                listarOrdenesPorMeseroEstatus(Constantes.usuario.getId(), tipoConsulta);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void listarOrdenesPorMeseroEstatus(int mesero, int estado) {
        String url = Constantes.URL_BASE + "orden.php?mesero=" + mesero + "&estado=" + estado;
        ArrayList<Orden> ordenes = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
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
                            construirRecycler(ordenes);
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

    private void construirRecycler(ArrayList<Orden> listaOrdenes) {
        RecyclerView recyclerView = findViewById(R.id.ordenesReciclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OrdenesAdapter ordenesAdapter = new OrdenesAdapter(listaOrdenes, this);
        ordenesAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeseroActivity.this, DetalleOrdenActivity.class);
                intent.putExtra("idOrden", listaOrdenes.get(recyclerView.getChildAdapterPosition(view)).getIdOrden());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(ordenesAdapter);

        if (ordenesAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }
}