package com.example.cafeteria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.R;
import com.example.cafeteria.adapters.BebidasAdapter;
import com.example.cafeteria.adapters.PlatillosAdapter;
import com.example.cafeteria.models.Platillo;
import com.example.cafeteria.utils.Constantes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    ArrayList<Platillo> listaPlatillos, listaBebidas, listaExtras;
    TextView textViewTotal;

    private int selectedTab = 1;

    LinearLayout platillos_layout, bebidas_layout, extras_layout;
    ImageView platillo_image, bebida_image, extras_image;
    TextView platillo_txt, bebida_txt, extras_txt;

    RecyclerView platillos_RV, bebidas_RV, extras_RV;

    Button ordenarButton;

    BebidasAdapter extrasAdapter;
    FloatingActionButton ordenarFButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);

        textViewTotal = findViewById(R.id.totalAmountTextView);

        listaPlatillos = new ArrayList<>();
        listaBebidas = new ArrayList<>();
        listaExtras = new ArrayList<>();

        ordenarButton = findViewById(R.id.ordenarButton);
        ordenarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = llenarOrden(listaPlatillos, listaBebidas, listaExtras);
                registrarOrden(jsonArray);
            }
        });


        listarPlatillos();
        listarBebidas();
        construirExtrasRecycler(listaExtras);

        ordenarFButton = findViewById(R.id.ordenarFButton);
        ordenarFButton.setOnClickListener(view -> {
            showInputDialog();
        });

        platillos_RV = findViewById(R.id.platillosReciclerView);
        bebidas_RV = findViewById(R.id.bebidasReciclerView);
        extras_RV = findViewById(R.id.extrasReciclerView);

        platillos_layout = findViewById(R.id.platillos_layout);
        platillo_image = findViewById(R.id.platillos_image);
        platillo_txt = findViewById(R.id.platillos_txt);

        bebidas_layout = findViewById(R.id.bebidas_layout);
        bebida_image = findViewById(R.id.bebidas_image);
        bebida_txt = findViewById(R.id.bebidas_txt);

        extras_layout = findViewById(R.id.extras_layout);
        extras_image = findViewById(R.id.extras_image);
        extras_txt = findViewById(R.id.extras_txt);

        platillos_layout.setBackgroundResource(R.drawable.round_back_home_100);

        platillos_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedTab != 1) {
                    ocultarElementosTab();
                    platillos_RV.setVisibility(View.VISIBLE);
                    platillo_txt.setVisibility(View.VISIBLE);
                    platillos_layout.startAnimation(scaleAnimation);
                    platillos_layout.setBackgroundResource(R.drawable.round_back_home_100);
                    selectedTab = 1;

                }
            }
        });

        bebidas_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedTab != 2) {
                    ocultarElementosTab();
                    bebidas_RV.setVisibility(View.VISIBLE);
                    bebida_txt.setVisibility(View.VISIBLE);
                    bebidas_layout.startAnimation(scaleAnimation);
                    bebidas_layout.setBackgroundResource(R.drawable.round_back_home_100);
                    selectedTab = 2;

                }
            }
        });

        extras_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedTab != 3) {
                    ocultarElementosTab();
                    extras_RV.setVisibility(View.VISIBLE);
                    extras_txt.setVisibility(View.VISIBLE);
                    extras_layout.startAnimation(scaleAnimation);
                    extras_layout.setBackgroundResource(R.drawable.round_back_home_100);
                    ordenarFButton.setVisibility(View.VISIBLE);
                    selectedTab = 3;
                }
            }
        });
    }

    public void ocultarElementosTab() {
        ordenarFButton.setVisibility(View.GONE);

        platillos_RV.setVisibility(View.GONE);
        bebidas_RV.setVisibility(View.GONE);
        extras_RV.setVisibility(View.GONE);

        bebida_txt.setVisibility(View.GONE);
        platillo_txt.setVisibility(View.VISIBLE);
        extras_txt.setVisibility(View.GONE);

        platillos_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        bebidas_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        extras_layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public void listarPlatillos() {

        String url = Constantes.URL_BASE + "platillo.php?categoria=1";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listaPlatillos.add(new Platillo(jsonObject.getInt("id"), jsonObject.getString("descripcion"), jsonObject.getDouble("precio"), jsonObject.getString("menu_desc"), jsonObject.getInt("categoria_id")));
                            }
                            construirPlatillosRecycler(listaPlatillos);
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

    private void construirPlatillosRecycler(ArrayList<Platillo> listaPlatillos) {
        RecyclerView recyclerView = findViewById(R.id.platillosReciclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PlatillosAdapter platillosAdapter = new PlatillosAdapter(listaPlatillos, textViewTotal);
        recyclerView.setAdapter(platillosAdapter);
    }

    public void listarBebidas() {
        String url = Constantes.URL_BASE + "platillo.php?categoria=2";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                listaBebidas.add(new Platillo(jsonObject.getInt("id"), jsonObject.getString("descripcion"), jsonObject.getDouble("precio"), jsonObject.getString("menu_desc"), jsonObject.getInt("categoria_id")));
                            }
                            construirBebidasRecycler(listaBebidas);
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

    private void construirBebidasRecycler(ArrayList<Platillo> listaBebidas) {
        RecyclerView recyclerView = findViewById(R.id.bebidasReciclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BebidasAdapter bebidasAdapter = new BebidasAdapter(listaBebidas, textViewTotal);
        recyclerView.setAdapter(bebidasAdapter);
    }

    private JSONArray llenarOrden(ArrayList<Platillo> listaPlatilos, ArrayList<Platillo> listaBebidas, ArrayList<Platillo> listaExtras) {
        JSONArray requestData = new JSONArray();

        int total = obtenerTotalOrden(listaPlatilos, listaBebidas, listaExtras);
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

            for (Platillo bebida : listaBebidas) {
                if (bebida.getCantidad() != 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("mesa", Constantes.MESA_SELECCIONADA);
                    jsonObject.put("total", total);
                    jsonObject.put("platillo", bebida.getId());
                    jsonObject.put("cantidadPlatillo", bebida.getCantidad());
                    jsonObject.put("precioPlatillo", bebida.getPrecio());
                    requestData.put(jsonObject);
                }
            }

            for (Platillo platillo : listaExtras) {
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

    private int obtenerTotalOrden(ArrayList<Platillo> listaPlatilos, ArrayList<Platillo> listaBebidas, ArrayList<Platillo> listaExtras) {
        int suma = 0;
        for (Platillo platillo : listaPlatilos) {
            if (platillo.getCantidad() != 0) {
                suma += platillo.getCantidad() * platillo.getPrecio();
            }
        }
        for (Platillo platillo : listaBebidas) {
            if (platillo.getCantidad() != 0) {
                suma += platillo.getCantidad() * platillo.getPrecio();
            }
        }
        for (Platillo platillo : listaExtras) {
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
                            int idOrden = jsonObject.getInt("idOrden");

                            Toast.makeText(MenuActivity.this, mensaje, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MenuActivity.this, DetalleOrdenActivity.class);
                            intent.putExtra("idOrden", idOrden);
                            intent.putExtra("origen", "C");
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

    private void showInputDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_input, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Capturar datos")
                .setPositiveButton("Guardar", (dialog, which) -> {
                    EditText editText = dialogView.findViewById(R.id.editText);
                    String inputData = editText.getText().toString();

                    verificarExistenciaPlatillo(inputData);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void verificarExistenciaPlatillo(String platillo) {
        String url = Constantes.URL_BASE + "platillo.php?platillo=" + platillo;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() == 0) {
                                Toast.makeText(MenuActivity.this, "El platillo ingresado no se encuentra dentro de nuestro menú, por favor elija otro", Toast.LENGTH_SHORT).show();
                            }
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                if (!verificarExistenciaListas(jsonObject.getInt("id"))) {
                                    listaExtras.add(new Platillo(jsonObject.getInt("id"), jsonObject.getString("descripcion"), jsonObject.getDouble("precio"), jsonObject.getString("menu_desc"), jsonObject.getInt("categoria_id")));
                                    extrasAdapter.notifyDataSetChanged();
                                }

                            }
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

    private void construirExtrasRecycler(ArrayList<Platillo> listaExtras) {
        RecyclerView recyclerViewExtras = findViewById(R.id.extrasReciclerView);
        recyclerViewExtras.setLayoutManager(new LinearLayoutManager(this));

        extrasAdapter = new BebidasAdapter(listaExtras, textViewTotal);
        recyclerViewExtras.setAdapter(extrasAdapter);
    }

    private boolean verificarExistenciaListas(int idABuscar) {
        for (Platillo platillo : listaPlatillos) {
            if (platillo.getId() == idABuscar) {
                Toast.makeText(MenuActivity.this, "El platillo ingresado ya existe en el menú de platillos actuales", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        for (Platillo platillo : listaBebidas) {
            if (platillo.getId() == idABuscar) {
                Toast.makeText(MenuActivity.this, "La bebida ingresada ya existe en el menú de bebidas actuales", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        for (Platillo platillo : listaExtras) {
            Toast.makeText(MenuActivity.this, "El platillo ingresado ya existe en la lista de extras", Toast.LENGTH_SHORT).show();
            if (platillo.getId() == idABuscar) {
                return true;
            }
        }
        return false;
    }
}