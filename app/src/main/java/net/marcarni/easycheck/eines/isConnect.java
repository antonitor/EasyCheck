package net.marcarni.easycheck.eines;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.marcarni.easycheck.R;

/**
 * Created by Maria on 03/12/2017.
 */

public   class isConnect
{
    static ConnectivityManager connectivityManager;
    static NetworkInfo actNetInfo;



   public static  boolean isDisponible(Context context) {

       connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
       actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }



    public static void MissatgeAlert(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon( R.drawable.fail);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }


}

