package net.marcarni.easycheck;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import net.marcarni.easycheck.RecyclerView.HeaderAdapter_Consulta;
import net.marcarni.easycheck.RecyclerView.Header_Consulta;
import net.marcarni.easycheck.Utils.DescargaServei;
import net.marcarni.easycheck.Utils.DescargaTreballador;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static net.marcarni.easycheck.R.id.cancelar_filtros;
import static net.marcarni.easycheck.R.id.seleccionar_hora;

public class ConsultaServeisOnlineActivity extends MenuAppCompatActivity implements View.OnClickListener {
    private static final int DATE_PICKER_REQUEST = 22;
    private static final int HOUR_PICKER_REQUEST = 25;
    private String fecha = null;
    private HeaderAdapter_Consulta headerAdapter_consulta;
    private ArrayList<Header_Consulta> myDataset;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Spinner spinnerTreballadors;
    private ArrayList<Treballador> treballadors;

    List<Treballador> listaTreballadors = new ArrayList<>();
    List<Servei> llistaServeis = new ArrayList<>();
    List<Servei> serveisTreballador = new ArrayList<>();
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_serveis_online);
        Toolbar editToolbar = (Toolbar) findViewById(R.id.filter_toolbar);
        editToolbar.inflateMenu(R.menu.toolbar_menu);
        spinnerTreballadors = (Spinner) findViewById(R.id.spinner_de_treballadors);
        myDataset = new ArrayList<>();
        headerAdapter_consulta = new HeaderAdapter_Consulta(myDataset);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_consulta);

        if (fecha == null) {
            (findViewById(seleccionar_hora)).setVisibility(View.INVISIBLE);
            (findViewById(cancelar_filtros)).setVisibility(View.INVISIBLE);
        }
        new descargarTreballadors().execute(DescargaTreballador.IP);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seleccionar_data:
                PickerData();
                break;

        }
    }

    public void PickerData() {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(ConsultaServeisOnlineActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                selectedmonth = selectedmonth + 1;
                fecha = "" + selectedday + "/" + selectedmonth + "/" + selectedyear;

            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Selecciona Data");

        mDatePicker.show();

    }


    private class descargarTreballadors extends AsyncTask<String, ArrayList, ArrayList<Treballador>> {


        @Override
        protected void onPostExecute(ArrayList<Treballador> s) {
            super.onPostExecute(s);
            headerAdapter_consulta.notifyDataSetChanged();
            linearLayoutManager = new LinearLayoutManager(ConsultaServeisOnlineActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(headerAdapter_consulta);
            implementarSpinner();
            (findViewById(R.id.seleccionar_data)).setOnClickListener(ConsultaServeisOnlineActivity.this);

        }

        protected ArrayList<Treballador> doInBackground(String... urls) {
            treballadors = (ArrayList<Treballador>) RetornaTreballadors();
            llistaServeis = RetornaServeis();
            return null;

        }

        private void implementarSpinner() {
            cursor = getCursorSpinner();

            android.widget.SimpleCursorAdapter adapter = new android.widget.SimpleCursorAdapter(ConsultaServeisOnlineActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    cursor,
                    new String[]{"nom"},
                    new int[]{android.R.id.text1}, 0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerTreballadors.setAdapter(adapter);


            spinnerTreballadors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                    int id_treballador = Integer.parseInt(cursor.getString(0));
                    if (id_treballador == 0) {
                        headerAdapter_consulta.actualitzaRecycler(retornaHeader((ArrayList<Servei>) llistaServeis));
                    } else {

                        headerAdapter_consulta.actualitzaRecycler((ArrayList<Header_Consulta>) RetornaServeisPerId(id_treballador));


                        Toast.makeText(parent.getContext(), cursor.getString(1) + cursor.getString(0), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }


    }

    public List<Treballador> RetornaTreballadors() {
        return listaTreballadors = (ArrayList<Treballador>) DescargaTreballador.obtenirTreballadorsDelServer();

    }

    public List<Servei> RetornaServeis() {
        return llistaServeis = DescargaServei.obtenirServeisDelServer(DescargaTreballador.IP);
    }


    private Cursor getCursorSpinner() {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "nom"});
        cursor.addRow(new String[]{"0", "Tots"});
        for (Treballador treb : listaTreballadors) {
            cursor.addRow(new String[]{String.valueOf(treb.getId()), treb.getNom()});
        }

        return cursor;
    }

    public ArrayList retornaHeader(ArrayList<Servei> s) {
        myDataset = new ArrayList<>();
        for (int i = 0; i < s.size(); i++) {
            int idTreb = (s.get(i).getId_treballador());
            String nom = treballadors.get(idTreb).getNom();
            String descripcio = s.get(i).getDescripcio();
            String idServei = String.valueOf(s.get(i).getId());
            String data = s.get(i).getData_servei();
            String hora_ini = s.get(i).getHora_inici();
            String hora_fi = s.get(i).getHora_final();

            myDataset.add(new Header_Consulta(RetornaNomTreballador(idTreb), descripcio, idServei, data, hora_ini, hora_fi));
        }

        return myDataset;
    }

    public String RetornaNomTreballador(int id) {
        String nom = null;
        for (int i = 0; i < treballadors.size(); i++) {
            if (treballadors.get(i).getId() == id) {
                nom = treballadors.get(i).getNom() + " " + treballadors.get(i).getCognom1() + " " + treballadors.get(i).getCognom2();
            }
        }
        return nom;
    }

    public List RetornaServeisPerId(int id) {
        myDataset = new ArrayList<>();
        for (int i = 0; i < llistaServeis.size(); i++) {
            if (llistaServeis.get(i).getId_treballador() == id) {
                myDataset.add(new Header_Consulta(RetornaNomTreballador(id)
                        , llistaServeis.get(i).getDescripcio()
                        , String.valueOf(llistaServeis.get(i).getId()), llistaServeis.get(i).getData_servei(),
                        llistaServeis.get(i).getHora_inici(), llistaServeis.get(i).getHora_final()));
            }
        }
        return myDataset;
    }


}