package com.example.cafeteria.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class AdminActivity extends AppCompatActivity {

    EditText nombre, user, pass;
    Spinner spinner;
    int rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        nombre = findViewById(R.id.et_nombre);
        user = findViewById(R.id.et_username);
        pass = findViewById(R.id.et_password);

        TextView txtPrincipal = findViewById(R.id.txt_principal);
        txtPrincipal.setText("¡Hola! " + Constantes.usuario.getNombre() + "... ¿Que deseas hacer hoy?");

        Button registrar = findViewById(R.id.btn_registrar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altaUsuario();
            }
        });

        spinner = findViewById(R.id.spinnerRol);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opcionesRol, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                switch (selectedOption) {
                    case "Administrador":
                        rol = 1;
                        break;
                    case "Mesero":
                        rol = 2;
                        break;
                    case "Cocinero":
                        rol = 3;
                        break;
                }
                Log.e("onItemSelected", "onItemSelected: " + rol);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void limpiarCampos() {
        spinner.setSelection(0);
        this.nombre.setText("");
        this.user.setText("");
        this.pass.setText("");
    }
    private void altaUsuario() {
        String nombre = this.nombre.getText().toString();
        String username = this.user.getText().toString();
        String pass = this.pass.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(AdminActivity.this);

        String url = Constantes.URL_BASE + "usuario.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", nombre);
            jsonObject.put("user", username);
            jsonObject.put("password", pass);
            jsonObject.put("rol", rol);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(AdminActivity.this, response.getString("mensaje"), Toast.LENGTH_SHORT).show();
                            Log.d("onResponse-altaUsuario", "RESPUESTA: " + response.getString("mensaje"));
                            limpiarCampos();
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