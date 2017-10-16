package net.marcarni.easycheck.SQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import net.marcarni.easycheck.SQLite.ContracteBD.Reserves;
import net.marcarni.easycheck.SQLite.ContracteBD.Serveis;
import net.marcarni.easycheck.SQLite.ContracteBD.Treballador;

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
     * Metode per retonar el query
     * @return SQLiteQueryBuilder creat
     */
    public SQLiteQueryBuilder RetornaQuery(){
        SQLiteQueryBuilder QBuilder = new SQLiteQueryBuilder();
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


