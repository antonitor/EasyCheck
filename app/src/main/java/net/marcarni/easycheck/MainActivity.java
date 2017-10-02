package net.marcarni.easycheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView defaultMethodTextView = findViewById(R.id.defecte);
        String defaultMethod = sharedPreferences.getString(getString(R.string.pref_manager_default_key), getString(R.string.pref_manager_default_qr_value));
        if (defaultMethod.equals(getString(R.string.pref_manager_default_qr_value)) ) {
            defaultMethodTextView.setText(getString(R.string.pref_manager_default_qr_label));
        } else if (defaultMethod.equals(getString(R.string.pref_manager_default_dni_value))) {
            defaultMethodTextView.setText(getString(R.string.pref_manager_default_dni_label));
        } else {
            defaultMethodTextView.setText(getString(R.string.pref_manager_default_loc_label));
        }


        ((Button)findViewById(R.id.scan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CheckCameraPermissionsActivity.class));
            }
        });
    }
}
