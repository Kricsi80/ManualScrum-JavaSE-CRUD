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
import services.ProjectService;

public class ProjectController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application:json");
        
        try (PrintWriter out = response.getWriter()) {
            
            ProjectService ps = new ProjectService();
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("manual_scrumPU");
            EntityManager em = emf.createEntityManager();

            //get all projects
            if (request.getParameter("task").equals("getAllProjects")) {
                
                JSONArray projects = ps.getAllProjects(em);
                out.print(projects.toString());
            }

            //get project by id
            if (request.getParameter("task").equals("getProjectById")) {
                
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
                    
                    JSONObject project = ps.getProjectById(em, id);
                    out.print(project.toString());
                }
            }

            //create project
            if (request.getParameter("task").equals("createNewProject")) {
                
                String title = request.getParameter("title");
                JSONObject message = new JSONObject();

                if (title.trim().length() > 0) {
                    
                    if (ps.createNewProject(em, title)) {
                        
                        message.put("msg", "Project with title: " + title + " created");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong creating project");
                    }

                } else {
                    
                    message.put("msg", "The title must contain characters");
                }

                out.print(message.toString());
            }

            //delete project
            if (request.getParameter("task").equals("deleteProjectById")) {
                
                int id = 0;
                JSONObject message = new JSONObject();
                
                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    
                } else {
                    
                    if (ps.deleteProjectById(em, id)) {
                        
                        message.put("msg", "Project with id: " + id + " deleted");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong deleting project");
                    }
                }

                out.print(message.toString());
            }

            //update project
            if (request.getParameter("task").equals("updateProjectTitleById")) {
                
                int id = 0;
                String title = request.getParameter("title");
                JSONObject message = new JSONObject();

                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id > 0 && title.trim().length() > 0) {
                    
                    if (ps.updateProjectTitleById(em, id, title)) {
                        
                        message.put("msg", "Project with id: " + id + " updated to title: " + title);
                        
                    } else {
                        
                        message.put("msg", "Something went wrong updating project");                        
                    }

                } else {
                    
                    message.put("msg", "The id must be number and greater than 0, the title must contain at least one character");

                }
                out.print(message.toString());
            }
            
            //get project statistics
            if (request.getParameter("task").equals("getProjectStatisticsById")){
                int id = 0;
                JSONObject result = new JSONObject();
                
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                    
                } catch (Exception e) {
                }
                
                if (id > 0) {
                    
                result = ps.getProjectStatisticsById(em, id);
                    
                } else {
                    
                    result.put("msg", "The id must be number and greater than 0, the name must contain at least one character");
                    
                }
                
                out.print(result.toString());  
            
            }
            
            //validate project
            if (request.getParameter("task").equals("validateProjectById")){
                int id = 0;
                JSONObject result = new JSONObject();
                
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                    
                } catch (Exception e) {
                }
                
                if (id > 0) {
                    
                result = ps.validateProjectById(em, id);
                    
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
