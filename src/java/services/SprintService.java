package services;

import java.util.List;
import javax.persistence.EntityManager;
import models.Project;
import models.Sprint;
import org.json.JSONArray;
import org.json.JSONObject;

public class SprintService {

    public JSONObject getSprintById(EntityManager em, int id) {
        
        Sprint sprint = Sprint.getSprintById(em, id);
        JSONObject result = new JSONObject();
        
        if (sprint.getId() == null) {
            
            result.put("msg", "Tere is no Sprint with this id");
            
        } else {
            
            result = sprint.toJson();
            
        }

        return result;
    }

    public JSONArray getAllSprintByProjectId(EntityManager em, int id) {
        
        JSONArray result = new JSONArray();
        
        //ellenőrzés, hogy van-e ilyen poject
        if (Project.getProjectById(em, id).getId() == null) {
            
            JSONObject message = new JSONObject();
            message.put("msg", "There is no Project with this id");
            result.put(message);
            
        } else {
            
            List<Sprint> sprints = Sprint.getAllSprintByProjectId(em, id);
            for (Sprint sprint : sprints) {
                result.put(sprint.toJson());
                
            }
        }

        return result;
    }
    
    public boolean createNewSprintByProjectId(EntityManager em, int id) {
        
        //ellenőrzés, hogy van-e ilyen project
        if (Project.getProjectById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
        return Sprint.createNewSprintByProjectId(em, id);
        
        }
    }

    public boolean deleteSprintById(EntityManager em, int id) {
        
        //ellenőrzés, hogy van-e ilyen sprint
        if (Sprint.getSprintById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
        return Sprint.deleteSprintById(em, id);
        
        }
    }

    public boolean deleteAllSprintByProjectId(EntityManager em, int id) {
        
        //ellenőrzés, hogy van-e ilyen prject
        if (Project.getProjectById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
        return Sprint.deleteAllSprintByProjectId(em, id);
        
        }
    }

    public boolean updateSprintFinishedById(EntityManager em, int id) {
        
        //ellenőrzés, hogy van-e ilyen sprint
        if (Sprint.getSprintById(em, id).getId() == null) {
            
            return false;
        
        //direkt nem vizsgálom, ha olyan isFinishedet akarnak állítani, ami már alapból át van
        //mert a hiba helyett szerintem jobb, ha ilyenkor ugyanúgy marad          
            
        } else {
            
            // ha el tudja végezni a task módosításokat
            if (Sprint.finishSprint(em, id)) {
                
            try {
                Sprint sprint = em.find(Sprint.class, id);

                em.getTransaction().begin();

                sprint.setIsFinished(true);

                em.getTransaction().commit();

                return true;

            } catch (Exception e) {

                return false;

            }
                
            } else {
                return false;
            }
            
        }
    }
}
