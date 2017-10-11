package net.marcarni.easycheck.RecyclerView;


public class Header{

    String nom,dni,data,qr,localitzacio,email,check;


    public Header(String nom, String dni, String data, String qr, String localitzacio, String email, String check) {
        this.nom = nom;
        this.dni = dni;
        this.data = data;
        this.qr = qr;
        this.localitzacio = localitzacio;
        this.email = email;
        this.check = check;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getLocalitzacio() {
        return localitzacio;
    }

    public void setLocalitzacio(String localitzacio) {
        this.localitzacio = localitzacio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
