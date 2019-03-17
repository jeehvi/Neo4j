/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.manager;

import com.mycompany.incidenciasneo4j.InputAsker;
import com.mycompany.incidenciasneo4j.dao.NeoDAO;
import com.mycompany.incidenciasneo4j.model.Employee;
import java.util.List;

/**
 *
 * @author JaviB
 */
public class Manager {

    NeoDAO dao = new NeoDAO("bolt://localhost:7687", "neo4j", "1234");
    Employee userLogged = null;

    public void callMenu() {
        boolean goodLogin = false;
        do {
            goodLogin = login();
        } while (goodLogin == false);

        int op;
        boolean end = false;
        do {
            menu();
            op = InputAsker.askInt("Opcion: ");
            switch (op) {
                case 0:
                    end = true;
                    break;
                case 1:
                    //insert employee
                    insertEmployee();
                    break;
                case 2:
                    //modify employee

                    break;
                case 3:
                    //delete employee
                    removeEmployee();
                    break;
                case 4:
                    //get incidence by id

                    break;
                case 5:
                    //get all incidences

                    break;
                case 6:
                    //insert incidence

                    break;
                case 7:
                    //get incidence by destinator

                    break;
                case 8:
                    //get incidence by origin

                    break;
                case 9:
                    //insert event to history table

                    break;
                case 10:
                    //Get last acces from an employee

                    break;
                case 11:
                    //employee ranking by number of incidences

                    break;
                default:
                    System.out.println("Wrong option");
                    break;
            }

        } while (end != true);

    }

    public void menu() {
        System.out.println("-----MENU-----");
        System.out.println("1-Insert an employee");
        System.out.println("2-Modify an employee");
        System.out.println("3-Delete an employee");
        System.out.println("4-Get Incidence by Id");
        System.out.println("5-Get all Incidences");
        System.out.println("6-Insert an Incidence");
        System.out.println("7-Get Incidence by destination");
        System.out.println("8-Get Incidence by origin");
        System.out.println("9-Insert event to history table");
        System.out.println("10-Get last acces from an employee");
        System.out.println("11-Employee ranking by the number of Incidences");
    }

    public boolean login() {
        System.out.println("-----LOG IN-----");
        String username = InputAsker.askString("Username: ");
        String password = InputAsker.askString("Password: ");
        return dao.correctLogin(username, password);
    }

    public void insertEmployee() {
        System.out.println("-----INSERT EMPLOYEE-----");
        String username = InputAsker.askString("Username: ");
        String password = InputAsker.askString("Password: ");
        dao.insertEmployee(username, password);
    }

    public void removeEmployee() {
        System.out.println("-----REMOVE EMPLOYEE-----");
        List<Employee> list = dao.getAllEmployees();
        int contador = 0;
        for (int i = 0; i < list.size(); i++) {
            contador++;
            System.out.println(contador + "-" + list.get(i).toString());
        }
        int selected = InputAsker.askInt("Which employee do you want to delete?(0 TO CANCEL)");
        if(selected<0){
            System.out.println("Opcion incorrecta");
        }else if(selected>list.size()){
            System.out.println("Opcion incorrecta");
        } else if(selected ==0){
            System.out.println("Operacion cancelada");
        } else {
            dao.removeEmployee(list.get(selected-1));
        }
    }

}
