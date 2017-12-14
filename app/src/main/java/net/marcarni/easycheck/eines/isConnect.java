package net.marcarni.easycheck.eines;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author by Maria on 03/12/2017.
 */
/**
 * Classe per controlar la connectivitat i l'accés disponible pel server
 */
public   class isConnect {
    static ConnectivityManager connectivityManager;
    static NetworkInfo actNetInfo;


    /**
     * Mètode que controlarà la connexió a internet
     * @author Maria
     * @param context context o activitat on es controlarà la connectivitat
     * @return retornarà true o false segons la connectivitat
     */
    public static boolean isDisponible(Context context) {

        connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }



    public static boolean isPortOpen(final String ip, final int port, final int timeout) {

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;

        } catch (ConnectException ce) {
            ce.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }



}