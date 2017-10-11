package net.marcarni.easycheck;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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

import static net.marcarni.easycheck.R.id.seleccionar_hora;

public class ConsultaServeisActivity extends MenuAppCompatActivity {

    private static final int DATE_PICKER_REQUEST = 22;
    private static final int HOUR_PICKER_REQUEST = 25;
    DBInterface db;
    private HeaderAdapter_Consulta headerAdapter_consulta;
    ArrayList<Header_Consulta> myDataset;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    long treballador=0;
    String fecha = null;
    String time=null;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_serveis);
        myDataset = new ArrayList<>();
        headerAdapter_consulta = new HeaderAdapter_Consulta(myDataset);
        db=new DBInterface(this);
        db.obre();
        //Configuració del toolbar amb els filtres
        Toolbar editToolbar = (Toolbar) findViewById(R.id.filter_toolbar);
        editToolbar.inflateMenu(R.menu.toolbar_menu);
        Spinner spinnerTreballadors = (Spinner) findViewById(R.id.spinner_de_treballadors);
        // Afegeixo Recycler per instanciar
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_consulta);


        Cursor cursor = getCursorSpinner(db.RetornaTotsElsTreballadors());
        android.widget.SimpleCursorAdapter adapter = new android.widget.SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                cursor,
                new String[]{"nom"}, //Columna del cursor que volem agafar
                new int[]{android.R.id.text1}, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTreballadors.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        (findViewById(R.id.seleccionar_data)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultaServeisActivity.this, CalendarActivity.class);
                startActivityForResult(intent, DATE_PICKER_REQUEST);
            }
        });
        (findViewById(seleccionar_hora)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ConsultaServeisActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hora = Integer.toString(selectedHour);
                        String minuts = Integer.toString(selectedMinute);
                        if (hora.length()==1) hora="0"+hora;
                        if (minuts.length()==1) minuts="0"+minuts;
                        time=(hora + ":" + minuts);
                        Toast.makeText(getBaseContext(),time,Toast.LENGTH_LONG).show();
                        db.obre();
                        Cursor cursor;
                        if (treballador==0) {
                            myDataset = new ArrayList<Header_Consulta>();
                            cursor = db.RetornaServei_data_hora(fecha,time);
                            myDataset=mouCursor(cursor);
                            headerAdapter_consulta.actualitzaRecycler(myDataset);
                        } else {
                            myDataset = new ArrayList<Header_Consulta>();
                            cursor = db.RetornaServei_Treballador_data_hora((int)treballador,fecha,time);
                            myDataset=mouCursor(cursor);
                            headerAdapter_consulta.actualitzaRecycler(myDataset);
                        }
                        db.tanca();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        (findViewById(R.id.seleccionar_data)).setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(final View view) {
                if (fecha != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage(fecha)
                            .setTitle("Data Seleccionada:")
                            .setCancelable(false)

                            .setNegativeButton("Acceptar\t",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();
                                        }
                                    });
                    builder.setPositiveButton("\tReiniciar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            fecha=null;
                            (findViewById(seleccionar_hora)).setVisibility(View.INVISIBLE);
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                } else Toast.makeText(view.getContext(), "selecciona data!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        if (spinnerTreballadors != null) {
            spinnerTreballadors.setAdapter(adapter);
            spinnerTreballadors.setOnItemSelectedListener(new myOnItemSelectedListener());
        }

        // Afegim Recycler
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(headerAdapter_consulta);
        db.tanca();
        if (fecha==null) (findViewById(seleccionar_hora)).setVisibility(View.INVISIBLE);
    }

    private Cursor getCursorSpinner(Cursor cursor){
        MatrixCursor extras = new MatrixCursor(new String[] { "_id", "nom" });
        extras.addRow(new String[] { "0", "Tots" });
        Cursor[] cursors = { extras, cursor };
        return new MergeCursor(cursors);
    }

    /**
     * Recull el resultat de CalendarActivity
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        switch (requestCode){
            case DATE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    String data = intent.getStringExtra("DATA");
                    fecha = data;
                    (findViewById(seleccionar_hora)).setVisibility(View.VISIBLE);
                    db.obre();
                    Cursor cursor = null;
                    carregarDataTreballador(cursor);
                    db.tanca();
                }
                break;
        }
    }

    public void carregarDataTreballador (Cursor cursor){
        if (treballador==0) {
            myDataset = new ArrayList<Header_Consulta>();
            cursor = db.RetornaServei_data(fecha);
            myDataset=mouCursor(cursor);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        } else {
            myDataset = new ArrayList<Header_Consulta>();
            cursor = db.RetornaServei_Treballador_data((int)treballador,fecha);
            myDataset=mouCursor(cursor);
            headerAdapter_consulta.actualitzaRecycler(myDataset);
        }
    }

    class myOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            //TODO 3: Recarregar aquí el RecyclerView segons el treballador seleccionat (el paràmetre id correspón a la columna _id de treballadors)

            // Toast.makeText(view.getContext(), "Treballador amb _ID = " + id + " seleccionat.", Toast.LENGTH_SHORT ).show();
            db.obre();
            Cursor cursor=null;
            treballador = id;
            if (fecha==null){
                if (treballador == 0) {
                    myDataset = new ArrayList<Header_Consulta>();
                    cursor = db.RetornaTotsElsServeis();
                    myDataset=mouCursor(cursor);
                    headerAdapter_consulta.actualitzaRecycler(myDataset);
                } else {
                    myDataset = new ArrayList<Header_Consulta>();
                    cursor = db.RetornaServei_Treballador((int) id);
                    myDataset=mouCursor(cursor);
                    headerAdapter_consulta.actualitzaRecycler(myDataset);
                }
            } else {
                carregarDataTreballador(cursor);
            }
            db.tanca();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}

    }
    public ArrayList mouCursor(Cursor cursor){
        if (cursor.moveToFirst()) {
            do {
                myDataset.add(new Header_Consulta(cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.NOM))+" "+cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.COGNOM1))+" "+cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.COGNOM2)),
                        cursor.getString(cursor.getColumnIndex(ContracteBD.Serveis.DESCRIPCIO)),cursor.getString(cursor.getColumnIndex(ContracteBD.Reserves.ID_SERVEI)),cursor.getString(cursor.getColumnIndex(ContracteBD.Serveis.DATA_SERVEI)),cursor.getString(cursor.getColumnIndex(ContracteBD.Serveis.HORA_INICI)),cursor.getString(cursor.getColumnIndex(ContracteBD.Serveis.HORA_FI))));
            } while (cursor.moveToNext());
        }
        return myDataset;
    }
}

