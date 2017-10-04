package net.marcarni.easycheck.RecyclerView;


public class Header {
    String dni,data;

    public String getData() {
        return data;
    }

    public Header(String headerCode, String responsableName, String dni, String data) {
        this.headerCode = headerCode;
        this.responsableName = responsableName;
        this.dni=dni;
        this.data=data;


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

    String responsableName;
}
