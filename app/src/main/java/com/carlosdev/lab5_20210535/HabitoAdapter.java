package com.carlosdev.lab5_20210535;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HabitoAdapter extends RecyclerView.Adapter<HabitoAdapter.HabitoViewHolder> {

    private List<Habito> listaHabitos;
    private SimpleDateFormat dateFormat;
    private OnHabitoClickListener listener;

    public interface OnHabitoClickListener {
        void onHabitoClick(int position, Habito habito);
        void onHabitoLongClick(int position, Habito habito);
    }

    public HabitoAdapter() {
        this.listaHabitos = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    public void setOnHabitoClickListener(OnHabitoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habito, parent, false);
        return new HabitoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitoViewHolder holder, int position) {
        Habito habito = listaHabitos.get(position);

        holder.textNombre.setText(habito.getNombre());
        holder.textCategoria.setText("Categoría: " + habito.getCategoria());
        holder.textFrecuencia.setText("Cada " + habito.getFrecuenciaHoras() + " horas");
        holder.textFechaHora.setText("Inicio: " + dateFormat.format(habito.getFechaHoraInicio()));

        // Configurar clicks
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHabitoClick(position, habito);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onHabitoLongClick(position, habito);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaHabitos.size();
    }

    public void actualizarHabitos(List<Habito> nuevosHabitos) {
        this.listaHabitos.clear();
        this.listaHabitos.addAll(nuevosHabitos);
        notifyDataSetChanged();
    }

    public void agregarHabito(Habito habito) {
        this.listaHabitos.add(habito);
        notifyItemInserted(listaHabitos.size() - 1);
    }

    static class HabitoViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textCategoria, textFrecuencia, textFechaHora;

        public HabitoViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombreHabito);
            textCategoria = itemView.findViewById(R.id.textCategoriaHabito);
            textFrecuencia = itemView.findViewById(R.id.textFrecuenciaHabito);
            textFechaHora = itemView.findViewById(R.id.textFechaHoraHabito);
        }
    }
}