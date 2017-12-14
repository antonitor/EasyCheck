package net.marcarni.easycheck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import net.marcarni.easycheck.SQLite.DBInterface;
import net.marcarni.easycheck.Utils.NetUtils;
import net.marcarni.easycheck.Utils.PostResponse;
import net.marcarni.easycheck.eines.Missatges;
import net.marcarni.easycheck.eines.Utilitats;
import net.marcarni.easycheck.eines.isConnect;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Maria
 *
 * Classe encarregada de fer les sustitucions
 *
 * Fucionalitat:
 * - La principal funcionalitat és la comprovació de la correcta substitució d'un treballador a un servei.
 * Solarment un administrador tindrà accés a aquesta funcionalitat.
 * Solament amb connexió internet es podrà realitzar la substitució
 * Trobem un seguit de restriccions per la correcta substitució creades per mètodes o mostrades per
 *      Alertdialogs recollides del servidor.
 */

public class SubstitucioActionActivity extends AppCompatActivity implements View.OnClickListener{
    TextView Tdescripcio, Tdata, Thora_inici, Titol;

    String descripcio, hora_inici, hora_final, data, idServei;
    int idTreballador;
    int treballador;
    Spinner spinnerTreballadors;
    Cursor cursor;
    private Gson gson = new Gson();
    Button btnAcceptar, btnCancelar;
    private String ip=LoginActivity.IP;
    private int port=LoginActivity.PORT;
    private static  String PATH="/easycheckapi/treballador";
    private static String ID_SERVEI="idservei=";
    private static String ID_TREBALLADOR="&idtreballador=";
    DBInterface db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitucio_action);
        Tdescripcio = findViewById(R.id.descripcio_servei);
        Tdata = findViewById(R.id.textViewdata);

        Thora_inici = findViewById(R.id.textViewHora_inici);
        Titol=findViewById(R.id.titol);
        spinnerTreballadors = findViewById(R.id.spinner_de_treballadors);
        btnAcceptar=findViewById(R.id.Acceptar);
        btnCancelar=findViewById(R.id.btnCancelar);
        db=new DBInterface(this);

        cursor = Utilitats.getCursorSpinner((ArrayList<Treballador>) Treballador.getTreballadors());

        android.widget.SimpleCursorAdapter adapter = new android.widget.SimpleCursorAdapter(
                SubstitucioActionActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                cursor,
                new String[]{"nom"},
                new int[]{android.R.id.text1}, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTreballadors.setAdapter(adapter);
        btnAcceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

         idServei = getIntent().getStringExtra("servei");

        Servei servei = Utilitats.cercaServei(idServei);
        descripcio = servei.getDescripcio();
        hora_final = servei.getHora_final();
        hora_inici = servei.getHora_inici();
        idTreballador = servei.getId_treballador();
        data = servei.getData_servei();

        Tdescripcio.setText(descripcio);
        spinnerTreballadors.setSelection(idTreballador);
        Tdata.setText("Data del servei: "+data);
        Thora_inici.setText("Hora del servei: " +hora_inici+" - "+hora_final);





        spinnerTreballadors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                treballador=cursor.getInt(0);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
           );



    }

    /**
     * Mètode implementat per controlar la correcta selecció del treballador per la substitució, així com de
     * correcta connexió a internet.
     *
     * @param view
     */
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.Acceptar:
                        if (cursor.getInt(0) != 0) {
                            if (idTreballador == cursor.getInt(0)) {
                                Missatges.AlertMissatge("SELECCIONAR TREBALLADOR",
                                        "Ha seleccionat el mateix treballador pel servei. Seleccioni un treballador diferent!!", R.drawable.ic_prohibit, SubstitucioActionActivity.this);
                            } else {
                                if (isConnect.isDisponible(SubstitucioActionActivity.this)) {
                                    new DescarregaServer().execute(new Integer[]{Integer.valueOf(idServei), cursor.getInt(0)});

                                } else {
                                    Missatges.AlertMissatge("WIFI NO DISPONIBLE", "Aquesta operació no es podrà realitzar sense connexió", R.drawable.fail, SubstitucioActionActivity.this);
                                }
                            }
                        }
                        else
                        {
                            Missatges.AlertMissatge("TREBALLADOR NO VALID!!", "Aquesta opció no es pot triar. Seleccioni un altre treballador.",R.drawable.ic_prohibit,SubstitucioActionActivity.this);
                        }
                            break;
                            case R.id.btnCancelar:
                                finish();
                                break;


                }
            }

    /**
     * Classe que hereta de Asyntask, encarregada de connectar i enviar petició HTTP al servidor per substitució
     */
    public class DescarregaServer extends AsyncTask<Integer, ArrayList, PostResponse> {

        /**
         *  Mostrarà missatge en cas de problema en l'operació de substitució, falta de connexió, etc.
         * @param r PostResponse amb:
         *          - Integer amb el codi (0,1) d'operació
         *          - String amb missatge resposta en cas problema
         */
                @Override
                protected void onPostExecute(PostResponse r) {
                    super.onPostExecute(r);
                    String resposta = r.getMessage();
                    int codiResultat = r.getRequestCode();
                    if (codiResultat == 0) {
                        Missatges.AlertMissatge("OPERACIÓ NO REALITZADA", resposta, R.drawable.ic_problem, SubstitucioActionActivity.this);
                    } else {

                        db.obre();
                        boolean database = db.ActualitzaServei(idServei, cursor.getInt(0));
                        db.tanca();
                        if (database) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubstitucioActionActivity.this);
                            alertDialog.setTitle("OPERACIÓ REALITZADA");
                            alertDialog.setMessage("Substitució realitzada amb éxit");
                            alertDialog.setIcon(R.drawable.ic_ok);
                            alertDialog.setCancelable(false);
                            alertDialog.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            startActivity(new Intent(SubstitucioActionActivity.this, SubstitucionsActivity.class));


                                        }
                                    }
                            );

                            alertDialog.show();

                        } else {
                            Missatges.AlertMissatge("ERROR D'ACTUALITZACIÓ", "S'ha produit un error a l'actualització de la base de dades interna.\n Reinicii l'aplicació", R.drawable.ic_problem, SubstitucioActionActivity.this);
                        }
                    }


                }

        /**
         * Mètode d'asyntask que connecta amb el servidor, i envia petició al servidor de substitucio amb protocol HTTP
         * @param ints Integers amb l'id del servei que es vol fer la substitucio i id del treballador substitut
         * @return PostResponse amb el resultat de l'operació
         */
                protected PostResponse doInBackground(Integer... ints) {
                    PostResponse response = null;
                    try {
                        URL url = new URL("http://" + ip + ":" + port + PATH);
                        String json = NetUtils.doPostRequest(url, ID_SERVEI + ints[0] + ID_TREBALLADOR + ints[1]);
                        response = gson.fromJson(json, PostResponse.class);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    return response;
                }
            }

        }