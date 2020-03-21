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
import services.SprintService;

public class SprintController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application:jsoncharset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            
            SprintService ss = new SprintService();
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("manual_scrumPU");
            EntityManager em = emf.createEntityManager();

            //get all sprints by project id
            if (request.getParameter("task").equals("getAllSprintByProjectId")) {
                
                int id = 0;
                JSONObject message = new JSONObject();

                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    out.print(message);
                    
                } else {
                    
                    JSONArray sprints = ss.getAllSprintByProjectId(em, id);
                    
                    if (sprints.length() == 0) {
                        
                        message.put("msg", "There are no sprints in this Project");
                        out.print(message.toString());

                    } else {
                        
                        out.print(sprints.toString());

                    }
                }
            }

            //get sprint by id
            if (request.getParameter("task").equals("getSprintById")) {
                
                int id = 0;
                JSONObject message = new JSONObject();
                
                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    out.print(message);
                    
                } else {
                    
                    JSONObject sprint = ss.getSprintById(em, id);
                    out.print(sprint.toString());

                }
            }

            //create sprint by project id
            if (request.getParameter("task").equals("createNewSprint")) {
                
                int id = 0;
                JSONObject message = new JSONObject();

                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");

                } else {
                    
                    if (ss.createNewSprintByProjectId(em, id)) {

                        message.put("msg", "Sprint for project id: " + id + " created");

                    } else {

                        message.put("msg", "Something went wrong creating sprint");
                    }
                }

                out.print(message.toString());
            }

            //delete sprint by id
            if (request.getParameter("task").equals("deleteSprintById")) {
                
                int id = 0;
                JSONObject message = new JSONObject();
                
                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    
                } else {
                    
                    if (ss.deleteSprintById(em, id)) {
                        
                        message.put("msg", "Sprint with id: " + id + " deleted");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong deleting sprint");
                    }
                }

                out.print(message.toString());
            }

            //delete all sprint from a project
            if (request.getParameter("task").equals("deleteAllSprintByProjectId")) {
                
                int id = 0;
                JSONObject message = new JSONObject();

                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    
                } else {
                    
                    if (ss.deleteAllSprintByProjectId(em, id)) {
                        
                        message.put("msg", "All Sprints in project id: " + id + " deleted");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong deleting sprints");
                    }
                }

                out.print(message.toString());
            }

            //update sprint/ finish sprint
            if (request.getParameter("task").equals("updateSprintFinishedById")) {
                
                int id = 0;
                JSONObject message = new JSONObject();

                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    
                } else {
                    
                    if (ss.updateSprintFinishedById(em, id)) {
                        
                        message.put("msg", "Sprint id: " + id + " isFinished set to true");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong finishing sprint");
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
