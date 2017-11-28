package net.marcarni.easycheck.Utils;

import net.marcarni.easycheck.model.Reserva;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Carlos Alberto Castro Cañabate
 * Classe per introduir els objectes del server al model
 */
public class IntroduccioObjectes {
    public IntroduccioObjectes(){
        añadirObjetosAClases();
    }

    public void añadirObjetosAClases(){
        Treballador.getTreballadors().clear();
        Servei.getLlistaServeis().clear();
        Reserva.getReservas().clear();

        DescargaReserva totsReserves = new DescargaReserva();
        ArrayList<Reserva> reserves = (ArrayList<Reserva>) totsReserves.obtenirReservesDelServer();
        Iterator itR = reserves.iterator();
        while (itR.hasNext()){
            Reserva r = (Reserva) itR.next();
            Reserva.setReservas(r);
        }

        DescargaServei totsServeis= new DescargaServei();
        DescargaTreballador todo = new DescargaTreballador();
        ArrayList<Treballador> treballadors = (ArrayList<Treballador>) todo.obtenirTreballadorsDelServer();
        Iterator it = treballadors.iterator();
        while(it.hasNext()){
            Treballador t = (Treballador) it.next();
            Treballador.setTreballadors(t);

            ArrayList<Servei> serveis = (ArrayList<Servei>) totsServeis.obtenirServeisDelServer();
            Iterator itS = serveis.iterator();
            while (itS.hasNext()){
                Servei s = (Servei) itS.next();
                if (t.getId()==s.getId_treballador()){
                    Servei.setLlistaServeis(s);
                }
            }
        }
    }
}
