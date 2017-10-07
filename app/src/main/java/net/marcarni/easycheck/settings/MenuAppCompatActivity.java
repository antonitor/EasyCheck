package net.marcarni.easycheck.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import net.marcarni.easycheck.CheckCameraPermissionsActivity;
import net.marcarni.easycheck.ConsultaServeisActivity;
import net.marcarni.easycheck.DniActivity;
import net.marcarni.easycheck.MainActivity;
import net.marcarni.easycheck.QRScanner;
import net.marcarni.easycheck.R;
import net.marcarni.easycheck.settings.SettingsActivity;

public class MenuAppCompatActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

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
            case R.id.action_logout:
                Intent logout = new Intent(this, MainActivity.class);
                startActivity(logout);
                finish();
                return true;
            case R.id.menu_consultes:
                showConsultesSubmenuPopup((View)findViewById(R.id.menu_consultes));
                return true;
            case R.id.menu_gestor:
                showGestorSubmenuPopup((View)findViewById(R.id.menu_gestor));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showGestorSubmenuPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.getMenuInflater().inflate(R.menu.gestor_submenu, popup.getMenu());
        popup.show();
    }

    public void showConsultesSubmenuPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.getMenuInflater().inflate(R.menu.consultes_submenu, popup.getMenu());
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_qr:
                Intent startQrActivity = new Intent(this, CheckCameraPermissionsActivity.class);
                finish();
                startActivity(startQrActivity);
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
}
