package net.marcarni.easycheck.eines;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Maria on 03/12/2017.
 */

public   class isConnect
{
    static ConnectivityManager connectivityManager;
    static NetworkInfo actNetInfo;
   static  WifiManager admin_wifi;



   public static  boolean isDisponible(Context context) {

       connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
       actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }


}



