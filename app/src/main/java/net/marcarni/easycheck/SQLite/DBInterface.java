package net.marcarni.easycheck.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import net.marcarni.easycheck.SQLite.ContracteBD.Reserves;
import net.marcarni.easycheck.SQLite.ContracteBD.Treballador;


public class DBInterface {
    public static final String TAG = "DBInterface";
    public static final String BD_NOM = "BDReserva";
    public static final int VERSIO = 1;

    public String[] arrayReserva() {
        String[] Reserva = {Reserves._ID,Reserves.LOCALIZADOR,Reserves.FECHA_RESERVA,Reserves.FECHA_SERVICIO,Reserves.ID_SERVICIO,Reserves.NOMBRE_TITULAR,Reserves.APELLIDO1_TITULAR,
                Reserves.APELLIDO2_TITULAR,Reserves.TELEFONO_TITULAR,Reserves.EMAIL_TITULAR,Reserves.ID_PAIS_TITULAR,Reserves.LANG_TITULAR,Reserves.QR_CODE,Reserves.CHECK_IN,Reserves.DNI_TITULAR};
        return Reserva;
    }

    private final Context context;
    private AjudaBD ajuda;
    private SQLiteDatabase bd;

    public DBInterface(Context con) {
        this.context = con;
        ajuda = new AjudaBD(context);
    }

    public DBInterface obre() {
        bd = ajuda.getWritableDatabase();
        return this;
    }

    // mètode per tancar la base de dades
    public void tanca() {
        ajuda.close();
    }

    public void Esborra() {
        bd.execSQL("drop table if exists " + Reserves.NOM_TAULA + " ;");
        ajuda.onCreate(bd);
    }

    public Cursor RetornaTotesLesReserves (){
        String[] ReservesTotals = arrayReserva();
        String orderBy=Reserves.NOMBRE_TITULAR+" ASC";
        return bd.query(Reserves.NOM_TAULA,ReservesTotals, null, null, null,null,orderBy);
    }
    public Cursor RetornaReservaDNI_DATA(String dni,String data) {
        String[] reserva = arrayReserva();

        Cursor cursor; //= bd.query(true, BD_TAULA, reserva,DNI_TITULAR + " like ? ", new String[]{dni}, null, null, null, null);
        cursor=bd.rawQuery("select * from Reserva where dniTitular = ? and fechaServicio =?",new String[]{dni,data});
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }
    public Cursor RetornaReservaQR(String qr) {
        String[] reserva = arrayReserva();
        Cursor cursor = bd.query(true, Reserves.NOM_TAULA, reserva,Reserves.QR_CODE + " like ? ", new String[]{qr}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor RetornaReservaLocalitzador(String loc) {
        String[] reserva = arrayReserva();
        Cursor cursor;
        cursor=bd.rawQuery("select * from Reserva where localizador = ? ",new String[]{loc});
        //Cursor cursor = bd.query(true, BD_TAULA, reserva,LOCALIZADOR+ " like ? ", new String[]{loc}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor RetornaReservaDNI(String dni) {
        String[] reserva = arrayReserva();

        Cursor cursor; //= bd.query(true, BD_TAULA, reserva,DNI_TITULAR + " like ? ", new String[]{dni}, null, null, null, null);
        cursor=bd.rawQuery("select * from Reserva where dniTitular = ? ",new String[]{dni});
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }
    public Cursor RetornaReservaData(String data) {
        String[] reserva = arrayReserva();

        Cursor cursor; //= bd.query(true, BD_TAULA, reserva,DNI_TITULAR + " like ? ", new String[]{dni}, null, null, null, null);
        cursor=bd.rawQuery("select * from Reserva where fechaServicio = ? ",new String[]{data});
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }
    public void ActalitzaCheckInReserva (String dni){

        //String checkIn="1";
        //bd.rawQuery("update Reserva set checkIn = '"+checkIn+"' where dniTitular = ? ",new String[]{dni});
        Log.d("proba",Boolean.toString(bd.isOpen()));
        ContentValues valores = new ContentValues();
        valores.put("checkIn","1");
        String where ="dniTitular like ? ";
        String[] selection={dni};
        bd.update("Reserva", valores,where, selection);
        Log.d("proba","Actualitzat");

    }

    public long InserirReserva(String localizador, String fechaReserva,
                               String fechaServicio, String idServicio, String nombreTitular,
                               String apellido1Titular, String apellido2Titular, String telefonoTitular,
                               String emailTitular, String idPaisTitular, String langTitular,
                               String qrCode, String checkIn, String dniTitular) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Reserves.LOCALIZADOR,localizador);
        initialValues.put(Reserves.FECHA_RESERVA,fechaReserva);
        initialValues.put(Reserves.FECHA_SERVICIO,fechaServicio);
        initialValues.put(Reserves.ID_SERVICIO,idServicio);
        initialValues.put(Reserves.NOMBRE_TITULAR,nombreTitular);
        initialValues.put(Reserves.APELLIDO1_TITULAR,apellido1Titular);
        initialValues.put(Reserves.APELLIDO2_TITULAR,apellido2Titular);
        initialValues.put(Reserves.TELEFONO_TITULAR,telefonoTitular);
        initialValues.put(Reserves.EMAIL_TITULAR,emailTitular);
        initialValues.put(Reserves.ID_PAIS_TITULAR,idPaisTitular);
        initialValues.put(Reserves.LANG_TITULAR,langTitular);
        initialValues.put(Reserves.QR_CODE,qrCode);
        initialValues.put(Reserves.CHECK_IN,checkIn);
        initialValues.put(Reserves.DNI_TITULAR,dniTitular);

        return bd.insert(Reserves.NOM_TAULA, null, initialValues);
    }

    public void proba (){
        Log.d("proba","Conectat!");
        Log.d("proba",Boolean.toString(bd.isOpen()));
    }



}
