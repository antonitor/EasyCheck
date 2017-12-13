package net.marcarni.easycheck.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.marcarni.easycheck.CheckCameraPermissionsActivity;
import net.marcarni.easycheck.ConsultaServeisActivity;
import net.marcarni.easycheck.ConsultaServeisOnlineActivity;
import net.marcarni.easycheck.DniActivity;
import net.marcarni.easycheck.LoginActivity;
import net.marcarni.easycheck.R;
import net.marcarni.easycheck.SubstitucionsActivity;
import net.marcarni.easycheck.eines.Missatges;
import net.marcarni.easycheck.eines.isConnect;

/**
 * Aquesta clase hereta d'AppCompatActivty a l'hora que implementa PopupMenu.OnItemClickListener.
 * Afegeix el menú principal amb Logout i Settings, i dos PopupMenu, un pels gestors de reserves
 * i l'altre per les consultes de serveis, i gestiona els corresponents Listeners.
 *
 * @author Antoni Torres Marí
 */
public class MenuAppCompatActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private String titol, missatge;
    private int icon;

    /**
     * Created by Antoni Torres Marí
     * <p>
     * Infla el menú principal main.xml
     *
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
     * <p>
     * Gestiona l'esdeveniment quan es selecciona un MenuItem del menú principal
     *
     * @param item MenuItem seleccionat
     * @return true si s'ha capturat un dels items del menú
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case R.id.action_logout:
                Intent logout = new Intent(this, LoginActivity.class);
                startActivity(logout);
                finish();
                return true;
            case R.id.menu_consultes:
                //En premer aquest botó es desplega un PopupMenu
                showConsultesSubmenuPopup((View) findViewById(R.id.menu_consultes));
                return true;
            case R.id.menu_gestor:
                //En premer aquest botó es desplega un PopupMenu
                showGestorSubmenuPopup((View) findViewById(R.id.menu_gestor));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Created by Antoni Torres Marí
     * <p>
     * De aquet View que pasem per paràmetre s'hi desplega un PopupMenú inflat amb el recurs
     * gestor_submenu.xml i s'hi afegeix el listener PopupMenu.OnMenuItemClickListener que implementa
     * aquesta clase
     *
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
     * <p>
     * De aquet View que pasem per paràmetre s'hi desplega un PopupMenú inflat amb el recurs
     * consultes_submenu.xml i s'hi afegeix el listener PopupMenu.OnMenuItemClickListener que implementa
     * aquesta clase
     *
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
     * <p>
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
                if (isConnect.isDisponible(this)) {
                    Intent startServicesOnline = new Intent(this, ConsultaServeisOnlineActivity.class);
                    finish();
                    startActivity(startServicesOnline);
                    return true;


                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("WIFI NO DISPONIBLE");
                    alertDialog.setMessage("Es mostraran les dades en mode OFFLINE");
                    alertDialog.setIcon(R.drawable.fail);
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent startServicesActivity = new Intent(MenuAppCompatActivity.this, ConsultaServeisActivity.class);
                                    finish();
                                    startActivity(startServicesActivity);

                                }
                            }
                    );

                    alertDialog.show();
                    return true;


                }
            case R.id.action_replacement:
                if (LoginActivity.IS_ADMIN.equals("1")) {
                    if (isConnect.isDisponible(this)) {
                        Intent subsActivity = new Intent(this, SubstitucionsActivity.class);
                        finish();
                        startActivity(subsActivity);
                    } else {
                        titol = "WIFI NO DISPONIBLE";
                        missatge = "Aquesta operació no es pot dur a terme en aquests moments";
                        icon = R.drawable.fail;
                        Missatges.AlertMissatge(titol, missatge, icon,this);

                    }


                } else {
                    titol = "ACCÉS DENEGAT";
                    missatge = "\n\t\t No té privilegis per fer substitucions";
                    icon = (R.drawable.ic_prohibit);
                   Missatges.AlertMissatge(titol, missatge, icon,this);

                }
                return true;
            default:

                return false;
        }
    }



    }

