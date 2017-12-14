package net.marcarni.easycheck;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;

import net.marcarni.easycheck.RecyclerView.HeaderAdapter_Substitucio;
import net.marcarni.easycheck.RecyclerView.Header_Consulta;
import net.marcarni.easycheck.Utils.DescargaServei;
import net.marcarni.easycheck.Utils.DescargaTreballador;
import net.marcarni.easycheck.eines.Missatges;
import net.marcarni.easycheck.eines.Utilitats;
import net.marcarni.easycheck.eines.isConnect;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Maria
 *
 * Classe prèvia a l'activitat de substitució.
 * Solament tindrà accés usuari administrador.
 * L'objectiu es mostrar tots els serveis detallats o filtrats per treballador i data.
 * En clicar un servei s'obrirà l'activitat encarregada de la substitució.
 */

public class SubstitucionsActivity extends MenuAppCompatActivity {


    private String fecha = null, time = null;
    private HeaderAdapter_Substitucio headerAdapter_substitucio;
    private ArrayList<Header_Consulta> myDataset;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Spinner spinnerTreballadors;
    private ArrayList<Treballador> treballadors;
    private int treballador;
    List<Treballador> listaTreballadors = new ArrayList<>();
    List<Servei> llistaServeis = new ArrayList<>();
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitucions);
        Toolbar editToolbar = findViewById(R.id.filter_toolbar);
        editToolbar.inflateMenu(R.menu.toolbar_menu);
        spinnerTreballadors = findViewById(R.id.spinner_de_treballadors);
        myDataset = new ArrayList<>();
        headerAdapter_substitucio = new HeaderAdapter_Substitucio(myDataset);
        recyclerView = findViewById(R.id.recyclerView_consulta);


        findViewById(R.id.cancelar_filtros).setVisibility(View.INVISIBLE);
        findViewById(R.id.seleccionar_hora).setVisibility(View.GONE);
        headerAdapter_substitucio.notifyDataSetChanged();
        linearLayoutManager = new LinearLayoutManager(SubstitucionsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(headerAdapter_substitucio);

        new DescarregaServer().execute(LoginActivity.IP);



    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////              AsynTask              //////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Classe encarregada de la descarrega dels serveis i treballadors i mostrar-les en un recycler amb
     * opció de filtrat(treballador i data)
     *
     * La funcionalitat comprovarà connectivitat cada cop que es vulgui accedir a una substitució
     */
    public class DescarregaServer extends AsyncTask<String, ArrayList, ArrayList<Treballador>> {


        @Override
        protected void onPostExecute(ArrayList<Treballador> s) {
            super.onPostExecute(s);


            implementarSpinner();

            findViewById(R.id.seleccionar_data).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PickerData();
                }
            });
            findViewById((R.id.cancelar_filtros)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    esborraFiltres();
                    if (isConnect.isDisponible(SubstitucionsActivity.this)) {
                        new DescarregaServer().execute(LoginActivity.IP);
                        implementarSpinner();
                        llistaSenseFiltrats();
                    } else {
                        Missatges.AlertMissatge("WIFI NO DISPONIBLE", "És necessària connexió a internet per dur a terme les substitucions", R.drawable.fail, SubstitucionsActivity.this);
                    }

                }
            });

        }

        protected ArrayList<Treballador> doInBackground(String... urls) {

            treballadors = (ArrayList<Treballador>) RetornaTreballadors();
            llistaServeis = RetornaServeis();
            Treballador.setTreballadors(RetornaTreballadors());
            omplirServei((ArrayList<Servei>) llistaServeis);

            return null;

        }

        /**
         * Mètode que carrega un Arraylist de serveis, previament descarregat del servidor
         * a la llista stàtica de la classe Servei
         *
         * @param llistaServeis ArrayList amb el llistat de tots els serveis que es troben al servidor.
         */

        public void omplirServei(ArrayList<Servei> llistaServeis) {
            for (int i = 0; i < llistaServeis.size(); i++) {
                Servei.setLlistaServeis(llistaServeis.get(i));
            }

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////             SPINNER TREBALLADORS            /////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * Mètode encarregat de repoblar i implementar l'spinner amb els treballadors
         *
         */
        private void implementarSpinner() {

            cursor = Utilitats.getCursorSpinner(treballadors);

            android.widget.SimpleCursorAdapter adapter = new android.widget.SimpleCursorAdapter(
                    SubstitucionsActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    cursor,
                    new String[]{"nom"},
                    new int[]{android.R.id.text1}, 0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTreballadors.setAdapter(adapter);
            spinnerTreballadors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    treballador = cursor.getInt(0);
                    if (fecha == null) {
                        if (treballador == 0) {
                            llistaSenseFiltrats();
                        } else {
                            llistaPerTreballador();
                        }
                    } else {
                        if (cursor.getInt(0) == 0) {
                            myDataset = new ArrayList<Header_Consulta>();
                            myDataset = Utilitats.RetornaTotElsServeisPerData(fecha, (ArrayList<Servei>) llistaServeis);
                            headerAdapter_substitucio.actualitzaRecycler(myDataset);
                        } else {
                            myDataset = new ArrayList<Header_Consulta>();
                            myDataset = Utilitats.RetornaTotElsServeisPerData_Treballador(fecha, cursor.getInt(0), (ArrayList<Servei>) llistaServeis);
                            headerAdapter_substitucio.actualitzaRecycler(myDataset);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        }


        //////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////          LListats          /////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * Mètode cridat per actualitzar i mostrar
         */
        public void llistaSenseFiltrats() {

            headerAdapter_substitucio.actualitzaRecycler(Utilitats.retornaTotsElsServeis((ArrayList) llistaServeis));
        }

        public void llistaPerTreballador() {

            headerAdapter_substitucio.actualitzaRecycler((ArrayList<Header_Consulta>) Utilitats.RetornaServeisPerId(treballador, (ArrayList) llistaServeis));

        }


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////      Descarrega del server              /////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////


        public List<Treballador> RetornaTreballadors() {

            return listaTreballadors = DescargaTreballador.obtenirTreballadorsDelServer(LoginActivity.IP);

        }

        public List<Servei> RetornaServeis() {

            return llistaServeis = DescargaServei.obtenirServeisDelServer(LoginActivity.IP);


        }


        //////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////    UTILS            ///////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        public void carregarDataTreballador() {

            if (treballador == 0) {
                myDataset = new ArrayList<Header_Consulta>();
                myDataset = Utilitats.RetornaTotElsServeisPerData(fecha, (ArrayList<Servei>) llistaServeis);
                headerAdapter_substitucio.actualitzaRecycler(myDataset);
            } else {
                myDataset = new ArrayList<Header_Consulta>();
                myDataset = Utilitats.RetornaTotElsServeisPerData_Treballador(fecha, treballador, (ArrayList<Servei>) llistaServeis);
                headerAdapter_substitucio.actualitzaRecycler(myDataset);
            }

        }

        public void PickerData() {
            Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog mDatePicker;
            mDatePicker = new DatePickerDialog(SubstitucionsActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    selectedmonth = selectedmonth + 1;
                    fecha = "" + selectedday + "/" + selectedmonth + "/" + selectedyear;
                    if (Utilitats.isOK(selectedmonth)) {
                        fecha = "" + selectedday + "/0" + selectedmonth + "/" + selectedyear;
                        {
                            if (Utilitats.isOK(selectedday)) {
                                fecha = "0" + selectedday + "/0" + selectedmonth + "/" + selectedyear;
                            }

                        }
                    }
                    findViewById(R.id.cancelar_filtros).setVisibility(View.VISIBLE);
                    carregarDataTreballador();
                }

            }, mYear, mMonth, mDay);
            mDatePicker.setTitle("Selecciona Data");

            mDatePicker.show();

        }
    }

    private void esborraFiltres() {
        fecha = null;
        time = null;
        treballador = 0;
        findViewById(R.id.cancelar_filtros).setVisibility(View.INVISIBLE);

    }


}