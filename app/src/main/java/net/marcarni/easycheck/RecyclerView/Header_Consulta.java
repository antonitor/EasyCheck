package net.marcarni.easycheck.RecyclerView;

public class Header_Consulta {
    public String nomTreballador;
    public  String descripcioServei;
    public String idServei;

    public Header_Consulta(String nomTreballador, String descripcioServei, String idServei) {
        this.nomTreballador = nomTreballador;
        this.descripcioServei = descripcioServei;
        this.idServei = idServei;
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

    public String getIdServei() {
        return idServei;
    }

    public void setIdServei(String idServei) {
        this.idServei = idServei;
    }
}

