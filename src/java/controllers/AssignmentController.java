package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import services.AssignmentService;

public class AssignmentController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application:json");
        
        try (PrintWriter out = response.getWriter()) {
            
            AssignmentService as = new AssignmentService();
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("manual_scrumPU");
            EntityManager em = emf.createEntityManager();

            //get Assignments by employeeid
            if (request.getParameter("task").equals("getAllAssignmentByEmployeeId")) {

                int employeeId = 0;
                JSONObject message = new JSONObject();

                try {
                    employeeId = Integer.parseInt(request.getParameter("id"));
                } catch (Exception e) {
                }

                if (employeeId == 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    out.print(message.toString());

                } else {

                    JSONArray assignments = as.getAllAssignmentByEmployeeId(em, employeeId);

                    if (assignments.length() == 0) {
                        
                        message.put("msg", "No task is assigned to this employee");
                        out.print(message.toString());
                        
                    } else {
                        
                        out.print(assignments.toString());
                    }
                }
            }

            //get Assignments by task
            if (request.getParameter("task").equals("getAllAssignmentByTaskId")) {

                int taskId = 0;
                JSONObject message = new JSONObject();

                try {
                    taskId = Integer.parseInt(request.getParameter("id"));
                } catch (Exception e) {
                }

                if (taskId <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    out.print(message.toString());
                    
                } else {
                    
                    JSONArray assignments = as.getAllAssignmentByTaskId(em, taskId);
                    
                    if (assignments.length() == 0) {
                        
                        message.put("msg", "Nobody is assigned to this task");
                        out.print(message.toString());

                    } else {
                        
                        out.print(assignments.toString());
                    }
                }
            }

            //create assignment
            if (request.getParameter("task").equals("createAssignment")) {
                
                int taskId = 0;
                int employeeId = 0;

                try {
                    taskId = Integer.parseInt(request.getParameter("taskId"));
                    employeeId = Integer.parseInt(request.getParameter("employeeId"));
                } catch (Exception e) {
                }

                JSONObject message = new JSONObject();

                if (taskId == 0 || employeeId == 0) {
                    
                    message.put("msg", "The input parameters must be numbers and greater than 0");
                    
                } else {
                    
                    if (as.createNewAssignment(em, taskId, employeeId)) {
                        
                        message.put("msg", "Assignment created on employee id: " + employeeId + " and task id: " + taskId);
                        
                    } else {
                        
                        message.put("msg", "Something went wrong creating Assignment");
                        
                    }
                }
                out.print(message.toString());
            }

            //delete assignment 
            if (request.getParameter("task").equals("deleteAssignmentById")) {
                
                int id = 0;
                JSONObject message = new JSONObject();

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (Exception e) {
                }

                if (id == 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    
                } else {
                    
                    if (as.deleteAssignmentById(em, id)) {
                        
                        message.put("msg", "Assignment with id: " + id + " deleted");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong deleting Assignment");
                        
                    }
                }
                out.print(message.toString());
            }

            //delete all assignments by employee id
            if (request.getParameter("task").equals("deleteAllAssignmentByEmployeeId")) {
                
                int employeeId = 0;
                JSONObject message = new JSONObject();

                try {
                    employeeId = Integer.parseInt(request.getParameter("id"));
                } catch (Exception e) {
                }

                if (employeeId == 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    
                } else {
                    
                    if (as.deleteAllAssignmentByEmployeeId(em, employeeId)) {
                        
                        message.put("msg", "Assignments deleted by employee id: " + employeeId);

                    } else {
                        
                        message.put("msg", "Something went wrong deleting Assignments by employee id: " + employeeId);
                        
                    }
                }
                out.print(message.toString());
            }

            //delete all assignments by task id
            if (request.getParameter("task").equals("deleteAllAssignmentByTaskId")) {
                
                int taskId = 0;
                JSONObject message = new JSONObject();

                try {
                    taskId = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (taskId == 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    
                } else {

                    if (as.deleteAllAssignmentByTaskId(em, taskId)) {
                        
                        message.put("msg", "Assignments deleted by task id: " + taskId);
                       
                    } else {
                        
                        message.put("msg", "Something went wrong deleting Assignments by task id: " + taskId);
                        
                    }
                }
                out.print(message.toString());
            }
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
