package com.medrasoft.pruebamascotas;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class SeleccionarActividad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_actividad);

        Button btnRegistrarMascota = (Button) findViewById(R.id.btn_registrar_mascota);
        Button btnConsultaEliminacion = (Button) findViewById(R.id.btn_consulta_eliminacion);
        Button btnRegistrarVacuna = (Button) findViewById(R.id.btn_registrar_vacuna);
        Button btnRegistrarPropietario = (Button) findViewById(R.id.btn_registrar_propiertario);
        Button btnSalir = (Button) findViewById(R.id.btn_salir);

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConsultaEliminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ConsultaEliminacion.class);
                startActivity(intent);
            }
        });

        btnRegistrarVacuna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegistrarVacuna.class);
                startActivity(intent);
            }
        });

        btnRegistrarPropietario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegistrarPropietario.class);
                startActivity(intent);
            }
        });

        btnRegistrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegistrarMascota.class);
                startActivity(intent);
            }
        });
    }
}
