package services;

import java.util.List;
import javax.persistence.EntityManager;
import models.Project;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProjectService {

    public JSONObject getProjectById(EntityManager em, int projectId) {
        
        Project project = Project.getProjectById(em, projectId);
        JSONObject result = new JSONObject();
        
        //ellenőrzés, hogy van-e ilyen project
        if (project.getId() == null) {
            
            result.put("msg", "There is no project with this id");
            
        } else {
            
            result = project.toJson();
            
        }

        return result;
    }

    public JSONArray getAllProjects(EntityManager em) {
        
        List<Project> projects = Project.getAllProjects(em);
        JSONArray result = new JSONArray();

        ////ellenőrzés, hogy a lista üres-e
        if (projects.isEmpty()) {
            
            JSONObject message = new JSONObject();
            message.put("msg", "There are no Projects");
            result.put(message);
            
        } else {
            
            for (Project project : projects) {
                result.put(project.toJson());
                
            }
        }
        
        return result;
    }

    public boolean createNewProject(EntityManager em, String title) {

        return Project.createNewProject(em, title);        
    }

    public boolean deleteProjectById(EntityManager em, int id) {
        
        //ellenőrzés, hogy van-e ilyen project
        if (Project.getProjectById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
        return Project.deleteProjectById(em, id);
        
        }
    }

    public boolean updateProjectTitleById(EntityManager em, int id, String title) {
        
        //ellenőrzés, hogy van-e ilyen project
        if (Project.getProjectById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
            try {
                Project project = em.find(Project.class, id);

                em.getTransaction().begin();

                project.setTitle(title);

                em.getTransaction().commit();

                return true;

            } catch (Exception e) {
                
                return false;
                
            }
        }  
    }
    
    public JSONObject getProjectStatisticsById(EntityManager em, int id){
        if (Project.getProjectById(em, id).getId() == null) {

            JSONObject message = new JSONObject();
            message.put("msg", "There is no project with this id");
            return message;

        } else {

        return Project.getProjectStatisticsById(em, id);

        }    
    
    }
    
    public JSONObject validateProjectById(EntityManager em, int id){
        if (Project.getProjectById(em, id).getId() == null) {
            
            JSONObject message = new JSONObject();
            message.put("msg", "There is no project with this id");
            return message;
            
        } else {
            
        return Project.validateProjectById(em, id);
        
        }    
    
    }
}
