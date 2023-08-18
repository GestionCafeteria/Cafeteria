package com.example.cafeteria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.R;
import com.example.cafeteria.services.LoginService;
import com.example.cafeteria.utils.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText username = findViewById(R.id.et_username);
        EditText password = findViewById(R.id.et_password);


        Button ingresar = findViewById(R.id.btn_ingresar);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                login(user, pass);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    public void login(String username, String password) {
        String url = Constantes.URL_BASE + "usuario.php";

        JSONObject loginData = new JSONObject();
        try {
            loginData.put("user", username);
            loginData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, loginData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int idUsuario = response.getInt("id");
                            int rol = response.getInt("rol_id");
                            String nombre = response.getString("nombre");

                            Constantes.usuario.setId(idUsuario);
                            Constantes.usuario.setRol(rol);
                            Constantes.usuario.setNombre(nombre);

                            Intent intent = null;
                            if (rol == 1) {
                                intent = new Intent(Login.this, AdminActivity.class);
                            } else if (rol == 2) {
                                intent = new Intent(Login.this, MeseroActivity.class);
                            } else if (rol == 3) {
                                intent = new Intent(Login.this, CocineroActivity.class);
                            }
                            startActivity(intent);
                            Toast.makeText(Login.this, "Bienvenido " + nombre, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            int statusCode = networkResponse.statusCode;
                            if (statusCode == 403) {
                                Toast.makeText(Login.this, "Credenciales Inválidas", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Log.d("ERROR", error.toString());
                    }
                });
        queue.add(jsonObjectRequest);
    }
}