package net.marcarni.easycheck.Utils;

/**
 * @author Carlos Alberto Castro Cañabate
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import net.marcarni.easycheck.CheckCameraPermissionsActivity;
import net.marcarni.easycheck.DniActivity;
import net.marcarni.easycheck.R;


public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    public FingerprintHandler(Context context) {
        this.context = context;
    }

    /**
     * Mètode per autentificar l'imprenta digital
     * @param fingerprintManager fingerprintManager,
     * @param cryptoObject cryptoObject
     */
    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cenCancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;
        fingerprintManager.authenticate(cryptoObject,cenCancellationSignal,0,this,null);


    }

    /**
     * Mètode per mostrar missatge al no autentificar l'imprenta digital
     */
    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(context, "Error al llegir la imprenta digital!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Mètode per gestionar les accions a realitzar un  cop autentificada l'imprenta digital
     * @param result de l'autentificació.
     */
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultMethod = sharedPreferences.getString(context.getString(R.string.pref_manager_default_key),context.getString(R.string.pref_manager_default_qr_value));

        Intent mDniIntent;
        if (defaultMethod.equals(context.getString(R.string.pref_manager_default_qr_value))) {
            Toast.makeText(context, "QR", Toast.LENGTH_SHORT).show();
            mDniIntent = new Intent(context, CheckCameraPermissionsActivity.class);
            //Si el gestor per defecte és DNI, el botó login llença dniactivity amb dni
        } else if (defaultMethod.equals(context.getString(R.string.pref_manager_default_dni_value))) {
            mDniIntent = new Intent(context, DniActivity.class);
            mDniIntent.putExtra("DATO", "DNI");
            //Si el gestor per defecte és Loc, el botó login llença dniactivity amb Loc
        } else {
            Toast.makeText(context, "LOCALITZADOR", Toast.LENGTH_SHORT).show();
            mDniIntent = new Intent(context, DniActivity.class);
            mDniIntent.putExtra("DATO", "LOCALITZADOR");
        }
        context.startActivity(mDniIntent);

    }
}
