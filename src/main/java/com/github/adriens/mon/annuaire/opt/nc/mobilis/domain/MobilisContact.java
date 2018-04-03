/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.mon.annuaire.opt.nc.mobilis.domain;

/**
 *
 * @author salad74
 */
public class MobilisContact {

    public MobilisContact(){
        
    }
    
    public MobilisContact(String aNom, String aMobilis){
        setNom(aNom.trim());
        setMobilis(aMobilis);
    }
    
    @Override
    public String toString(){
        return getNom() + " : " + getMobilis();
    }
    
    
    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the mobilis
     */
    public String getMobilis() {
        return mobilis;
    }

    /**
     * @param mobilis the mobilis to set
     */
    public void setMobilis(String mobilis) {
        this.mobilis = mobilis;
    }
    private String nom;
    private String mobilis;
    
}
