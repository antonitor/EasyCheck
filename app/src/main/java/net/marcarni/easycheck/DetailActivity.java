package net.marcarni.easycheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.marcarni.easycheck.settings.SettingsActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ((TextView)findViewById(R.id.code_readed)).setText(getIntent().getStringExtra(getString(R.string.scanner_result)));
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
            case R.id.action_logout:
                Intent logout = new Intent(this, MainActivity.class);
                startActivity(logout);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void next(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultMethod = sharedPreferences.getString(getString(R.string.pref_manager_default_key), "");
        if (defaultMethod.equals(getString(R.string.pref_manager_default_qr_value)) ) {
            Intent startQrActivity = new Intent(this, QRScanner.class);
            startActivity(startQrActivity);
        } else if (defaultMethod.equals(getString(R.string.pref_manager_default_dni_value))) {
            String dniManagerMethod = getString(R.string.pref_manager_default_dni_label);
            Toast.makeText(this,dniManagerMethod + ": no implementat.",Toast.LENGTH_LONG).show();
        } else {
            String dniManagerMethod = getString(R.string.pref_manager_default_loc_label);
            Toast.makeText(this,dniManagerMethod + ": no implementat.",Toast.LENGTH_LONG).show();
        }
    }

}
