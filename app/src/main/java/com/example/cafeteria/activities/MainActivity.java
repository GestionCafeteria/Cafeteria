package com.example.cafeteria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.adapters.MesasAdapter;
import com.example.cafeteria.R;
import com.example.cafeteria.models.Mesa;
import com.example.cafeteria.services.MyBackgroundService;
import com.example.cafeteria.services.OrdenService;
import com.example.cafeteria.utils.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // En una actividad, por ejemplo, en el método onCreate()
        /*Intent serviceIntent = new Intent(this, MyBackgroundService.class);
        startService(serviceIntent);*/

        new OrdenService(this).listarOrdenesPorMeseroEstatus(2, 0);
        listarMesasDisponibles();
    }

    public void listarMesasDisponibles() {
        String url = Constantes.URL_BASE + "mesa.php";
        ArrayList<Mesa> mesas = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                mesas.add(new Mesa(jsonObject.getString("id"), jsonObject.getInt("estado")));
                            }
                            construirRecycler(mesas);
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

    private void construirRecycler(ArrayList<Mesa> listaMesas) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myReciclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        MesasAdapter mesasAdapter = new MesasAdapter(listaMesas);
        mesasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listaMesas.get(recyclerView.getChildAdapterPosition(view)).getEstado() == 0) {
                    Constantes.MESA_SELECCIONADA = listaMesas.get(recyclerView.getChildAdapterPosition(view)).getId();
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Esta mesa se encuentra ocupada por el momento", Toast.LENGTH_SHORT).show();
                }

            }
        });
        recyclerView.setAdapter(mesasAdapter);
    }


    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
}