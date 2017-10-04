package net.marcarni.easycheck;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.marcarni.easycheck.R;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;
import net.marcarni.easycheck.settings.SettingsActivity;

public class CheckCameraPermissionsActivity extends MenuAppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSIONS = 2;
    private ImageButton mButton;
    private TextView mTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_camera_permissions);
        mButton = (ImageButton) findViewById(R.id.preguntar_permissos);
        mTextView = (TextView) findViewById(R.id.permissos_necessaris);
        mButton.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkPermissions(null);

    }

    public void checkPermissions(View view){
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS);
            }
        } else {
            startScanning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanning();
                } else {
                    mButton.setVisibility(View.VISIBLE);
                    mTextView.setVisibility(View.VISIBLE);
                }
                return;
            }
        }
    }

    private void startScanning(){
        Intent intent = new Intent(this, QRScanner.class);
        boolean autoFocus = sharedPreferences.getBoolean(getString(R.string.pref_autofocus_key), getResources().getBoolean(R.bool.pref_autofocus_default));
        intent.putExtra(getString(R.string.auto_focus_extra), autoFocus);
        finish();
        startActivity(intent);
    }
}
