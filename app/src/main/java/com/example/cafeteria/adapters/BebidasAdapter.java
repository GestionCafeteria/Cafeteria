package com.example.cafeteria.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteria.R;
import com.example.cafeteria.models.Platillo;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BebidasAdapter extends RecyclerView.Adapter<BebidasAdapter.ViewHolderPlatillos> implements View.OnClickListener{

    ArrayList<Platillo> listaBebidas;
    TextView textViewTotal;
    private View.OnClickListener listener;

    public BebidasAdapter(ArrayList<Platillo> listaPlatillos, TextView textViewTotal) {
        this.listaBebidas = listaPlatillos;
        this.textViewTotal = textViewTotal;
    }

    public BebidasAdapter(ArrayList<Platillo> listaPlatillos) {
        this.listaBebidas = listaPlatillos;
    }

    @Override
    public ViewHolderPlatillos onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout= R.layout.item_platillo;
        View view= LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        view.setOnClickListener(this);

        return new ViewHolderPlatillos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderPlatillos holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        Platillo item = listaBebidas.get(position);

        holder.itemNameTextView.setText(item.getNombre());
        holder.itemPriceTextView.setText("$" + decimalFormat.format(item.getPrecio()) + " c/u");
        holder.quantityTextView.setText(String.valueOf(item.getCantidad()));

        holder.decreaseButton.setOnClickListener(v -> {
            int newQuantity = item.getCantidad() - 1;
            if (newQuantity >= 0) {
                double totalActual = Double.parseDouble(textViewTotal.getText().toString());
                totalActual = totalActual - item.getPrecio();
                textViewTotal.setText(String.valueOf(totalActual));

                item.setCantidad(newQuantity);
                notifyDataSetChanged();
            }
        });

        holder.increaseButton.setOnClickListener(v -> {
            int newQuantity = item.getCantidad() + 1;

            double totalActual = Double.parseDouble(textViewTotal.getText().toString());
            totalActual = totalActual + item.getPrecio();
            textViewTotal.setText(String.valueOf(totalActual));

            item.setCantidad(newQuantity);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listaBebidas.size();
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

    public class ViewHolderPlatillos extends RecyclerView.ViewHolder {

        TextView itemNameTextView, itemPriceTextView, quantityTextView;
        Button decreaseButton, increaseButton;

        public ViewHolderPlatillos(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemName);
            itemPriceTextView = itemView.findViewById(R.id.itemPrice);
            quantityTextView = itemView.findViewById(R.id.quantityText);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            increaseButton = itemView.findViewById(R.id.increaseButton);
        }
    }

    private void actualizarTotal() {

    }
}
