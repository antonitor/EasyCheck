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
import net.marcarni.easycheck.eines.Missatges;
import net.marcarni.easycheck.eines.Utilitats;
import net.marcarni.easycheck.eines.isConnect;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static net.marcarni.easycheck.R.id.cancelar_filtros;
import static net.marcarni.easycheck.R.id.seleccionar_hora;


/**
 * @author Maria
 *         <p>
 *         Activitat creada per mostrar  els serveis descarregats del servidor
 *         Es mostraran en un RecyclerView amb un adaptador personalitzat.
 *
 *         Funcionalitats:
 *
 *         1. Administrador:
 *         - Podrà llistar tots els serveis, de tots els treballadors
 *         - Podrà llistar serveis per treballador
 *         - Podrà llistar serveis per data
 *         - Podrà llistar serveis per data i hora
 *         - Podrà llistar serveis per treballador i data
 *         - Podra llistar serveis per treballador data i hora
 *         2. Usuari no administrador
 *         - Podrà consultar els seus serveis
 *         - Podrà consultar els seus serveis per data
 *         - Podrà consultar els seus serveis per data i hora
 *
 */
public class ConsultaServeisOnlineActivity extends MenuAppCompatActivity implements View.OnLongClickListener {

    private String fecha = null, time = null;
    private HeaderAdapter_Consulta headerAdapter_consulta;
    private ArrayList<Header_Consulta> myDataset;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Spinner spinnerTreballadors;
    private ArrayList<Treballador> treballadors;
    private int treballador;
    private List<Treballador> listaTreballadors = new ArrayList<>();
    private List<Servei> llistaServeis = new ArrayList<>();
    private Cursor cursor;
    private String isAdmin;
    private String ID_treballador = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_serveis_online);
        Toolbar editToolbar = (Toolbar) findViewById(R.id.filter_toolbar);
        editToolbar.inflateMenu(R.menu.toolbar_menu);


        // inicialització de les variables.
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

    /**
     * Mètode que mostrarà un AlertDialog fent LongClick amb la data i l'hora respectivament
     * @param view
     * @return
     */
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
            Missatges.AlertMissatge(titol, variable, icon, this);
        } else {
            missatge(vacio);
        }
        return false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////              AsynTask              //////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Classe que extends d'asyntask encarregada de descarregar i mostrar els serveis, treballadors i reserves
     * del servidor.
     */
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
            Treballador.setTreballadors(treballadors);
            return null;

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////             SPINNER TREBALLADORS            /////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * Mètode encarregat d'omplir un cursor amb el treballador amb una id específica.
         * Es farà servir en cas que el treballador no sigui administrador.
         * @return Cursor amb id i nom del treballador.
         */
        private Cursor getCursorTreballador() {
            MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "nom"});
            cursor.addRow(new String[]{String.valueOf(ID_treballador), RetornaNoAdmin(Integer.parseInt(ID_treballador))});


            return cursor;
        }

        /**
         * Mètode per omplir i implementar l'spinner de Treballador
         * Comprova si l'usuari connectat  és administrador. -
         * 1. Administrdor: L'spinner mostraran tots els treballadors en aquell moment al server
         * 2. No administrador: L'spinner només mostrarà l'usuari connectat en aquell moment.
         */
        private void implementarSpinner() {
            if (isAdmin.equals("1"))
                cursor = Utilitats.getCursorSpinner(treballadors);
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


            /**
             * Filtratge de les consultes per data,hora i treballador.
             */
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
                            myDataset = Utilitats.RetornaTotElsServeisPerDataiHora(fecha, time, (ArrayList<Servei>) llistaServeis);
                            headerAdapter_consulta.actualitzaRecycler(myDataset);
                        } else {
                            myDataset = new ArrayList<Header_Consulta>();
                            myDataset = Utilitats.RetornaTotsElsServeisPerTreballadorDataIHora(cursor.getInt(0), fecha, time, (ArrayList) llistaServeis);
                            headerAdapter_consulta.actualitzaRecycler(myDataset);
                        }

                    } else if (time == null && fecha != null) {
                        if (treballador == 0) {
                            carregarDataTreballador();
                        } else {
                            myDataset = new ArrayList<Header_Consulta>();
                            myDataset = Utilitats.RetornaTotElsServeisPerData_Treballador(fecha, cursor.getInt(0), (ArrayList) llistaServeis);
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

    /**
     * Mètode per actualitzar un Recycler cada cop que es realitza una consulta.
     */
    private void llistaSenseFiltrats() {
        headerAdapter_consulta.actualitzaRecycler(Utilitats.retornaTotsElsServeis((ArrayList) llistaServeis));
    }

    /**
     * Mètode per actualitzar un Recycler cada cop que es realitza una consulta.
     */
    private void llistaPerTreballador() {
        headerAdapter_consulta.actualitzaRecycler((ArrayList<Header_Consulta>) Utilitats.RetornaServeisPerId(treballador, (ArrayList) llistaServeis));

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////      Descarrega del server              /////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Mètode que realitza una crida al servidor per descargar llista de treballadors.
     * Comprovarà connexió prèviament.
     * @return List amb els treballadors al servidor en aquell moment.
     */
    private List<Treballador> RetornaTreballadors() {
        if (isConnect.isDisponible(this)) {
            return listaTreballadors = DescargaTreballador.obtenirTreballadorsDelServer();
        } else {
            missatgeNoConnexio();

        }
        return null;
    }

    /**
     * Mètode que realitza una crida al servidor per descargar llista de serveis
     * Comprovarà connexió prèviament.
     * @return List amb els serveis  al servidor en aquell moment.
     *
     */
    public List<Servei> RetornaServeis() {
        if (isConnect.isDisponible(this)) {
            return llistaServeis = DescargaServei.obtenirServeisDelServer(LoginActivity.IP);
        } else {
            missatgeNoConnexio();
        }
        return null;

    }

    /**
     * Mètode que comprova id d'un treballador al servidor i retorna el nom
     * @param id Integer amb l'id del treballador
     * @return String amb el nom del treballador.
     */
    private String RetornaNoAdmin(int id) {
        String nom = null;
        for (int i = 0; i < Treballador.getTreballadors().size(); i++) {
            if (Treballador.getTreballadors().get(i).getId() == id) {
                nom = Treballador.getTreballadors().get(i).getNom();
            }
        }
        return nom;
    }


    /**
     * Mètode auxiliar per carregar data per la cerca dels serveis per data
     */
    private void carregarDataTreballador() {

        if (treballador == 0) {
            myDataset = new ArrayList<Header_Consulta>();
            myDataset = Utilitats.RetornaTotElsServeisPerData(fecha, (ArrayList) llistaServeis);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        } else {
            myDataset = new ArrayList<Header_Consulta>();
            myDataset = Utilitats.RetornaTotElsServeisPerData_Treballador(fecha, treballador, (ArrayList) llistaServeis);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        }

    }

    /**
     * Mètode auxiliar per carregar hora per la cerca dels serveis per hora
     */
    private void carregarHoraTreballador() {

        if (treballador == 0) {
            myDataset = new ArrayList<Header_Consulta>();
            myDataset = Utilitats.RetornaTotElsServeisPerDataiHora(fecha, time, (ArrayList) llistaServeis);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        } else {
            treballador = cursor.getInt(0);
            myDataset = new ArrayList<Header_Consulta>();
            myDataset = Utilitats.RetornaTotsElsServeisPerTreballadorDataIHora(treballador, fecha, time, (ArrayList) llistaServeis);
            headerAdapter_consulta.actualitzaRecycler(myDataset);

        }

    }

    /**
     * Metode per setear les vistes a Visibles o invisibles segons la circunstàncies.
     */
    private void setVisible() {
        (findViewById(seleccionar_hora)).setVisibility(View.VISIBLE);
        (findViewById(cancelar_filtros)).setVisibility(View.VISIBLE);
    }

    private void setInvisible() {
        (findViewById(seleccionar_hora)).setVisibility(View.INVISIBLE);
        (findViewById(cancelar_filtros)).setVisibility(View.INVISIBLE);

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////             Missatges           //////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Mètode per enviar missatge de No connexió a internet.
     */
    private void missatgeNoConnexio() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("WIFI NO DISPONIBLE");
        alertDialog.setMessage("Es mostraran les dades OFFLINE");
        alertDialog.setIcon(R.drawable.fail);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startServicesActivity = new Intent(getApplicationContext(), ConsultaServeisActivity.class);
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

    /**
     * Utilitat TimePickerDialog per seleccionar data i hora a l'aplicació
     */
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


    private  void PickerData() {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(ConsultaServeisOnlineActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                selectedmonth = selectedmonth + 1;
                fecha = "" + selectedday + "/" + selectedmonth + "/" + selectedyear;
                if (Utilitats.isOK(selectedmonth)) {
                    fecha = "" + selectedday + "/0" + selectedmonth + "/" + selectedyear;
                    {
                        if (Utilitats.isOK(selectedday)) {
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