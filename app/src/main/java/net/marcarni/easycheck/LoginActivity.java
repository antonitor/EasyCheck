package net.marcarni.easycheck;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.marcarni.easycheck.SQLite.ContracteBD;
import net.marcarni.easycheck.SQLite.DBInterface;
import net.marcarni.easycheck.Utils.DescargaReserva;
import net.marcarni.easycheck.Utils.DescargaServei;
import net.marcarni.easycheck.Utils.DescargaTreballador;
import net.marcarni.easycheck.Utils.FingerPrint;
import net.marcarni.easycheck.model.Client;
import net.marcarni.easycheck.model.Reserva;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;

import java.util.ArrayList;

/**
 * @author  Carlos Alberto Castro Cañabate
 */
public class LoginActivity extends AppCompatActivity {
    DBInterface db;
    Button buttonEntrar;
    Intent mDniIntent;
    EditText textUserName, textPassword;
    Cursor cursor;
    String huella = null;
    FingerPrint fingerPrint;
    boolean loginCorrecte = false;
    public static String IS_ADMIN;
    public static final String IP="192.168.1.4";
    public static String ID_TREBALLADOR=null, NOM_USUARI="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DBInterface(this);
        new LoginActivity.descargarDades().execute(IP);

    }
    public void buttonEntrarListener(){

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            /**
             * Mètode per gestionar l'esdevenimnet onClick del view mLoginButton
             *
             * @param view buttonEntrar
             */
            @Override
            public void onClick(View view) {
                loginCorrecte = ferLogin();
                if (loginCorrecte){
                    startActivity(mDniIntent);
                }
            }
        });
    }
    public Boolean ferLogin(){
        loginCorrecte = false;
        String userName = textUserName.getText().toString();
        String password = textPassword.getText().toString();
        db = new DBInterface(getApplicationContext());
        db.obre();
        cursor = db.verificarLogin(userName,password);
        if ((cursor != null) && (cursor.getCount() > 0)){
            mouCursor(cursor); // Recogemos el id treballador, nom_usuari, i si es admin

            if (huella == null && ! textUserName.getText().toString().equalsIgnoreCase("admin")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Quieres relacionar tu contraseña con tu huella dactilar?")
                        .setTitle("Atención!!")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                huella = "NO";
                                guardarPreferencias();
                                loginCorrecte=true;
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Acceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        huella = "SI";
                                        Toast.makeText(LoginActivity.this,"Introduce huella digital", Toast.LENGTH_SHORT).show();
                                        guardarPreferencias();
                                        fingerPrint = new FingerPrint(LoginActivity.this);
                                    }
                                }
                        );
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                loginCorrecte=true;
            }
        } else {
            Toast.makeText(this,"Login Incorrecto!", Toast.LENGTH_SHORT).show();
            loginCorrecte=false;
        }
        db.tanca();
        return loginCorrecte;
    }

    public void guardarPreferencias(){
        SharedPreferences.Editor editor = getSharedPreferences("HUELLA_CONFIG", MODE_PRIVATE).edit();
        editor.putString("HUELLA",huella);
        editor.putString("ID_TRABAJADOR",ID_TREBALLADOR);
        editor.putString("NOM_USUARI",NOM_USUARI);
        editor.putString("IS_ADMIN",IS_ADMIN);
        editor.apply();
    }
    public void cargarPreferencias(){
        SharedPreferences prefs = getSharedPreferences("HUELLA_CONFIG", MODE_PRIVATE);
        huella = prefs.getString("HUELLA", null);
        if (huella != null) {
            if (huella.equalsIgnoreCase("SI")){
                NOM_USUARI = prefs.getString("NOM_USUARI", "");
                ID_TREBALLADOR = prefs.getString("ID_TRABAJADOR", "");
                IS_ADMIN = prefs.getString("IS_ADMIN", "");
                fingerPrint = new FingerPrint(this);
            }
        }
    }
    public void mouCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                ID_TREBALLADOR = cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador._ID));
                NOM_USUARI = cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.NOM))+" "+
                        cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.COGNOM1))+" "+
                        cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.COGNOM2));
                IS_ADMIN = cursor.getString(cursor.getColumnIndex(ContracteBD.Treballador.ADMIN));
                Log.d("IS_ADMIN",IS_ADMIN);
            } while (cursor.moveToNext());
        }
    }

    private class descargarDades extends AsyncTask<String, ArrayList,String> {
        protected String doInBackground(String... urls) {
            db.obre();
            db.Esborra();

            ArrayList<Treballador> llistaDeTreballadors = (ArrayList<Treballador>) DescargaTreballador.obtenirTreballadorsDelServer(urls[0]);
            for(int i=0;i<llistaDeTreballadors.size();i++){
                Treballador t= llistaDeTreballadors.get(i);
                db.InserirTreballador(t.getDni(),t.getNom(),t.getCognom1(),t.getCognom2(),t.getLogin(),Integer.toString(t.getEsAdmin()),t.getPassword());
            }
            ArrayList<Servei> llistaDeServeis= (ArrayList<Servei>) DescargaServei.obtenirServeisDelServer(urls[0]);
            for(int i=0;i<llistaDeServeis.size();i++) {
                Servei s = llistaDeServeis.get(i);
                db.InserirServei(s.getDescripcio(), Integer.toString(s.getId_treballador()), s.getData_servei(), s.getHora_inici(), s.getHora_final());
            }
            ArrayList<Reserva> llistaDeReserves= (ArrayList<Reserva>) DescargaReserva.obtenirReservesDelServer(urls[0]);
            for(int i=0;i<llistaDeReserves.size();i++){
                Reserva r=llistaDeReserves.get(i);
                db.InserirReserva(r.getLocalitzador(),r.getData_reserva(),r.getId_servei(),r.getId(),r.getQr_code(),Integer.toString(r.getCheckin()));
                Client c = r.getClient();
                db.InserirClient(c.getNom_titular(),c.getCognom1_titular(),c.getCognom2_titular(),c.getTelefon_titular(),c.getEmail_titular(),c.getDni_titular());
            }
            db.tanca();
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (huella!=null) fingerPrint = new FingerPrint(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultMethod = sharedPreferences.getString(getString(R.string.pref_manager_default_key), getString(R.string.pref_manager_default_qr_value));
        cargarPreferencias();
        buttonEntrar = (Button) findViewById(R.id.buttonEntrar);
        textPassword = (EditText) findViewById(R.id.textPassword);
        textUserName = (EditText) findViewById(R.id.textUserName);
        Log.d("PREFERENCIA: ",defaultMethod);
        if (defaultMethod.equals(getString(R.string.pref_manager_default_qr_value))) {
            Toast.makeText(this, "QR", Toast.LENGTH_SHORT).show();
            mDniIntent = new Intent(LoginActivity.this, CheckCameraPermissionsActivity.class);
            buttonEntrarListener();
            //Si el gestor per defecte és DNI, el botó login llença dniactivity amb dni
        } else if (defaultMethod.equals(getString(R.string.pref_manager_default_dni_value))) {
            mDniIntent = new Intent(this, DniActivity.class);
            mDniIntent.putExtra("DATO", "DNI");
            buttonEntrarListener();
            //Si el gestor per defecte és Loc, el botó login llença dniactivity amb Loc
        } else {
            mDniIntent = new Intent(this, DniActivity.class);
            mDniIntent.putExtra("DATO", "LOCALITZADOR");
            buttonEntrarListener();
        }
    }

}
