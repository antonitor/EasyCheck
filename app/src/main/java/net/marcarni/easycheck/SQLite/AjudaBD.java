package net.marcarni.easycheck.SQLite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;
import net.marcarni.easycheck.SQLite.ContracteBD.Reserves;
import net.marcarni.easycheck.SQLite.ContracteBD.Serveis;
import net.marcarni.easycheck.SQLite.ContracteBD.Treballador;

/*****************************************************************************************
 *********************************  CREACIÓ DE BASE DE DADES *****************************
 *****************************************************************************************
 *****************************************************************************************

 la classe SQLiteOpenHelper, que és una classe que serveix
 d’ajuda per gestionar la creació de bases de dades i gestió de versions.
 AjudaBD  hereta d’aquesta d’SQLiteOpenHelper s'’ocupa d’obrir la base de dades si aquesta existeix
 o crear-la en cas contrari, i actualitzar-la si és necessari.*/

class AjudaBD extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "easycheck.db";
    private static final int DATABASE_VERSION = 1;

    AjudaBD(Context con)
    {
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //crea una nova base de dades.
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BD_CREATE_TREBALLADOR);
        db.execSQL(BD_CREATE_SERVEIS);
        db.execSQL(BD_CREATE_RESERVES);
    }

    public static final String BD_CREATE_RESERVES = "CREATE TABLE " + ContracteBD.Reserves.NOM_TAULA + "("
            + Reserves._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Reserves.LOCALIZADOR + " TEXT NOT NULL, "
            + Reserves.FECHA_RESERVA + " TEXT NOT NULL, "
            + Reserves.FECHA_SERVICIO + " TEXT NOT NULL, "
            + Reserves.ID_SERVICIO + " INTEGER NOT NULL, "
            + Reserves.NOMBRE_TITULAR + " TEXT NOT NULL, "
            + Reserves.APELLIDO1_TITULAR+ " TEXT NOT NULL, "
            + Reserves.APELLIDO2_TITULAR + " TEXT NOT NULL, "
            + Reserves.TELEFONO_TITULAR + " TEXT, "
            + Reserves.EMAIL_TITULAR + " TEXT, "
            + Reserves.ID_PAIS_TITULAR+ " TEXT, "
            + Reserves.LANG_TITULAR + " TEXT, "
            + Reserves.QR_CODE + " TEXT NOT NULL, "
            + Reserves.CHECK_IN+ " TEXT NOT NULL, "
            + Reserves.DNI_TITULAR+ " TEXT NOT NULL, "
            + "FOREIGN KEY("+ Reserves.ID_SERVICIO+") REFERENCES " + Serveis.NOM_TAULA +"(" + Serveis._ID +"));";;

    public static final String BD_CREATE_SERVEIS = "CREATE TABLE " + Serveis.NOM_TAULA + "("
            + Serveis._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Serveis.DESCRIPCIO + " TEXT NOT NULL, "
            + Serveis.ID_TREBALLADOR + " INTEGER NOT NULL, "
            + "FOREIGN KEY("+ Serveis.ID_TREBALLADOR+") REFERENCES " + Treballador.NOM_TAULA +"(" + Treballador._ID +"));";


    public static final String BD_CREATE_TREBALLADOR = "CREATE TABLE " + Treballador.NOM_TAULA + "("
            + Treballador._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Treballador.NOM + " TEXT NOT NULL, "
            + Treballador.APELLIDO1 + " TEXT NOT NULL, "
            + Treballador.APELLIDO2 +" TEXT NOT NULL, "
            + Treballador.ADMIN + " TEXT NOT NULL, "
            + Treballador.LOGIN + " TEXT NOT NULL, "
            + Treballador.PASSWORD + " TEXT NOT NULL);";

    //El que fa és eliminar-la (fer un drop de la taula) i tornar-la a crear.
    public void onUpgrade(SQLiteDatabase db, int VersioAntiga, int VersioNova) {
        Log.w(TAG, "Actualitzant Base de dades versió " + VersioAntiga + " a " + VersioNova + ". Destruirà totes les dades");
        db.execSQL("Drop table if exists " + Reserves.NOM_TAULA);
        db.execSQL("Drop table if exists " + Serveis.NOM_TAULA);
        db.execSQL("Drop table if exists " + Treballador.NOM_TAULA);
        onCreate(db);
    }

}