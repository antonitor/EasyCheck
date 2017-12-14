package net.marcarni.easycheck.eines;

import android.database.Cursor;
import android.database.MatrixCursor;

import net.marcarni.easycheck.RecyclerView.Header_Consulta;
import net.marcarni.easycheck.model.Servei;
import net.marcarni.easycheck.model.Treballador;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor by Maria on 11/12/2017.
 *
 *
 * Aquesta classe arreplega un seguit de mètodes que es faran servir en diverses Activitats.
 *
 */

public class Utilitats {

   public static  ArrayList myDataset;

    /**
     * @author Maria
     * @param x Integer que es comprovarà si es troba entre 0 i 9
     * @return retornarà false o true depenent de si es troba l'integer entre 0 i 9
     *
     */
   public static  boolean isOK(int x) {
        for (int i = 0; i < 10; i++) {
            if (x == i) {
                return true;
            }
        }
        return false;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////     Spinner            ///////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Mètode per omplir un cursor amb els item d'un arrayList
     *
     * @author Maria
     * @param treballadors ArrayList d'on es treuran el llistat de treballadors
     * @return retornarà cursor amb el nom complert del treballador i el seu id
     */
    public static  Cursor getCursorSpinner(ArrayList<Treballador> treballadors) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "nom"});
        cursor.addRow(new String[]{"0", "Tots"});
        for (Treballador treb : treballadors) {
            cursor.addRow(new String[]{String.valueOf(treb.getId()), treb.getNom() + " " + treb.getCognom1() + " " + treb.getCognom2()});
        }

        return cursor;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////     Consultes             ///////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Mètode per omplir un ArrayList. Es treuran dades de dos arrayList i es convinaran per omplir un tercer
     * arrayList
     *
     * @author Maria
     * @param id del servei
     * @param i integer amb la posicio dintre d'un arraylist, d'on  es treuen el serveis, per
     *          introduir a l'arrayList resultant
     * @param llistaServeis arrayList d'on es treuran els serveis, per omplir l'arrayList resultant
     * @param treballadors arraylist d'on es treuran els treballadors que ompliran l'arrayList resultant
     * @return retornarà true o false si s'ha omplert amb éxit l'arraylist resultat de les conbinacions dels dos arrays
     *
     */
    public static boolean ompleArrayList(int id, int i,ArrayList<Servei>llistaServeis,ArrayList<Treballador> treballadors) {
        return myDataset.add(new Header_Consulta(RetornaNomTreballador(id,treballadors)
                , llistaServeis.get(i).getDescripcio()
                , String.valueOf(llistaServeis.get(i).getId()), llistaServeis.get(i).getData_servei(),
                llistaServeis.get(i).getHora_inici(), llistaServeis.get(i).getHora_final()));
    }


    /**
     * Mètode per retornar tots els serveis de l'arrayList entrat per paràmetres
     *
     * @author Maria
     * @param llistaServeis d'on es treuran tots els serveis i s'omplira un arrayList
     * @return ArrayList amb els serveis i id del treballador assignat al servei.
     *
     */
    public static ArrayList retornaTotsElsServeis(ArrayList<Servei>llistaServeis) {
        myDataset = new ArrayList<>();
        for (int i = 0; i < llistaServeis.size(); i++) {
            int idTreb = (llistaServeis.get(i).getId_treballador());
            ompleArrayList(idTreb, i,llistaServeis,null);
        }

        return myDataset;
    }


    /**
     * Mètode que retorna nom complert d'un treballador de un arrayList amb el seu id
     *
     * @author Maria
     * @param id integer amb l'id del treballador a cercar
     * @param treballadors ArrayList amb tots els treballadors descarregats del server
     * @return retornarà el nom complert del treballador que  coincideix amb el parametre id
     */
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


    /**
     * Mètode que retornarà un llistat de Serveis per Id
     *
     * @author Maria
     * @param id del servei
     * @param llistaServeis arrayList de serveis
     * @return retornarà List amb els serveis que coincideixen amb l'id
     */
    public static List RetornaServeisPerId(int id,ArrayList<Servei>llistaServeis) {
        myDataset = new ArrayList<>();

        for (int i = 0; i < llistaServeis.size(); i++) {
            if (llistaServeis.get(i).getId_treballador() == id) {
                ompleArrayList(id, i,llistaServeis,null);
            }
        }
        return myDataset;
    }

    /**
     * Mètode que retornarà List amb els serveis que coincideixen amb un String amb una data determinada.
     *
     * @author Maria
     * @param data String amb la data a cercar
     * @param llistaServeis ArrayList amb els serveis
     * @return List amb els serveis que coincideixin amb la data introduida per paràmetre.
     */
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


    /**
     * Mètode que retornara un List amb els serveis assignats a un treballador per data
     *
     * @author Maria
     * @param data String amb la data a cercar
     * @param id Integer amb l'id del treballador a cercar
     * @param llistaServeis ArrayList de serveis per cercar per id i data
     * @return List amb els serveis assignats a un treballador per la seva id i la data.
     */
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

    /**
     * Mètode que retornarà List amb els serveis filtrats per data i hora
     *
     * @author Maria
     * @param data String amb la data com a filtrat de cerca
     * @param hora String amb l'hora  com a filtrat de cerca
     * @param llistaServeis ArrayList on es cercarà els serveis filtrats per data i hora
     * @return List amb el resultat de les cerques amb filtratge de data i hora
     */
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

    /**
     * Mètode que retornarà List amb filtratge de Data, hora i treballador
     *
     * @author Maria
     * @param treballador Integer amb l'id del treballador a cercar
     * @param fecha String amb la data a cercar
     * @param time String amb l'hora a cercar
     * @param llistaServeis ArrayList d'on es cercarà els serveis filtrats per id, data i hora
     * @return List amb el resultat dels serveis que coincideixen amb l'id del treballador, data i hora.
     */
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

    /**
     * Mètode que retornarà un servei per id
     *
     * @author Maria.
     * @param id String amb l'id del servei
     * @return servei que té l'id introduit per paràmetre
     */
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
