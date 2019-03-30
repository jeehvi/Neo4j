/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.exceptions;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author JaviB
 */
public class Exceptions extends Exception {
    public static final int EMPLOYEE_DO_NOT_EXIST = 0;
    public static final int INCORRECT_LOGIN = 1;
    public static final int THERE_IS_NO_EMPLOYEES = 2;
    public static final int THERE_IS_NO_INCIDENCES = 3;
    public static final int USERNAME_EXISTS = 4;
    public static final int EMPLOYEE_HAS_NO_INCIDENCES = 5;
    public static final int FAIL_CREATE_EVENT = 5;
   
    private final int code;
    
    private final List<String> messages = Arrays.asList(
            "Employee does not exist",
            "Incorrect login",
            "There is no employees on our database",
            "There is no Incidences on our database",
            "Username already exists",
            "This Employee doesn't have any Incidence",
            "Event not created correctly"
    );

    public Exceptions(int code) {
        this.code = code;
    }

    
     @Override
    public String getMessage() {
        return messages.get(code); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Get the value of code
     *
     * @return the value of code
     */
     
    public int getCode() {
        return this.code;
    }

    
}
