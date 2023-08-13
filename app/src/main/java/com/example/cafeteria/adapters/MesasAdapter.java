package com.example.cafeteria.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteria.R;
import com.example.cafeteria.models.Mesa;

import java.util.ArrayList;

public class MesasAdapter extends RecyclerView.Adapter<MesasAdapter.ViewHolderMesas> implements View.OnClickListener{

    ArrayList<Mesa> listaMesas;
    private View.OnClickListener listener;

    public MesasAdapter(ArrayList<Mesa> listaMesas) {
        this.listaMesas = listaMesas;
    }

    @Override
    public ViewHolderMesas onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout= R.layout.item_mesa;
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,null,false);

        view.setOnClickListener(this);

        return new ViewHolderMesas(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderMesas holder, int position) {
        int color = 0;
        if (listaMesas.get(position).getEstado() == 0) {
            color = Color.rgb(109, 209, 149);
        } else {
            color = Color.rgb(227, 170, 120);
        }
        holder.etiNombre.setBackgroundColor(color);

        String texto = "Mesa #" + listaMesas.get(position).getId();
        holder.etiNombre.setText(texto);
    }

    @Override
    public int getItemCount() {
        return listaMesas.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolderMesas extends RecyclerView.ViewHolder {

        TextView etiNombre;

        public ViewHolderMesas(View itemView) {
            super(itemView);
            etiNombre= (TextView) itemView.findViewById(R.id.idNombre);
        }
    }

}
