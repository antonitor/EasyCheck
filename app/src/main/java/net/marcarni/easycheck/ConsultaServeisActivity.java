package net.marcarni.easycheck;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import net.marcarni.easycheck.settings.MenuAppCompatActivity;

public class ConsultaServeisActivity extends MenuAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_serveis);

        //Configuració del toolbar amb els filtres
        Toolbar editToolbar = (Toolbar) findViewById(R.id.filter_toolbar);
        editToolbar.inflateMenu(R.menu.toolbar_menu);
        Spinner spinnerTreballadors = (Spinner) findViewById(R.id.spinner_de_treballadors);

        //Cursor amb dades test, s'ha d'esborrar el métode getFakeCursor y extraure-les de la bbdd
        Cursor cursorTest =  getFakeCursor(); //db.obtenirLlistaDeTreballadors();

        android.widget.SimpleCursorAdapter  adapter = new android.widget.SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                cursorTest,
                new String[] {"treballador"}, //Columna del cursor que volem agafar
                new int[] {android.R.id.text1},0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTreballadors.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spinnerTreballadors != null) {
            spinnerTreballadors.setAdapter(adapter);

            spinnerTreballadors.setOnItemSelectedListener(new myOnItemSelectedListener());
        }
    }

    public MatrixCursor getFakeCursor(){
        String[] columns = new String[] { "_id", "treballador" };

        MatrixCursor matrixCursor= new MatrixCursor(columns);
        startManagingCursor(matrixCursor);

        matrixCursor.addRow(new Object[] { 1, "Toni"});
        matrixCursor.addRow(new Object[] { 2, "Mari"});
        matrixCursor.addRow(new Object[] { 3, "Carlos"});
        return matrixCursor;
    }
}

class myOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        //Recarregar aquí el RecyclerView segons el treballador seleccionat
        Toast.makeText(view.getContext(), "Treballador amb _ID = " + id + " seleccionat.", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
