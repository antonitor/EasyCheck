package net.marcarni.easycheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.marcarni.easycheck.settings.SettingsActivity;

public class DniActivity extends AppCompatActivity {
    static final int ACTIVITAT_DATA = 1;
    TextView textView;
    EditText editTextDni;
    TextView textViewData;
    EditText editTextLocaltizador;
    Button buttonCheckIn;
    Button buttonData;
    String cadena = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dni);

        textView = (TextView) findViewById(R.id.textView);
        editTextDni = (EditText) findViewById(R.id.editTextDni);
        textViewData = (TextView) findViewById(R.id.textViewData);
        editTextLocaltizador = (EditText) findViewById(R.id.editTextLocalitzador);
        buttonCheckIn = (Button) findViewById(R.id.buttonCheckIn);
        buttonData = (Button) findViewById(R.id.buttonData);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            cadena = (String) bundle.get("DATO");
            if (cadena.equalsIgnoreCase("DNI")){
                editTextLocaltizador.setVisibility(View.GONE);
            } else {
                buttonData.setVisibility(View.GONE);
                editTextDni.setVisibility(View.GONE);
                textViewData.setVisibility(View.GONE);
            }
            textView.setText(cadena);
            String data = intent.getStringExtra("DATA");
            textViewData.setText(data);
        }
        buttonCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DniActivity.this,DetallActivity.class);
                if (cadena.equalsIgnoreCase("DNI")){
                    if (editTextDni.getText().toString().trim() != "")
                        intent.putExtra("DNI",editTextDni.getText().toString());
                    if (textViewData.getText() != "")
                        intent.putExtra("DATA",textViewData.getText());
                } else {
                    intent.putExtra("LOCALITZADOR",editTextLocaltizador.getText().toString());
                }
                startActivity(intent);

            }
        });
        buttonData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (DniActivity.this,CalendarActivity.class);
                startActivityForResult(intent, 1); // L'activitat retornarà un resultat.
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        //Comprovem a quina activitat correspon
        if(requestCode == ACTIVITAT_DATA)
        {
            //Si l'activitat ha acabat correctament
            if(resultCode == RESULT_OK)
            {
                //Recollim el text escollit al calendar (format data)
                String data = intent.getStringExtra("DATA");
                textViewData.setText(data);

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case R.id.action_qr:
                Intent startQrActivity = new Intent(this, QRScanner.class);
                startActivity(startQrActivity);
                return true;
            case R.id.action_dni:
                Intent startDniActivity = new Intent(this, DniActivity.class);
                startDniActivity.putExtra("DATO", "DNI");
                finish();
                startActivity(startDniActivity);
                return true;
            case R.id.action_loc:
                Intent startLocActivity = new Intent(this, DniActivity.class);
                startLocActivity.putExtra("DATO", "LOCALITZADOR");
                finish();
                startActivity(startLocActivity);
                return true;
            case R.id.action_logout:
                Intent logout = new Intent(this, MainActivity.class);
                startActivity(logout);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
