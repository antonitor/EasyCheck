package net.marcarni.easycheck.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.marcarni.easycheck.CheckCameraPermissionsActivity;
import net.marcarni.easycheck.ConsultaServeisActivity;
import net.marcarni.easycheck.DniActivity;
import net.marcarni.easycheck.MainActivity;
import net.marcarni.easycheck.R;

/**
 * Aquesta clase hereta d'AppCompatActivty a l'hora que implementa PopupMenu.OnItemClickListener.
 * Afegeix el menú principal amb Logout i Settings, i dos PopupMenu, un pels gestors de reserves
 * i l'altre per les consultes de serveis, i gestiona els corresponents Listeners.
 *
 * @author Antoni Torres Marí
 */
public class MenuAppCompatActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    /**
     * Created by Antoni Torres Marí
     *
     * Infla el menú principal main.xml
     * @param menu menú de la toolbar
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Created by Antoni Torres Marí
     *
     * Gestiona l'esdeveniment quan es selecciona un MenuItem del menú principal
     * @param item MenuItem seleccionat
     * @return true si s'ha capturat un dels items del menú
     */
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
                //En premer aquest botó es desplega un PopupMenu
                showConsultesSubmenuPopup((View)findViewById(R.id.menu_consultes));
                return true;
            case R.id.menu_gestor:
                //En premer aquest botó es desplega un PopupMenu
                showGestorSubmenuPopup((View)findViewById(R.id.menu_gestor));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Created by Antoni Torres Marí
     *
     * De aquet View que pasem per paràmetre s'hi desplega un PopupMenú inflat amb el recurs
     * gestor_submenu.xml i s'hi afegeix el listener PopupMenu.OnMenuItemClickListener que implementa
     * aquesta clase
     * @param v View a partir del qual es desplegará el popupMenu
     */
    public void showGestorSubmenuPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.getMenuInflater().inflate(R.menu.gestor_submenu, popup.getMenu());
        popup.show();
    }

    /**
     * Created by Antoni Torres Marí
     *
     * De aquet View que pasem per paràmetre s'hi desplega un PopupMenú inflat amb el recurs
     * consultes_submenu.xml i s'hi afegeix el listener PopupMenu.OnMenuItemClickListener que implementa
     * aquesta clase
     * @param v View a partir del qual es desplegará el popupMenu
     */
    public void showConsultesSubmenuPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.getMenuInflater().inflate(R.menu.consultes_submenu, popup.getMenu());
        popup.show();
    }


    /**
     * Created by Antoni Torres Marí
     *
     * Metode implementat de l'interfície PopupMenu.OnMenuItemClickListener que gestiona els
     * esdeveniments que es llençen al premer un dels items d'un dels PopupMenu
     *
     * @param item MenuItem seleccionat
     * @return true si s'ha seleccionat un dels MenuItem que es capturen en el switch
     */
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
