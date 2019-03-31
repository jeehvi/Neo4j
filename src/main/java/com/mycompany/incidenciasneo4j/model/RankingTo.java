/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.model;

/**
 *
 * @author JaviB
 */
public class RankingTo {

    String username;
    int nIncidences;

    public RankingTo(String username, int nIncidences) {
        this.username = username;
        this.nIncidences = nIncidences;
    }

    public RankingTo() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getnIncidences() {
        return nIncidences;
    }

    public void setnIncidences(int nIncidences) {
        this.nIncidences = nIncidences;
    }

    @Override
    public String toString() {
        return "Employee{" + "Username= " + username + ", Urgent Incidences= " + nIncidences + '}';
    }

    
    
}
