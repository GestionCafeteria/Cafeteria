package com.example.cafeteria.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteria.R;
import com.example.cafeteria.models.OrdenPlatillo;
import com.example.cafeteria.models.Platillo;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlatillosOrdenAdapter extends RecyclerView.Adapter<PlatillosOrdenAdapter.ViewHolderPlatillosOrden> implements View.OnClickListener{

    ArrayList<OrdenPlatillo> listaPlatillosOrden;
    private View.OnClickListener listener;

    public PlatillosOrdenAdapter(ArrayList<OrdenPlatillo> listaPlatillosOrden) {
        this.listaPlatillosOrden = listaPlatillosOrden;
    }

    @Override
    public ViewHolderPlatillosOrden onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout= R.layout.orden_platillo_item;
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,null,false);

        view.setOnClickListener(this);

        return new ViewHolderPlatillosOrden(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderPlatillosOrden holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        OrdenPlatillo item = listaPlatillosOrden.get(position);

        holder.platilloTextView.setText(item.getPlatillo());
        holder.cantidadTextView.setText(String.valueOf(item.getCantidad()));
        holder.precioTextView.setText("$" + decimalFormat.format(item.getPrecio()) + " c/u");
    }

    @Override
    public int getItemCount() {
        return listaPlatillosOrden.size();
    }


    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolderPlatillosOrden extends RecyclerView.ViewHolder {

        TextView platilloTextView, cantidadTextView, precioTextView;

        public ViewHolderPlatillosOrden(View itemView) {
            super(itemView);
            platilloTextView = itemView.findViewById(R.id.tv_platillo);
            cantidadTextView = itemView.findViewById(R.id.tv_cantidad);
            precioTextView = itemView.findViewById(R.id.tv_precio);
        }
    }

}
