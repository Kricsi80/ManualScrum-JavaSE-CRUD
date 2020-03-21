package services;

import java.util.List;
import javax.persistence.EntityManager;
import models.Employee;
import org.json.JSONArray;
import org.json.JSONObject;

public class EmployeeService {

    public JSONObject getEmployeeById(EntityManager em, int id) {
        
        Employee employee = Employee.getEmployeeById(em, id);
        JSONObject result = new JSONObject();
        
        //ellenőrzés, hogy van-e ilyen employee
        if (employee.getId() == null) {
            
            result.put("msg", "There is no employee with this id");
            
        } else {
            
            result = employee.toJson();
            
        }

        return result;
    }

    public JSONArray getAllEmployees(EntityManager em) {
        
        List<Employee> employees = Employee.getAllEmployees(em);
        JSONArray result = new JSONArray();
        
        //ellenőrzés, hogy a lista üres-e
        if (employees.isEmpty()) {
            
            JSONObject message = new JSONObject();
            message.put("msg", "There are no Employees");
            result.put(message);
            
        } else {
            
            for (Employee employee : employees) {

                result.put(employee.toJson());

            }
        }

        return result;
    }

    public boolean createNewEmployee(EntityManager em, String name) {

        return Employee.createNewEmployee(em, name);
    }

    public boolean deleteEmployeeById(EntityManager em, int id) {

        //ellenőrzés, hogy van-e ilyen employee
        if (Employee.getEmployeeById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
            return Employee.deleteEmployeeById(em, id);
            
        }
    }

    public boolean updateEmployeeNameById(EntityManager em, int id, String name) {

        //ellenőrzés, hogy van-e ilyen employee
        if (Employee.getEmployeeById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
            try {
                Employee employee = em.find(Employee.class, id);

                em.getTransaction().begin();

                employee.setName(name);

                em.getTransaction().commit();

                return true;

            } catch (Exception e) {
                
                return false;
                
            }
        }
    }
    
    public JSONObject getEmployeeStatistics(EntityManager em, int id){
        
        if (Employee.getEmployeeById(em, id).getId() == null) {
            
            JSONObject message = new JSONObject();
            message.put("msg", "There is no employee with this id");
            return message;
            
        } else {
            
        return Employee.getEmployeeStatisticsById(em, id);
        
        }        
    }
}
