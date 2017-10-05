package net.marcarni.easycheck.RecyclerView;


import net.marcarni.easycheck.SQLite.DBInterface;

public class Header {
    String dni,data,check;
    DBInterface db;

    public String getData() {
        return data;
    }

    public Header(String headerCode, String responsableName, String dni, String data, String check) {
        this.headerCode = headerCode;
        this.responsableName = responsableName;
        this.dni=dni;
        this.data=data;
        this.check=check;
    }

    String headerCode;

    public void setData(String data) {
        this.data = data;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getHeaderCode() {
        return headerCode;
    }

    public void setHeaderCode(String headerCode) {
        this.headerCode = headerCode;
    }

    public void setResponsableName(String responsableName) {
        this.responsableName = responsableName;
    }

    public String getResponsableName() {
        return responsableName;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {

        this.check = check;
    }

    String responsableName;
}
