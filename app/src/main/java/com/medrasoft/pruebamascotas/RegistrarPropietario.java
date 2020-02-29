package com.medrasoft.pruebamascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.medrasoft.pruebamascotas.data.PropietarioDB;

import java.util.HashMap;

public class RegistrarPropietario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_propietario);

        Button btnRegistrar = (Button) findViewById(R.id.btn_registrar_nuevo_propietario);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etCedula = (EditText) findViewById(R.id.et_cedula);
                final EditText etNombre = (EditText) findViewById(R.id.et_nombre);
                final EditText etTelefono = (EditText) findViewById(R.id.et_telefono);

                long cedula = Long.parseLong(etCedula.getText().toString());
                String nombre = etNombre.getText().toString();
                long telefono = Long.parseLong(etTelefono.getText().toString());

                PropietarioDB dbuser = new PropietarioDB();
                HashMap<String, String> datos = new HashMap<String, String>();
                datos.put(PropietarioDB.CEDULA, cedula+"");
                datos.put(PropietarioDB.NOMBRE, nombre+"");
                datos.put(PropietarioDB.TELEFONO, telefono+"");
                dbuser.insertData(getBaseContext(), datos);

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}
