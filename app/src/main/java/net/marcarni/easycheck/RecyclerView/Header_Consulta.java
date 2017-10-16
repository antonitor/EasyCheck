package net.marcarni.easycheck.RecyclerView;

public class Header_Consulta {
    public String nomTreballador;
    public  String descripcioServei;
    public String idServei;
    public String dataServei;
    public String horaInici;
    public String horaFi;

    /**
     * Mètode constructor per a la classe Header_Consulta
     * @param nomTreballador nom del treballador que realitzarà el servei
     * @param descripcioServei servei a realitzar
     * @param idServei identificador del servei a realitzar
     * @param dataServei data del servei a realitzar
     * @param horaInici del servei a realitzar
     * @param horaFi del servei a realitzar
     */
    public Header_Consulta(String nomTreballador, String descripcioServei, String idServei, String dataServei, String horaInici, String horaFi) {
        this.nomTreballador = nomTreballador;
        this.descripcioServei = descripcioServei;
        this.idServei = idServei;
        this.dataServei = dataServei;
        this.horaInici = horaInici;
        this.horaFi = horaFi;
    }

    /**
     * Mètode per obtenir el nom del treballador
     * @return nom del treballador
     */
    public String getNomTreballador() {
        return nomTreballador;
    }
    /**
     * Mètode per actualitzar el nom del treballador
     * @param nomTreballador a actualitzar
     */
    public void setNomTreballador(String nomTreballador) {
        this.nomTreballador = nomTreballador;
    }

    /**
     * Mètode per obtenir la descripció del servei
     * @return descripció del servei
     */
    public String getDescripcioServei() {
        return descripcioServei;
    }

    /**
     * Mètode per actualitzar la descripció del servei
     * @param descripcioServei a actualitzar
     */
    public void setDescripcioServei(String descripcioServei) {
        this.descripcioServei = descripcioServei;
    }

    /**
     * Mètode per obtenir l'Id del servei
     * @return id del servei
     */
    public String getIdServei() {
        return idServei;
    }

    /**
     * Mètode per actualitzar l'id del servei
     * @param idServei a actualitzar
     */
    public void setIdServei(String idServei) {
        this.idServei = idServei;
    }

    /**
     * Mètode per obtenir la data del servei
     * @return data del servei
     */
    public String getDataServei() {
        return dataServei;
    }

    /**
     * Mètode per actualitzar la data del servei
     * @param dataServei a actualitzar
     */
    public void setDataServei(String dataServei) {
        this.dataServei = dataServei;
    }

    /**
     * Mètode per obtenir l'hora d'inici del servei
     * @return hora d'inici
     */
    public String getHoraInici() {
        return horaInici;
    }

    /**
     * Mètode per actualitzar l'hora d'inici del servei
     * @param horaInici del servei
     */
    public void setHoraInici(String horaInici) {
        this.horaInici = horaInici;
    }

    /**
     * Mètode per obtenir l'hora final del servei
     * @return hora final
     */
    public String getHoraFi() {
        return horaFi;
    }

    /**
     * Metode per actualitzar l'hora final del servei
     * @param horaFi del servei.
     */
    public void setHoraFi(String horaFi) {
        this.horaFi = horaFi;
    }
}

