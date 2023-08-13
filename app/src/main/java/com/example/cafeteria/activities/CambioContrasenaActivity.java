package com.example.cafeteria.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.R;
import com.example.cafeteria.utils.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

public class CambioContrasenaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_contrasena);

        EditText password = findViewById(R.id.et_password);
        Button ingresar = findViewById(R.id.btn_actualizar);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarContrasena(password.getText().toString());
            }
        });
    }

    public void cambiarContrasena(String password) {
        String url = Constantes.URL_BASE + "usuario.php";

        JSONObject loginData = new JSONObject();
        try {
            loginData.put("id", Constantes.usuario.getId());
            loginData.put("password", password);
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
                            Toast.makeText(CambioContrasenaActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CambioContrasenaActivity.this, "Error inesperado: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.toString());
                    }
                });
        queue.add(jsonObjectRequest);
    }
}