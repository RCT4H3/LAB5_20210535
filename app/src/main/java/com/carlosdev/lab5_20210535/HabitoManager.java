package com.carlosdev.lab5_20210535;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HabitoManager {
    private static final String PREF_NAME = "HabitosPreferences";
    private static final String KEY_HABITOS = "lista_habitos";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public HabitoManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void guardarHabitos(List<Habito> habitos) {
        String jsonHabitos = gson.toJson(habitos);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_HABITOS, jsonHabitos);
        editor.apply();
    }

    public List<Habito> cargarHabitos() {
        String jsonHabitos = sharedPreferences.getString(KEY_HABITOS, "");

        if (jsonHabitos.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            Type listType = new TypeToken<List<Habito>>(){}.getType();
            List<Habito> habitos = gson.fromJson(jsonHabitos, listType);
            return habitos != null ? habitos : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void agregarHabito(Habito habito) {
        List<Habito> habitos = cargarHabitos();
        habitos.add(habito);
        guardarHabitos(habitos);
    }

    public void eliminarHabito(int posicion) {
        List<Habito> habitos = cargarHabitos();
        if (posicion >= 0 && posicion < habitos.size()) {
            habitos.remove(posicion);
            guardarHabitos(habitos);
        }
    }

    public void actualizarHabito(int posicion, Habito habitoActualizado) {
        List<Habito> habitos = cargarHabitos();
        if (posicion >= 0 && posicion < habitos.size()) {
            habitos.set(posicion, habitoActualizado);
            guardarHabitos(habitos);
        }
    }

    public void limpiarHabitos() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_HABITOS);
        editor.apply();
    }

    public boolean tieneHabitos() {
        return !cargarHabitos().isEmpty();
    }

    public int contarHabitos() {
        return cargarHabitos().size();
    }
}