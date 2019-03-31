/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.incidenciasneo4j.dao;

import com.mycompany.incidenciasneo4j.Main;
import com.mycompany.incidenciasneo4j.manager.Manager;
import com.mycompany.incidenciasneo4j.model.Employee;
import com.mycompany.incidenciasneo4j.model.Event;
import com.mycompany.incidenciasneo4j.model.Incidence;
import com.mycompany.incidenciasneo4j.model.RankingTo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;

/**
 *
 * @author JaviB
 */
public class NeoDAO implements AutoCloseable {

    private final Driver driver;
    Manager manager = Main.manager;

    public NeoDAO(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void insertEmployee(Employee e) {
        if (!userExists(e)) {
            try (Session session = driver.session()) {
                session.run("CREATE (employee:Employee{username:'" + e.getUsername() + "',password:'" + e.getPass() + "',department:'" + e.getDepartment() + "'})");
            }
        } else {
            System.out.println("Username already exists");
        }
    }

    public void insertIncidence(Incidence i) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String creationDate = df.format(date);
        try (Session session = driver.session()) {
            session.run("CREATE (incidence:Incidence{creationDate:'" + creationDate + "',sender:" + i.getOrigin().getId() + ",receiver:" + i.getDestination().getId() + ",urgent:" + i.isUrgent() + ",description:'" + i.getDescription() + "'})");
        }
    }

    public Employee getEmployeeById(int id){
        Employee emp = new Employee();
        try (Session session = driver.session()) {
            StatementResult x = session.run("MATCH(e:Employee) where id(e)=" + id + " return id(e) as id,e.username as username,e.password as password,e.department as department");
            Record record = x.next();
            emp.setId(record.get("id").asInt());
            emp.setUsername(record.get("username").asString());
            emp.setPass(record.get("password").asString());
            emp.setDepartment(record.get("department").asString());
        } catch (NoSuchRecordException ex) {
            System.out.println("Employee does not exist");
        }
        return emp;
    }

