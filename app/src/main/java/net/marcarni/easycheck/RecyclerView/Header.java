package net.marcarni.easycheck.RecyclerView;


public class Header{

    String nom,dni,data,qr,localitzacio,email,check,servei;
    int _id;

    /**
     * Constructor clase Header
     * @param nom del client que fa la reserva
     * @param dni del client que fa la reserva
     * @param data del servei
     * @param qr del servei
     * @param localitzacio del servei
     * @param email del client que fa la reserva
     * @param check de la reserva
     * @param servei de la reserva
     */
    public Header(int _id, String nom, String dni, String data, String qr, String localitzacio, String email, String check, String servei) {
        this._id = _id;
        this.nom = nom;
        this.dni = dni;
        this.data = data;
        this.qr = qr;
        this.localitzacio = localitzacio;
        this.email = email;
        this.check = check;
        this.servei = servei;
    }

    /**
     * Metode per obtenir el servei
     * @return reservei
     */
    public String getServei() {
        return servei;
    }

    /**
     * Metode per actualitzar el servei
     * @param servei servei a actualizar
     */
    public void setServei(String servei) {
        this.servei = servei;
    }

    /**
     * Metode per obtenir el nom del client que fa la reserva
     * @return nom del client que fa la reserva
     */
    public String getNom() {
        return nom;
    }

    /**
     * Metode per actualitzar el nom del client que fa la reserva
     * @param nom del client que fa la reserva
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Metode per obtenir el dni del client que fa la reserva
     * @return dni del client que fa la reserva
     */
    public String getDni() {
        return dni;
    }

    /**
     * Metode per actualitzar el dni del client que fa la reserva
     * @param dni del client que fa la reserva
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Metode per obtenir la data de el servei
     * @return data del servei
     */
    public String getData() {
        return data;
    }

    /**
     * Mètode per actualitzar la data del servei
     * @param data del servei
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Metode per obtenir el QR de la reserva
     * @return qr de la reserva
     */
    public String getQr() {
        return qr;
    }

    /**
     * Mètode per actualizar el qr de la reserva
     * @param qr a actualitzar
     */
    public void setQr(String qr) {
        this.qr = qr;
    }

    /**
     * Mètode per obtenir la localització de el servei
     * @return localització del servei
     */
    public String getLocalitzacio() {
        return localitzacio;
    }

    /**
     * Mètode per actualitzar la localització del servei
     * @param localitzacio a actualitzar
     */
    public void setLocalitzacio(String localitzacio) {
        this.localitzacio = localitzacio;
    }

    /**
     * Mètode per obtenir l'email del client que fa la reserva
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Mètode per actualitzar l'email del client que fa la reserva
     * @param email del client que fa la reserva
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Metode per obtenir si la reserva te el check-in fet o noo
     * @return check
     */
    public String getCheck() {
        return check;
    }

    /**
     * Metode per actualitzar l'estat del check-in
     * @param check a actualitzar
     */
    public void setCheck(String check) {
        this.check = check;
    }

    /**
     * Metode per obtenir el _id corresponent
     * @return _id
     */
    public int get_id() {return this._id; }
}
