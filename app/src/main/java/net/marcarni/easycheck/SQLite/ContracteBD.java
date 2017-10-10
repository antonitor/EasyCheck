package net.marcarni.easycheck.SQLite;

import android.provider.BaseColumns;

/**
 * Created by Toni on 07/10/2017.
 */

public class ContracteBD {

    //BaseColumns és una interficie que afegeix la String _id i _count
    public static final class Reserves implements BaseColumns {
        public static final String NOM_TAULA = "Reserva";
        public static final String LOCALITZADOR = "localiTzador";
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

    public static final class Treballador implements BaseColumns {
        public static final String NOM_TAULA = "Treballador";
        public static final String NOM = "nom";
        public static final String COGNOM1 = "cognom1";
        public static final String COGNOM2 = "cognom2";
        public static final String ADMIN = "esAdmin";
        public static final String LOGIN = "login";
    }

    public static final class Serveis implements BaseColumns {
        public static final String NOM_TAULA = "Serveis";
        public static final String DESCRIPCIO = "descripcio";
        public static final String ID_TREBALLADOR = "idTreballador";
        public static final String DATA_SERVEI = "dataServei";
        public static final String HORA_INICI = "horaInici";
        public static final String HORA_FI = "horaFi";
    }



}
