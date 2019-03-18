/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.dao;

import com.mycompany.incidenciasneo4j.model.Employee;
import java.util.ArrayList;
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

    public void insertEmployee(Employee e) {
        try (Session session = driver.session()) {
            session.run("CREATE (employee:Employee{username:'" + e.getUsername() + "',password:'" + e.getPass() + "'})");
        }

    }
    
    public void insertIncidence(Employee o,Employee d,boolean isUrgent,String description){
        try(Session session = driver.session()){
            session.run("CREATE (incidence:Incidence{})");
        }
    }

    public Employee getEmployeeById(Employee e) {
        Employee emp = new Employee();
        try (Session session = driver.session()) {
            StatementResult x = session.run("MATCH(e:Employee) where id(e)="+e.getId()+" return id(e) as id,e.username as username,e.password as password");
            Record record = x.next();
            emp.setId(record.get("id").asInt());
            emp.setUsername(record.get("username").asString());
            emp.setPass(record.get("password").asString());
        }
        return emp;
    }
     
    
 public Employee loginEmployee(String user, String pass) {
     Employee e = new Employee();
     try (Session session = driver.session()) {
            StatementResult sr = session.run("MATCH (e:Employee {username:'"+user+"',password:'"+pass+"'}) RETURN id(e) as id,e.username as username,e.password as password");
            Record record = sr.next();
            e.setId(record.get("id").asInt());
            e.setUsername(record.get("username").asString());
            e.setPass(record.get("password").asString());
    }catch(NoSuchRecordException ex){
            System.out.println("Login Incorrecto");
    }
        return e;
}
     
    
    public void removeEmployee(Employee e) {
        try (Session session = driver.session()) {
            session.run("MATCH(e:Employee) WHERE id(e)=" + e.getId() + " DELETE e");
        }
    }
    
    public void updateEmployee(Employee e){
        try(Session session = driver.session()){
            session.run("MATCH(e:Employee) WHERE id(e)="+e.getId()+" SET e.password = '"+e.getPass()+"'");
        }
    }

    /*public List getAllEmployees() {
        List<Employee> employees = new ArrayList();
        try (Session session = driver.session()) {
            StatementResult rs = session.run("MATCH(e:Employee) RETURN id(e) as id,e.username as username,e.password as password");
            List<Record> list = rs.list();
            for (int i = 0; i < list.size(); i++) {
                Employee e = new Employee();
                e.setId(list.get(i).get("id").asInt());
                e.setUsername(list.get(i).get("username").asString());
                e.setPass(list.get(i).get("password").asString());
                employees.add(e);
            }
        } catch (NoSuchRecordException ex) {
            System.out.println("No hay empleados");
        }
        return employees;
    }*/
}
