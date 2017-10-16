package net.marcarni.easycheck.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.marcarni.easycheck.SQLite.ContracteBD.Reserves;
import net.marcarni.easycheck.SQLite.ContracteBD.Serveis;
import net.marcarni.easycheck.SQLite.ContracteBD.Treballador;

import static net.marcarni.easycheck.R.id.dni;


public class DBInterface {
    public static final String TAG = "DBInterface";
    ConsultesSQL consulta=new ConsultesSQL();
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

    /**
     * Mètode constructor de la classe DBInterface
     * @param con contexte
     */
    public DBInterface(Context con) {
        this.context = con;
        ajuda = new AjudaBD(context);
    }

    /**
     * Mètode per obrir la bd
     * @return this
     */
    public DBInterface obre() {
        bd = ajuda.getWritableDatabase();
        return this;
    }

    /**
     * Mètode per tancar la base de dades
     */
    public void tanca() {
        ajuda.close();
    }

    /**
     * Mètode per esborrar les taules de la base de dades
     */
    public void Esborra() {
        bd.execSQL("drop table if exists " + Reserves.NOM_TAULA + " ;");
        bd.execSQL("drop table if exists " + Serveis.NOM_TAULA + " ;");
        bd.execSQL("drop table if exists " + Treballador.NOM_TAULA + " ;");
        ajuda.onCreate(bd);
    }

    /**
     * Mètode per retornar totes les reserves sense filtres
     * @return cursor amb les reserves a retornar
     */
    public Cursor RetornaTotesLesReserves() {

        String orderBy = Reserves.NOM_TITULAR + " ASC";
        Cursor cursor = consulta.RetornaQuery().query(bd, null, null, null, null, null, orderBy);
        consulta.mouCursor(cursor);
        return cursor;
    }

    /**
     * Mètode per retornar les reserves que coindideixen amb el dni del client i la data del servei
     * @param dni del client
     * @param data del servei
     * @return cursor amb les reserves a retornar
     */
    public Cursor RetornaReservaDNI_DATA(String dni, String data) {

        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor  cursor = consulta.RetornaQuery().query(bd, null, Reserves.DNI_TITULAR + " = ? AND " + Serveis.DATA_SERVEI + " = ? ", new String[]{dni, data},null,null,orderBy);
        consulta.mouCursor(cursor);
        return cursor;
    }

    /**
     * Mètode per retornar les reserves que coincideixen amb el identificador del QR de la reserva
     * @param qr a filtrar
     * @return cursor amb les reserves a retornar
     */
    public Cursor RetornaReservaQR(String qr) {

        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor cursor =consulta.RetornaQuery().query(bd, null, Reserves.QR_CODE + " = ? ", new String[]{qr},null,null,orderBy);
        consulta.mouCursor(cursor);
        return cursor;
    }

    /**
     * Mètode per retornar les reserves que coincideixen amb el localitzador de la reserva
     * @param loc localitzador de la reserva
     * @return cursor amb les reserves a retornar
     */
    public Cursor RetornaReservaLocalitzador(String loc) {

        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor cursor = consulta.RetornaQuery().query(bd, null, Reserves.LOCALITZADOR + " = ? ", new String[]{loc},null,null,orderBy);
        consulta.mouCursor(cursor);
        return cursor;
    }

    /**
     * Mètode per retornar les reserves que coincideixen amb el dni de la reserva
     * @param dni de la reserva
     * @return cursor amb les reserves a retornar
     */
    public Cursor RetornaReservaDNI(String dni) {
        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor cursor = consulta.RetornaQuery().query(bd, null, Reserves.DNI_TITULAR + " = ? ", new String[]{dni},null,null,orderBy);
        consulta.mouCursor(cursor);
        return cursor;
    }

    /**
     * Mètode per inserir una reserva
     * @param localizador de la reserva
     * @param fechaReserva data de la reserva
     * @param idServicio id del servei
     * @param nombreTitular nom del client
     * @param apellido1Titular 1 cognom del client
     * @param apellido2Titular 2 cognom del client
     * @param telefonoTitular telèfon del client
     * @param emailTitular email del client
     * @param qrCode codi qr de la reserva
     * @param checkIn estat de la reserva
     * @param dniTitular dni de el client
     * @return
     */
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

    /**
     * Mètode per retornar les reserves filtrades per una data concreta
     * @param data per filtrar
     * @return cursor amb les reserves a retornar
     */
    public Cursor RetornaReservaData(String data) {

        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor cursor = consulta.RetornaQuery().query(bd, null, Serveis.DATA_SERVEI + " = ? ", new String[]{data},null,null,orderBy);
        consulta.mouCursor(cursor);
        return cursor;
    }

    /**
     * Mètode per retornar totes les reserves d'un servei
     * @param idServei a filtrar
     * @return cursor amb les reserves a retornar
     */
    public Cursor RetornaReservaId_Servei(String idServei) {

        String orderBy = Serveis.HORA_INICI + " ASC";
        Cursor cursor =consulta.RetornaQuery().query(bd, null, Reserves.ID_SERVEI + " = ? ", new String[]{idServei},null,null,orderBy);
        consulta.mouCursor(cursor);
        return cursor;
    }

