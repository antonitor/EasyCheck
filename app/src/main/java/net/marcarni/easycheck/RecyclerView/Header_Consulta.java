package net.marcarni.easycheck.RecyclerView;

/**
 * Created by Maria on 07/10/2017.
 */

public class Header_Consulta {
    public String nomTreballador;
   // public  String cognom1Treballador;
   // public  String cognom2Treballador;
    public  String descripcioServei;

    public Header_Consulta(String nomTreballador, String descripcioServei) {
        this.nomTreballador = nomTreballador;
       // String cognom1Treballador, String cognom2Treballador,
       // this.cognom1Treballador = cognom1Treballador;
       // this.cognom2Treballador = cognom2Treballador;
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
/*
    public String getCognom1Treballador() {
        return cognom1Treballador;
    }

    public String getCognom2Treballador() {
        return cognom2Treballador;
    }


}
    public void setCognom1Treballador(String cognom1Treballador) {
        this.cognom1Treballador = cognom1Treballador;
    }

    public void setCognom2Treballador(String cognom2Treballador) {
        this.cognom2Treballador = cognom2Treballador;*/
    }