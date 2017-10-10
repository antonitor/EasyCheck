package net.marcarni.easycheck.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import net.marcarni.easycheck.SQLite.ContracteBD.Reserves;
import net.marcarni.easycheck.SQLite.ContracteBD.Serveis;
import net.marcarni.easycheck.SQLite.ContracteBD.Treballador;


public class DBInterface {
    public static final String TAG = "DBInterface";

    public String[] arrayReserva() {
        String[] Reserva = {
                Reserves._ID,
                Reserves.LOCALITZADOR,
                Reserves.DATA_RESERVA,
                Reserves.ID_SERVEI,
                Reserves.NOM_TITULAR,
                Reserves.COGNOM1_TITULAR,
                Reserves.COGNOM2_TITULAR,
                Reserves.TELEFON_TITULAR,
                Reserves.EMAIL_TITULAR,
                Reserves.QR_CODE,
                Reserves.CHECK_IN,
                Reserves.DNI_TITULAR};
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
        bd.execSQL("drop table if exists " + Serveis.NOM_TAULA + " ;");
        bd.execSQL("drop table if exists " + Treballador.NOM_TAULA + " ;");
        ajuda.onCreate(bd);
    }

    public Cursor RetornaTotesLesReserves() {
        //String[] ReservesTotals = arrayReserva();
        String orderBy = Reserves.NOM_TITULAR + " ASC";
        //return bd.query(Reserves.NOM_TAULA, ReservesTotals, null, null, null, null, orderBy);

        SQLiteQueryBuilder QBuilder = new SQLiteQueryBuilder();
        QBuilder.setTables(Reserves.NOM_TAULA + " LEFT JOIN " + Serveis.NOM_TAULA + " ON " + Reserves.ID_SERVEI + " = "  + Serveis.NOM_TAULA + "." + Serveis._ID);
        Cursor cursor = QBuilder.query(bd, null, null, null, null, null, orderBy);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor RetornaReservaDNI_DATA(String dni, String data) {
        //String[] reserva = arrayReserva();

        Cursor cursor; //= bd.query(true, BD_TAULA, reserva,DNI_TITULAR + " like ? ", new String[]{dni}, null, null, null, null);
        //cursor = bd.rawQuery("select * from " + Reserves.NOM_TAULA + " where " + Reserves.DNI_TITULAR + " = ? and " + Reserves.FECHA_SERVICIO + " =?", new String[]{dni, data});

        SQLiteQueryBuilder QBuilder = new SQLiteQueryBuilder();
        QBuilder.setTables(Reserves.NOM_TAULA + " LEFT JOIN " + Serveis.NOM_TAULA + " ON " + Reserves.ID_SERVEI + " = "  + Serveis.NOM_TAULA + "." + Serveis._ID);
        String orderBy = Serveis.HORA_INICI + " ASC";
        cursor = QBuilder.query(bd, null, Reserves.DNI_TITULAR + " = ? AND " + Serveis.DATA_SERVEI + " = ? ", new String[]{dni, data},null,null,orderBy);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor RetornaReservaQR(String qr) {
        //String[] reserva = arrayReserva();
        //Cursor cursor = bd.query(true, Reserves.NOM_TAULA, reserva, Reserves.QR_CODE + " like ? ", new String[]{qr}, null, null, null, null);

        SQLiteQueryBuilder QBuilder = new SQLiteQueryBuilder();
        QBuilder.setTables(Reserves.NOM_TAULA + " LEFT JOIN " + Serveis.NOM_TAULA + " ON " + Reserves.ID_SERVEI + " = "  + Serveis.NOM_TAULA + "." + Serveis._ID);
        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor cursor = QBuilder.query(bd, null, Reserves.QR_CODE + " = ? ", new String[]{qr},null,null,orderBy);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor RetornaReservaLocalitzador(String loc) {
        //String[] reserva = arrayReserva();
        //Cursor cursor;
        //cursor = bd.rawQuery("select * from " + Reserves.NOM_TAULA + " where " + Reserves.LOCALITZADOR + " = ? ", new String[]{loc});
        //Cursor cursor = bd.query(true, BD_TAULA, reserva,LOCALIZADOR+ " like ? ", new String[]{loc}, null, null, null, null);

        SQLiteQueryBuilder QBuilder = new SQLiteQueryBuilder();
        QBuilder.setTables(Reserves.NOM_TAULA + " LEFT JOIN " + Serveis.NOM_TAULA + " ON " + Reserves.ID_SERVEI + " = "  + Serveis.NOM_TAULA + "." + Serveis._ID);
        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor cursor = QBuilder.query(bd, null, Reserves.LOCALITZADOR + " = ? ", new String[]{loc},null,null,orderBy);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor RetornaReservaDNI(String dni) {
        //String[] reserva = arrayReserva();
        //Cursor cursor; //= bd.query(true, BD_TAULA, reserva,DNI_TITULAR + " like ? ", new String[]{dni}, null, null, null, null);
        //cursor = bd.rawQuery("select * from " + Reserves.NOM_TAULA + " where " + Reserves.DNI_TITULAR + " = ? ", new String[]{dni});

        SQLiteQueryBuilder QBuilder = new SQLiteQueryBuilder();
        QBuilder.setTables(Reserves.NOM_TAULA + " LEFT JOIN " + Serveis.NOM_TAULA + " ON " + Reserves.ID_SERVEI + " = "  + Serveis.NOM_TAULA + "." + Serveis._ID);
        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor cursor = QBuilder.query(bd, null, Reserves.DNI_TITULAR + " = ? ", new String[]{dni},null,null,orderBy);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public long InserirReserva(String localizador, String fechaReserva, int idServicio, String nombreTitular,
                               String apellido1Titular, String apellido2Titular, String telefonoTitular,
                               String emailTitular, String qrCode, String checkIn, String dniTitular) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Reserves.LOCALITZADOR, localizador);
        initialValues.put(Reserves.DATA_RESERVA, fechaReserva);
        initialValues.put(Reserves.ID_SERVEI, idServicio);
        initialValues.put(Reserves.NOM_TITULAR, nombreTitular);
        initialValues.put(Reserves.COGNOM1_TITULAR, apellido1Titular);
        initialValues.put(Reserves.COGNOM2_TITULAR, apellido2Titular);
        initialValues.put(Reserves.TELEFON_TITULAR, telefonoTitular);
        initialValues.put(Reserves.EMAIL_TITULAR, emailTitular);
        initialValues.put(Reserves.QR_CODE, qrCode);
        initialValues.put(Reserves.CHECK_IN, checkIn);
        initialValues.put(Reserves.DNI_TITULAR, dniTitular);

        return bd.insert(Reserves.NOM_TAULA, null, initialValues);
    }

    public Cursor RetornaReservaData(String data) {
        //String[] reserva = arrayReserva();

        Cursor cursor; //= bd.query(true, BD_TAULA, reserva,DNI_TITULAR + " like ? ", new String[]{dni}, null, null, null, null);
        //cursor = bd.rawQuery("select " + Reserves.LOCALITZADOR + " from " + Reserves.NOM_TAULA + " where " + Reserves.FECHA_SERVICIO + " = ? ", new String[]{data});

        SQLiteQueryBuilder QBuilder = new SQLiteQueryBuilder();
        QBuilder.setTables(Reserves.NOM_TAULA + " LEFT JOIN " + Serveis.NOM_TAULA + " ON " + Reserves.ID_SERVEI + " = " + Serveis.NOM_TAULA + "." + Serveis._ID);
        String orderBy = Serveis.HORA_INICI + " ASC";
        cursor = QBuilder.query(bd, null, Serveis.DATA_SERVEI + " = ? ", new String[]{data},null,null,orderBy);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public void ActalitzaCheckInReserva(String dni) {

        //String checkIn="1";
        //bd.rawQuery("update Reserva set checkIn = '"+checkIn+"' where dniTitular = ? ",new String[]{dni});
        Log.d("proba", Boolean.toString(bd.isOpen()));
        ContentValues valores = new ContentValues();
        valores.put(Reserves.CHECK_IN, "1");
        String where = Reserves.DNI_TITULAR + " like ? ";
        String[] selection = {dni};
        bd.update(Reserves.NOM_TAULA, valores, where, selection);
        Log.d("proba", "Actualitzat");

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////   TREBALLADORS   //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public long InserirTreballador(String nom, String cognom1, String cognom2, String login, String admin) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Treballador.NOM, nom);
        initialValues.put(Treballador.COGNOM1, cognom1);
        initialValues.put(Treballador.COGNOM2, cognom2);
        initialValues.put(Treballador.LOGIN, login);
        initialValues.put(Treballador.ADMIN, admin);

        return bd.insert(Treballador.NOM_TAULA, null, initialValues);
    }

    // array amb tots els atributs de la classe treballador
    public String[] arrayTreballadors() {
        String[] Treballadors = {Treballador._ID, Treballador.NOM, Treballador.COGNOM1, Treballador.COGNOM2, Treballador.LOGIN, Treballador.ADMIN};
        return Treballadors;
    }

    //Query que retorna tots els treballadors
    public Cursor RetornaTotsElsTreballadors() {
        String consultaSQL="Select t."+Treballador._ID+", t."+Treballador.NOM+
                " FROM "+Treballador.NOM_TAULA+" t";
        return bd.rawQuery(consultaSQL,null);

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////    SERVEIS   //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String[] arrayServeis() {
        String[] Servei = {Serveis._ID, Serveis.ID_TREBALLADOR, Serveis.DESCRIPCIO};
        return Servei;
    }

    public long InserirServei(String descripcio, String idTreballador, String dataServei, String horaInici, String horaFi) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Serveis.DESCRIPCIO, descripcio);
        initialValues.put(Serveis.ID_TREBALLADOR, idTreballador);
        initialValues.put(Serveis.DATA_SERVEI, dataServei);
        initialValues.put(Serveis.HORA_INICI, horaInici);
        initialValues.put(Serveis.HORA_FI, horaFi);

        return bd.insert(Serveis.NOM_TAULA, null, initialValues);
    }

    public Cursor RetornaTotsElsServeis() {

        String consultaSQL = "Select distinct t." + Treballador.NOM + ", t." + Treballador.COGNOM1 + ", t." + Treballador.COGNOM2 +
                ", s." + Serveis.DESCRIPCIO + ", r." + Reserves.ID_SERVEI +
                " FROM " + Serveis.NOM_TAULA + " s " +
                " LEFT JOIN  " + Treballador.NOM_TAULA + " t ON t." + Treballador._ID + " = s." + Serveis.ID_TREBALLADOR +
                " LEFT join " + Reserves.NOM_TAULA + " r  ON s." + Serveis._ID + " = r." + Reserves.ID_SERVEI +"";
          //      " GROUP BY 1,2,3";

        String[] ServeisTotals = arrayServeis();
        return bd.rawQuery(consultaSQL, null);


    }

    /* Mètode que rebrà per mostrar els serveis relacionats amb el id del treballado i es relaciona amb
        les reserves asociades a aquestes serveis
*/
    public Cursor RetornaServei_Treballador(int id) {

        String[] args = new String[]{String.valueOf(id)};

      String consultaSQL= "Select distinct t." + Treballador.NOM + ", t." + Treballador.COGNOM1 + ", t." + Treballador.COGNOM2 +
              ", s." + Serveis.DESCRIPCIO + ", r." + Reserves.ID_SERVEI +
              " FROM " + Serveis.NOM_TAULA + " s " +
              " JOIN " + Treballador.NOM_TAULA + " t ON t." + Treballador._ID + " = s." + Serveis.ID_TREBALLADOR +
              " and t."+Treballador._ID+"= ?" +
              " LEFT join " +  Reserves.NOM_TAULA  +" r  on s."+ Serveis._ID+" = r."+Reserves.ID_SERVEI +"";


        return bd.rawQuery(consultaSQL, args);


    }


    public Cursor RetornaReservaId_Servei(String idServei) {
        //Cursor cursor;
        //cursor = bd.rawQuery("select * from " + Reserves.NOM_TAULA + " where " + Reserves.ID_SERVEI + " = ? ", new String[]{id_reserva});

        SQLiteQueryBuilder QBuilder = new SQLiteQueryBuilder();
        QBuilder.setTables(Reserves.NOM_TAULA + " LEFT JOIN " + Serveis.NOM_TAULA + " ON " + Reserves.ID_SERVEI + " = "  + Serveis.NOM_TAULA + "." + Serveis._ID);
        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor cursor = QBuilder.query(bd, null, Reserves.ID_SERVEI + " = ? ", new String[]{idServei},null,null,orderBy);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void proba() {
        Log.d("proba", "Conectat!");
        Log.d("proba", Boolean.toString(bd.isOpen()));
    }



}
