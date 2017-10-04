package net.marcarni.easycheck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.marcarni.easycheck.RecyclerView.Header;
import net.marcarni.easycheck.RecyclerView.HeaderAdapter;
import net.marcarni.easycheck.SQLite.DBInterface;
import net.marcarni.easycheck.settings.SettingsActivity;

import java.util.ArrayList;

public class DetallActivity extends AppCompatActivity {

    DBInterface db;
    private HeaderAdapter headerAdapter;
    ArrayList<Header> myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detall);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myDataset=new ArrayList<>();
        headerAdapter= new HeaderAdapter(myDataset);
        recyclerView.setAdapter(headerAdapter);
        db = new DBInterface(this);

        CrearReserva();
        consultes();
        verifica();
    }
    public void verifica(){
        if (myDataset.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Reserva no trobada!")
                    .setTitle("Atenció!!")
                    .setCancelable(false)
                    .setPositiveButton("Acceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        }
    }
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
        } else {
            //NO S'HAURÍA D'ARRIBAR A AQUEST PUNT!!!
            Toast.makeText(this, "No s'ah rebut cap criteri de cerca", Toast.LENGTH_LONG).show();
        }
    }

    private void RetornaReservaDNI(String dni) {
        db.obre();
        Cursor cursor=db.RetornaReservaDNI(dni);
        if(cursor.moveToFirst()){
            do {
                myDataset.add(new Header(cursor.getString(6)+" "+cursor.getString(7)+" "+cursor.getString(8),
                        "DNI: "+cursor.getString(15)+"  "+"Data Servei:" +cursor.getString(4),
                        "QR: "+cursor.getString(13)+"   Localització: "+cursor.getString(2)
                        , cursor.getString(10)));

            }while(cursor.moveToNext());
        }
        db.tanca();
    }

    public void CrearReserva(){
        db.obre();
        db.Esborra();
                                  //LOC                   DATA                                                                                      QR            DNI
        db.InserirReserva("001","123456","16/1/2017","29/10/2017","12345","Maria","Ortega","Cobos","12345678","maria@gmail.com","12","Spanish","45R545WE45","1","41471860P","01");
        db.InserirReserva("002","123446","16/3/2017","19/11/2017","12345","Joana","Fidel","Sanchis","12345998","jaoan@gmail.com","12","Spanish","45R545WE45","2","38039532Q","01");
        db.InserirReserva("003","55546","3/3/2017","3/1/2018","12995","Pere","Fernandez","Pujol","22342998","perean@gmail.com","14","French","854HFHH945","2","99392359K","02");
        db.InserirReserva("004","54446","10/3/2017","23/2/2018","12995","Pamela","Sanchez","Grau","14445998","raimn@gmail.com","14","Spanish","66FHHF45","2","72339884P","02");
        db.InserirReserva("005","5746","9/3/2017","31/1/2018","12995","Antoni","Puig","Puigdemont","12356998"," olmean@gmail.com","13","French","867FHH9945","2","20841817E","02");
        db.InserirReserva("006","55666","10/3/2017","19/1/2018","12995","Enma","Smith","Delon","16789998","iuoean@gmail.com","13","English","ABCDE","2","97620922K","02");
        db.InserirReserva("006","55666","10/3/2017","19/11/2017","12995","Enma","Smith","Delon","16789998","iuoean@gmail.com","13","English","ABCDE","2","17370830B","02");

        db.tanca();
    }


    public void RetornaReservaDNI_DATA(String dni,String data){

        db.obre();
        Cursor cursor=db.RetornaReservaDNI_DATA(dni,data);
        CursorBD(cursor);
        db.tanca();
    }
    public void RetornaReservaDATA(String data){

        db.obre();
        Cursor cursor=db.RetornaReservaData(data);
        CursorBD(cursor);
        db.tanca();
    }
    private void RetornaReservaLoc(String loc) {
        Toast.makeText(getBaseContext(), loc, Toast.LENGTH_SHORT).show();
        db.obre();
        Cursor cursor=db.RetornaReservaLocalitzador(loc);
        CursorBD(cursor);
        db.tanca();
    }

    public void RetornaReserva(String dni, String data){
        db.obre();
        Cursor cursor=db.RetornaTotesLesReserves();
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * Afegit per toni
     * @param qrCode
     */
    public void RetornaReservaQR(String qrCode){
        db.obre();
        Cursor cursor=db.RetornaReservaQR(qrCode);
        CursorBD(cursor);
        db.tanca();
    }

    public void CursorBD(Cursor cursor){
        if(cursor.moveToFirst()){
            do {
                myDataset.add(new Header(cursor.getString(6)+" "+cursor.getString(7)+" "+cursor.getString(8),
                        "DNI: "+cursor.getString(15)+"  "+"Data Servei:" +cursor.getString(4),
                        "QR: "+cursor.getString(13)+"   Localització"+cursor.getString(2)
                        , cursor.getString(10)));

            }while(cursor.moveToNext());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case R.id.action_qr:
                Intent startQrActivity = new Intent(this, QRScanner.class);
                startActivity(startQrActivity);
                return true;
            case R.id.action_dni:
                Intent startDniActivity = new Intent(this, DniActivity.class);
                startDniActivity.putExtra("DATO", "DNI");
                finish();
                startActivity(startDniActivity);
                return true;
            case R.id.action_loc:
                Intent startLocActivity = new Intent(this, DniActivity.class);
                startLocActivity.putExtra("DATO", "LOCALITZADOR");
                finish();
                startActivity(startLocActivity);
                return true;
            case R.id.action_logout:
                Intent logout = new Intent(this, MainActivity.class);
                startActivity(logout);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
