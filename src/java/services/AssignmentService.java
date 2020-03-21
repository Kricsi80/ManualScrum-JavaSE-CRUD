package services;

import java.util.List;
import javax.persistence.EntityManager;
import models.Assignment;
import models.Employee;
import models.Task;
import org.json.JSONArray;
import org.json.JSONObject;

public class AssignmentService {

    public JSONArray getAllAssignmentByEmployeeId(EntityManager em, int employeeId) {
        
        JSONArray result = new JSONArray();

        //ellenőrzés, hogy van-e ilyen employee
        if (Employee.getEmployeeById(em, employeeId).getId() == null) {
            
            JSONObject message = new JSONObject();
            message.put("msg", "There is no employee with this id");
            result.put(message);
            
        } else {
            
            List<Assignment> assignments = Assignment.getAllAssignmentByEmployeeId(em, employeeId);
            for (Assignment assignment : assignments) {
                
                result.put(assignment.toJson());
                
            }
        }

        return result;
    }

    public JSONArray getAllAssignmentByTaskId(EntityManager em, int taskId) {
        
        JSONArray result = new JSONArray();

        //ellenőrzés, hogy van-e ilyen task
        if (Task.getTaskById(em, taskId).getId() == null) {
            
            JSONObject message = new JSONObject();
            message.put("msg", "There is no task with this id");
            result.put(message);
            
        } else {

            List<Assignment> assignments = Assignment.getAllAssignmentByTaskId(em, taskId);
            for (Assignment assignment : assignments) {
                
                result.put(assignment.toJson());
                
            }
        }
        return result;
    }

    public boolean createNewAssignment(EntityManager em, int taskId, int employeeId) {

        //ellenőrzés, hogy van-e ilyen employee
        if (Employee.getEmployeeById(em, employeeId).getId() == null) {
            
            return false;
            
        //ellenőrzés, hogy van-e ilyen task
        } else if ((Task.getTaskById(em, taskId).getId() == null)) {
            
            return false;
            
        } else {

            return Assignment.createNewAssignment(em, taskId, employeeId);
        }
    }

    public boolean deleteAssignmentById(EntityManager em, int id) {
        
        //ellenőrzés, hogy van-e ilyen assignment
        if (Assignment.getAssignmentById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
        return Assignment.deleteAssignmentById(em, id);
        
        }
    }

    public boolean deleteAllAssignmentByEmployeeId(EntityManager em, int employeeId) {

        //ellenőrzés, hogy van-e ilyen employee
        if (Employee.getEmployeeById(em, employeeId).getId() == null) {
            
            return false;

        } else {
            
            return Assignment.deleteAllAssignmentByEmployeeId(em, employeeId);

        }
    }

    public boolean deleteAllAssignmentByTaskId(EntityManager em, int taskId) {

        //ellenőrzés, hogy van-e ilyen task
        if (Task.getTaskById(em, taskId).getId() == null) {
            
            return false;
            
        } else {
            
            return Assignment.deleteAllAssignmentByTaskId(em, taskId);

        }
    }
}
