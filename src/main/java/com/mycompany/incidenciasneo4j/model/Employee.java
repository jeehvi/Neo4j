/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.model;

/**
 *
 * @author darre
 */
public class Employee {

    private int id;
    private String username;
    private String pass;
    

    public Employee(int id,String username, String pass) {
        this.id = id;
        this.username = username;
        this.pass = pass;
    }

    public Employee() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    /**
     * Get the value of pass
     *
     * @return the value of pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * Set the value of pass
     *
     * @param pass new value of pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Employee{username=" + username + ", pass=" + pass + '}';
    }

    
}