    public Employee loginEmployee(String user, String pass){
        Employee e = new Employee();
        try (Session session = driver.session()) {
            StatementResult sr = session.run("MATCH (e:Employee {username:'" + user + "',password:'" + pass + "'}) RETURN id(e) as id,e.username as username,e.password as password,e.department as department");
            Record record = sr.next();
            e.setId(record.get("id").asInt());
            e.setUsername(record.get("username").asString());
            e.setPass(record.get("password").asString());
            e.setDepartment(record.get("department").asString());
        } catch (NoSuchRecordException ex) {
            System.out.println("Incorrect Login");
            
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

    public List getAllEmployees() {
        List<Employee> employees = new ArrayList();
        try (Session session = driver.session()) {
            StatementResult rs = session.run("MATCH(e:Employee) RETURN id(e) as id,e.username as username,e.password as password,e.department as department");
            List<Record> list = rs.list();
            for (int i = 0; i < list.size(); i++) {
                Employee e = new Employee();
                e.setId(list.get(i).get("id").asInt());
                e.setUsername(list.get(i).get("username").asString());
                e.setPass(list.get(i).get("password").asString());
                e.setDepartment(list.get(i).get("department").asString());
                employees.add(e);
            }
        } catch (NoSuchRecordException ex) {
            System.out.println("There is no employees");
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

    public List<Incidence> selectUserIncidences(Employee logged) {
        List<Incidence> incidences = new ArrayList();
        try (Session session = driver.session()) {
            String query = "MATCH(i:Incidence) WHERE i.sender=" + logged.getId() + " or i.receiver=" + logged.getId() + " RETURN id(i) as id,i.creationDate as date,i.sender as senderId,i.receiver as receiverId,i.urgent as urgent,i.description as description";
            StatementResult rs = session.run(query);
            List<Record> list = rs.list();
            for (int i = 0; i < list.size(); i++) {
                Incidence incidence = new Incidence();
                incidence.setId(list.get(i).get("id").asInt());

                String date = list.get(i).get("date").asString();
                incidence.setCreationDate(date);

                Employee sender = getEmployeeById(list.get(i).get("receiverId").asInt());
                incidence.setOrigin(sender);

                Employee receiver = getEmployeeById(list.get(i).get("senderId").asInt());
                incidence.setDestination(receiver);

                incidence.setUrgent(list.get(i).get("urgent").asBoolean());
                incidence.setDescription(list.get(i).get("description").asString());
                incidences.add(incidence);
            }
        } catch (NoSuchRecordException ex) {
            System.out.println("There is no Employees");
        }

        return incidences;
    }

    public List<Incidence> selectAllIncidences() {
        List<Incidence> incidences = new ArrayList();
        try (Session session = driver.session()) {
            String query = "MATCH(i:Incidence) RETURN id(i) as id,i.creationDate as date,i.sender as senderId,i.receiver as receiverId,i.urgent as urgent,i.description as description";
            StatementResult rs = session.run(query);
            List<Record> list = rs.list();
            for (int i = 0; i < list.size(); i++) {
                Incidence incidence = new Incidence();
                incidence.setId(list.get(i).get("id").asInt());

                String date = list.get(i).get("date").asString();
                incidence.setCreationDate(date);

                Employee sender = getEmployeeById(list.get(i).get("receiverId").asInt());
                incidence.setOrigin(sender);

                Employee receiver = getEmployeeById(list.get(i).get("senderId").asInt());
                incidence.setDestination(receiver);

                incidence.setUrgent(list.get(i).get("urgent").asBoolean());
                incidence.setDescription(list.get(i).get("description").asString());
                incidences.add(incidence);
            }
        } catch (NoSuchRecordException ex) {
            System.out.println("There is no Incidences");
        }
        return incidences;
    }
    
    public List<Incidence> getIncidencesByDestinator(Employee e) {
        List<Incidence> incidences = new ArrayList();
        try (Session session = driver.session()) {
            String query = "MATCH(i:Incidence) WHERE i.receiver=" + e.getId() + " RETURN id(i) as id,i.creationDate as date,i.sender as senderId,i.receiver as receiverId,i.urgent as urgent,i.description as description";
            StatementResult rs = session.run(query);
            List<Record> list = rs.list();
            for (int i = 0; i < list.size(); i++) {
                Incidence incidence = new Incidence();
                incidence.setId(list.get(i).get("id").asInt());

                String date = list.get(i).get("date").asString();
                incidence.setCreationDate(date);

                Employee sender = getEmployeeById(list.get(i).get("receiverId").asInt());
                incidence.setOrigin(sender);

                Employee receiver = getEmployeeById(list.get(i).get("senderId").asInt());
                incidence.setDestination(receiver);

                incidence.setUrgent(list.get(i).get("urgent").asBoolean());
                incidence.setDescription(list.get(i).get("description").asString());
                incidences.add(incidence);
            }
        } catch (NoSuchRecordException ex) {
            System.out.println("This employee does not have Incidences");;
        }
        return incidences;
    }
    
      public List<Incidence> getIncidencesByOrigin(Employee e) {
        List<Incidence> incidences = new ArrayList();
        try (Session session = driver.session()) {
            String query = "MATCH(i:Incidence) WHERE i.sender=" + e.getId() + " RETURN id(i) as id,i.creationDate as date,i.sender as senderId,i.receiver as receiverId,i.urgent as urgent,i.description as description";
            StatementResult rs = session.run(query);
            List<Record> list = rs.list();
            for (int i = 0; i < list.size(); i++) {
                Incidence incidence = new Incidence();
                incidence.setId(list.get(i).get("id").asInt());

                String date = list.get(i).get("date").asString();
                incidence.setCreationDate(date);

                Employee sender = getEmployeeById(list.get(i).get("receiverId").asInt());
                incidence.setOrigin(sender);

                Employee receiver = getEmployeeById(list.get(i).get("senderId").asInt());
                incidence.setDestination(receiver);

                incidence.setUrgent(list.get(i).get("urgent").asBoolean());
                incidence.setDescription(list.get(i).get("description").asString());
                incidences.add(incidence);
            }
            if(list.size()==0){
                System.out.println("This employee does not have any Incidence");
            }
        } catch (NoSuchRecordException ex) {
            System.out.println("Employee has no Incidences");
        }
        return incidences;
    }
      
      public Event getUserLastAcces(Employee e){
          Event acces = new Event();
          try (Session session = driver.session()) {
            StatementResult rs = session.run("Match(e:Event) where e.Employee='"+e.getId()+"' and e.typeEvent = 'login' return e.Date,e.Employee,e.typeEvent  order by e.Date desc limit 1");
            Record record = rs.next();
           acces.setDate(record.get("e.Date").asString());
           acces.setEmployee(getEmployeeById(Integer.parseInt(record.get("e.Employee").asString())));
           acces.setTypeEvent(setEventCode(record.get("e.typeEvent").asString()));
        } catch (NoSuchRecordException ex) {
              System.out.println("User has not login yet");
        }
          return acces;
      }
    
    /**
     * Method to create an Event
     *
     * @param e
     * @throws Exceptions
     */
    public void insertEvent(Event e){
        String messageEvent = geteventMessage(e.getTypeEvent());
        try (Session session = driver.session()) {
            session.run("CREATE (e:Event{Date:'" + e.getDate() + "', Employee:'" + e.getEmployee().getId() + "',typeEvent:'" + messageEvent + "'})");
        } catch (NoSuchRecordException ex) {
            System.out.println("Something went wrong");
        }
    }
    
    public List<RankingTo> getRankingEmployees(){
        List<RankingTo> ranking = new ArrayList();
         try (Session session = driver.session()) {
            StatementResult rs = session.run("MATCH(e:Incidence) WHERE e.urgent = true RETURN count(e.sender) as incidencias,e.sender order by incidencias desc");
            List<Record> record = rs.list();
            for(Record r: record){
                RankingTo rt= new RankingTo();
                Employee sender = getEmployeeById(r.get("e.sender").asInt());
                rt.setUsername(sender.getUsername());
                rt.setnIncidences(r.get("incidencias").asInt());
                ranking.add(rt);
            }
        } catch (NoSuchRecordException ex) {
              System.out.println("Something wrong");
        }
        return ranking;
    }
    
    /**
     * Send an String message and returns an int code
     *
     * @param s
     * @return
     */
    public int setEventCode(String s) {
        int code = 0;
        switch (s) {
            case "login":
                code = 1;
                break;
            case "urgent incidence":
                code = 2;
                break;
            case "checkUserReceiver":
                code = 3;
                break;
        }
        return code;
    }
    
        /**
     * Send an int code and returns an String message
     *
     * @param i
     * @return
     */
    public String geteventMessage(int i) {
        String message = "";
        switch (i) {
            case 1:
                message = "login";
                break;
            case 2:
                message = "urgent incidence";
                break;
            case 3:
                message = "checkUserReceiver";
                break;
        }
        return message;
    }

    


    
}
