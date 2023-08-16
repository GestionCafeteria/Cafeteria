package com.example.cafeteria.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.utils.Constantes;


public class MyBackgroundService extends Service {
    private static final long INTERVAL = 10000; // 30 segundos
    private Handler handler = new Handler();
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        startRepeatingTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String url = Constantes.URL_BASE + "orden.php?tipoConsulta=consultarPreparada&idMesero=2";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        // Procesa la respuesta y compara el valor del campo con el valor previo
                        // Si el valor cambia, muestra una notificación
                        Log.i("Dentro-run", "run: DATO CONSULTADO ES " + response);
                    },
                    error -> {
                        // Manejo de errores
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

            handler.postDelayed(this, INTERVAL);
        }
    };

    void startRepeatingTask() {
        runnable.run();
    }

    void stopRepeatingTask() {
        handler.removeCallbacks(runnable);
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Cambio en el campo de la base de datos")
                .setContentText("El valor del campo ha cambiado.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}
