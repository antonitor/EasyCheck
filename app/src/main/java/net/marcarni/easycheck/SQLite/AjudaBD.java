package net.marcarni.easycheck.SQLite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static net.marcarni.easycheck.SQLite.DBInterface.BD_NOM;
import static net.marcarni.easycheck.SQLite.DBInterface.VERSIO;

/*****************************************************************************************
 *********************************  CREACIÓ DE BASE DE DADES *****************************
 *****************************************************************************************
 *****************************************************************************************

 la classe SQLiteOpenHelper, que és una classe que serveix
 d’ajuda per gestionar la creació de bases de dades i gestió de versions.
 AjudaBD  hereta d’aquesta d’SQLiteOpenHelper s'’ocupa d’obrir la base de dades si aquesta existeix
 o crear-la en cas contrari, i actualitzar-la si és necessari.*/

class AjudaBD extends SQLiteOpenHelper {

    AjudaBD(Context con)
    {
        super(con, BD_NOM, null, VERSIO);
    }

    //crea una nova base de dades.
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BD_CREATE_RESERVES);
    }

    public static final String BD_CREATE_RESERVES = "CREATE TABLE " + ContracteBD.Reserves.NOM_TAULA + "("
            + ContracteBD.Reserves._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ContracteBD.Reserves.LOCALIZADOR + " TEXT NOT NULL, "
            + ContracteBD.Reserves.FECHA_RESERVA + " TEXT NOT NULL, "
            + ContracteBD.Reserves.FECHA_SERVICIO + " TEXT NOT NULL, "
            + ContracteBD.Reserves.ID_SERVICIO + " INTEGER NOT NULL, "
            + ContracteBD.Reserves.NOMBRE_TITULAR + " TEXT NOT NULL, "
            + ContracteBD.Reserves.APELLIDO1_TITULAR+ " TEXT NOT NULL, "
            + ContracteBD.Reserves.APELLIDO2_TITULAR + " TEXT NOT NULL, "
            + ContracteBD.Reserves.TELEFONO_TITULAR + " TEXT, "
            + ContracteBD.Reserves.EMAIL_TITULAR + " TEXT, "
            + ContracteBD.Reserves.ID_PAIS_TITULAR+ " TEXT, "
            + ContracteBD.Reserves.LANG_TITULAR + " TEXT, "
            + ContracteBD.Reserves.QR_CODE + " TEXT NOT NULL, "
            + ContracteBD.Reserves.CHECK_IN+ " TEXT NOT NULL, "
            + ContracteBD.Reserves.DNI_TITULAR+ " TEXT NOT NULL, "
            + "FOREIGN KEY("+ ContracteBD.Reserves.ID_SERVICIO+") REFERENCES" + ContracteBD.Serveis.NOM_TAULA +"(" + ContracteBD.Serveis._ID +");";;

    public static final String BD_CREATE_SERVEIS = "CREATE TABLE" + ContracteBD.Serveis.NOM_TAULA + "("
            + ContracteBD.Serveis._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ContracteBD.Serveis.DESCRIPCIO + " TEXT NOT NULL, "
            + ContracteBD.Serveis.ID_TREBALLADOR + " INTEGER NOT NULL, "
            + "FOREIGN KEY("+ ContracteBD.Serveis.ID_TREBALLADOR+") REFERENCES" + ContracteBD.Treballador.NOM_TAULA +"(" + ContracteBD.Treballador._ID +");";


    public static final String BD_CREATE_TREBALLADOR = "CREATE TABLE " + ContracteBD.Treballador.NOM_TAULA + "("
            + ContracteBD.Treballador._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ContracteBD.Treballador.NOM + " TEXT NOT NULL, "
            + ContracteBD.Treballador.APELLIDO1 + " TEXT NOT NULL, "
            + ContracteBD.Treballador.APELLIDO2 +" TEXT NOT NULL, "
            + ContracteBD.Treballador.ADMIN + " TEXT NOT NULL, "
            + ContracteBD.Treballador.LOGIN + " TEXT NOT NULL, "
            + ContracteBD.Treballador.PASSWORD + " TEXT NOT NULL);";

    //El que fa és eliminar-la (fer un drop de la taula) i tornar-la a crear.
    public void onUpgrade(SQLiteDatabase db, int VersioAntiga, int VersioNova) {
        Log.w(TAG, "Actualitzant Base de dades versió " + VersioAntiga + " a " + VersioNova + ". Destruirà totes les dades");

        db.execSQL("Drop table if exists " + ContracteBD.Reserves.NOM_TAULA);
        onCreate(db);
    }

}