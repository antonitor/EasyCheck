package net.marcarni.easycheck.SQLite;

/**
 * Created by Maria on 29/09/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static net.marcarni.easycheck.SQLite.DBInterface.BD_CREATE;
import static net.marcarni.easycheck.SQLite.DBInterface.BD_NOM;
import static net.marcarni.easycheck.SQLite.DBInterface.BD_TAULA;
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
        db.execSQL(BD_CREATE);
    }

    //El que fa és eliminar-la (fer un drop de la taula) i tornar-la a crear.
    public void onUpgrade(SQLiteDatabase db, int VersioAntiga, int VersioNova) {
        Log.w(TAG, "Actualitzant Base de dades versió " + VersioAntiga + " a " + VersioNova + ". Destruirà totes les dades");

        db.execSQL("Drop table if exists " + BD_TAULA);
        onCreate(db);
    }

}