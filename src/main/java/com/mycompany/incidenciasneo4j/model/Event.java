/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.model;

import java.util.Date;

/**
 *
 * @author darre
 */
public class Event {

    private Date date;
    private String argument;

    public Event(Date date, String argument) {
        this.date = date;
        this.argument = argument;
    }

    public Event() {
    }

    /**
     * Get the value of argument
     *
     * @return the value of argument
     */
    public String getArgument() {
        return argument;
    }

    /**
     * Set the value of argument
     *
     * @param argument new value of argument
     */
    public void setArgument(String argument) {
        this.argument = argument;
    }

    /**
     * Get the value of date
     *
     * @return the value of date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the value of date
     *
     * @param date new value of date
     */
    public void setDate(Date date) {
        this.date = date;
    }

}
