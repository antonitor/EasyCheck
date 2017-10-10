package net.marcarni.easycheck.RecyclerView;

public class Header_Consulta {
    public String nomTreballador;
    public  String descripcioServei;
    public String idServei;
    public String dataServei;
    public String horaInici;
    public String horaFi;

    public Header_Consulta(String nomTreballador, String descripcioServei, String idServei, String dataServei, String horaInici, String horaFi) {
        this.nomTreballador = nomTreballador;
        this.descripcioServei = descripcioServei;
        this.idServei = idServei;
        this.dataServei = dataServei;
        this.horaInici = horaInici;
        this.horaFi = horaFi;
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

    public String getDataServei() {
        return dataServei;
    }

    public void setDataServei(String dataServei) {
        this.dataServei = dataServei;
    }

    public String getHoraInici() {
        return horaInici;
    }

    public void setHoraInici(String horaInici) {
        this.horaInici = horaInici;
    }

    public String getHoraFi() {
        return horaFi;
    }

    public void setHoraFi(String horaFi) {
        this.horaFi = horaFi;
    }
}

