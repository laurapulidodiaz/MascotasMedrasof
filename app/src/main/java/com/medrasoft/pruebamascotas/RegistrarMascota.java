package com.medrasoft.pruebamascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medrasoft.pruebamascotas.data.MascotaDB;
import com.medrasoft.pruebamascotas.data.PropietarioDB;
import com.medrasoft.pruebamascotas.model.Mascota;
import com.medrasoft.pruebamascotas.model.Propietario;

import java.util.HashMap;
import java.util.List;

public class RegistrarMascota extends AppCompatActivity {
    ArrayAdapter<String> adaptadorPropietarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mascota);

        final Spinner spTipo = (Spinner) findViewById(R.id.sp_tipo_mascota);
        final TextView tvPropietario = (TextView) findViewById(R.id.tv_propietario_mascota);
        EditText etBuscaPropietario = (EditText) findViewById(R.id.et_propietario_mascota);
        ListView lvPropietarios = (ListView) findViewById(R.id.list_propietarios);
        Button btnVolver = (Button) findViewById(R.id.btn_cancelar_nm);
        Button btnRegistrar = (Button) findViewById(R.id.btn_registrar_nueva_mascota);

        String[] tiposMascota = new String[]{"Perro","Gato","Pájaro"};
        ArrayAdapter<String> adaptadorTipoMascota = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,tiposMascota);
        spTipo.setAdapter(adaptadorTipoMascota);

        inicializarAdaptadorPropietarios();
        lvPropietarios.setTextFilterEnabled(true);
        lvPropietarios.setAdapter(adaptadorPropietarios);

        lvPropietarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                tvPropietario.setText(adaptadorPropietarios.getItem(position));
                tvPropietario.setTextColor(Color.GREEN);
                Toast.makeText(getApplicationContext(), "Propietario seleccionado: "+adaptadorPropietarios.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

        etBuscaPropietario.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                RegistrarMascota.this.adaptadorPropietarios.getFilter().filter(arg0);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) { }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String propietario = tvPropietario.getText().toString();
                if(propietario.startsWith("No ha seleccionado")){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar un propietario.", Toast.LENGTH_SHORT).show();
                } else {
                    final EditText etCedula = (EditText) findViewById(R.id.et_cedula_mascota);
                    final EditText etNombre = (EditText) findViewById(R.id.et_nombre_mascota);
                    final EditText etEdad = (EditText) findViewById(R.id.et_edad_mascota);
                    final EditText etRaza = (EditText) findViewById(R.id.et_raza_mascota);

                    int cedula = Integer.parseInt(etCedula.getText().toString());
                    String nombre = etNombre.getText().toString();
                    int edad = Integer.parseInt(etEdad.getText().toString());
                    String raza = etRaza.getText().toString();
                    String tipo = spTipo.getSelectedItem().toString();

                    MascotaDB dbuser = new MascotaDB();
                    HashMap<String, String> datos = new HashMap<String, String>();
                    datos.put(MascotaDB.CEDULA, cedula+"");
                    datos.put(MascotaDB.NOMBRE, nombre+"");
                    datos.put(MascotaDB.EDAD, edad+"");
                    datos.put(MascotaDB.RAZA, raza+"");
                    datos.put(MascotaDB.TIPO, tipo+"");
                    datos.put(MascotaDB.PROPIETARIO_ID, getPropietarioId(propietario)+"");
                    dbuser.insertData(getBaseContext(), datos);

                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void inicializarAdaptadorPropietarios(){
        List<Propietario> propietarios = (List<Propietario>) (new PropietarioDB()).getAllDataObjects(this.getBaseContext(), Propietario.class, null, null);
        String[] values = new String[propietarios.size()];
        for(int i=0; i<propietarios.size(); i++) values[i]=propietarios.get(i).getNombre();
        adaptadorPropietarios = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,values);
    }

    private int getPropietarioId(String nombrepropietario){
        List<Propietario> propietarios = (List<Propietario>) (new PropietarioDB()).getAllDataObjects(this.getBaseContext(), Propietario.class, PropietarioDB.NOMBRE + " like '" + nombrepropietario + "'", null);
        return propietarios.get(0).getId();
    }
}
