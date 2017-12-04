package net.marcarni.easycheck;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.marcarni.easycheck.RecyclerView.HeaderAdapter_Consulta;
import net.marcarni.easycheck.RecyclerView.Header_Consulta;
import net.marcarni.easycheck.Utils.DescargaTreballador;
import net.marcarni.easycheck.model.Treballador;

import java.util.ArrayList;
import java.util.List;

public class ConsultaServeisOnlineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int DATE_PICKER_REQUEST = 22;
    private static final int HOUR_PICKER_REQUEST = 25;

    private HeaderAdapter_Consulta headerAdapter_consulta;
    private ArrayList<Header_Consulta> myDataset;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private long treballador = 0;
    private String fecha = null;
    private String time = null;
    private ArrayAdapter<String> adapter;
    private Spinner spinnerTreballadors;
    private ArrayList <Treballador> treballadors;
    TextView t;
    List <Treballador> listaTreballadors=new ArrayList<>();
    List <String> nomsTreballadors=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_serveis_online);
        Toolbar editToolbar = (Toolbar) findViewById(R.id.filter_toolbar);
        editToolbar.inflateMenu(R.menu.toolbar_menu);
        t = (TextView) findViewById(R.id.text);
        spinnerTreballadors = (Spinner) findViewById(R.id.spinner_de_treballadors);
        myDataset = new ArrayList<>();
        headerAdapter_consulta = new HeaderAdapter_Consulta(myDataset);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_consulta);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomsTreballadors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTreballadors.setAdapter(adapter);

        spinnerTreballadors.setOnItemSelectedListener(this);
        new descargarTreballadors().execute(DescargaTreballador.IP);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        Toast.makeText(this,
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class descargarTreballadors extends AsyncTask<String, ArrayList, ArrayList<Treballador>> {
        @Override
        protected void onPostExecute(ArrayList<Treballador> s) {
            super.onPostExecute(s);


            for(int i=0;i<s.size();i++){
                nomsTreballadors.add(s.get(i).getNom());
            }



        }

        protected ArrayList<Treballador> doInBackground(String... urls) {

            return (ArrayList<Treballador>) DescargaTreballador.obtenirTreballadorsDelServer(urls[0]);


        }

        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();

        }
    }}