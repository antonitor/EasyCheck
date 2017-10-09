package net.marcarni.easycheck.SQLite;

import android.provider.BaseColumns;

/**
 * Created by Toni on 07/10/2017.
 */

public class ContracteBD {

    //BaseColumns és una interficie que afegeix la String _id i _count
    public static final class Reserves implements BaseColumns {
        public static final String NOM_TAULA = "Reserva";
        public static final String LOCALIZADOR = "localizador";
        public static final String FECHA_RESERVA = "fechaReserva";
        public static final String FECHA_SERVICIO = "fechaServicio";
        public static final String ID_SERVICIO = "idServicio";
        public static final String NOMBRE_TITULAR = "nombreTitular";
        public static final String APELLIDO1_TITULAR = "apellido1Titular";
        public static final String APELLIDO2_TITULAR = "apellido2Titular";
        public static final String TELEFONO_TITULAR = "telefonoTitular";
        public static final String EMAIL_TITULAR = "emailTitular";
        public static final String QR_CODE = "qrCode";
        public static final String CHECK_IN = "checkIn";
        public static final String DNI_TITULAR = "dniTitular";
    }

    public static final class Treballador implements BaseColumns {
        public static final String NOM_TAULA = "Treballador";
        public static final String NOM = "nom";
        public static final String APELLIDO1 = "apellido1";
        public static final String APELLIDO2 = "apellido2";
        public static final String ADMIN = "esAdmin";
        public static final String LOGIN = "login";
        public static final String PASSWORD = "password";
    }

    public static final class Serveis implements BaseColumns {
        public static final String NOM_TAULA = "Serveis";
        public static final String DESCRIPCIO = "descripcio";
        public static final String ID_TREBALLADOR = "idTreballador";
    }



}
