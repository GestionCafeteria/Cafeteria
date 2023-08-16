package com.example.cafeteria.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetalleOrdenActivity extends AppCompatActivity {

    TextView tv_mesero, tv_montoTotal, tv_estatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_orden);

        Intent intent = getIntent();
        int id = intent.getIntExtra("idOrden", 0);
        String origen = intent.getStringExtra("origen");

        tv_mesero = findViewById(R.id.tv_mesero);
        tv_montoTotal = findViewById(R.id.tv_montoTotal);
        tv_estatus = findViewById(R.id.tv_estatus);

        Button cerrarCuenta = findViewById(R.id.btn_cerrarOrden);
        if (origen.equals("C")) {
            cerrarCuenta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualizarEstadoMesa(Constantes.MESA_SELECCIONADA, 0);
                    actualizarOrden(id, 6);
                    Intent intent = new Intent(DetalleOrdenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            cerrarCuenta.setVisibility(View.VISIBLE);
        } else {
            cerrarCuenta.setVisibility(View.GONE);
        }

        TextView editText = findViewById(R.id.titulo);
        editText.setText("Detalle de la orden #" + id);
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
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        if (listaOrdenesPlatillo.size() > 0) {
            tv_mesero.setText(listaOrdenesPlatillo.get(0).getNombreMesero());
            tv_montoTotal.setText("$" + decimalFormat.format(listaOrdenesPlatillo.get(0).getTotal()));
            tv_estatus.setText(listaOrdenesPlatillo.get(0).getEstatus());
        }

        RecyclerView recyclerView = findViewById(R.id.platillosOrdenReciclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PlatillosOrdenAdapter ordenesAdapter = new PlatillosOrdenAdapter(listaOrdenesPlatillo);
        recyclerView.setAdapter(ordenesAdapter);
    }

    public void actualizarEstadoMesa(String id, int estatus) {
        String url = Constantes.URL_BASE + "mesa.php";

        JSONObject loginData = new JSONObject();
        try {
            loginData.put("idMesa", id);
            loginData.put("nuevoEstatus", estatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, loginData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String mensaje = response.getString("mensaje");
                            Toast.makeText(DetalleOrdenActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetalleOrdenActivity.this, "Error inesperado: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.toString());
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void actualizarOrden(int idOrden, int nuevoEstatus) {
        RequestQueue queue = Volley.newRequestQueue(DetalleOrdenActivity.this);

        String url = Constantes.URL_BASE + "orden.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idOrden", idOrden);
            jsonObject.put("nuevoEstatus", nuevoEstatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(DetalleOrdenActivity.this, response.getString("mensaje"), Toast.LENGTH_SHORT).show();
                            Log.d("onResponse-actualizarOrden", "RESPUESTA: " + response.getString("mensaje"));
                        } catch (JSONException e) {
                            Log.e("JSONException", "Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error: " + error.getMessage());
                    }
                });

        queue.add(request);
    }
}