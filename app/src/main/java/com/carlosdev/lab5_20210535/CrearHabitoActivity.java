package com.carlosdev.lab5_20210535;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CrearHabitoActivity extends AppCompatActivity {

    private TextInputEditText editNombre, editFrecuencia;
    private Spinner spinnerCategoria;
    private Button btnSeleccionarFecha, btnSeleccionarHora, btnGuardar, btnCancelar;
    private TextView textFechaHoraSeleccionada;

    private Calendar fechaHoraSeleccionada;
    private SimpleDateFormat dateFormat;

    private String[] categorias = {"Ejercicio", "Alimentación", "Sueño", "Trabajo", "Estudio", "Salud", "Fiesta", "Tomar Alcohol", "Llorar por jalar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_habito);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarVistas();
        configurarSpinner();
        configurarEventos();

        fechaHoraSeleccionada = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    private void inicializarVistas() {
        editNombre = findViewById(R.id.editNombre);
        editFrecuencia = findViewById(R.id.editFrecuencia);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
        textFechaHoraSeleccionada = findViewById(R.id.textFechaHoraSeleccionada);
    }

    private void configurarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    private void configurarEventos() {
        btnSeleccionarFecha.setOnClickListener(v -> mostrarDatePicker());
        btnSeleccionarHora.setOnClickListener(v -> mostrarTimePicker());
        btnGuardar.setOnClickListener(v -> guardarHabito());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void mostrarDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    fechaHoraSeleccionada.set(Calendar.YEAR, year);
                    fechaHoraSeleccionada.set(Calendar.MONTH, month);
                    fechaHoraSeleccionada.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    actualizarTextoFechaHora();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void mostrarTimePicker() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    fechaHoraSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    fechaHoraSeleccionada.set(Calendar.MINUTE, minute);
                    fechaHoraSeleccionada.set(Calendar.SECOND, 0);
                    actualizarTextoFechaHora();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }

    private void actualizarTextoFechaHora() {
        String fechaHoraTexto = dateFormat.format(fechaHoraSeleccionada.getTime());
        textFechaHoraSeleccionada.setText("Seleccionado: " + fechaHoraTexto);
    }

    private void guardarHabito() {
        String nombre = editNombre.getText().toString().trim();
        String frecuenciaTexto = editFrecuencia.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();

        if (nombre.isEmpty()) {
            editNombre.setError("Ingrese el nombre del hábito");
            editNombre.requestFocus();
            return;
        }

        if (frecuenciaTexto.isEmpty()) {
            editFrecuencia.setError("Ingrese la frecuencia en horas");
            editFrecuencia.requestFocus();
            return;
        }

        int frecuencia;
        try {
            frecuencia = Integer.parseInt(frecuenciaTexto);
            if (frecuencia <= 0) {
                editFrecuencia.setError("La frecuencia debe ser mayor a 0");
                editFrecuencia.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editFrecuencia.setError("Ingrese un número válido");
            editFrecuencia.requestFocus();
            return;
        }

        Habito nuevoHabito = new Habito(nombre, categoria, frecuencia, fechaHoraSeleccionada.getTime());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("nuevo_habito", nuevoHabito);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "Hábito creado exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}