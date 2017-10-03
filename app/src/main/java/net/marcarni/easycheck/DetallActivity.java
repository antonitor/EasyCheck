package net.marcarni.easycheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetallActivity extends AppCompatActivity {
    TextView textViewDni;
    TextView textViewData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detall);

        textViewDni = (TextView) findViewById(R.id.textViewDni);
        textViewData = (TextView) findViewById(R.id.textViewData);
        String dni = getIntent().getExtras().getString("DNI");
        String data = getIntent().getExtras().getString("DATA");
        textViewDni.setText(dni);
        textViewData.setText(data);
    }
}
