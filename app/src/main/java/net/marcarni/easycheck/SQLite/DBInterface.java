package net.marcarni.easycheck.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Maria on 29/09/2017.
 */

public class DBInterface {
    public static final String TAG = "DBInterface";
    public static final String BD_NOM = "BDReserva";
    public static final String BD_TAULA = "Reserva";
    public static final int VERSIO = 1;

    public static final String ID = "id";
    public static final String ID_RESERVA="idReserva";
    public static final String LOCALIZADOR="localizador";
    public static final String FECHA_RESERVA="fechaReserva";
    public static final String FECHA_SERVICIO ="fechaServicio";
    public static final String ID_SERVICIO="idServicio";
    public static final String NOMBRE_TITULAR="nombreTitular";
    public static final String APELLIDO1_TITULAR="apellido1Titular";
    public static final String APELLIDO2_TITULAR ="apellido2Titular";
    public static final String TELEFONO_TITULAR="telefonoTitular";
    public static final String EMAIL_TITULAR="emailTitular";
    public static final String ID_PAIS_TITULAR="idPaisTitular";
    public static final String LANG_TITULAR="langTitular";
    public static final String QR_CODE="qrCode";
    public static final String CHECK_IN="checkIn";
    public static final String DNI_TITULAR="dniTitular";
    public static final String ID_TRABAJADOR_ASIGNADO="idTrabajadorAsignado";

    public String[] arrayReserva() {
        String[] Reserva = {ID,ID_RESERVA,LOCALIZADOR,FECHA_RESERVA,FECHA_SERVICIO,ID_SERVICIO,NOMBRE_TITULAR,APELLIDO1_TITULAR,
        APELLIDO2_TITULAR,TELEFONO_TITULAR,EMAIL_TITULAR,ID_PAIS_TITULAR,LANG_TITULAR,QR_CODE,CHECK_IN,DNI_TITULAR,ID_TRABAJADOR_ASIGNADO};
        return Reserva;
    }

    public static final String BD_CREATE = "CREATE TABLE " + BD_TAULA + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ID_RESERVA + " TEXT not null, "
            + LOCALIZADOR + " TEXT NOT NULL, "
            + FECHA_RESERVA + " TEXT NOT NULL, "
            + FECHA_SERVICIO + " TEXT NOT NULL, "
            + ID_SERVICIO + " TEXT NOT NULL, "
            + NOMBRE_TITULAR + " TEXT NOT NULL, "
            + APELLIDO1_TITULAR+ " TEXT NOT NULL, "
            + APELLIDO2_TITULAR + " TEXT NOT NULL, "
            + TELEFONO_TITULAR + " TEXT, "
            + EMAIL_TITULAR + " TEXT, "
            + ID_PAIS_TITULAR+ " TEXT, "
            + LANG_TITULAR + " TEXT, "
            + QR_CODE + " TEXT NOT NULL, "
            + CHECK_IN+ " TEXT NOT NULL, "
            + DNI_TITULAR+ " TEXT NOT NULL, "
            + ID_TRABAJADOR_ASIGNADO+" TEXT NOT NULL );";

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
        bd.execSQL("drop table if exists " + BD_TAULA + " ;");
        bd.execSQL(BD_CREATE);
    }
    public long InserirReserva(String idReserva, String localizador, String fechaReserva,
                               String fechaServicio, String idServicio, String nombreTitular,
                               String apellido1Titular, String apellido2Titular, String telefonoTitular,
                               String emailTitular, String idPaisTitular, String langTitular,
                               String qrCode, String checkIn, String dniTitular, String idTrabajadorAsignado) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ID_RESERVA,idReserva);
        initialValues.put(LOCALIZADOR,localizador);
        initialValues.put(FECHA_RESERVA,fechaReserva);
        initialValues.put(FECHA_SERVICIO,fechaServicio);
        initialValues.put(ID_SERVICIO,idServicio);
        initialValues.put(NOMBRE_TITULAR,nombreTitular);
        initialValues.put(APELLIDO1_TITULAR,apellido1Titular);
        initialValues.put(APELLIDO2_TITULAR,apellido2Titular);
        initialValues.put(TELEFONO_TITULAR,telefonoTitular);
        initialValues.put(EMAIL_TITULAR,emailTitular);
        initialValues.put(ID_PAIS_TITULAR,idPaisTitular);
        initialValues.put(LANG_TITULAR,langTitular);
        initialValues.put(QR_CODE,qrCode);
        initialValues.put(CHECK_IN,checkIn);
        initialValues.put(DNI_TITULAR,dniTitular);
        initialValues.put(ID_TRABAJADOR_ASIGNADO,idTrabajadorAsignado);

        return bd.insert(BD_TAULA, null, initialValues);


    }
    public Cursor RetornaTotesLesReserves (){
        String[] ReservesTotals = arrayReserva();
        String orderBy=NOMBRE_TITULAR+" ASC";
        return bd.query(BD_TAULA,ReservesTotals, null, null, null,null,orderBy);
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
        Cursor cursor = bd.query(true, BD_TAULA, reserva,QR_CODE + " like ? ", new String[]{qr}, null, null, null, null);

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

}
