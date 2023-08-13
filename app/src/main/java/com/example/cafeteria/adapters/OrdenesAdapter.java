package com.example.cafeteria.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cafeteria.R;
import com.example.cafeteria.activities.Login;
import com.example.cafeteria.models.Orden;
import com.example.cafeteria.utils.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrdenesAdapter extends RecyclerView.Adapter<OrdenesAdapter.ViewHolderOrdenes> implements View.OnClickListener {

    ArrayList<Orden> listaOrdenes;
    Context context;
    private View.OnClickListener listener;

    public OrdenesAdapter(ArrayList<Orden> listaOrdenes, Context context) {
        this.listaOrdenes = listaOrdenes;
        this.context = context;
    }

    @Override
    public ViewHolderOrdenes onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.orden_platillo_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        view.setOnClickListener(this);

        return new ViewHolderOrdenes(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderOrdenes holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        deshabilitarBotones(holder);
        switch (listaOrdenes.get(position).getEstatus()) {
            case 1:
                if (Constantes.usuario.getRol() == 2) {
                    holder.aproveButton.setEnabled(true);
                    holder.aproveButton.setVisibility(View.VISIBLE);
                    holder.aproveButton.setOnClickListener(v -> {
                        actualizarOrden(listaOrdenes.get(position).getIdOrden(), 2);
                        eliminarElementoLista(listaOrdenes.get(position).getIdOrden());
                        notifyDataSetChanged();
                    });
                }
                break;
            case 2:
                if (Constantes.usuario.getRol() == 3) {
                    holder.preparingButton.setEnabled(true);
                    holder.preparingButton.setVisibility(View.VISIBLE);
                    holder.preparingButton.setOnClickListener(v -> {
                        actualizarOrden(listaOrdenes.get(position).getIdOrden(), 3);
                        eliminarElementoLista(listaOrdenes.get(position).getIdOrden());
                        notifyDataSetChanged();
                    });
                }
                break;
            case 3:
                if (Constantes.usuario.getRol() == 3) {
                    holder.preparedButton.setEnabled(true);
                    holder.preparedButton.setVisibility(View.VISIBLE);
                    holder.preparedButton.setOnClickListener(v -> {
                        actualizarOrden(listaOrdenes.get(position).getIdOrden(), 4);
                        eliminarElementoLista(listaOrdenes.get(position).getIdOrden());
                        notifyDataSetChanged();
                    });
                }
                break;
            case 4:
                if (Constantes.usuario.getRol() == 2) {
                    holder.deliveredButton.setEnabled(true);
                    holder.deliveredButton.setVisibility(View.VISIBLE);
                    holder.deliveredButton.setOnClickListener(v -> {
                        actualizarOrden(listaOrdenes.get(position).getIdOrden(), 5);
                        eliminarElementoLista(listaOrdenes.get(position).getIdOrden());
                        notifyDataSetChanged();
                    });
                }
                break;
        }

        holder.txtID.setText(String.valueOf(listaOrdenes.get(position).getIdOrden()));
        holder.txtMesa.setText(String.valueOf(listaOrdenes.get(position).getMesa()));
        holder.txtTotal.setText("$" + decimalFormat.format(listaOrdenes.get(position).getTotal()));
    }

    public void deshabilitarBotones(ViewHolderOrdenes holder) {
        holder.aproveButton.setEnabled(false);
        holder.aproveButton.setVisibility(View.INVISIBLE);

        holder.preparingButton.setEnabled(false);
        holder.preparingButton.setVisibility(View.INVISIBLE);

        holder.preparedButton.setEnabled(false);
        holder.preparedButton.setVisibility(View.INVISIBLE);

        holder.deliveredButton.setEnabled(false);
        holder.deliveredButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return listaOrdenes.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public class ViewHolderOrdenes extends RecyclerView.ViewHolder {

        TextView txtID, txtTotal, txtMesa;
        Button aproveButton, preparingButton, preparedButton, deliveredButton;

        public ViewHolderOrdenes(View itemView) {
            super(itemView);
            txtID = (TextView) itemView.findViewById(R.id.itemID);
            txtTotal = (TextView) itemView.findViewById(R.id.itemTotal);
            txtMesa = (TextView) itemView.findViewById(R.id.itemMesa);

            aproveButton = (Button) itemView.findViewById(R.id.aproveButton);
            preparingButton = (Button) itemView.findViewById(R.id.preparingButton);
            preparedButton = (Button) itemView.findViewById(R.id.preparedButton);
            deliveredButton = (Button) itemView.findViewById(R.id.deliveredButton);

        }
    }

    private void actualizarOrden(int idOrden, int nuevoEstatus) {
        RequestQueue queue = Volley.newRequestQueue(context);

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
                            Toast.makeText(context, response.getString("mensaje"), Toast.LENGTH_SHORT).show();
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

    public void eliminarElementoLista(int id) {
        Orden ordenAEliminar = null;
        for (Orden orden : listaOrdenes) {
            if (orden.getIdOrden() == id) {
                ordenAEliminar = orden;
                break;
            }
        }

        if (ordenAEliminar != null) {
            listaOrdenes.remove(ordenAEliminar);
        }
    }
}
