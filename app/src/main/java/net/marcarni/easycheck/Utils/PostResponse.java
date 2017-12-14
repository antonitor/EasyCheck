package net.marcarni.easycheck.Utils;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author Antoni Torres Marí
 */
public class PostResponse implements Serializable{
    private int requestCode; //0 -> Error, 1 -> Correcto
    private String message; 
    
    public PostResponse(){}
    
    public PostResponse(int code, String message) {
        this.requestCode = code;
        this.message = message;
    }

    /**
     * @return the requestCode
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     * @param requestCode the requestCode to set
     */
    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
