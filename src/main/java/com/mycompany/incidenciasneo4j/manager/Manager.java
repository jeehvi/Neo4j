/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.manager;

import com.mycompany.incidenciasneo4j.InputAsker;
import com.mycompany.incidenciasneo4j.dao.NeoDAO;
import com.mycompany.incidenciasneo4j.model.Employee;
import com.mycompany.incidenciasneo4j.model.Incidence;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neo4j.driver.v1.Record;

/**
 *
 * @author JaviB
 */
public class Manager {

    NeoDAO dao = new NeoDAO("bolt://localhost:7687", "neo4j", "1234");
    Employee userLogged = new Employee();

    public void callMenu() {
        try {
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
                        updateEmployee();
                        break;
                    case 3:
                        //delete employee
                        removeEmployee();
                        break;
                    case 4:
                        //get incidence by id
                        getIncidenceById();
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
            dao.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
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
        System.out.println("0-EXIT");
    }

    public boolean login() {
        System.out.println("-----LOG IN-----");
        String username = InputAsker.askString("Username: ");
        String password = InputAsker.askString("Password: ");
        Employee e = dao.loginEmployee(username, password);
        if (e.getUsername() != null) {
            userLogged = e;
            return true;
        }
        return false;
    }

    public void insertEmployee() {
        System.out.println("-----INSERT EMPLOYEE-----");
        String username = InputAsker.askString("Username: /(0) TO CANCEL");
        if (username.equals("0")) {
            System.out.println("The operation was cancelled");
        } else {
            String password = InputAsker.askString("Password: ");
            String department = InputAsker.askString("Department: ");
            Employee e = new Employee();
            e.setUsername(username);
            e.setPass(password);
            e.setDepartment(department);
            dao.insertEmployee(e);
        }
    }

    public void removeEmployee() {
        try {
            System.out.println("Are you sure do you want to delete your account?");
            String answer = InputAsker.askString("YES/NO");
            switch (answer.toLowerCase()) {
                case "y":
                case "yes":
                    dao.removeEmployee(userLogged);
                    dao.close();
                    callMenu();
                    break;
                case "n":
                case "no":
                    System.out.println("The operation was cancelled.");
                    break;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void updateEmployee() {
        System.out.println("-----UPDATE EMPLOYEE-----");
        System.out.println("User -> " + userLogged.getUsername() + " - Department -> " + userLogged.getDepartment());

        System.out.println("1-Update password");
        System.out.println("2-Update department");

        int opc = InputAsker.askInt("What do you want to modify?/(0) TO CANCEL");
        switch (opc) {
            case 0:
                System.out.println("The operation was cancelled");
                break;
            case 1:
                boolean actualPassOk = false;
                do {
                    String actualPassword = InputAsker.askString("Actual Password:/(0 TO CANCEL)");
                    if (actualPassword.equals("0")) {
                        System.out.println("The operation was cancelled");
                        actualPassOk = true;
                    } else {
                        if (actualPassword.equals(userLogged.getPass())) {
                            String newPassword = InputAsker.askString("New Password: ");
                            userLogged.setPass(newPassword);
                            dao.updateEmployee(userLogged);
                            actualPassOk = true;
                        } else {
                            System.out.println("Incorrect Password");
                        }
                    }
                } while (actualPassOk != true);
                break;
            case 2:
                String newDepartment = InputAsker.askString("Set a new department:");
                userLogged.setDepartment(newDepartment);
                dao.updateEmployee(userLogged);
                break;

        }

    }

    public void getIncidenceById() {
        System.out.println("-----SHOW INCIDENCE DETAILS-----");
        List<Incidence> list = dao.getAllIncidences(userLogged);
        for (Incidence i : list) {
                System.out.println("Incidence " + i.getId() + "  ->  Sender: " + i.getOrigin().getUsername() + " Receiver: " + i.getDestination().getUsername());
        }
        int incidence = InputAsker.askInt("See details of Incidence with id: ");
        boolean exists = false;
        Incidence choosen = new Incidence();
        for(Incidence i : list){
            if(i.getId() == incidence){
                choosen = i;
                exists = true;
            }
        }
        if(!exists){
            System.out.println("Incorrect Incidence");
        } else{
            System.out.println("Incidence: "+choosen.getId() + "  -> Creation Date: "+choosen.getCreationDate()+" Sender: " + choosen.getOrigin().getUsername() + " Receiver: " + choosen.getDestination().getUsername() + " Description: "+choosen.getDescription());
        }
    }

}
