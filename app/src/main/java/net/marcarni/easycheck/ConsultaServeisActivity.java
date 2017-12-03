package net.marcarni.easycheck;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
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
import net.marcarni.easycheck.SQLite.ContracteBD;
import net.marcarni.easycheck.SQLite.DBInterface;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

import static net.marcarni.easycheck.LoginActivity.IS_ADMIN;
import static net.marcarni.easycheck.R.id.cancelar_filtros;
import static net.marcarni.easycheck.R.id.seleccionar_hora;

public class ConsultaServeisActivity extends MenuAppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private static final int DATE_PICKER_REQUEST = 22;
    private static final int HOUR_PICKER_REQUEST = 25;
    private DBInterface db;
    private HeaderAdapter_Consulta headerAdapter_consulta;
    private ArrayList<Header_Consulta> myDataset;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private long treballador = 0;
    private String fecha = null;
    private String time = null;
    private android.widget.SimpleCursorAdapter adapter;
    private Spinner spinnerTreballadors;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_serveis);

        //Configuració del toolbar amb els filtres
        Toolbar editToolbar = (Toolbar) findViewById(R.id.filter_toolbar);
        editToolbar.inflateMenu(R.menu.toolbar_menu);
        spinnerTreballadors = (Spinner) findViewById(R.id.spinner_de_treballadors);


        /**
         * Created by Maria Remedios Ortega
         * Instanciació del Recycler i de l'arrayList
         */

        myDataset = new ArrayList<>();
        headerAdapter_consulta = new HeaderAdapter_Consulta(myDataset);
        db = new DBInterface(this);
        db.obre();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_consulta);


        /*
         * Created by Antoni Torres Marí
         */
        //Recull el cursor amb els treballadors de la base de dades i el passa per el mètode
        //getCursorSpinner per tal d'afegirli la columna amb id=0 i nom=Tots
        Cursor cursor = getCursorSpinner(db.RetornaTotsElsTreballadors());
        //Instància un SimpleCursorAdapter amb el cursor creat anteriorment que mostrarà
        //les dades de la columna "nom"
        adapter = new android.widget.SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                cursor,
                new String[]{"nom"}, //Columna del cursor que volem agafar
                new int[]{android.R.id.text1}, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Afegeix l'adapter al Spinner de treballadors
        spinnerTreballadors.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /**
         * @author Carlos Alberto Castro Cañabate
         */
        (findViewById(R.id.cancelar_filtros)).setOnClickListener(this);
        (findViewById(R.id.seleccionar_data)).setOnClickListener(this);
        (findViewById(R.id.seleccionar_data)).setOnLongClickListener(this);
        (findViewById(seleccionar_hora)).setOnClickListener(this);
        (findViewById(R.id.seleccionar_hora)).setOnLongClickListener(this);

        if (spinnerTreballadors != null) {
            spinnerTreballadors.setAdapter(adapter);
            spinnerTreballadors.setOnItemSelectedListener(new myOnItemSelectedListener());
            spinnerTreballadors.setVisibility(View.VISIBLE);
        }

        /**
         *  Implementat by Maria
         *  Afegim Recycler, adaptador i comprovem si es administrador
         */
        comprobaAdmin();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(headerAdapter_consulta);
        db.tanca();


        if (fecha == null) {
            (findViewById(seleccionar_hora)).setVisibility(View.INVISIBLE);
            (findViewById(cancelar_filtros)).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per gestionar l'esdeveniment onClick
     * @param view que interacturarà
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seleccionar_hora:
                PickerHora();
                break;
            case R.id.seleccionar_data:
                PickerData();
                break;
            case R.id.cancelar_filtros:
                fecha = null;
                time = null;
                treballador = 0;
                (findViewById(seleccionar_hora)).setVisibility(View.INVISIBLE);
                (findViewById(cancelar_filtros)).setVisibility(View.INVISIBLE);
                spinnerTreballadors.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                llistatSenseFiltre();
        }
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per gestionar l'esdeveniment onLongClick
     * @param view que interacturarà
     * @return false
     */
    @Override
    public boolean onLongClick(View view) {
        String variable = null, titol = null, vacio = null;
        switch (view.getId()) {
            case R.id.seleccionar_data:
                variable = fecha;
                titol = "Data Seleccionada:";
                vacio = "Selecciona data!";
                break;
            case R.id.seleccionar_hora:
                variable = time;
                titol = "Hora Seleccionada:";
                vacio = "Selecciona hora!";
                break;
        }
        if (variable != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage(variable)
                    .setTitle(titol)
                    .setCancelable(false)
                    .setNegativeButton("Acceptar\t",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else Toast.makeText(view.getContext(), vacio, Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Created by Antoni Torres Marí
     *
     * Torna el cursor afegint-li una línia extra amb _id = 0 i nom "Tots"
     * @param cursor cursor al que se li afegeix la nova línia
     * @return cursor amb nova línia
     */
    private Cursor getCursorSpinner(Cursor cursor) {
        MatrixCursor extras = new MatrixCursor(new String[]{"_id", "nom"});
        extras.addRow(new String[]{"0", "Tots"});
        Cursor[] cursors = {extras, cursor};
        return new MergeCursor(cursors);
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per carregar Llistats amb la data y Hora + (treballador opcionalment)
     */
    public void carregarHoraTreballador() {
        db.obre();
        Cursor cursor;
        if (treballador == 0) {
            myDataset = new ArrayList<Header_Consulta>();
            cursor = db.RetornaServei_data_hora(fecha, time);
            myDataset = mouCursor(cursor);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        } else {
            myDataset = new ArrayList<Header_Consulta>();
            cursor = db.RetornaServei_Treballador_data_hora((int) treballador, fecha, time);
            myDataset = mouCursor(cursor);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        }
        db.tanca();
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per carregar Llistats amb la data + (treballador opcionalment)
     */
    public void carregarDataTreballador() {
        db.obre();
        Cursor cursor = null;
        if (treballador == 0) {
            myDataset = new ArrayList<Header_Consulta>();
            cursor = db.RetornaServei_data(fecha);
            myDataset = mouCursor(cursor);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        } else {
            myDataset = new ArrayList<Header_Consulta>();
            cursor = db.RetornaServei_Treballador_data((int) treballador, fecha);
            myDataset = mouCursor(cursor);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        }
        db.tanca();
    }
    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per carregar Llistats de tots el treballadors (sense cap filtre)
     */
    public void llistatSenseFiltre() {
        db.obre();
        myDataset = new ArrayList<Header_Consulta>();
        Cursor cursor = db.RetornaTotsElsServeis();
        myDataset = mouCursor(cursor);
        headerAdapter_consulta.actualitzaRecycler(myDataset);
        db.tanca();
    }

    class myOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        /**
         * @author Carlos Alberto Castro Cañabate
         *
         * Mètode per fer una acció una vegada seleccionat un treballador a l'spinner de treballadors.
         * @param adapterView adaptador
         * @param view spinner
         * @param position posició a l'spinner
         * @param id correspón a la columna _id de treballadors
         */
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Cursor cursor = null;
            treballador = id;
            if (fecha == null) {
                if (treballador == 0) {
                    llistatSenseFiltre();
                } else {
                    db.obre();
                    myDataset = new ArrayList<Header_Consulta>();
                    cursor = db.RetornaServei_Treballador((int) treballador);
                    myDataset = mouCursor(cursor);
                    headerAdapter_consulta.actualitzaRecycler(myDataset);
                    db.tanca();
                }

            } else if (time != null && fecha != null) {
                if (treballador == 0) {
                    db.obre();
                    myDataset = new ArrayList<Header_Consulta>();
                    cursor = db.RetornaServei_data_hora(fecha, time);
                    myDataset = mouCursor(cursor);
                    headerAdapter_consulta.actualitzaRecycler(myDataset);
                    db.tanca();
                } else {
                    db.obre();
                    myDataset = new ArrayList<Header_Consulta>();
                    cursor = db.RetornaServei_Treballador_data_hora((int) treballador, fecha, time);
                    myDataset = mouCursor(cursor);
                    headerAdapter_consulta.actualitzaRecycler(myDataset);
                    db.tanca();
                }
            } else if (time == null && fecha != null) {
                if (treballador == 0) {
                    carregarDataTreballador();
                } else {
                    db.obre();
                    myDataset = new ArrayList<Header_Consulta>();
                    cursor = db.RetornaServei_Treballador_data((int) treballador, fecha);
                    myDataset = mouCursor(cursor);
                    headerAdapter_consulta.actualitzaRecycler(myDataset);
                    db.tanca();
                }
            }
        }

        /**
         * Mètode per realitzar una acció quan no hi ha rés seleccionat. Al nostre cas sempre hi ha selecció.
         * @param adapterView adaptador
         */
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per afegir entrada al dataset
     * @param cursor que conté les dades
     * @return myDataset
     */
    public ArrayList mouCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                myDataset.add(new Header_Consulta(cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.NOM))
                        + " " + cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.COGNOM1))
                        + " " + cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.COGNOM2)),
                        cursor.getString(cursor.getColumnIndex(ContracteBD.Serveis.DESCRIPCIO)),
                        cursor.getString(cursor.getColumnIndex(ContracteBD.Reserves.ID_SERVEI)),
                        cursor.getString(cursor.getColumnIndex(ContracteBD.Serveis.DATA_SERVEI)),
                        cursor.getString(cursor.getColumnIndex(ContracteBD.Serveis.HORA_INICI)),
                        cursor.getString(cursor.getColumnIndex(ContracteBD.Serveis.HORA_FI))));
            } while (cursor.moveToNext());
        }
        return myDataset;
    }

    /**
     *  @author Maria Remedios Ortega
     *  Mètode per comprobar si el treballador es admin
     */
    public void comprobaAdmin() {
        if (IS_ADMIN == "0") {
            spinnerTreballadors.setVisibility(View.GONE);
        } else {
            spinnerTreballadors.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @author Maria Remedios Ortega
     * Mètode per obrir el TimePickerDialog, seleccionar la hora i retornarla
     * per consulta posterior.
     */
    public void PickerHora(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ConsultaServeisActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obrir el DataPickerDialog
     */
    public void PickerData(){
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(ConsultaServeisActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                selectedmonth = selectedmonth + 1;
                fecha="" + selectedday + "/" + selectedmonth + "/" + selectedyear;
                (findViewById(seleccionar_hora)).setVisibility(View.VISIBLE);
                (findViewById(cancelar_filtros)).setVisibility(View.VISIBLE);
                carregarDataTreballador();

            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Selecciona Data");
        mDatePicker.show();
    }
}
