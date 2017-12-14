package net.marcarni.easycheck.eines;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @auhtor by Maria on 12/12/2017.
 *
 * Aquesta classe s'ha creat per enviar un alertDialog
 */

public class Missatges {

    /**
     * @author Maria
     * @param titol String amb el titol del missatge
     * @param missatge String amb el cos del missatge
     * @param icon Icon que es mostrarà dependent del tipus de missatge
     * @param context contexte on es farà servir el missatge
     */
    public static void AlertMissatge(String titol, String missatge, int icon, Context context) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(titol);
        alertDialog.setMessage(missatge);
        alertDialog.setIcon(icon);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {




                    }
                }
        );

        alertDialog.show();

    }


}
