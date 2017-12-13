package net.marcarni.easycheck.eines;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Maria on 12/12/2017.
 */

public class Missatges {


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
