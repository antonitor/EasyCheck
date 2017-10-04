package net.marcarni.easycheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import net.marcarni.easycheck.settings.MenuAppCompatActivity;
import net.marcarni.easycheck.settings.SettingsActivity;

import java.io.IOException;

public class QRScanner extends MenuAppCompatActivity {


    private SurfaceView mCameraView;
    private CameraSource mCameraSource;
    private BarcodeDetector mBarcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        Intent intentThatStartedThisActivity = getIntent();
        boolean autoFocus = intentThatStartedThisActivity.getBooleanExtra(getString(R.string.auto_focus_extra),true);
        startScanning(autoFocus);
    }

    public void startScanning(boolean autoFocus) {
        mCameraView = (SurfaceView) findViewById(R.id.camera);
        mBarcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE | Barcode.EAN_13 | Barcode.EAN_8).build();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(1600, 1024).setAutoFocusEnabled(autoFocus).build();
        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
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

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraSource.release();
        mBarcodeDetector.release();
    }

}
