package net.marcarni.easycheck.RecyclerView;

/**
 * Created by Maria on 07/10/2017.
 */

public class Header_Consulta {
    public String nomTreballador;
    public  String descripcioServei;

    public Header_Consulta(String nomTreballador, String descripcioServei) {
        this.nomTreballador = nomTreballador;
       this.descripcioServei = descripcioServei;
    }

    public void setNomTreballador(String nomTreballador) {
        this.nomTreballador = nomTreballador;
    }

    public String getDescripcioServei() {
        return descripcioServei;
    }

    public void setDescripcioServei(String descripcioServei) {
        this.descripcioServei = descripcioServei;
    }

    public String getNomTreballador() {
        return nomTreballador;
    }

    }