package net.marcarni.easycheck.SQLite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.marcarni.easycheck.SQLite.ContracteBD.Reserves;
import net.marcarni.easycheck.SQLite.ContracteBD.Serveis;
import net.marcarni.easycheck.SQLite.ContracteBD.Treballador;

import static android.content.ContentValues.TAG;


/**
 * @author Maria Remedios Ortega
 *
 */

/*****************************************************************************************
 *********************************  CREACIÓ DE BASE DE DADES *****************************
 *****************************************************************************************
 ****************************************************************************************/


/**
 * La classe SQLiteOpenHelper, que és una classe que serveix
 * d’ajuda per gestionar la creació de bases de dades i gestió de versions.
 * AjudaBD  hereta d’aquesta d’SQLiteOpenHelper s'’ocupa d’obrir la base de dades si aquesta existeix
 * o crear-la en cas contrari, i actualitzar-la si és necessari.
 */
public class AjudaBD extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "easycheck.db";
    private static final int DATABASE_VERSION = 1;

    public AjudaBD(Context con)
    {
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Crea les taules a la basse de dades, l'ordre és important, per tal de no crear
     * conflictes amb les claus foranes: primer Treballador, després Serveis i per últim Reserves
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BD_CREATE_TREBALLADOR);
        db.execSQL(BD_CREATE_SERVEIS);
        db.execSQL(BD_CREATE_RESERVES);
        db.execSQL(BD_CREATE_CLIENT);
    }

    /**
     * Afegit taula client
     */
    public static final String BD_CREATE_CLIENT = "CREATE TABLE " + ContracteBD.Client.NOM_TAULA + "("
            + ContracteBD.Client._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ContracteBD.Client.NOM_TITULAR + " TEXT NOT NULL, "
            + ContracteBD.Client.COGNOM1_TITULAR + " TEXT NOT NULL, "
            + ContracteBD.Client.COGNOM2_TITULAR + " TEXT NOT NULL, "
            + ContracteBD.Client.TELEFON_TITULAR + " TEXT NOT NULL, "
            + ContracteBD.Client.EMAIL_TITULAR +" TEXT, "
            + ContracteBD.Client.DNI_TITULAR +" TEXT NOT NULL);";



    public static final String BD_CREATE_RESERVES = "CREATE TABLE " + ContracteBD.Reserves.NOM_TAULA + "("
            + Reserves._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Reserves.LOCALITZADOR + " TEXT NOT NULL, "
            + Reserves.DATA_RESERVA + " TEXT NOT NULL, "
            + Reserves.ID_SERVEI + " INTEGER NOT NULL, "
            + Reserves.ID_CLIENT + " INTEGER NOT NULL, "
           // + Reserves.NOM_TITULAR + " TEXT NOT NULL, "
           // + Reserves.COGNOM1_TITULAR+ " TEXT NOT NULL, "
           // + Reserves.COGNOM2_TITULAR + " TEXT NOT NULL, "
            //+ Reserves.TELEFON_TITULAR + " TEXT, "
           // + Reserves.EMAIL_TITULAR + " TEXT, "
            + Reserves.QR_CODE + " TEXT NOT NULL, "
            + Reserves.CHECK_IN+ " TEXT NOT NULL, "
            //+ Reserves.DNI_TITULAR+ " TEXT NOT NULL, "
            // afegir foreig key a reserves de client

            +"FOREIGN KEY ("+ Reserves.ID_CLIENT+") REFERENCES "+ ContracteBD.Client.NOM_TAULA+"( "+ ContracteBD.Client._ID+"),"

            + "FOREIGN KEY("+ Reserves.ID_SERVEI+") REFERENCES " + Serveis.NOM_TAULA +"(" + Serveis._ID +"));";

    public static final String BD_CREATE_SERVEIS = "CREATE TABLE " + Serveis.NOM_TAULA + "("
            + Serveis._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Serveis.DESCRIPCIO + " TEXT NOT NULL, "
            + Serveis.ID_TREBALLADOR + " INTEGER NOT NULL, "
            + Serveis.DATA_SERVEI + " TEXT NOT NULL, "
            + Serveis.HORA_INICI + " TEXT NOT NULL, "
            + Serveis.HORA_FI + " TEXT NOT NULL, "
            + "FOREIGN KEY("+ Serveis.ID_TREBALLADOR+") REFERENCES " + Treballador.NOM_TAULA +"(" + Treballador._ID +"));";


    public static final String BD_CREATE_TREBALLADOR = "CREATE TABLE " + Treballador.NOM_TAULA + "("
            + Treballador._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            //Afegit camp dni
            + Treballador.DNI + " TEXT NOT NULL, "
            + Treballador.NOM + " TEXT NOT NULL, "
            + Treballador.COGNOM1 + " TEXT NOT NULL, "
            + Treballador.COGNOM2 +" TEXT NOT NULL, "
            + Treballador.ADMIN + " TEXT NOT NULL, "
            + Treballador.LOGIN + " TEXT NOT NULL,"
            // afegit camp password
            + Treballador.PASSWORD + "TEXT NOT NULL);";

    /**
     * Elimina les taules i les torna a crear.
     * L'ordre és important, primer Reserves, després Serveis i per últim Treballador,
     * per tal de no crear un conflicte amb les claus foranes.
     */
    public void onUpgrade(SQLiteDatabase db, int VersioAntiga, int VersioNova) {
        Log.w(TAG, "Actualitzant Base de dades versió " + VersioAntiga + " a " + VersioNova + ". Destruirà totes les dades");
        db.execSQL("Drop table if exists " + Reserves.NOM_TAULA);
        db.execSQL("Drop table if exists " + Serveis.NOM_TAULA);
        db.execSQL("Drop table if exists " + Treballador.NOM_TAULA);
        db.execSQL("Drop table if exists " + ContracteBD.Client.NOM_TAULA);
        onCreate(db);
    }

}