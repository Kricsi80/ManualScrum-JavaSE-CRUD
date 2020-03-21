package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.json.JSONObject;

@Entity
@Table(name = "task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t")
    , @NamedQuery(name = "Task.findById", query = "SELECT t FROM Task t WHERE t.id = :id")
    , @NamedQuery(name = "Task.findByTitle", query = "SELECT t FROM Task t WHERE t.title = :title")
    , @NamedQuery(name = "Task.findByDescription", query = "SELECT t FROM Task t WHERE t.description = :description")
    , @NamedQuery(name = "Task.findByPosition", query = "SELECT t FROM Task t WHERE t.position = :position")})
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "position")
    private int position;
    @JoinColumn(name = "sprint_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sprint sprintId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskId")
    private Collection<Assignment> assignmentCollection;

    public Task() {
    }

    public Task(Integer id) {
        this.id = id;
    }

    public Task(Integer id, String title, String description, int position) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Sprint getSprintId() {
        return sprintId;
    }

    public void setSprintId(Sprint sprintId) {
        this.sprintId = sprintId;
    }

    @XmlTransient
    public Collection<Assignment> getAssignmentCollection() {
        return assignmentCollection;
    }

    public void setAssignmentCollection(Collection<Assignment> assignmentCollection) {
        this.assignmentCollection = assignmentCollection;
    }
    
     //START of own code
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("id", this.id);
        j.put("title", this.title);
        j.put("description", this.description);
        j.put("position", this.position);
    
        // dokumentáció
        j.put("sprintId", Integer.parseInt(this.sprintId.toString()));
        return j;
    }

    public static List<Task> getAllTasksBySprintId(EntityManager em, int sprintId) {
        
        List<Task> tasks = new ArrayList();
        
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getAllTasksBySprintId");
        spq.registerStoredProcedureParameter("sprint_idIN", Integer.class, ParameterMode.IN);
        spq.setParameter("sprint_idIN", sprintId);

        List<Object[]> resultList = spq.getResultList();

        for (Object[] result : resultList) {
            int id = Integer.parseInt(result[0].toString());
            Task task = em.find(Task.class, id);
            tasks.add(task);
        }

        return tasks;
    }

    public static Task getTaskById(EntityManager em, int taskId) {
        
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getTaskById");
        spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
        spq.setParameter("idIN", taskId);

        Task taskFound = new Task();

        List<Object[]> resultList = spq.getResultList();

        for (Object[] result : resultList) {
            int id = Integer.parseInt(result[0].toString());
            Task task = em.find(Task.class, id);
            taskFound = task;
        }

        return taskFound;
    }

    public static boolean createNewTask(EntityManager em, String title, String description, int sprintId) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("createNewTask");
            spq.registerStoredProcedureParameter("titleIN", String.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter("descriptionIN", String.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter("sprint_idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("titleIN", title);
            spq.setParameter("descriptionIN", description);
            spq.setParameter("sprint_idIN", sprintId);
            spq.execute();
            
            return true;
            
        } catch (Exception e) {
            
            return false;            
        }
    }

    public static boolean deleteTaskById(EntityManager em, int id) {
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("deleteTaskById");
            spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("idIN", id);
            spq.execute();

            return true;

        } catch (Exception e) {
            
            return false;            
        }
    }

    public static boolean deleteAllTaskBySprintId(EntityManager em, int id) {
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("deleteAllTaskBySprintId");
            spq.registerStoredProcedureParameter("sprint_idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("sprint_idIN", id);
            spq.execute();

            return true;

        } catch (Exception e) {
            
            return false;            
        }
    }
    
    public static JSONObject validateTaskById(EntityManager em, int id){
        
        JSONObject result = new JSONObject();
        
        int position = Task.getTaskById(em, id).getPosition();
        
        if (position >3 || position < 0) {
            result.put("task_invalid", "invalid position found in task id: " + id + "!");
        } else{
        result.put("taks_valid", "task id: " + id + "is valid.");
        }        
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
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
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
