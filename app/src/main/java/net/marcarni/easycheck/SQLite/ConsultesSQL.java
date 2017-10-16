package net.marcarni.easycheck.SQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import net.marcarni.easycheck.R;
import net.marcarni.easycheck.SQLite.ContracteBD.Reserves;
import net.marcarni.easycheck.SQLite.ContracteBD.Serveis;
import net.marcarni.easycheck.SQLite.ContracteBD.Treballador;

import java.util.HashMap;

public class ConsultesSQL {
    String RetornaTotsElsTreballadors ="Select t."+ ContracteBD.Treballador._ID+", t."+ ContracteBD.Treballador.NOM+
            " FROM "+ ContracteBD.Treballador.NOM_TAULA+" t";

    String RetornaTotsElsServeis = "Select distinct t." + ContracteBD.Treballador.NOM + ", t." + ContracteBD.Treballador.COGNOM1 + ", t." + Treballador.COGNOM2 +
            ", s." + ContracteBD.Serveis.DESCRIPCIO + ", r." + ContracteBD.Reserves.ID_SERVEI + ", s." + ContracteBD.Serveis.DATA_SERVEI+", s." + Serveis.HORA_INICI+", s." + Serveis.HORA_FI+
            " FROM " + ContracteBD.Serveis.NOM_TAULA + " s " +
            " LEFT JOIN  " + ContracteBD.Treballador.NOM_TAULA + " t ON t." + ContracteBD.Treballador._ID + " = s." + ContracteBD.Serveis.ID_TREBALLADOR +
            " LEFT join " + ContracteBD.Reserves.NOM_TAULA + " r  ON s." + ContracteBD.Serveis._ID + " = r." + ContracteBD.Reserves.ID_SERVEI +"";

    String RetornaServeiTreballadorPerID= "Select distinct t." + Treballador.NOM + ", t." + Treballador.COGNOM1 + ", t." + Treballador.COGNOM2 +
            ", s." + Serveis.DESCRIPCIO + ", r." + Reserves.ID_SERVEI + ", s." + Serveis.DATA_SERVEI+", s." + Serveis.HORA_INICI+", s." + Serveis.HORA_FI+
            " FROM " + Serveis.NOM_TAULA + " s " +
            " JOIN " + Treballador.NOM_TAULA + " t ON t." + Treballador._ID + " = s." + Serveis.ID_TREBALLADOR +
            " and t."+ Treballador._ID+"= ?" +
            " LEFT join " +  Reserves.NOM_TAULA  +" r  on s."+ Serveis._ID+" = r."+ Reserves.ID_SERVEI +"";
    String  RetornaServeiPerID_DATA = "Select distinct t." + Treballador.NOM + ", t." + Treballador.COGNOM1 + ", t." + Treballador.COGNOM2 +
            ", s." + Serveis.DESCRIPCIO + ", r." + Reserves.ID_SERVEI + ", s." + Serveis.DATA_SERVEI + ", s." + Serveis.HORA_INICI + ", s." + Serveis.HORA_FI +
            " FROM " + Serveis.NOM_TAULA + " s " +
            " JOIN " + Treballador.NOM_TAULA + " t ON t." + Treballador._ID + " = s." + Serveis.ID_TREBALLADOR +
            " and t." + Treballador._ID + "= ? and s." + Serveis.DATA_SERVEI + " = ? " +
            " LEFT join " + Reserves.NOM_TAULA + " r  on s." + Serveis._ID + " = r." + Reserves.ID_SERVEI + "";

    String RetornaServei_Treballador_data_hora = "Select distinct t." + Treballador.NOM + ", t." + Treballador.COGNOM1 + ", t." + Treballador.COGNOM2 +
            ", s." + Serveis.DESCRIPCIO + ", r." + Reserves.ID_SERVEI + ", s." + Serveis.DATA_SERVEI + ", s." + Serveis.HORA_INICI + ", s." + Serveis.HORA_FI +
            " FROM " + Serveis.NOM_TAULA + " s " +
            " JOIN " + Treballador.NOM_TAULA + " t ON t." + Treballador._ID + " = s." + Serveis.ID_TREBALLADOR +
            " and t." + Treballador._ID + "= ? and s." + Serveis.DATA_SERVEI + " = ? and s."+ Serveis.HORA_INICI+" = ? " +
            " LEFT join " + Reserves.NOM_TAULA + " r  on s." + Serveis._ID + " = r." + Reserves.ID_SERVEI + "";

