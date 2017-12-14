package net.marcarni.easycheck;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.marcarni.easycheck.RecyclerView.Header;
import net.marcarni.easycheck.RecyclerView.HeaderAdapter;
import net.marcarni.easycheck.SQLite.ContracteBD;
import net.marcarni.easycheck.SQLite.ContracteBD.Reserves;
import net.marcarni.easycheck.SQLite.ContracteBD.Serveis;
import net.marcarni.easycheck.SQLite.DBInterface;
import net.marcarni.easycheck.Utils.NetUtils;
import net.marcarni.easycheck.Utils.PostResponse;
import net.marcarni.easycheck.eines.isConnect;
import net.marcarni.easycheck.model.Reserva;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetallActivity extends MenuAppCompatActivity implements LoaderManager.LoaderCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener, HeaderAdapter.ListItemClickListener {

    private DBInterface db;
    private HeaderAdapter headerAdapter;
    private ArrayList<Header> myDataset;
    private ProgressBar mLoadingIndicator;
    private String mHost;
    private int mPort;
    private static final String SERVEIS_PATH = "/easycheckapi/servei";
    private static final String CHECKIN_PATH = "/easycheckapi/reserva";
    private static final int GET_REQUEST_LOADER = 1;

    //Recycler Item TextViews
    private int mClickedItemId;
    private TextView mClickedItemCheckText;
    private View mClickedItemView;

    /**
     * @param savedInstanceState Bundle
     * @author Carlos Alberto Castro Cañabate
     * Mètode onCreate de DetallActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detall);

        configuraSharedPreferences();
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /**
         * @author Maria Remedios Ortega Cobos
         * instanciació i inicialització del Recycler, adaptador i
         * ArrayList
         */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myDataset = new ArrayList<>();
        headerAdapter = new HeaderAdapter(myDataset, this);
        recyclerView.setAdapter(headerAdapter);


        /*
        @author Antoni Torres Marí

        Mitjançant el mètode static de NetUtil comprovem si disposem de connexió a internet.
        Si disposem d'internet realitzarem les consultes pertinents al servidor.
        En cas de no disposar d'internet mostrarem un missatge al usuari i l'activity
        obtindrà les dades offline de la base de dades SQLITE
         */
        if (NetUtils.comprovaXarxa(this)) {
            consultesOnline();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("WIFI NO DISPONIBLE");
            alertDialog.setMessage("Es mostraran les dades OFFLINE");
            alertDialog.setIcon(R.drawable.fail);
            alertDialog.setPositiveButton("Acceptar", null);
            alertDialog.show();

            db = new DBInterface(this);
            consultes();

            if (myDataset.isEmpty()) {
                errorDialog(this, "Reserva no trobada!", R.drawable.not_found);
            }
        }
    }

    /**
     * @author Antoni Torres Marí
     *
     * Agafa el host i el port de les preferencies d'usuari i enregistra aquesta Activity
     * com a listener.
     */
    private void configuraSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mHost = sharedPreferences.getString(getString(R.string.pref_host_key), getString(R.string.pref_host_default));
        mPort = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_port_key), getString(R.string.pref_port_default)));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * @author Antoni Torres Marí
     *
     * Aquesta activity implementa el listener per tal de actualitzar el port i el host enseguida
     * que aquestes dades canvien a les preferencies
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_host_key))) {
            mHost = sharedPreferences.getString(getString(R.string.pref_host_key), getString(R.string.pref_host_default));
        } else if (key.equals(getString(R.string.pref_port_key))) {
            mPort = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_port_key), getString(R.string.pref_port_default)));
        }
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Modificat per Antoni Torres Mari TEA4
     * Mètode que verigica si el dataSet es buit, es a dir sino hi ha reserva
     */
    public void errorDialog(final Context context, String message, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Atenció!!")
                .setCancelable(false)
                .setIcon(icon)
                .setPositiveButton("Acceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (getIntent().hasExtra(getString(R.string.scanner_result))) {
                                    Intent intent = new Intent(context, CheckCameraPermissionsActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les dades de l'intent.
     */
    public void consultes() {
        if (getIntent().hasExtra("LOCALITZADOR")) {
            String loc = getIntent().getExtras().getString("LOCALITZADOR");
            RetornaReservaLoc(loc);
        } else if (getIntent().hasExtra(getString(R.string.scanner_result))) {
            String qrCode = getIntent().getStringExtra(getString(R.string.scanner_result));
            RetornaReservaQR(qrCode);
        } else if (getIntent().hasExtra("DNI") && getIntent().hasExtra("DATA")) {
            String dni = getIntent().getExtras().getString("DNI");
            String data = getIntent().getExtras().getString("DATA");
            RetornaReservaDNI_DATA(dni, data);
        } else if (getIntent().hasExtra("DATA")) {
            String data = getIntent().getExtras().getString("DATA");
            RetornaReservaDATA(data);
        } else if (getIntent().hasExtra("DNI")) {
            String dni = getIntent().getExtras().getString("DNI");
            RetornaReservaDNI(dni);
        } else if (getIntent().hasExtra("ID_SERVEI")) {
            String id_reserva = getIntent().getExtras().getString("ID_SERVEI");
            RetornaReservaId_Reserva(id_reserva);
        } else {
            Toast.makeText(this, "No s'ah rebut cap criteri de cerca", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param id_reserva a filtrar
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per id
     */
    public void RetornaReservaId_Reserva(String id_reserva) {
        db.obre();
        Cursor cursor = db.RetornaReservaId_Servei(id_reserva);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @param dni a filtrar
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per dni
     */
    public void RetornaReservaDNI(String dni) {
        db.obre();
        Cursor cursor = db.RetornaReservaDNI(dni);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @param dni  a filtrar
     * @param data a fitrar
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per dni i data
     */
    public void RetornaReservaDNI_DATA(String dni, String data) {
        db.obre();
        Cursor cursor = db.RetornaReservaDNI_DATA(dni, data);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @param data a filtrar
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per data
     */
    public void RetornaReservaDATA(String data) {
        db.obre();
        Cursor cursor = db.RetornaReservaData(data);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @param loc localitzador a filtrar
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per localitzador
     */
    private void RetornaReservaLoc(String loc) {
        //Toast.makeText(getBaseContext(), loc, Toast.LENGTH_SHORT).show();
        db.obre();
        Cursor cursor = db.RetornaReservaLocalitzador(loc);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @param qrCode a filtrar
     * @author Carlos Alberto Castro Cañabate
     * Mètode per obtenir les reserves filtrades per QR
     */
    public void RetornaReservaQR(String qrCode) {
        db.obre();
        Cursor cursor = db.RetornaReservaQR(qrCode);
        CursorBD(cursor);
        db.tanca();
    }

    /**
     * @param cursor a afegir entrada
     * @author Carlos Alberto Castro Cañabate
     * Mètode per afegir una nova entrada al dataSet
     */
    public void CursorBD(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                myDataset.add(new Header(cursor.getInt(cursor.getColumnIndex(Reserves._ID)), cursor.getString(cursor.getColumnIndex(ContracteBD.Client.NOM_TITULAR)) + " " + cursor.getString(cursor.getColumnIndex(ContracteBD.Client.COGNOM1_TITULAR)) + " " + cursor.getString(cursor.getColumnIndex(ContracteBD.Client.COGNOM2_TITULAR)),
                        "DNI: " + cursor.getString(cursor.getColumnIndex(ContracteBD.Client.DNI_TITULAR)), "Data Servei: " + cursor.getString(cursor.getColumnIndex(Serveis.DATA_SERVEI)),
                        "QR: " + cursor.getString(cursor.getColumnIndex(Reserves.QR_CODE)), "Localització: " + cursor.getString(cursor.getColumnIndex(Reserves.LOCALITZADOR))
                        , cursor.getString(cursor.getColumnIndex(ContracteBD.Client.EMAIL_TITULAR)), cursor.getString(cursor.getColumnIndex(Reserves.CHECK_IN)), cursor.getString(cursor.getColumnIndex(Serveis.DESCRIPCIO))));

            } while (cursor.moveToNext());
        }
    }

    /**
     * @author Antoni Torres Marí
     *
     * Mètode que obtè les dades del intent i les passa al Loader encarregat de
     * obtindre les dades del servidor
     */
    public void consultesOnline() {
        Bundle queryBundle = new Bundle();
        if (getIntent().hasExtra("LOCALITZADOR")) {
            String loc = getIntent().getExtras().getString("LOCALITZADOR");
            queryBundle.putString("loc", loc);
        } else if (getIntent().hasExtra(getString(R.string.scanner_result))) {
            String qrCode = getIntent().getStringExtra(getString(R.string.scanner_result));
            queryBundle.putString("qrcode", qrCode);
        } else if (getIntent().hasExtra("DNI") && getIntent().hasExtra("DATA")) {
            String dni = getIntent().getExtras().getString("DNI");
            String data = getIntent().getExtras().getString("DATA");
            queryBundle.putString("dni", dni);
            queryBundle.putString("data", data);
        } else if (getIntent().hasExtra("DATA")) {
            String data = getIntent().getExtras().getString("DATA");
            queryBundle.putString("data", data);
        } else if (getIntent().hasExtra("DNI")) {
            String dni = getIntent().getExtras().getString("DNI");
            queryBundle.putString("dni", dni);
        } else if (getIntent().hasExtra("ID_SERVEI")) {
            String id_servei = getIntent().getExtras().getString("ID_SERVEI");
            queryBundle.putString("idservei", id_servei);
        } else {
            Toast.makeText(this, "No s'ah rebut cap criteri de cerca", Toast.LENGTH_LONG).show();
        }
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(GET_REQUEST_LOADER, queryBundle, this);
    }

    /**
     * @author Antoni Torres Marí
     *
     * Aquest mètode crea un AsyncTaskLoader que realitzarà la consulta al servidor segons el criteri
     * de cerca i mostrarà les dades al recyclerview
     */
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<ArrayList<Header>>(this) {
            Gson gson;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                gson = new Gson();
                //Mostra Loading Indicator
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public ArrayList<Header> loadInBackground() {
                //Si per algún motiu no pot establir connexió amb el server tornarà null
                if (!isConnect.isPortOpen(mHost, mPort, 3000)) {
                    return null;
                }
                URL url = NetUtils.buildUrl(mHost, mPort, SERVEIS_PATH, null);
                //Efectua la petició GET per tal de descarregar tots els serveis en forma de json
                String json = NetUtils.doGetRequest(url);
                Type tipusLlistaDeServeis = new TypeToken<List<Servei>>() {
                }.getType();
                //Transforma aquest json en una llista d'objectes Servei
                List<Servei> llistaServeis = gson.fromJson(json, tipusLlistaDeServeis);
                ArrayList<Header> data = new ArrayList<>();
                //Si el criteri de cerca es localitzador, filtra les reserves que hi corresponen i ho afegeix a una llista d'objectes Header
                if (args.containsKey("loc")) {
                    for (Servei servei : llistaServeis) {
                        for (Reserva reserva : servei.getLlistaReserves()) {
                            if (reserva.getLocalitzador().equals(args.get("loc"))) {
                                data.add(new Header(reserva.getId(), reserva.getClient().getNom_titular() +
                                        " " + reserva.getClient().getCognom1_titular() + " " + reserva.getClient().getCognom2_titular(),
                                        reserva.getClient().getDni_titular(), servei.getData_servei(), reserva.getQr_code(), reserva.getLocalitzador(),
                                        reserva.getClient().getEmail_titular(), "" + reserva.getCheckin(), servei.getDescripcio()));
                            }
                        }
                    }
                    //Si els criteris de cerca son dni i data, filtra les reserves que hi corresponen i ho afegeix a una llista d'objectes Header
                } else if (args.containsKey("dni") && args.containsKey("data")) {
                    for (Servei servei : llistaServeis) {
                        for (Reserva reserva : servei.getLlistaReserves()) {
                            if (reserva.getClient().getDni_titular().equals(args.get("dni")) && servei.getData_servei().equals(args.get("data"))) {
                                data.add(new Header(reserva.getId(), reserva.getClient().getNom_titular() +
                                        " " + reserva.getClient().getCognom1_titular() + " " + reserva.getClient().getCognom2_titular(),
                                        reserva.getClient().getDni_titular(), servei.getData_servei(), reserva.getQr_code(), reserva.getLocalitzador(),
                                        reserva.getClient().getEmail_titular(), "" + reserva.getCheckin(), servei.getDescripcio()));
                            }
                        }
                    }
                    //Si el criteri de cerca es dni, filtra les reserves que hi corresponen i ho afegeix a una llista d'objectes Header
                } else if (args.containsKey("dni")) {
                    for (Servei servei : llistaServeis) {
                        for (Reserva reserva : servei.getLlistaReserves()) {
                            if (reserva.getClient().getDni_titular().equals(args.get("dni"))) {
                                data.add(new Header(reserva.getId(), reserva.getClient().getNom_titular() +
                                        " " + reserva.getClient().getCognom1_titular() + " " + reserva.getClient().getCognom2_titular(),
                                        reserva.getClient().getDni_titular(), servei.getData_servei(), reserva.getQr_code(), reserva.getLocalitzador(),
                                        reserva.getClient().getEmail_titular(), "" + reserva.getCheckin(), servei.getDescripcio()));
                            }
                        }
                    }
                    //Si el criteri de cerca es data, filtra les reserves dels serveis que hi corresponen i ho afegeix a una llista d'objectes Header
                } else if (args.containsKey("data")) {
                    for (Servei servei : llistaServeis) {
                        if (servei.getData_servei().equals(args.get("data"))) {
                            for (Reserva reserva : servei.getLlistaReserves()) {
                                data.add(new Header(reserva.getId(), reserva.getClient().getNom_titular() +
                                        " " + reserva.getClient().getCognom1_titular() + " " + reserva.getClient().getCognom2_titular(),
                                        reserva.getClient().getDni_titular(), servei.getData_servei(), reserva.getQr_code(), reserva.getLocalitzador(),
                                        reserva.getClient().getEmail_titular(), "" + reserva.getCheckin(), servei.getDescripcio()));
                            }
                        }
                    }
                    //Si el criteri de cerca es el codi qr, filtra les reserves que hi corresponen i ho afegeix a una llista d'objectes Header
                } else if (args.containsKey("qrcode")) {
                    for (Servei servei : llistaServeis) {
                        for (Reserva reserva : servei.getLlistaReserves()) {
                            if (reserva.getQr_code().equals(args.get("qrcode"))) {
                                data.add(new Header(reserva.getId(), reserva.getClient().getNom_titular() +
                                        " " + reserva.getClient().getCognom1_titular() + " " + reserva.getClient().getCognom2_titular(),
                                        reserva.getClient().getDni_titular(), servei.getData_servei(), reserva.getQr_code(), reserva.getLocalitzador(),
                                        reserva.getClient().getEmail_titular(), "" + reserva.getCheckin(), servei.getDescripcio()));
                            }
                        }
                    }
                    //Si el criteri de cerca es la id del servei, filtra les reserves del servei correponent i ho afegeix a una llista d'objectes Header
                } else if (args.containsKey("idservei")) {
                    for (Servei servei : llistaServeis) {
                        if (args.get("idservei").equals("" + servei.getId())) {
                            for (Reserva reserva : servei.getLlistaReserves()) {
                                data.add(new Header(reserva.getId(), reserva.getClient().getNom_titular() +
                                        " " + reserva.getClient().getCognom1_titular() + " " + reserva.getClient().getCognom2_titular(),
                                        reserva.getClient().getDni_titular(), servei.getData_servei(), reserva.getQr_code(), reserva.getLocalitzador(),
                                        reserva.getClient().getEmail_titular(), "" + reserva.getCheckin(), servei.getDescripcio()));
                            }
                        }
                    }
                }
                return data;
            }
        };
    }

    /**
     * @author Antoni Torres Marí
     *
     * Un cop s'han carregat les reserves en la llista d'objectes Header actúa en consequencia:
     */
    @Override
    public void onLoadFinished(Loader loader, Object data) {
        //Amaga la barra de progrés
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        //Si el loader torna null es que ha succeit algún problema amb la connexió
        if (data == null) {
            errorDialog(DetallActivity.this, "No s'ha pogut conectar amb el servidor.", R.drawable.connection_fail);
        //Si el loader ha tornat una llista buida vol dir que no ha trobat cap reserva amb els criteris de cerca donats
        } else if (((ArrayList<Header>) data).isEmpty()) {
            errorDialog(DetallActivity.this, "Reserva no trobada!", R.drawable.not_found);
        //Si la llista no es null ni buida, afegeix la llista al adapter per tal que ens mostri els resultats per pantalla
        } else {
            myDataset = (ArrayList<Header>) data;
            headerAdapter.actualitzaRecycler((ArrayList<Header>) data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    /**
     * @author Antoni Torres Marí
     *
     * En destruir aquesta activity es des-registra el listener de preferences
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * @author Antoni Torres Marí
     *
     * Aquesta classe AsyncTask ens crearà un fil d'execució que provarà de realitzar el check-in de una reserva
     */
    public class CheckInAsyncTask extends AsyncTask<String, Void, String> {

        Gson gson;

        @Override
        protected void onPreExecute() {
            gson = new Gson();
            //Mostram la barra de progrés
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... args) {
            //Si per algún motiu no pot establir connexió amb el server tornarà null
            if (!isConnect.isPortOpen(mHost, mPort, 3000)) {
                return null;
            }
            String query = args[0];
            //Efectua la petició POST i torna el resultat en forma de String json
            URL url = NetUtils.buildUrl(mHost, mPort, CHECKIN_PATH, null);
            return NetUtils.doPostRequest(url, query);
        }

        @Override
        protected void onPostExecute(String json) {
            //Amaga la barra de progrés
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            //Si el String json és null vol dir que ha succeit algún problema de connexió
            if (json == null) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetallActivity.this);
                alertDialog.setTitle("Check-In");
                alertDialog.setIcon(R.drawable.connection_fail);
                alertDialog.setMessage("No s'ha pogut establir connexió amb el servidor.");
                alertDialog.setPositiveButton("Acceptar", null);
                alertDialog.show();
            } else {
                //Transformem el String json a un objecte PostResponse
                PostResponse response = gson.fromJson(json, PostResponse.class);
                //Si el codi de PostResponse es 1 vol dir que el check-in ha anat be. Mostrarem el missatge
                //del servidor canviarem el color de fons del ítem del reyclerview a verd
                if (response.getRequestCode() == 1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetallActivity.this);
                    alertDialog.setTitle("Check-In");
                    alertDialog.setIcon(R.drawable.check_ok);
                    alertDialog.setMessage(response.getMessage());
                    alertDialog.setPositiveButton("Acceptar", null);
                    alertDialog.show();
                    mClickedItemView.setBackgroundColor(Color.rgb(204, 255, 204));
                    mClickedItemCheckText.setText("Check-In:  Realitzat");
                    db = new DBInterface(DetallActivity.this);
                    db.obre();
                    db.ActalitzaCheckInReserva(mClickedItemId);
                    db.tanca();
                } else {
                    //Si el codi de PostResponse es 0 vol dir que el check-in no ha anat be. Mostrarem el missatge
                    //del servidor i canviarem el color de fons del ítem del reyclerview a vermell
                    mClickedItemView.setBackgroundColor(Color.rgb(255, 204, 204));
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetallActivity.this);
                    alertDialog.setTitle("Check-In");
                    alertDialog.setIcon(R.drawable.check_fail);
                    alertDialog.setMessage(response.getMessage());
                    alertDialog.setPositiveButton("Acceptar", null);
                    alertDialog.show();
                }
            }
        }
    }

    /**
     * @author Antoni Torres Marí
     *
     * En realitzar un clic sobre un item del recyclerview iniciarem un fil d'execució que provarà
     * de realitzar el check-in sobre la reserva corresponent.
     * @param clickedItemId id de la reserva corresponent
     */
    @Override
    public void onListItemClick(final int clickedItemId, final View v, final TextView checkText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Vols Confirmar el Check-IN?")
                .setTitle("Atenció!!")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Acceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                mClickedItemView = v;
                                mClickedItemId = clickedItemId;
                                mClickedItemCheckText = checkText;

                                String query = "checkin=" + clickedItemId;
                                new CheckInAsyncTask().execute(query);
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }
}
