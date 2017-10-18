package net.marcarni.easycheck.SQLite;

import android.provider.BaseColumns;

/**
 * Created by Antoni Torres Marí
 *
 * Una classe Contract és un contenidor per constants que defineixen noms d'URI (identificadors
 * uniformes de recursos), taules i columnes. La classe Contract et permet utilitzar les mateixes
 * constants en totes les altres classes del mateix paquet. Això et permet canviar el nom d'una
 * columna en un lloc i que aquest canvi es propagui a tot el codi.
 */
public class ContracteBD {

    /**
     * Created by Antoni Torres Marí
     *
     * Per prevenir que algú accidentalment instancii aquest contracte,
     * el constructor s'ha fet private
     */
    private ContracteBD(){}

    //Inner class que defineix els continguts de la taula Reserva
    public static final class Reserves implements BaseColumns {
        public static final String NOM_TAULA = "Reserva";
        public static final String LOCALITZADOR = "localitzador";
        public static final String DATA_RESERVA = "dataReserva";
        public static final String ID_SERVEI = "idServei";
        public static final String NOM_TITULAR = "nomTitular";
        public static final String COGNOM1_TITULAR = "cognom1Titular";
        public static final String COGNOM2_TITULAR = "cognom2Titular";
        public static final String TELEFON_TITULAR = "telefonTitular";
        public static final String EMAIL_TITULAR = "emailTitular";
        public static final String QR_CODE = "qrCode";
        public static final String CHECK_IN = "checkIn";
        public static final String DNI_TITULAR = "dniTitular";
    }

    //Inner class que defineix els continguts de la taula Treballador
    public static final class Treballador implements BaseColumns {
        public static final String NOM_TAULA = "Treballador";
        public static final String NOM = "nom";
        public static final String COGNOM1 = "cognom1";
        public static final String COGNOM2 = "cognom2";
        public static final String ADMIN = "esAdmin";
        public static final String LOGIN = "login";
    }

    //Inner class que defineix els continguts de la taula Serveis
    public static final class Serveis implements BaseColumns {
        public static final String NOM_TAULA = "Serveis";
        public static final String DESCRIPCIO = "descripcio";
        public static final String ID_TREBALLADOR = "idTreballador";
        public static final String DATA_SERVEI = "dataServei";
        public static final String HORA_INICI = "horaInici";
        public static final String HORA_FI = "horaFi";
    }



}
