package com.carlosdev.lab5_20210535;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int SELECCIONAR_IMAGEN = 100;
    private static final String NOMBRE_IMAGEN = "imagen_usuario.jpg";

    private TextView textSaludo, textMensaje;
    private ImageView imagenPersonal;
    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSaludo = findViewById(R.id.textSaludo);
        textMensaje = findViewById(R.id.textMensaje);
        imagenPersonal = findViewById(R.id.imagenPersonal);
        ImageButton botonEditarNombre = findViewById(R.id.botonEditarNombre);
        ImageButton botonEditarMensaje = findViewById(R.id.botonEditarMensaje);
        Button botonConfiguraciones = findViewById(R.id.botonConfiguraciones); // si lo usas
        Button botonHabitos = findViewById(R.id.botonHabitos); // si lo usas

        preferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);

        String nombre = preferencias.getString("nombre", "Usuario");
        String mensaje = preferencias.getString("mensaje", "¡Tú puedes borracho!!");

        textSaludo.setText("¡Hola, " + nombre + "!");
        textMensaje.setText(mensaje);

        cargarImagen();

        imagenPersonal.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECCIONAR_IMAGEN);
        });

        botonEditarNombre.setOnClickListener(v -> mostrarDialogoNombre(nombre));

        botonEditarMensaje.setOnClickListener(v -> mostrarDialogoMensaje(mensaje));

        botonConfiguraciones.setOnClickListener(v -> {
            // startActivity(new Intent(this, ConfiguracionesActivity.class));
        });

        botonHabitos.setOnClickListener(v -> {
            startActivity(new Intent(this, HabitosActivity.class));
        });
    }

    //Se Uso IA para tener una guia de como hacer un Dialog para modifciar los dos campos (el saludo y la frase)
    private void mostrarDialogoNombre(String nombreActual) {
        EditText entrada = new EditText(this);
        entrada.setText(nombreActual);

        new AlertDialog.Builder(this)
                .setTitle("Editar nombre")
                .setView(entrada)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevoNombre = entrada.getText().toString().trim();
                    if (!nuevoNombre.isEmpty()) {
                        SharedPreferences.Editor editor = preferencias.edit();
                        editor.putString("nombre", nuevoNombre);
                        editor.apply();
                        textSaludo.setText("¡Hola, " + nuevoNombre + "!");
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoMensaje(String mensajeActual) {
        EditText entrada = new EditText(this);
        entrada.setText(mensajeActual);

        new AlertDialog.Builder(this)
                .setTitle("Editar mensaje motivacional")
                .setView(entrada)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevoMensaje = entrada.getText().toString().trim();
                    if (!nuevoMensaje.isEmpty()) {
                        SharedPreferences.Editor editor = preferencias.edit();
                        editor.putString("mensaje", nuevoMensaje);
                        editor.apply();
                        textMensaje.setText(nuevoMensaje);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void guardarImagen(Bitmap bitmap) {
        try (FileOutputStream fos = openFileOutput(NOMBRE_IMAGEN, MODE_PRIVATE)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            Log.e("MainActivity", "Error al guardar imagen", e);
        }
    }

    private void cargarImagen() {
        try {
            File archivo = new File(getFilesDir(), NOMBRE_IMAGEN);
            if (archivo.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(archivo));
                imagenPersonal.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            Log.e("MainActivity", "Error al cargar imagen", e);
        }
    }

    @Override
    protected void onActivityResult(int codigo, int resultado, @Nullable Intent datos) {
        super.onActivityResult(codigo, resultado, datos);
        if (codigo == SELECCIONAR_IMAGEN && resultado == RESULT_OK && datos != null) {
            Uri uriImagen = datos.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriImagen);
                imagenPersonal.setImageBitmap(bitmap);
                guardarImagen(bitmap);
            } catch (IOException e) {
                Log.e("MainActivity", "Error al obtener imagen", e);
            }
        }
    }
}