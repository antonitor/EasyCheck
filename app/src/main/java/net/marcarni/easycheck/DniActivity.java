package net.marcarni.easycheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DniActivity extends AppCompatActivity {
    TextView textView;
    EditText editTextDni;
    EditText editTextData;
    EditText editTextHora;
    EditText editTextLocaltizador;
    Button buttonCheckIn;
    String cadena = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dni);

        textView = (TextView) findViewById(R.id.textView);
        editTextDni = (EditText) findViewById(R.id.editTextDni);
        editTextData = (EditText) findViewById(R.id.editTextData);
        editTextHora = (EditText) findViewById(R.id.editTextHora);
        editTextLocaltizador = (EditText) findViewById(R.id.editTextLocalitzador);
        buttonCheckIn = (Button) findViewById(R.id.buttonCheckIn);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            cadena = (String) bundle.get("DATO");
            if (cadena.equalsIgnoreCase("DNI")){
                editTextLocaltizador.setVisibility(View.INVISIBLE);
            } else {
                editTextDni.setVisibility(View.INVISIBLE);
                editTextData.setVisibility(View.INVISIBLE);
                editTextHora.setVisibility(View.INVISIBLE);
            }
            textView.setText(cadena);
        }
        buttonCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),DetallActivity.class);
                if (cadena.equalsIgnoreCase("DNI")){
                    Toast.makeText(getBaseContext(), "Enviaments dades DNI.", Toast.LENGTH_SHORT).show();
                    intent.putExtra("DNI",editTextDni.getText());
                    intent.putExtra("DATA",editTextData.getText());
                    intent.putExtra("HORA",editTextHora.getText());
                } else{
                    Toast.makeText(getBaseContext(), "Enviaments dades Localitzador", Toast.LENGTH_SHORT).show();
                    intent.putExtra("LOCALITZADOR",editTextLocaltizador.getText());
                }
                startActivity(intent);
            }
        });
    }
}
