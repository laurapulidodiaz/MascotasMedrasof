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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.medrasoft.pruebamascotas.data.MascotaDB;
import com.medrasoft.pruebamascotas.data.VacunaDB;
import com.medrasoft.pruebamascotas.model.Mascota;
import com.medrasoft.pruebamascotas.model.Vacuna;

import java.util.HashMap;
import java.util.List;

public class RegistrarVacuna extends AppCompatActivity {
    ArrayAdapter<String> adaptadorMascotas;
    ArrayAdapter<String> adaptadorVacunas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_vacuna);

        final LinearLayout lyBuscarMascota = (LinearLayout) findViewById(R.id.buscamascotaly);
        final LinearLayout lyVacunasPrevias = (LinearLayout) findViewById(R.id.vacunaspreviasly);
        final LinearLayout lyNuevaVacuna = (LinearLayout) findViewById(R.id.nuevavacunaly);

        final TextView tvMascota = (TextView) findViewById(R.id.tv_mascota);
        EditText etBuscaMascota = (EditText) findViewById(R.id.et_busca_mascota);
        ListView lvMascotas = (ListView) findViewById(R.id.list_mascotas);
        Button btnVolver = (Button) findViewById(R.id.btn_volver_vacuna);

        ListView lvVacunas = (ListView) findViewById(R.id.list_vacunas);

        final EditText etNombre = (EditText) findViewById(R.id.et_nombre_vacuna);
        final EditText etFecha = (EditText) findViewById(R.id.et_fecha_vacuna);
        final EditText etDosis = (EditText) findViewById(R.id.et_dosis_vacuna);

        lyBuscarMascota.setVisibility(View.VISIBLE);
        lyVacunasPrevias.setVisibility(View.GONE);
        lyNuevaVacuna.setVisibility(View.GONE);

        Button btnCancelar = (Button) findViewById(R.id.btn_cancelar_vacuna);
        Button btnRegistrar = (Button) findViewById(R.id.btn_registrar_nueva_vacuna);

        //Llenar adaptador de mascotas y filtro de busqueda
        inicializarAdaptadorMascotas();
        lvMascotas.setTextFilterEnabled(true);
        lvMascotas.setAdapter(adaptadorMascotas);

        lvMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                tvMascota.setText(adaptadorMascotas.getItem(position));
                tvMascota.setTextColor(Color.GREEN);
                inicializarAdaptadorVacunas(adaptadorMascotas.getItem(position));
                Toast.makeText(getApplicationContext(), "Mascota seleccionada: "+adaptadorMascotas.getItem(position), Toast.LENGTH_SHORT).show();
                lyBuscarMascota.setVisibility(View.GONE);
                lyVacunasPrevias.setVisibility(View.VISIBLE);
                lyNuevaVacuna.setVisibility(View.VISIBLE);
            }
        });

        etBuscaMascota.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                RegistrarVacuna.this.adaptadorMascotas.getFilter().filter(arg0);
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

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int dosis = Integer.parseInt(etDosis.getText().toString());
                    String nombre = etNombre.getText().toString();
                    String fecha = etFecha.getText().toString();
                    String mascota = tvMascota.getText().toString();

                    VacunaDB dbuser = new VacunaDB();
                    HashMap<String, String> datos = new HashMap<String, String>();
                    datos.put(VacunaDB.DOSIS, dosis+"");
                    datos.put(VacunaDB.NOMBRE, nombre+"");
                    datos.put(VacunaDB.FECHA, fecha+"");
                    datos.put(VacunaDB.MASCOTA_ID, getMascotaId(mascota)+"");
                    dbuser.insertData(getBaseContext(), datos);

                    setResult(Activity.RESULT_OK);
                    finish();
            }
        });

    }

    private void inicializarAdaptadorMascotas(){
        List<Mascota> mascotas = (List<Mascota>) (new MascotaDB()).getAllDataObjects(this.getBaseContext(), Mascota.class, null, null);
        String[] values = new String[mascotas.size()];
        for(int i=0; i<mascotas.size(); i++) values[i]=mascotas.get(i).getNombre();
        adaptadorMascotas = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,values);
    }

    private int getMascotaId(String nombremascota){
        List<Mascota> mascotas = (List<Mascota>) (new MascotaDB()).getAllDataObjects(this.getBaseContext(), Mascota.class, MascotaDB.NOMBRE + " like '" + nombremascota + "'", null);
        return mascotas.get(0).getId();
    }

    private void inicializarAdaptadorVacunas(String nombreMascota){
        List<Vacuna> vacunas = (List<Vacuna>) (new VacunaDB()).getAllDataObjects(this.getBaseContext(), Vacuna.class, VacunaDB.MASCOTA_ID +"=?", new String[]{getMascotaId(nombreMascota)+""});
        String[] values = new String[vacunas.size()];
        for(int i=0; i<vacunas.size(); i++) values[i]=vacunas.get(i).getNombre();
        adaptadorVacunas = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,values);
    }
}
