package net.marcarni.easycheck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetallActivity extends AppCompatActivity {
    TextView textViewDni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detall);

        textViewDni = (TextView) findViewById(R.id.textViewDni);
        String dni = getIntent().getExtras().getString("DNI");
        textViewDni.setText(dni);

    }
}
