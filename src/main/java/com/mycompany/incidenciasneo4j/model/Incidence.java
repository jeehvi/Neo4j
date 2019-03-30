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
    private String creationDate;
    private Employee employeeReceiver;
    private Employee employeeSender;
    private Boolean urgent;
    private String description;

    public Incidence(int id, Employee destination, Employee origin, Boolean urgent,String description) {
        this.id = id;
        this.employeeReceiver = destination;
        this.employeeSender = origin;
        this.urgent = urgent;
        this.description = description;
    }

    public Incidence() {
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
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
     * Get the value of employeeSender
     *
     * @return the value of employeeSender
     */
    public Employee getOrigin() {
        return employeeSender;
    }

    /**
     * Set the value of employeeSender
     *
     * @param origin new value of employeeSender
     */
    public void setOrigin(Employee origin) {
        this.employeeSender = origin;
    }

    /**
     * Get the value of employeeReceiver
     *
     * @return the value of employeeReceiver
     */
    public Employee getDestination() {
        return employeeReceiver;
    }

    /**
     * Set the value of employeeReceiver
     *
     * @param destination new value of employeeReceiver
     */
    public void setDestination(Employee destination) {
        this.employeeReceiver = destination;
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

    @Override
    public String toString() {
        String urgent = "";
        if(this.urgent == true){
            urgent = "Urgent Incidence!";
        } else {
            urgent = "Not Urgent Incidence";
        }
        return "Incidence{Creation Date = " + creationDate + ", Employee origin = " + employeeReceiver.getUsername() + ", Destination employee = " + employeeSender.getUsername() + ", Urgent/Not Urgent = " + urgent + ", Description = " + description + '}';
    }
    
    
    

}
