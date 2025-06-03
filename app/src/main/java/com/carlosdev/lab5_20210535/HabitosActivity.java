package com.carlosdev.lab5_20210535;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitosActivity extends AppCompatActivity {

    private Button btnNuevoHabito;
    private RecyclerView recyclerHabitos;
    private HabitoAdapter habitoAdapter;
    private List<Habito> listaHabitos;
    private HabitoManager habitoManager;

    private ActivityResultLauncher<Intent> crearHabitoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habitos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        habitoManager = new HabitoManager(this);

        inicializarVistas();
        configurarRecyclerView();
        configurarEventos();
        inicializarLauncher();
        cargarHabitosGuardados();
    }

    private void inicializarVistas() {
        btnNuevoHabito = findViewById(R.id.btnNuevoHabito);
        recyclerHabitos = findViewById(R.id.recyclerHabitos);
    }

    private void configurarRecyclerView() {
        habitoAdapter = new HabitoAdapter();

        habitoAdapter.setOnHabitoClickListener(new HabitoAdapter.OnHabitoClickListener() {
            @Override
            public void onHabitoClick(int position, Habito habito) {
                Toast.makeText(HabitosActivity.this,
                        "Hábito: " + habito.getNombre(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHabitoLongClick(int position, Habito habito) {
                mostrarDialogoEliminar(position, habito);
            }
        });

        recyclerHabitos.setLayoutManager(new LinearLayoutManager(this));
        recyclerHabitos.setAdapter(habitoAdapter);
    }

    private void configurarEventos() {
        btnNuevoHabito.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearHabitoActivity.class);
            crearHabitoLauncher.launch(intent);
        });
    }

    private void inicializarLauncher() {
        crearHabitoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Habito nuevoHabito = (Habito) result.getData().getSerializableExtra("nuevo_habito");
                        if (nuevoHabito != null) {
                            habitoManager.agregarHabito(nuevoHabito);

                            cargarHabitosGuardados();

                            Toast.makeText(this, "Hábito guardado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void mostrarDialogoEliminar(int position, Habito habito) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Hábito")
                .setMessage("¿Estás seguro de que deseas eliminar el hábito '" + habito.getNombre() + "'?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    habitoManager.eliminarHabito(position);

                    cargarHabitosGuardados();

                    Toast.makeText(this, "Hábito eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cargarHabitosGuardados() {
        listaHabitos = habitoManager.cargarHabitos();
        habitoAdapter.actualizarHabitos(listaHabitos);

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarHabitosGuardados();
    }
}