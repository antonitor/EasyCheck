package net.marcarni.easycheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import net.marcarni.easycheck.SQLite.DBInterface;

/**
 * Classe provisional on s'haurá d'implementar la pantalla de Login
 */
public class MainActivity extends AppCompatActivity {

    Button mLoginButton, mExemplesButton;
    Intent mDniIntent;
    DBInterface db;
    CheckBox isAdmin;
    public static String IS_ADMIN;
    boolean c;

    /**
     * Created by Antoni Torres Marí
     *
     * Mètpde onCreate de la classe princial
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mExemplesButton = (Button) findViewById(R.id.exemples_button);
        isAdmin=(CheckBox)findViewById(R.id.isAdmin);
        db = new DBInterface(this);

        //Recull el gestor per defecte de SharedPreferences, que per defecte es QR
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultMethod = sharedPreferences.getString(getString(R.string.pref_manager_default_key), getString(R.string.pref_manager_default_qr_value));

        //Si el gestor per fefecte és QR, el botó login llença l'escaner
        if (defaultMethod.equals(getString(R.string.pref_manager_default_qr_value)) ) {
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Mètode per gestionar l'esdevenimnet onClick del view mLoginButton
                 * @param view mLoginButton
                 */
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, CheckCameraPermissionsActivity.class));
                    comprobaAdmin();
                }
            });
        //Si el gestor per defecte és DNI, el botó login llença dniactivity amb dni
        } else if (defaultMethod.equals(getString(R.string.pref_manager_default_dni_value))) {
            mDniIntent = new Intent(this, DniActivity.class);
            mDniIntent.putExtra("DATO", "DNI");
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Mètode per gestionar l'esdeveniment del view mLogginButton
                 * @param view mLoginButton
                 */
                @Override
                public void onClick(View view) {
                    startActivity(mDniIntent);
                    comprobaAdmin();
                }
            });
        //Si el gestor per defecte és Loc, el botó login llença dniactivity amb Loc
        } else {
            mDniIntent = new Intent(this, DniActivity.class);
            mDniIntent.putExtra("DATO", "LOCALITZADOR");
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Mètode per gestionar l'esdeveniment del view mLogginButton
                 * @param view mLoginButton
                 */
                @Override
                public void onClick(View view) {
                    startActivity(mDniIntent);
                    comprobaAdmin();
                }
            });
        }
        mExemplesButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Mètode per gestionar l'esdeveniment del view mExemplesButton
             * @param view mExemplesButton
             */
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "S'han esborrat les dades de la base de dades i s'han tornat a crear.", Toast.LENGTH_SHORT).show();
                CrearExemplesBD();
            }
        });
    }

    /**
     * Created by Maria Remedios Ortega
     *
    * Crea els exemples per tal de provar l'app, l'ordre és important:
    * primer treballador, després servei i per últim reserva, per tal de
    * no crear conflictes amb les claus primaries.
    */
    public void CrearExemplesBD(){
        db.obre();
        db.Esborra();

        // nom - cognom1 - cognom2 - login - esAdmin
        db.InserirTreballador("Toni","Torres","Mari","jacdemanec","1");
        db.InserirTreballador("Maria","Ortega","Cobo","mari","1");
        db.InserirTreballador("Carlos Alberto","Castro","Cañabate","carlos","1");

        // descripcio  - idTreballador - dataServei - horaInici - horaFi
        db.InserirServei("Tarragona - Reus", "3","29/10/2017","10:00","11:00");
        db.InserirServei("Sabadell - Girona", "3","31/1/2018","23:00","00:00");
        db.InserirServei("Barcelona - Seu d'urgell", "3","29/10/2017","12:00","17:00");
        db.InserirServei("Barcelona - Reus", "2","29/10/2017","12:00","16:00");
        db.InserirServei("Eivissa - Formentera", "2","19/11/2017","20:00","21:00");
        db.InserirServei("Formentera - Eivissa", "2","20/11/2017","08:00","10:00");
        db.InserirServei("Tarrassa - Pals", "1","19/11/2017","18:00","19:30");
        db.InserirServei("Barcelona - Seu d'urgell", "1","31/1/2018","10:00","23:00");
        db.InserirServei("Barcelona - París", "1","30/11/2017","10:00","21:30");



                       // localitzador, dataReserva, idServei, nomTitular, cognom1Titular, cognom2Titular, telefonTitular, emailTitular, qrCode, checkIn, dniTitular
        db.InserirReserva("123456","16/1/2017",1,"Maria","Ortega","Cobos","12345678","maria@gmail.com","45R545WE45","0","41471860P");
        db.InserirReserva("123446","16/3/2017",1,"Joana","Fidel","Sanchis","12345998","jaoan@gmail.com","45R545WE44","0","38039532Q");
        db.InserirReserva("555469","3/3/2017",2,"Pere","Fernandez","Pujol","22342998","perean@gmail.com","854HFHH945","0","99392359K");
        db.InserirReserva("544460","10/3/2017",2,"Pamela","Sanchez","Grau","14445998","raimn@gmail.com","66FHHF45","0","72339884P");
        db.InserirReserva("574697","9/3/2017",3,"Antoni","Puig","Puigdemont","12356998"," olmean@gmail.com","867FHH9945","0","20841817E");
        db.InserirReserva("556664","10/3/2017",3,"Enma","Smith","Delon","16789998","iuoean@gmail.com","ABCDEFG","0","97620922K");
        db.InserirReserva("556691","10/3/2017",3,"Nuria","Smith","Delon","16789998","iuoean@gmail.com","ABCDE","0","47169530A");
        db.InserirReserva("556665","10/3/2017",7,"Nuria","Smith","Delon","16789998","iuoean@gmail.com","ABSDKSD","0","47169530A");
        db.InserirReserva("169256","16/2/2017",5,"Ruben","Perez","Garcia","12345678","ruben@gmail.com","45R545WTR","0","57519693M");
        db.InserirReserva("122334","30/4/2017",4,"Ruben","Perez","Garcia","12345678","ruben@gmail.com","45R54TRWD","0","57519693M");
        db.InserirReserva("232323","19/2/2017",6,"Sergi","Samper","Garcia","646340938","sergi@gmail.com","45R5FDFS","0","79926534Q");
        db.InserirReserva("232324","19/3/2017",8,"Sergi","Samper","Garcia","646340938","sergi@gmail.com","45R5FDFP","0","79926534Q");

        db.tanca();
    }

    /**
     * Created by Maria Remedios Ortega
     *
     * Mètode per comprovar si el treballador es Admin
     * Aquest mètode es temporal, posteriorment quan s'implementi la
     * base de dades al servidor, s'haurà de sustituir o replantejar
     * d'una altre manera.
     *
     */
    public void comprobaAdmin(){
        c=isAdmin.isChecked();
        if (c){
            IS_ADMIN="1";
        }else{IS_ADMIN="0";}
    }
}
