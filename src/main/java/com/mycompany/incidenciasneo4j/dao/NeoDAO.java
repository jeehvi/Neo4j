/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.dao;

import com.mycompany.incidenciasneo4j.model.Employee;
import java.util.List;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import static org.neo4j.driver.v1.Values.parameters;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;

/**
 *
 * @author JaviB
 */
public class NeoDAO implements AutoCloseable {

    private final Driver driver;

    public NeoDAO(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void insertEmployee(String username, String password) {
        try (Session session = driver.session()) {
            session.run("CREATE (employee:Employee{username:'" + username + "',password:'" + password + "'})");
        }

    }

    /*public Employee getEmployeeById() {
        Employee e = null;
        try (Session session = driver.session()) {
            StatementResult x = session.run("MATCH(e:Employee) where id(e)=61 return id(e) as id,e.username as username,e.password as password");
            Record record = x.next();
            e.setId(record.get("id").asInt());
            e.setUsername(record.get("username").asString());
            e.setPass(record.get("password").asString());
        }
        return e;
    }
     */
    public boolean correctLogin(String user, String pass) {
        
    boolean checked = false;
        try (Session session = driver.session()) {
            StatementResult sr = session.run("MATCH (e:Employee {username:'"+user+"',password:'"+pass+"'}) return e");
            Record record = sr.next();
            checked = true;
    }catch(NoSuchRecordException ex){
            System.out.println("Login Incorrecto");
    }
        return checked;
}
}
