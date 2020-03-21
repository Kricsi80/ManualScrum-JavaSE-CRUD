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
import services.EmployeeService;

public class EmployeeController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application:json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            
            EmployeeService es = new EmployeeService();
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("manual_scrumPU");
            EntityManager em = emf.createEntityManager();

            //get all employees
            if (request.getParameter("task").equals("getAllEmployees")) {
                
                JSONArray employees = es.getAllEmployees(em);
                out.print(employees.toString());
            }

            //get employee by id
            if (request.getParameter("task").equals("getEmployeeById")) {
                
                int id = 0;
                JSONObject message = new JSONObject();
                
                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    out.print(message.toString());
                    
                } else {

                    JSONObject employee = es.getEmployeeById(em, id);
                    out.print(employee.toString());
                }
            }

            //create employee
            if (request.getParameter("task").equals("createNewEmployee")) {
                
                String name = request.getParameter("name");
                JSONObject message = new JSONObject();

                if (name.trim().length() > 0) {
                    
                    if (es.createNewEmployee(em, name)) {
                        
                        message.put("msg", "Employee with name: " + name + " created");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong creating employee");
                    }

                } else {
                    
                    message.put("msg", "The name must contain characters");
                }

                out.print(message.toString());
            }

            //delete employee
            if (request.getParameter("task").equals("deleteEmployeeById")) {
                
                int id = 0;
                JSONObject message = new JSONObject();
                
                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");

                } else {
                    
                    if (es.deleteEmployeeById(em, id)) {
                        
                        message.put("msg", "Employee with id: " + id + " deleted");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong deleting employee");
                        
                    }
                }
                out.print(message.toString());
            }

            //update employee
            if (request.getParameter("task").equals("updateEmployeeNameById")) {
                
                int id = 0;
                String name = request.getParameter("name");
                JSONObject message = new JSONObject();

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (Exception e) {
                }


                if (id > 0 && name.trim().length() > 0) {

                    if (es.updateEmployeeNameById(em, id, name)) {
                        
                        message.put("msg", "Employee with id: " + id + " updated to name: " + name);
                        
                    } else {
                        
                        message.put("msg", "Something went wrong updating employee");
                        
                    }
                } else {
                    
                    message.put("msg", "The id must be number and greater than 0, the name must contain at least one character");
                    
                }
                out.print(message.toString());
            }
            
            //get employee statistics
            if (request.getParameter("task").equals("getEmployeeStatistics")){
                
                int id = 0;
                JSONObject result = new JSONObject();
                
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                    
                } catch (Exception e) {
                }
                
                if (id > 0) {
                    
                result = es.getEmployeeStatistics(em, id);
                    
                } else {
                    
                    result.put("msg", "The id must be number and greater than 0, the name must contain at least one character");
                    
                }
                
                out.print(result.toString());            
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
