package net.marcarni.easycheck.eines;

import android.database.Cursor;
import android.database.MatrixCursor;

import net.marcarni.easycheck.RecyclerView.Header_Consulta;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maria on 11/12/2017.
 */

public class Utilitats {

   public static  ArrayList myDataset;


   public static  boolean isOK(int x) {
        for (int i = 0; i < 10; i++) {
            if (x == i) {
                return true;
            }
        }
        return false;
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////     Consultes             ///////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    public static  Cursor getCursorSpinner(ArrayList<Treballador> treballadors) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "nom"});
        cursor.addRow(new String[]{"0", "Tots"});
        for (Treballador treb : treballadors) {
            cursor.addRow(new String[]{String.valueOf(treb.getId()), treb.getNom() + " " + treb.getCognom1() + " " + treb.getCognom2()});
        }

        return cursor;
    }
    public static boolean ompleArrayList(int id, int i,ArrayList<Servei>llistaServeis,ArrayList<Treballador> treballadors) {
        return myDataset.add(new Header_Consulta(RetornaNomTreballador(id,treballadors)
                , llistaServeis.get(i).getDescripcio()
                , String.valueOf(llistaServeis.get(i).getId()), llistaServeis.get(i).getData_servei(),
                llistaServeis.get(i).getHora_inici(), llistaServeis.get(i).getHora_final()));
    }

    public static ArrayList retornaTotsElsServeis(ArrayList<Servei>llistaServeis) {
        myDataset = new ArrayList<>();
        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            ompleArrayList(idTreb, i,llistaServeis,null);
        }

        return myDataset;
    }

    public static String RetornaNomTreballador(int id,ArrayList<Treballador>treballadors) {
        String nom = null;
     treballadors= (ArrayList<Treballador>) Treballador.getTreballadors();
        for (int i = 0; i < treballadors.size(); i++) {
            if (treballadors.get(i).getId() == id) {
                nom = treballadors.get(i).getNom() + " " + treballadors.get(i).getCognom1() + " " +
                        treballadors.get(i).getCognom2();
            }
        }
        return nom;
    }


    public static List RetornaServeisPerId(int id,ArrayList<Servei>llistaServeis) {
        myDataset = new ArrayList<>();

        for (int i = 0; i < llistaServeis.size(); i++) {
            if (llistaServeis.get(i).getId_treballador() == id) {
                ompleArrayList(id, i,llistaServeis,null);
            }
        }
        return myDataset;
    }

    public static ArrayList RetornaTotElsServeisPerData(String data,ArrayList<Servei>llistaServeis) {
        myDataset = new ArrayList<>();
        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            if (llistaServeis.get(i).getData_servei().equals(data)) {
                ompleArrayList(idTreb, i,llistaServeis,null);
            }
        }
        return myDataset;
    }



    public static ArrayList RetornaTotElsServeisPerData_Treballador(String data, int id,ArrayList<Servei>llistaServeis) {
        myDataset = new ArrayList<>();

        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            if (llistaServeis.get(i).getData_servei().equals(data) &&
                    llistaServeis.get(i).getId_treballador() == id) {
                ompleArrayList(idTreb, i,llistaServeis,null);

            }

        }
        return myDataset;
    }
    public static  ArrayList RetornaTotElsServeisPerDataiHora(String data, String hora, ArrayList<Servei>llistaServeis) {
        myDataset = new ArrayList<>();
        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            if (llistaServeis.get(i).getData_servei().equals(data) && llistaServeis.get(i).getHora_inici().equals(hora)) {
                ompleArrayList(idTreb, i,llistaServeis,null);
            }
        }
        return myDataset;
    }
    public static ArrayList<Header_Consulta> RetornaTotsElsServeisPerTreballadorDataIHora(int treballador, String fecha, String time, ArrayList<Servei>llistaServeis) {
        myDataset = new ArrayList<>();

        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            if (llistaServeis.get(i).getData_servei().equals(fecha) &&
                    llistaServeis.get(i).getId_treballador() == treballador
                    && llistaServeis.get(i).getHora_inici().equals(time)) {
                ompleArrayList(idTreb, i,llistaServeis,null);

            }

        }
        return myDataset;
    }
    public static Servei cercaServei(String id) {
        Servei s = null;
        for (int i = 0; i < Servei.getLlistaServeis().size(); i++) {
            if (Servei.getLlistaServeis().get(i).getId() == Integer.parseInt(id)) {
                s = Servei.getLlistaServeis().get(i);
            }


        }
        return s;
    }


}