    /**
     * Mètode per actualitzar l'estat del camp check de la reserva segons el seu _id
     * @param _id camp de filtratge
     */
    public void ActalitzaCheckInReserva(int _id) {
        Log.d("_id ===>>> ", ""+_id);
        Log.d("proba", Boolean.toString(bd.isOpen()));
        ContentValues valores = new ContentValues();
        valores.put(Reserves.CHECK_IN, "1");
        String where = Reserves._ID + " = ? ";
        String[] selection = {""+_id};
        bd.update(Reserves.NOM_TAULA, valores, where, selection);
        Log.d("proba", "Actualitzat");

    }

                                             /** TREBALLADORS */

    /**
     * Mètode per inserir Treballadors
     * @param nom del treballador
     * @param cognom1 del treballador
     * @param cognom2 del treballador
     * @param login del treballador
     * @param admin camp per verificar si el treballador es admin o no
     * @return posicio a taula treballadors
     */
    public long InserirTreballador(String nom, String cognom1, String cognom2, String login, String admin) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Treballador.NOM, nom);
        initialValues.put(Treballador.COGNOM1, cognom1);
        initialValues.put(Treballador.COGNOM2, cognom2);
        initialValues.put(Treballador.LOGIN, login);
        initialValues.put(Treballador.ADMIN, admin);

        return bd.insert(Treballador.NOM_TAULA, null, initialValues);
    }

    /**
     * Mètode per obtenir tots els atributs de els trebalaldors
     * @return  array amb tots els atributs de la classe treballador
     */
    public String[] arrayTreballadors() {
        String[] Treballadors = {Treballador._ID, Treballador.NOM, Treballador.COGNOM1, Treballador.COGNOM2, Treballador.LOGIN, Treballador.ADMIN};
        return Treballadors;
    }

    /**
     * Query que retorna tots els treballadors
     * @return cursors amb tots els treballadors
     */
    public Cursor RetornaTotsElsTreballadors() {
        return bd.rawQuery(consulta.RetornaTotsElsTreballadors,null);
    }

                                                        /** SERVEIS **/

    /**
     *
     * @param descripcio del servei
     * @param idTreballador que realitzarà el servei
     * @param dataServei a realitzar
     * @param horaInici del servei a realitzar
     * @param horaFi del servei a realitzar
     * @return posició del servei a la taula
     */
    public long InserirServei(String descripcio, String idTreballador, String dataServei, String horaInici, String horaFi) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(Serveis.DESCRIPCIO, descripcio);
        initialValues.put(Serveis.ID_TREBALLADOR, idTreballador);
        initialValues.put(Serveis.DATA_SERVEI, dataServei);
        initialValues.put(Serveis.HORA_INICI, horaInici);
        initialValues.put(Serveis.HORA_FI, horaFi);

        return bd.insert(Serveis.NOM_TAULA, null, initialValues);
    }

    /**
     * Mètode per retornar tots els serveis sense filtrat.
     * @return cursor amb els serveis a retornar
     */
    public Cursor RetornaTotsElsServeis() {
        return bd.rawQuery(consulta.RetornaTotsElsServeis, null);
    }

    /**
     * Metode per retornar els serveis d'un treballador
     * @param id del treballador a filtrar
     * @return cursor amb els serveis a retornar
     */
    public Cursor RetornaServei_Treballador(int id) {
        String[] args = new String[]{String.valueOf(id)};
        return bd.rawQuery(consulta.RetornaServeiTreballadorPerID, args);
    }

    /**
     * Mètode per retornar els serveis filtrats per treballador i data
     * @param id del treballador a filtrar
     * @param data a filtrar
     * @return cursor amb els serveis a retornar
     */
    public Cursor RetornaServei_Treballador_data(int id,String data) {
        String[] args = new String[]{String.valueOf(id), data};
        return bd.rawQuery(consulta.RetornaServeiPerID_DATA, args);
    }

    /**
     * Mètode per retornar serveis filtrats per treballador, data i hora.
     * @param id del treballador a filrar
     * @param data a filtrar
     * @param hora a filtrar
     * @return cursor amb els serveis a retornar
     */
    public Cursor RetornaServei_Treballador_data_hora(int id,String data,String hora) {
        String[] args = new String[]{String.valueOf(id), data,hora};
        return bd.rawQuery(consulta.RetornaServei_Treballador_data_hora, args);
    }

    /**
     * Mètode per retornar els serveis filtrats per data i hora
     * @param data a filtrar
     * @param hora a filtrar
     * @return cursor amb els serveis a retornar
     */
    public Cursor RetornaServei_data_hora(String data,String hora) {
        String[] args = new String[]{data,hora};
        return bd.rawQuery(consulta.RetornaServei_data_hora, args);
    }

    /**
     * Mètode per retornar els serveis filtrats per data
     * @param data a filtrar
     * @return cursor amb els serveis a retornar
     */
    public Cursor RetornaServei_data(String data) {
        String[] args = new String[]{data};
        return bd.rawQuery(consulta.RetornaServeiData, args);
    }
}