    String RetornaServei_data_hora = "Select distinct t." + Treballador.NOM + ", t." + Treballador.COGNOM1 + ", t." + Treballador.COGNOM2 +
            ", s." + Serveis.DESCRIPCIO + ", r." + Reserves.ID_SERVEI + ", s." + Serveis.DATA_SERVEI + ", s." + Serveis.HORA_INICI + ", s." + Serveis.HORA_FI +
            " FROM " + Serveis.NOM_TAULA + " s " +
            " JOIN " + Treballador.NOM_TAULA + " t ON t." + Treballador._ID + " = s." + Serveis.ID_TREBALLADOR +
            " and s." + Serveis.DATA_SERVEI + " = ? and s."+ Serveis.HORA_INICI+" = ? " +
            " LEFT join " + Reserves.NOM_TAULA + " r  on s." + Serveis._ID + " = r." + Reserves.ID_SERVEI + "";
    String RetornaServeiData= "Select distinct t." + Treballador.NOM + ", t." + Treballador.COGNOM1 + ", t." + Treballador.COGNOM2 +
            ", s." + Serveis.DESCRIPCIO + ", r." + Reserves.ID_SERVEI + ", s." + Serveis.DATA_SERVEI + ", s." + Serveis.HORA_INICI + ", s." + Serveis.HORA_FI +
            " FROM " + Serveis.NOM_TAULA + " s " +
            " JOIN " + Treballador.NOM_TAULA + " t ON t." + Treballador._ID + " = s." + Serveis.ID_TREBALLADOR +
            " and s." + Serveis.DATA_SERVEI + " = ? " +
            " LEFT join " + Reserves.NOM_TAULA + " r  on s." + Serveis._ID + " = r." + Reserves.ID_SERVEI + "";

    /**
     * Metode per retonar el query.
     * Mitjançant un HashMap definim quines columnes es vol agafar entre les dues taules, i amb
     * el mètode setTables, amb quines taules volem fer un Join
     * @return SQLiteQueryBuilder creat
     */
    public SQLiteQueryBuilder RetornaQuery(){

        HashMap<String, String> gProjectionMap= new HashMap<>();
        gProjectionMap.put(Reserves._ID,Reserves.NOM_TAULA+"."+Reserves._ID);
        gProjectionMap.put(Reserves.LOCALITZADOR,Reserves.LOCALITZADOR);
        gProjectionMap.put(Reserves.NOM_TITULAR,Reserves.NOM_TITULAR);
        gProjectionMap.put(Reserves.COGNOM2_TITULAR,Reserves.COGNOM2_TITULAR);
        gProjectionMap.put(Reserves.COGNOM1_TITULAR, Reserves.COGNOM1_TITULAR);
        gProjectionMap.put(Reserves.EMAIL_TITULAR, Reserves.EMAIL_TITULAR);
        gProjectionMap.put(Reserves.CHECK_IN, Reserves.CHECK_IN);
        gProjectionMap.put(Reserves.QR_CODE,Reserves.QR_CODE);
        gProjectionMap.put(Reserves.DNI_TITULAR, Reserves.DNI_TITULAR);
        gProjectionMap.put(Serveis.DATA_SERVEI,Serveis.DATA_SERVEI);
        gProjectionMap.put(Serveis.DESCRIPCIO,Serveis.DESCRIPCIO);
        gProjectionMap.put(Serveis.HORA_INICI,Serveis.HORA_INICI);
        gProjectionMap.put(Serveis.HORA_FI,Serveis.HORA_FI);

        SQLiteQueryBuilder QBuilder = new SQLiteQueryBuilder();
        QBuilder.setProjectionMap(gProjectionMap);
        QBuilder.setTables(Reserves.NOM_TAULA + " LEFT JOIN " + Serveis.NOM_TAULA + " ON " + Reserves.ID_SERVEI + " = " + Serveis.NOM_TAULA + "." + Serveis._ID);

        return QBuilder;
    }

    /**
     * Metode per moure el cursor a la primera posició
     * @param cursor a moure
     * @return cursor a retornar
     */
    public Cursor mouCursor(Cursor cursor){
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }
}


