package net.marcarni.easycheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonDNI;
        Button buttonLocalitzador;
        // Inicialitzem Buttons
        buttonDNI = (Button) findViewById(R.id.button);
        buttonLocalitzador = (Button) findViewById(R.id.button2);
        // Assignem listeners:
        buttonDNI.setOnClickListener(this);
        buttonLocalitzador.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,DniActivity.class);
        switch (view.getId()){
            case R.id.button:
                intent.putExtra("DATO","DNI");
                break;
            case R.id.button2:
                intent.putExtra("DATO","LOCALITZADOR");
                break;
        }
        startActivity(intent);
    }
}
