package net.marcarni.easycheck.Utils;

import android.content.Context;

import net.marcarni.easycheck.SQLite.DBInterface;
import net.marcarni.easycheck.model.Client;
import net.marcarni.easycheck.model.Reserva;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Carlos on 28/11/2017.
 */

public class añadirObjetosASQLite {
    DBInterface db;
    Context context;
    public añadirObjetosASQLite(Context context) {
        añadirObjetos();
        context = context;
    }


    public void añadirObjetos(){



        db = new DBInterface(context);
        db.obre();
        db.Esborra();
        DescargaTreballador todo = new DescargaTreballador();
        ArrayList<Treballador> treballadors = (ArrayList<Treballador>) todo.obtenirTreballadorsDelServer();
        Iterator it = treballadors.iterator();
        while(it.hasNext()) {
            Treballador t = (Treballador) it.next();
            db.InserirTreballador(t.getDni(),t.getNom(),t.getCognom1(),t.getCognom2(),t.getLogin(),Integer.toString(t.getEsAdmin()),t.getPassword());
        }

        DescargaServei totsServeis= new DescargaServei();
        ArrayList<Servei> serveis = (ArrayList<Servei>) totsServeis.obtenirServeisDelServer();
        Iterator itS = serveis.iterator();
        while (itS.hasNext()){
            Servei s = (Servei) itS.next();
            db.InserirServei(s.getDescripcio(),Integer.toString(s.getId_treballador()),s.getData_servei(),s.getHora_inici(),s.getHora_final());

        }

        DescargaReserva totsReserves = new DescargaReserva();
        ArrayList<Reserva> reserves = (ArrayList<Reserva>) totsReserves.obtenirReservesDelServer();
        Iterator itR = reserves.iterator();
        while (itR.hasNext()){
            Reserva r = (Reserva) itR.next();
            db.InserirReserva(r.getLocalitzador(),r.getData_reserva(),r.getId_servei(),r.getId(),r.getQr_code(),Integer.toString(r.getCheckin()));
            Client c = r.getClient();
            db.InserirClient(c.getNom_titular(),c.getCognom1_titular(),c.getCognom2_titular(),c.getTelefon_titular(),c.getEmail_titular(),c.getDni_titular());
        }

        db.tanca();
    }
}
