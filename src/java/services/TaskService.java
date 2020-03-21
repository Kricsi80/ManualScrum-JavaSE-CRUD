package services;

import java.util.List;
import javax.persistence.EntityManager;
import models.Sprint;
import models.Task;
import org.json.JSONArray;
import org.json.JSONObject;

public class TaskService {

    public JSONObject getTaskById(EntityManager em, int taskId) {
        
        Task task = Task.getTaskById(em, taskId);
        JSONObject result = new JSONObject();
        
        // dokumentáció 1.1
        if (task.getId() == null) {
            
            result.put("msg", "Tere is no Task with this id");
            
        } else {
            
            result = task.toJson();
            
        }
        
        return result;
    }

    public JSONArray getAllTasksBySprintId(EntityManager em, int sprintId) {
        
        JSONArray results = new JSONArray();
        //ellenőrzés, hogy van-e ilyen sprint
        if (Sprint.getSprintById(em, sprintId).getId() == null) {
            
            JSONObject message = new JSONObject();
            message.put("msg", "There is no Sprint with this id");
            results.put(message);
            
        } else {
            
            List<Task> tasks = Task.getAllTasksBySprintId(em, sprintId);
            for (Task task : tasks) {
                results.put(task.toJson());
            }
        }

        return results;
    }

    public boolean createNewTask(EntityManager em, String title, String description, int sprintId) {
        
        //ellenőrzés, hogy van-e ilyen sprint
        if (Sprint.getSprintById(em, sprintId).getId() == null) {
            
            return false;
            
        } else {
            
            return Task.createNewTask(em, title, description, sprintId);
            
        }
    }

    public boolean deleteTaskById(EntityManager em, int id) {
        
        //ellenőrzés, hogy van-e ilyen task
        if (Task.getTaskById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
            return Task.deleteTaskById(em, id);
            
        }
    }

    public boolean deleteAllTaskBySprintId(EntityManager em, int id) {
        
        //ellenőrzés, hogy van-e ilyen sprint
        if (Sprint.getSprintById(em, id).getId() == null) {
            
            return false;
            
        } else {
            
            return Task.deleteAllTaskBySprintId(em, id);
        }

    }

    public boolean updateTaskById(EntityManager em, Integer id, String title, String description, int position, int sprintId) {
        
        //ellenőrzés, hogy van-e ilyen task
        if (Task.getTaskById(em, id).getId() == null) {
            
            return false;
            
        //ellenőrzés ha olyan értékat akarunk adni, amit nem lehet
        } else if (position < 0 || position > 3) {
            
            return false;
            
        // 3-as a finished pozíció. Finishedet nem lehet újra megváltoztatni
        } else if (Task.getTaskById(em, id).getPosition() == 3) {
            
            return false;
            
        } else {
            
            try {
                Task task = em.find(Task.class, id);

                em.getTransaction().begin();

                task.setTitle(title);
                task.setDescription(description);
                task.setPosition(position);
                task.setSprintId(new Sprint(sprintId));
                
                em.getTransaction().commit();

                return true;
                
            } catch (Exception e) {
                
                return false;
                
            }
        }
    }
}
