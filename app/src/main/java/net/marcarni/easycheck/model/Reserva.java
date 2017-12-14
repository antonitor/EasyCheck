/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.marcarni.easycheck.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Carlos Alberto Castro Cañabate
 */
public class Reserva implements Serializable {

    private int _id;
    private int id_servei;
    private Client client;
    private String localitzador;
    private String data_reserva;
    private String qr_code;
    private int checkin;
    static ArrayList<Reserva> reservas  = new ArrayList();

    private Reserva() {

    }
    /**
     * Constructor de Reserva
     * @param id
     * @param idSer
     * @param client
     * @param loc
     * @param data
     * @param qrcode
     * @param checkin
     */
    public Reserva(int id, int idSer, Client client, String loc, String data, String qrcode, int checkin) {
        this._id = id;
        this.id_servei = idSer;
        this.client = client;
        this.localitzador = loc;
        this.data_reserva = data;
        this.qr_code = qrcode;
        this.checkin = checkin;
    }

    /**
     * @return the _id
     */
    public int getId() {
        return _id;
    }

    /**
     * @param _id the _id to set
     */
    public void setId(int _id) {
        this._id = _id;
    }

    /**
     * @return the id_servei
     */
    public int getId_servei() {
        return id_servei;
    }

    /**
     * @param id_servei the id_servei to set
     */
    public void setId_servei(int id_servei) {
        this.id_servei = id_servei;
    }

    /**
     * @return the localitzador
     */
    public String getLocalitzador() {
        return localitzador;
    }

    /**
     * @param localitzador the localitzador to set
     */
    public void setLocalitzador(String localitzador) {
        this.localitzador = localitzador;
    }

    /**
     * @return the data_reserva
     */
    public String getData_reserva() {
        return data_reserva;
    }

    /**
     * @param data_reserva the data_reserva to set
     */
    public void setData_reserva(String data_reserva) {
        this.data_reserva = data_reserva;
    }

    /**
     * @return the qr_code
     */
    public String getQr_code() {
        return qr_code;
    }

    /**
     * @param qr_code the qr_code to set
     */
    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    /**
     * @return the checkin
     */
    public int getCheckin() {
        return checkin;
    }

    /**
     * @param checkin the checkin to set
     */
    public void setCheckin(int checkin) {
        this.checkin = checkin;
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
    /**
     *
     * @return ArrayList<Reserva>
     */
    public static ArrayList<Reserva> getReservas() {
        return reservas;
    }
    /**
     * Afegir una reserva al ArrayList
     * @param reserva a afegir
     */
    public static void setReservas(Reserva reserva) {
        Reserva.reservas.add(reserva);
    }



}
