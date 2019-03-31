/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j;

import com.mycompany.incidenciasneo4j.dao.NeoDAO;
import com.mycompany.incidenciasneo4j.manager.Manager;

/**
 *
 * @author JaviB
 */
public class Main {
    
    public static  Manager manager ;
    public static void main(String[] args) {
   manager = new Manager(); 
      
    manager.callMenu();
    
}
}
