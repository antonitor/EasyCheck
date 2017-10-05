package net.marcarni.easycheck.RecyclerView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import net.marcarni.easycheck.DetallActivity;
import net.marcarni.easycheck.settings.MenuAppCompatActivity;

public class Header extends MenuAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    String dni,data,check;


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
        Log.d("proba","DNI: " + this.getResponsableName().substring(5,14));
        Log.d("proba","Check: " + check);
        DetallActivity detall = new DetallActivity();
   //     detall.ferCheckIn(this.getResponsableName().substring(5,14));
        return check;
    }

    public void setCheck(String check) {
       // db = new DBInterface(this);
        this.check = check;
    }

    String responsableName;
}
