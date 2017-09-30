package net.marcarni.easycheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRScanner extends AppCompatActivity {

    private SurfaceView mCameraView;
    private CameraSource mCameraSource;
    private BarcodeDetector mBarcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner2);

        mCameraView = (SurfaceView) findViewById(R.id.camera);

        mBarcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE | Barcode.EAN_13 | Barcode.EAN_8).build();
        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector).setRequestedPreviewSize(1080, 720).setAutoFocusEnabled(true).build();

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
                    Intent intent = new Intent(QRScanner.this, DetailActivity.class);
                    intent.putExtra("QR", barcodes.valueAt(0).displayValue);
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
