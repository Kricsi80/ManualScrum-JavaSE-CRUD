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
import services.TaskService;

public class TaskController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application:json");
        
        try (PrintWriter out = response.getWriter()) {
            
            TaskService ts = new TaskService();
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("manual_scrumPU");
            EntityManager em = emf.createEntityManager();
            
            //get all tasks by sprint id
            if (request.getParameter("task").equals("getAllTasksBySprintId")) {
                
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
                    
                    JSONArray sprints = ts.getAllTasksBySprintId(em, id);
                    
                    if (sprints.length() == 0) {
                        
                        message.put("msg", "There are no tasks in this sprint");
                        out.print(message.toString());
                        
                    } else {
                        
                        out.print(sprints.toString());
                    }
                }
            }

            //get task by id
            if (request.getParameter("task").equals("getTaskById")) {
                
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
                    
                    JSONObject task = ts.getTaskById(em, id);
                    out.print(task.toString());
                }
            }

            //create task
            if (request.getParameter("task").equals("createNewTask")) {
                
                int sprintId = 0;
                String title = request.getParameter("title");
                String description = request.getParameter("description");

                JSONObject message = new JSONObject();

                try {
                    sprintId = Integer.parseInt(request.getParameter("sprintid"));

                } catch (Exception e) {
                }
                
                if (sprintId > 0 && title.trim().length() > 0 && description.trim().length() > 0) {
                    
                    if (ts.createNewTask(em, title, description, sprintId)) {
                        
                        message.put("msg", "Task title: " + title + " for sprint id: " + sprintId + " created");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong creating task");
                        
                    }

                } else {
                    
                    message.put("msg", "The id must be number and greater than 0, the title and description must contain at least one character");

                }

                out.print(message.toString());
            }

            //delete task
            if (request.getParameter("task").equals("deleteTaskById")) {
                
                int id = 0;
                JSONObject message = new JSONObject();

                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    
                } else {
                    
                    if (ts.deleteTaskById(em, id)) {
                        
                        message.put("msg", "Task with id: " + id + " deleted");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong deleting task");
                    }
                }

                out.print(message.toString());
            }

            //delete all tasks from a sprint
            if (request.getParameter("task").equals("deleteAllTaskBySprintId")) {
                
                int id = 0;
                JSONObject message = new JSONObject();
                
                try {
                    id = Integer.parseInt(request.getParameter("id"));

                } catch (Exception e) {
                }

                if (id <= 0) {
                    
                    message.put("msg", "The input parameter must be number and greater than 0");
                    
                } else {
                    
                    if (ts.deleteAllTaskBySprintId(em, id)) {
                        
                        message.put("msg", "All tasks in sprint id: " + id + " deleted");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong deleting tasks");
                    }
                }

                out.print(message.toString());
            }

            //update task by id
            if (request.getParameter("task").equals("updateTaskById")) {
                
                int id = 0;
                int sprintId = 0;
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                int position = -1;                
                JSONObject message = new JSONObject();

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                    sprintId = Integer.parseInt(request.getParameter("sptintId"));
                    position = Integer.parseInt(request.getParameter("position"));                    

                } catch (Exception e) {
                }


                if (id > 0 && title.trim().length() > 0 && description.trim().length() > 0 && position > -1 && sprintId > 0) {
                    
                    if (ts.updateTaskById(em, id, title, description, position, sprintId)) {
                        
                        message.put("msg", "Task id: " + id + " updated");
                        
                    } else {
                        
                        message.put("msg", "Something went wrong updating task");
                        
                    }

                } else {
                    
                    message.put("msg", "The input parameters doesnt match the required types (id: number>0, title and descrition: non empty string, position: number between 0 and 3)");

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
