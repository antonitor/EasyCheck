/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.marcarni.easycheck.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Toni
 */

public class Servei implements Serializable {
    
    private int _id;
    private String descripcio;    
    private int id_treballador;
    private String data_servei;
    private String hora_inici;
    private String hora_final;
    private List<Reserva> llistaReserves = new ArrayList();
    
    public Servei(){
        
    }
   
    public Servei (int id, String descripcio, int idTreb, String data_servei, String hora_inici, String hora_final, List<Reserva> llista){
        this._id = id;
        this.descripcio = descripcio;
        this.id_treballador = idTreb;
        this.data_servei = data_servei;
        this.hora_inici = hora_inici;
        this.hora_final = hora_final;
        this.llistaReserves = llista;
    }

    /**
     * @return the id
     */
    public int getId() {
        return _id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this._id = id;
    }

    /**
     * @return the descripcio
     */
    public String getDescripcio() {
        return descripcio;
    }

    /**
     * @param descripcio the descripcio to set
     */
    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }



    /**
     * @return the data_servei
     */
    public String getData_servei() {
        return data_servei;
    }

    /**
     * @param data_servei the data_servei to set
     */
    public void setData_servei(String data_servei) {
        this.data_servei = data_servei;
    }

    /**
     * @return the hora_inici
     */
    public String getHora_inici() {
        return hora_inici;
    }

    /**
     * @param hora_inici the hora_inici to set
     */
    public void setHora_inici(String hora_inici) {
        this.hora_inici = hora_inici;
    }

    /**
     * @return the hora_final
     */
    public String getHora_final() {
        return hora_final;
    }

    /**
     * @param hora_final the hora_final to set
     */
    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    /**
     * @return the llistaReserves
     */
    public List<Reserva> getLlistaReserves() {
        return llistaReserves;
    }

    /**
     * @param llistaReserves the llistaReserves to set
     */
    public void setLlistaReserves(List<Reserva> llistaReserves) {
        this.llistaReserves = llistaReserves;
    }

    /**
     * @return the id_treballador
     */
    public int getId_treballador() {
        return id_treballador;
    }

    /**
     * @param id_treballador the id_treballador to set
     */
    public void setId_treballador(int id_treballador) {
        this.id_treballador = id_treballador;
    }
  
    
}
