package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.json.JSONObject;
import org.json.JSONArray;
import services.TaskService;

@Entity
@Table(name = "sprint")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sprint.findAll", query = "SELECT s FROM Sprint s")
    , @NamedQuery(name = "Sprint.findById", query = "SELECT s FROM Sprint s WHERE s.id = :id")
    , @NamedQuery(name = "Sprint.findByIsFinished", query = "SELECT s FROM Sprint s WHERE s.isFinished = :isFinished")
    , @NamedQuery(name = "Sprint.findByCreatedAt", query = "SELECT s FROM Sprint s WHERE s.createdAt = :createdAt")})
public class Sprint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_finished")
    private boolean isFinished;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sprintId")
    private Collection<Task> taskCollection;
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Project projectId;

    public Sprint() {
    }

    public Sprint(Integer id) {
        this.id = id;
    }

    public Sprint(Integer id, boolean isFinished, Date createdAt) {
        this.id = id;
        this.isFinished = isFinished;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Collection<Task> getTaskCollection() {
        return taskCollection;
    }

    public void setTaskCollection(Collection<Task> taskCollection) {
        this.taskCollection = taskCollection;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    //START of own code
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("id", this.id);
        j.put("isFinished", this.isFinished);
        j.put("createdAt", this.createdAt);
        
        // dokumentáció
        j.put("projectId", Integer.parseInt(this.projectId.toString()));
        return j;
    }

    public static List<Sprint> getAllSprintByProjectId(EntityManager em, int Projectid) {
        
        List<Sprint> sprints = new ArrayList();
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getAllSprintByProjectId");
        spq.registerStoredProcedureParameter("project_idIN", Integer.class, ParameterMode.IN);
        spq.setParameter("project_idIN", Projectid);

        List<Object[]> resultList = spq.getResultList();

        for (Object[] result : resultList) {
            int id = Integer.parseInt(result[0].toString());
            Sprint sprint = em.find(Sprint.class, id);
            sprints.add(sprint);

        }

        return sprints;
    }

    public static Sprint getSprintById(EntityManager em, int sprintId) {
        
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getSprintById");
        spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
        spq.setParameter("idIN", sprintId);

        Sprint sprintFound = new Sprint();

        List<Object[]> resultList = spq.getResultList();

        for (Object[] result : resultList) {
            int id = Integer.parseInt(result[0].toString());
            Sprint sprint = em.find(Sprint.class, id);
            sprintFound = sprint;

        }
        
        return sprintFound;
    }
    
    //öszetett függvény    
    public static boolean createNewSprintByProjectId(EntityManager em, int id) {
        
        //segédváltozó, h bármilyen hiba esetén  ki lehessen lépni
        boolean isCreateSuccesful = true;
        
        // összes sprint a sprint projektjében
        List<Sprint> sprintList = Sprint.getAllSprintByProjectId(em, id);
        
        //segédváltozó: utolsó sprint
        Sprint lastSprint = sprintList.get(0);
        
        //megnézni, h minden sprint ebben a projektben  már le lett-e zárva
        //sima for, h ki lehessen brake-elni
        for (int i = 0; i < sprintList.size(); i++) {
            
            //ha nincs minden sprint lezárva
            if (sprintList.get(i).getIsFinished() == false) {
                isCreateSuccesful = false;
                break;
            }
            
            //utolsó sprint megkeresése
            //sprint régebbi, mint a lastSprint
            if (sprintList.get(i).getCreatedAt().compareTo(lastSprint.getCreatedAt()) > 0 ) {
                lastSprint = sprintList.get(i);
            }                        
        }        
        
        //ha nincs minden sprint lezárva, akkor ne is próbáljon új sprintet létrehozni
        if (isCreateSuccesful) {
        //létrehozni az új sprintet
            try {
                StoredProcedureQuery spq = em.createStoredProcedureQuery("createNewSprint");
                spq.registerStoredProcedureParameter("project_idIN", Integer.class, ParameterMode.IN);
                spq.setParameter("project_idIN", id);
                spq.execute();
                
            } catch (Exception e) {

                isCreateSuccesful = false;
            }        
            
        }
        
        //ha nem sikerült létrehozni az új sprintet, csak akkor ne is mozgassa át a taskokat
        if (isCreateSuccesful) {
                
            //Új lista az újonnan create-elt Sprinttel
            List<Sprint> newSprintList = Sprint.getAllSprintByProjectId(em, id);
            //újból eltávolítjuk, ami a régiben már benne volt
            newSprintList.removeAll(sprintList);
            int newSprintId = newSprintList.get(0).getId();
            
            TaskService ts = new TaskService();
            
            //minden nem 3-as (nem befejezett) task sprintid-ját átírni az új sprint id-jára, igy ott azonnal elérhetők
            for (Task taskInLastSprint : Task.getAllTasksBySprintId(em, lastSprint.getId())) {
                if (taskInLastSprint.getPosition() != 3) {
                    ts.updateTaskById(em, taskInLastSprint.getId(), taskInLastSprint.getTitle(), taskInLastSprint.getTitle(), 0, newSprintId);
                }
            }                
        }
        
        return isCreateSuccesful;
    }

    public static boolean deleteSprintById(EntityManager em, int id) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("deleteSprintById");
            spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("idIN", id);
            spq.execute();

            return true;

        } catch (Exception e) {
            
            return false;            
        }
    }

    public static boolean deleteAllSprintByProjectId(EntityManager em, int id) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("deleteAllSprintByProjectId");
            spq.registerStoredProcedureParameter("project_idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("project_idIN", id);
            spq.execute();

            return true;

        } catch (Exception e) {
            
            return false;            
        }
    }
    
    public static boolean finishSprint(EntityManager em, int id){
        
        try {
        //összes taskot bekérni
        List<Task> taskList = Task.getAllTasksBySprintId(em, id);
        TaskService ts = new TaskService();
        
        //ha nem finished a task, akkor átírni 0-ra       
        for (Task task : taskList) {
            //ha nem finished a task, akkor átírni 0-ra 
            if (task.getPosition() != 3) {                
                ts.updateTaskById(em, task.getId(), task.getTitle(), task.getTitle(), 0, task.getSprintId().getId());
            }
        }
            
        return true;
        
        } catch (Exception e) {
            
            return false;
        }
    }
    
    public static JSONObject validateSprintById(EntityManager em, int id) {
        
        JSONObject result = new JSONObject();        
        JSONArray tasksResults = new JSONArray();
        
        List<Task> taskList = Task.getAllTasksBySprintId(em, id);
        
        //összes taskkot ebben a sprntben validálni
        for (Task task : taskList) {
            
            tasksResults.put(Task.validateTaskById(em, task.getId()));
        }
        
        boolean isFinished = Sprint.getSprintById(em, id).getIsFinished();
        //ha a sprint már kész van csak finished taskok lehetnek benne, ezt ellenőrizni
        if (!isFinished) {
            for (Task task : taskList) {
                //ha van nem finished taks
                if(task.getPosition() != 3){
                result.put("sprint_invalid", "unfinished task id: " + task.getId() + " found!");
                }
                
            }
        } else {
            result.put("sprint_valid", "sprint id: " + id + " is valid.");
        }
        
        result.put("tasks", tasksResults);
        
        return result;
    }

    //END of own code
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sprint)) {
            return false;
        }
        Sprint other = (Sprint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }

}