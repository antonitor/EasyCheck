package net.marcarni.easycheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import net.marcarni.easycheck.RecyclerView.Header;
import net.marcarni.easycheck.RecyclerView.HeaderAdapter;
import net.marcarni.easycheck.SQLite.ContracteBD;
import net.marcarni.easycheck.SQLite.ContracteBD.Reserves;
import net.marcarni.easycheck.SQLite.ContracteBD.Serveis;
import net.marcarni.easycheck.SQLite.DBInterface;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;

import java.util.ArrayList;

public class DetallActivity extends MenuAppCompatActivity {

    DBInterface db;
    private HeaderAdapter headerAdapter;
    ArrayList<Header> myDataset;

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode onCreate de DetallActivity
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detall);


        /**
         * created by Maria Remedios Ortega Cobos
         * instanciació i inicialització del Recycler, adaptador i
         * ArrayList
         */
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myDataset=new ArrayList<>();
        headerAdapter= new HeaderAdapter(myDataset);
        recyclerView.setAdapter(headerAdapter);
        db = new DBInterface(this);

        consultes();
        verifica(this);
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode que verigica si el dataSet es buit, es a dir sino hi ha reserva
     */
    public void verifica(final Context context){
        if (myDataset.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Reserva no trobada!")
                    .setTitle("Atenció!!")
                    .setCancelable(false)
                    .setPositiveButton("Acceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    if (getIntent().hasExtra(getString(R.string.scanner_result))) {
                                        Intent intent = new Intent(context, CheckCameraPermissionsActivity.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les dades de l'intent.
     */
    public void consultes(){
        if (getIntent().hasExtra("LOCALITZADOR")){
            String loc = getIntent().getExtras().getString("LOCALITZADOR");
            RetornaReservaLoc(loc);
        } else if (getIntent().hasExtra(getString(R.string.scanner_result))) {
            String qrCode = getIntent().getStringExtra(getString(R.string.scanner_result));
            RetornaReservaQR(qrCode);
        } else if(getIntent().hasExtra("DNI") && getIntent().hasExtra("DATA")) {
            String dni = getIntent().getExtras().getString("DNI");
            String data = getIntent().getExtras().getString("DATA");
            RetornaReservaDNI_DATA(dni, data);
        } else if (getIntent().hasExtra("DATA")) {
            String data = getIntent().getExtras().getString("DATA");
            RetornaReservaDATA(data);
        } else if (getIntent().hasExtra("DNI")){
            String dni = getIntent().getExtras().getString("DNI");
            RetornaReservaDNI(dni);
        } else if (getIntent().hasExtra("ID_SERVEI")){
            String id_reserva = getIntent().getExtras().getString("ID_SERVEI");
            RetornaReservaId_Reserva(id_reserva);
        }else {
            Toast.makeText(this, "No s'ah rebut cap criteri de cerca", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per id
     * @param id_reserva a filtrar
     */
    public void RetornaReservaId_Reserva(String id_reserva){
        db.obre();
        Cursor cursor=db.RetornaReservaId_Servei(id_reserva);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per dni
     * @param dni a filtrar
     */
    public void RetornaReservaDNI(String dni) {
        db.obre();
        Cursor cursor=db.RetornaReservaDNI(dni);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per dni i data
     * @param dni a filtrar
     * @param data a fitrar
     */
    public void RetornaReservaDNI_DATA(String dni,String data){
        db.obre();
        Cursor cursor=db.RetornaReservaDNI_DATA(dni,data);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per data
     * @param data a filtrar
     */
    public void RetornaReservaDATA(String data){
        db.obre();
        Cursor cursor=db.RetornaReservaData(data);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per localitzador
     * @param loc localitzador a filtrar
     */
    private void RetornaReservaLoc(String loc) {
        //Toast.makeText(getBaseContext(), loc, Toast.LENGTH_SHORT).show();
        db.obre();
        Cursor cursor=db.RetornaReservaLocalitzador(loc);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per QR
     * @param qrCode a filtrar
     */
    public void RetornaReservaQR(String qrCode){
        db.obre();
        Cursor cursor=db.RetornaReservaQR(qrCode);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per afegir una nova entrada al dataSet
     * @param cursor a afegir entrada
     */
    public void CursorBD(Cursor cursor){
        if(cursor.moveToFirst()){
            do {
                myDataset.add(new Header(cursor.getInt(cursor.getColumnIndex(Reserves._ID)), cursor.getString(cursor.getColumnIndex(ContracteBD.Client.NOM_TITULAR))+" "+cursor.getString(cursor.getColumnIndex(ContracteBD.Client.COGNOM1_TITULAR))+" "+cursor.getString(cursor.getColumnIndex(ContracteBD.Client.COGNOM2_TITULAR)),
                        "DNI: "+cursor.getString(cursor.getColumnIndex(ContracteBD.Client.DNI_TITULAR)),"Data Servei: " +cursor.getString(cursor.getColumnIndex(Serveis.DATA_SERVEI)),
                        "QR: "+cursor.getString(cursor.getColumnIndex(Reserves.QR_CODE)),"Localització: "+cursor.getString(cursor.getColumnIndex(Reserves.LOCALITZADOR))
                        , cursor.getString(cursor.getColumnIndex(ContracteBD.Client.EMAIL_TITULAR)),cursor.getString(cursor.getColumnIndex(Reserves.CHECK_IN)),cursor.getString(cursor.getColumnIndex(Serveis.DESCRIPCIO))));

            }while(cursor.moveToNext());
        }
    }
}
