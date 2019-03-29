/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.dao;

import com.mycompany.incidenciasneo4j.exceptions.Exceptions;
import com.mycompany.incidenciasneo4j.model.Employee;
import com.mycompany.incidenciasneo4j.model.Incidence;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    public void insertEmployee(Employee e) throws Exceptions {
        if (!userExists(e)) {
            try (Session session = driver.session()) {
                session.run("CREATE (employee:Employee{username:'" + e.getUsername() + "',password:'" + e.getPass() + "',department:'" + e.getDepartment() + "'})");
            }
        } else {
            throw new Exceptions(Exceptions.USERNAME_EXISTS);
        }
    }

    public void insertIncidence(Incidence i) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String creationDate = df.format(date);
        try (Session session = driver.session()) {
            session.run("CREATE (incidence:Incidence{creationDate:'"+creationDate+"',sender:"+i.getOrigin().getId()+",receiver:"+i.getDestination().getId()+",urgent:"+i.isUrgent()+",description:'"+i.getDescription()+"'})");
        }
    }

    public Employee getEmployeeById(int id) throws Exceptions {
        Employee emp = new Employee();
        try (Session session = driver.session()) {
            StatementResult x = session.run("MATCH(e:Employee) where id(e)=" + id + " return id(e) as id,e.username as username,e.password as password");
            Record record = x.next();
            emp.setId(record.get("id").asInt());
            emp.setUsername(record.get("username").asString());
            emp.setPass(record.get("password").asString());
        }catch(NoSuchRecordException ex){
            throw new Exceptions(Exceptions.EMPLOYEE_DO_NOT_EXIST);
        }
        return emp;
    }

    public Employee loginEmployee(String user, String pass) throws Exceptions {
        Employee e = new Employee();
        try (Session session = driver.session()) {
            StatementResult sr = session.run("MATCH (e:Employee {username:'" + user + "',password:'" + pass + "'}) RETURN id(e) as id,e.username as username,e.password as password,e.department as department");
            Record record = sr.next();
            e.setId(record.get("id").asInt());
            e.setUsername(record.get("username").asString());
            e.setPass(record.get("password").asString());
            e.setDepartment(record.get("department").asString());
        } catch (NoSuchRecordException ex) {
           throw new Exceptions(Exceptions.INCORRECT_LOGIN);
        }
        return e;
    }

    public void removeEmployee(Employee e) {
        try (Session session = driver.session()) {
            session.run("MATCH(e:Employee) WHERE id(e)=" + e.getId() + " DELETE e");
        }
    }

    public void updateEmployee(Employee e) {
        try (Session session = driver.session()) {
            session.run("MATCH(e:Employee) WHERE id(e)=" + e.getId() + " SET e.password = '" + e.getPass() + "',e.department = '" + e.getDepartment() + "'");
        }
    }

    public List getAllEmployees() throws Exceptions {
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
            throw new Exceptions(Exceptions.THERE_IS_NO_EMPLOYEES);
        }
        return employees;
    }
    public boolean userExists(Employee e) {
        boolean exists = false;
        try (Session session = driver.session()) {
            StatementResult sr = session.run("MATCH (e:Employee {username:'" + e.getUsername() + "'}) RETURN id(e) as id,e.username as username,e.password as password");
            Record record = sr.next();
            exists = true;
        } catch (NoSuchRecordException ex) {
            exists = false;
        }
        return exists;
    }

    public List<Incidence> selectAllIncidences(Employee logged) throws Exceptions {
        List<Incidence> incidences = new ArrayList();
        try (Session session = driver.session()) {
            String query = "MATCH(i:Incidence) WHERE i.sender=" + logged.getId() + " or i.receiver=" + logged.getId() + " RETURN id(i) as id,i.creationDate as date,i.sender as senderId,i.receiver as receiverId,i.urgent as urgent,i.description as description";
            StatementResult rs = session.run(query);
            List<Record> list = rs.list();
            for (int i = 0; i < list.size(); i++) {
                Incidence incidence = new Incidence();
                incidence.setId(list.get(i).get("id").asInt());

                String dateTime = list.get(i).get("date").asLocalDateTime().toString();
                incidence.setCreationDate(dateTime);

                Employee sender = getEmployeeById(list.get(i).get("receiverId").asInt());
                incidence.setOrigin(sender);

                Employee receiver = getEmployeeById(list.get(i).get("senderId").asInt());
                incidence.setDestination(receiver);

                incidence.setUrgent(list.get(i).get("urgent").asBoolean());
                incidence.setDescription(list.get(i).get("description").asString());
                incidences.add(incidence);
            }
        } catch (NoSuchRecordException ex) {
           throw new Exceptions(Exceptions.THERE_IS_NO_INCIDENCES);

        }
        return incidences;
    }
}
