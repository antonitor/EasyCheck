package net.marcarni.easycheck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import net.marcarni.easycheck.settings.MenuAppCompatActivity;

import java.io.IOException;


/**
 * Aquesta Activity hereta de MenuAppCompatActivity per tal de mostrar el menú principal de l'App
 *
 * Implementa BarcodeDetector de les Apis de Google que, mitjançant la càmera del dispositiu,
 * capturarà el primer Codi De Barres (EAN_13 o EAN_8) o codi QR, i en pasarà el contingut a
 * l'Activity DetallActivity.class en forma de Extra.
 *
 * @Author Antoni Torres Marí
 */
public class QRScanner extends MenuAppCompatActivity {

    private SurfaceView mCameraView;
    private CameraSource mCameraSource;
    private BarcodeDetector mBarcodeDetector;

    /**
     * Created by Antoni Torres Marí
     *
     * Mètode onCreate de la classe QRScanner
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        Intent intentThatStartedThisActivity = getIntent();
        //Aquesta Activity reb un Extra booleà que indicarà si la opció AutoFocus de la càmera
        //ha de estar operatiu o no.
        boolean autoFocus = intentThatStartedThisActivity.getBooleanExtra(getString(R.string.auto_focus_extra),true);
        startScanning(autoFocus);
    }

    /**
     * Created by Antoni Torres Marí
     *
     * Engega la càmera del dispositiu i en mostra la captura al SurfaceView
     * Afegeix una nova instància de BarcodeDetector i l'afegeix al CameraSource
     * Afegeix els CallBacks al SurfaceView per tal de recollir els esdeveniments recollits
     * per la intància de BarcodeDetector dins aquesta classe
     * @param autoFocus opció AutoFocus de la càmera
     */
    public void startScanning(boolean autoFocus) {
        mCameraView = (SurfaceView) findViewById(R.id.camera);
        mBarcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE | Barcode.EAN_13 | Barcode.EAN_8).build();
        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(1600, 1024).setAutoFocusEnabled(autoFocus).build();
        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

            /**
             * Created by Antoni Torres Marí
             *
             * Torna a comprovar que s'han otorgat els permissos corresponents per utilitzar la
             * camèra del dispositiu
             * @param holder Holder del SurfaceView que implementa els Callbacks
             */
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCameraSource.start(mCameraView.getHolder());
                } catch (SecurityException se) {
                    Log.e("CAMERA PERMISSION", se.getMessage());
                } catch (IOException ioe) {
                    Log.e("CAMERA SOURCE", ioe.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            /**
             * Created by Antoni Torres Marí
             *
             * Metode que tanca la camera quan es finalitza l'Activity
             * @param holder SurfaceHolder
             */
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });

        //S'afegeix una nova instancia de Detector.Processor que serà el que recullirà
        //les captures de codis de barres o qr
        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            /**
             * Created by Antoni Torres Marí
             *
             * Quan es reb la primera detecció de codi de barres o QR es tanca aquesta
             * Activity i se n'envia el contingut a l'Activity DetallActivity.class
             * @param detections
             */
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    mBarcodeDetector.release();
                    Intent intent = new Intent(QRScanner.this, DetallActivity.class);
                    intent.putExtra(getString(R.string.scanner_result), barcodes.valueAt(0).displayValue);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Created by Antoni Torres Marí
     *
     * Aquesta Activity sobreescriu el mètode onMenuItemClick de MenuAppCompatActivity per tal
     * d'anul·lar l'esdeveniment que truca CheckCameraPermissionsActivity ja que si s'intentas
     * obrir una segona càmera l'aplicació fallaria
     * @param item MenuItem seleccionat
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            //Si es sel·lecciona l'opció per engegar el QRScanner no fa res
            case R.id.action_qr:
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
            case R.id.action_services:
                Intent startServicesActivity = new Intent(this, ConsultaServeisActivity.class);
                finish();
                startActivity(startServicesActivity);
                return true;
            default:
                return false;
        }
    }

    /**
     * Created by Antoni Torres Marí
     *
     * Al destruir aquesta Activity es tanca la instància de BarcodeDetector i el CameraSource
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraSource.release();
        mBarcodeDetector.release();
    }

}
