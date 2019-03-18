/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.model;

import com.mycompany.incidenciasneo4j.model.Employee;

/**
 *
 * @author darre
 */
public class Incidence {

    private int id;
    private Employee destination;
    private Employee origin;
    private Boolean urgent;
    private String description;

    public Incidence(int id, Employee destination, Employee origin, Boolean urgent,String description) {
        this.id = id;
        this.destination = destination;
        this.origin = origin;
        this.urgent = urgent;
        this.description = description;
    }

    public Incidence() {
    }

    /**
     * Get the value of urgent
     *
     * @return the value of urgent
     */
    public Boolean isUrgent() {
        return urgent;
    }

    /**
     * Set the value of urgent
     *
     * @param urgent new value of urgent
     */
    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }

    /**
     * Get the value of origin
     *
     * @return the value of origin
     */
    public Employee getOrigin() {
        return origin;
    }

    /**
     * Set the value of origin
     *
     * @param origin new value of origin
     */
    public void setOrigin(Employee origin) {
        this.origin = origin;
    }

    /**
     * Get the value of destination
     *
     * @return the value of destination
     */
    public Employee getDestination() {
        return destination;
    }

    /**
     * Set the value of destination
     *
     * @param destination new value of destination
     */
    public void setDestination(Employee destination) {
        this.destination = destination;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    

}
