package net.marcarni.easycheck;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TimePicker;
import android.widget.Toast;

import net.marcarni.easycheck.RecyclerView.HeaderAdapter_Consulta;
import net.marcarni.easycheck.RecyclerView.Header_Consulta;
import net.marcarni.easycheck.Utils.DescargaServei;
import net.marcarni.easycheck.Utils.DescargaTreballador;
import net.marcarni.easycheck.eines.isConnect;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static net.marcarni.easycheck.R.id.cancelar_filtros;
import static net.marcarni.easycheck.R.id.seleccionar_hora;

public class ConsultaServeisOnlineActivity extends MenuAppCompatActivity implements View.OnLongClickListener {

    private String fecha = null, time = null;
    private HeaderAdapter_Consulta headerAdapter_consulta;
    private ArrayList<Header_Consulta> myDataset;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Spinner spinnerTreballadors;
    private ArrayList<Treballador> treballadors;
    private int treballador;
    List<Treballador> listaTreballadors = new ArrayList<>();
    List<Servei> llistaServeis = new ArrayList<>();
    Cursor cursor;
    public String isAdmin;
    String ID_treballador = null;

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
            setInvisible();
        }
        isAdmin = LoginActivity.IS_ADMIN;
        ID_treballador = LoginActivity.ID_TREBALLADOR;


        findViewById(R.id.seleccionar_data).setOnLongClickListener(this);
        findViewById(R.id.seleccionar_hora).setOnLongClickListener(this);
        new DescarregaServer().execute(LoginActivity.IP);

    }

    @Override
    public boolean onLongClick(View view) {
        String variable = null, titol = null, vacio = null;
        int icon = 0;
        switch (view.getId()) {
            case R.id.seleccionar_data:
                variable = fecha;
                titol = " Data Seleccionada:";
                vacio = "Selecciona data!";
                icon = R.drawable.icon_data;
                break;
            case R.id.seleccionar_hora:
                variable = time;
                titol = "Hora Seleccionada:";
                vacio = "Selecciona hora!";
                icon = R.drawable.icon_hora;
                break;
        }
        if (variable != null) {
            MissatgeClick(variable, titol, icon);

        } else {
            missatge(vacio);
        }
        return false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////              AsynTask              //////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private class DescarregaServer extends AsyncTask<String, ArrayList, ArrayList<Treballador>> {


        @Override
        protected void onPostExecute(ArrayList<Treballador> s) {
            super.onPostExecute(s);

            headerAdapter_consulta.notifyDataSetChanged();
            linearLayoutManager = new LinearLayoutManager(ConsultaServeisOnlineActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(headerAdapter_consulta);

            implementarSpinner();

            (findViewById(R.id.seleccionar_data)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PickerData();
                    missatge(fecha);

                }
            });
            (findViewById(R.id.seleccionar_hora)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PickerHora();
                }
            });

            (findViewById(R.id.cancelar_filtros)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fecha = null;
                    time = null;
                    treballador = 0;
                    setInvisible();
                    if (isConnect.isDisponible(ConsultaServeisOnlineActivity.this)) {
                        new DescarregaServer().execute(LoginActivity.IP);
                        implementarSpinner();
                        llistaSenseFiltrats();
                    } else {
                        missatgeNoConnexio();
                    }


                }


            });

        }

        protected ArrayList<Treballador> doInBackground(String... urls) {

            treballadors = (ArrayList<Treballador>) RetornaTreballadors();
            llistaServeis = RetornaServeis();
            return null;

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////             SPINNER TREBALLADORS            /////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        private Cursor getCursorSpinner() {
            MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "nom"});
            cursor.addRow(new String[]{"0", "Tots"});
            for (Treballador treb : listaTreballadors) {
                cursor.addRow(new String[]{String.valueOf(treb.getId()), treb.getNom()});
            }

            return cursor;
        }

        private Cursor getCursorTreballador() {
            MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "nom"});
            cursor.addRow(new String[]{String.valueOf(ID_treballador), RetornaNoAdmin(Integer.parseInt(ID_treballador))});


            return cursor;
        }

        private void implementarSpinner() {
            if (isAdmin.equals("1"))
                cursor = getCursorSpinner();
            else
                cursor = getCursorTreballador();

            android.widget.SimpleCursorAdapter adapter = new android.widget.SimpleCursorAdapter(
                    ConsultaServeisOnlineActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    cursor,
                    new String[]{"nom"},
                    new int[]{android.R.id.text1}, 0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTreballadors.setAdapter(adapter);
            spinnerTreballadors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                    treballador = Integer.parseInt(cursor.getString(0));
                    if (fecha == null) {
                        // Tots sense data
                        if (treballador == 0) {
                            llistaSenseFiltrats();
                        } else {
                            llistaPerTreballador();
                        }

                    } else if (fecha != null && time != null) {
                        if (cursor.getInt(0) == 0) {
                            myDataset = new ArrayList<Header_Consulta>();
                            myDataset = RetornaTotElsServeisPerDataiHora(fecha, time);
                            headerAdapter_consulta.actualitzaRecycler(myDataset);
                        } else {
                            myDataset = new ArrayList<Header_Consulta>();
                            myDataset = RetornaTotsElsServeisPerTreballadorDataIHora(cursor.getInt(0), fecha, time);
                            headerAdapter_consulta.actualitzaRecycler(myDataset);
                        }

                    } else if (time == null && fecha != null) {
                        if (treballador == 0) {
                            carregarDataTreballador();
                        } else {
                            myDataset = new ArrayList<Header_Consulta>();
                            myDataset = RetornaTotElsServeisPerData_Treballador(fecha, cursor.getInt(0));
                            headerAdapter_consulta.actualitzaRecycler(myDataset);
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }


    }


    //////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////          LListats          /////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void llistaSenseFiltrats() {
        headerAdapter_consulta.actualitzaRecycler(retornaTotsElsServeis());
    }

    public void llistaPerTreballador() {
        headerAdapter_consulta.actualitzaRecycler((ArrayList<Header_Consulta>) RetornaServeisPerId(treballador));

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////      Descarrega del server              /////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public List<Treballador> RetornaTreballadors() {
        if (isConnect.isDisponible(this)) {
            return listaTreballadors = DescargaTreballador.obtenirTreballadorsDelServer();
        } else {
            missatgeNoConnexio();

        }
        return null;
    }

    public List<Servei> RetornaServeis() {
        if (isConnect.isDisponible(this)) {
            return llistaServeis = DescargaServei.obtenirServeisDelServer(LoginActivity.IP);
        } else {
            missatgeNoConnexio();
        }
        return null;

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////     Consultes             ///////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    public ArrayList retornaTotsElsServeis() {
        myDataset = new ArrayList<>();
        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            ompleArrayList(idTreb, i);
        }

        return myDataset;
    }

    public String RetornaNomTreballador(int id) {
        String nom = null;
        for (int i = 0; i < treballadors.size(); i++) {
            if (treballadors.get(i).getId() == id) {
                nom = treballadors.get(i).getNom() + " " + treballadors.get(i).getCognom1() + " " +
                        treballadors.get(i).getCognom2();
            }
        }
        return nom;
    }

    public String RetornaNoAdmin(int id) {
        String nom = null;
        for (int i = 0; i < Treballador.getTreballadors().size(); i++) {
            if (Treballador.getTreballadors().get(i).getId() == id) {
                nom = Treballador.getTreballadors().get(i).getNom();
            }
        }
        return nom;
    }

    public List RetornaServeisPerId(int id) {
        myDataset = new ArrayList<>();

        for (int i = 0; i < llistaServeis.size(); i++) {
            if (llistaServeis.get(i).getId_treballador() == id) {
                ompleArrayList(id, i);
            }
        }
        return myDataset;
    }

    public ArrayList RetornaTotElsServeisPerData(String data) {
        myDataset = new ArrayList<>();
        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            if (llistaServeis.get(i).getData_servei().equals(data)) {
                ompleArrayList(idTreb, i);
            }
        }
        return myDataset;
    }

    public ArrayList RetornaTotElsServeisPerDataiHora(String data, String hora) {
        myDataset = new ArrayList<>();
        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            if (llistaServeis.get(i).getData_servei().equals(data) && llistaServeis.get(i).getHora_inici().equals(hora)) {
                ompleArrayList(idTreb, i);
            }
        }
        return myDataset;
    }

    public ArrayList RetornaTotElsServeisPerData_Treballador(String data, int id) {
        myDataset = new ArrayList<>();

        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            if (llistaServeis.get(i).getData_servei().equals(data) &&
                    llistaServeis.get(i).getId_treballador() == cursor.getInt(0)) {
                ompleArrayList(idTreb, i);

            }

        }
        return myDataset;
    }

    private ArrayList<Header_Consulta> RetornaTotsElsServeisPerTreballadorDataIHora(int treballador, String fecha, String time) {
        myDataset = new ArrayList<>();

        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            if (llistaServeis.get(i).getData_servei().equals(fecha) &&
                    llistaServeis.get(i).getId_treballador() == treballador
                    && llistaServeis.get(i).getHora_inici().equals(time)) {
                ompleArrayList(idTreb, i);

            }

        }
        return myDataset;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////           UTILS    ///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean ompleArrayList(int id, int i) {
        return myDataset.add(new Header_Consulta(RetornaNomTreballador(id)
                , llistaServeis.get(i).getDescripcio()
                , String.valueOf(llistaServeis.get(i).getId()), llistaServeis.get(i).getData_servei(),
                llistaServeis.get(i).getHora_inici(), llistaServeis.get(i).getHora_final()));
    }

    public void carregarDataTreballador() {

        if (treballador == 0) {
            myDataset = new ArrayList<Header_Consulta>();
            myDataset = RetornaTotElsServeisPerData(fecha);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        } else {
            myDataset = new ArrayList<Header_Consulta>();
            myDataset = RetornaTotElsServeisPerData_Treballador(fecha, treballador);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        }

    }

    public void carregarHoraTreballador() {

        if (treballador == 0) {
            myDataset = new ArrayList<Header_Consulta>();
            myDataset = RetornaTotElsServeisPerDataiHora(fecha, time);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        } else {
            treballador = cursor.getInt(0);
            myDataset = new ArrayList<Header_Consulta>();
            myDataset = RetornaTotsElsServeisPerTreballadorDataIHora(treballador, fecha, time);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
            missatge(time);
        }

    }

    /*
  mètode auxiliar per formatar la data correctament
   */
    private boolean isOK(int x) {
        for (int i = 0; i < 10; i++) {
            if (x == i) {
                return true;
            }
        }
        return false;
    }


    public void setVisible() {
        (findViewById(seleccionar_hora)).setVisibility(View.VISIBLE);
        (findViewById(cancelar_filtros)).setVisibility(View.VISIBLE);
    }

    public void setInvisible() {
        (findViewById(seleccionar_hora)).setVisibility(View.INVISIBLE);
        (findViewById(cancelar_filtros)).setVisibility(View.INVISIBLE);

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////             Missatges           //////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void MissatgeClick(String variable, String titol, int x) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(variable)
                .setTitle(titol)
                .setCancelable(false)
                .setIcon(x)
                .setNegativeButton("Acceptar\t",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void missatgeNoConnexio() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("WIFI NO DISPONIBLE");
        alertDialog.setMessage("Es mostraran les dades OFFLINE");
        alertDialog.setIcon(R.drawable.fail);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startServicesActivity = new Intent(ConsultaServeisOnlineActivity.this, ConsultaServeisActivity.class);
                        finish();
                        startActivity(startServicesActivity);

                    }
                }
        );

        alertDialog.show();


    }

    public void missatge(String d) {
        Toast.makeText(this, d, Toast.LENGTH_SHORT).show();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////         PICKERS             //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void PickerHora() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ConsultaServeisOnlineActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hora = Integer.toString(selectedHour);
                String minuts = Integer.toString(selectedMinute);
                if (hora.length() == 1) hora = "0" + hora;
                if (minuts.length() == 1) minuts = "0" + minuts;
                time = (hora + ":" + minuts);
                carregarHoraTreballador();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Seleccionar horari");
        mTimePicker.show();
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
                if (isOK(selectedmonth)) {
                    fecha = "" + selectedday + "/0" + selectedmonth + "/" + selectedyear;
                    {
                        if (isOK(selectedday)) {
                            fecha = "0" + selectedday + "/0" + selectedmonth + "/" + selectedyear;
                        }

                    }
                }
                setVisible();
                carregarDataTreballador();
            }

        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Selecciona Data");

        mDatePicker.show();

    }
}