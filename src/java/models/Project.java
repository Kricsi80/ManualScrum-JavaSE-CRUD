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
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@Table(name = "project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p")
    , @NamedQuery(name = "Project.findById", query = "SELECT p FROM Project p WHERE p.id = :id")
    , @NamedQuery(name = "Project.findByTitle", query = "SELECT p FROM Project p WHERE p.title = :title")})
public class Project implements Serializable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectId")
    private Collection<Sprint> sprintCollection;

    public Project() {
    }

    public Project(Integer id) {
        this.id = id;
    }

    public Project(Integer id, String title) {
        this.id = id;
        this.title = title;
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

    @XmlTransient
    public Collection<Sprint> getSprintCollection() {
        return sprintCollection;
    }

    public void setSprintCollection(Collection<Sprint> sprintCollection) {
        this.sprintCollection = sprintCollection;
    }

    //START of own code
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("id", this.id);
        j.put("title", this.title);
        return j;
    }

    public static List<Project> getAllProjects(EntityManager em) {
        
        List<Project> projects = new ArrayList();
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getAllProjects");

        List<Object[]> resultList = spq.getResultList();

        for (Object[] result : resultList) {
            int id = Integer.parseInt(result[0].toString());
            Project project = em.find(Project.class, id);
            projects.add(project);
        }

        return projects;
    }

    public static Project getProjectById(EntityManager em, int projectId) {
        
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getProjectById");
        spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
        spq.setParameter("idIN", projectId);

        Project projFound = new Project();

        List<Object[]> resultList = spq.getResultList();

        for (Object[] result : resultList) {
            int id = Integer.parseInt(result[0].toString());
            Project project = em.find(Project.class, id);
            projFound = project;
        }

        return projFound;
    }

    public static boolean createNewProject(EntityManager em, String title) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("createNewProject");
            spq.registerStoredProcedureParameter("titleIN", String.class, ParameterMode.IN);
            spq.setParameter("titleIN", title);
            spq.execute();

            return true;

        } catch (Exception e) {
            
            return false;
        }
    }
    

    public static boolean deleteProjectById(EntityManager em, int id) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("deleteProjectById");
            spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("idIN", id);
            spq.execute();

            return true;

        } catch (Exception e) {
            
            return false;
        }
    }
    
    //összetett fv
    public static JSONObject getProjectStatisticsById(EntityManager em, int id){
        
        JSONObject statistics = new JSONObject();
                   
        List<Sprint> sprintList = Sprint.getAllSprintByProjectId(em, id);
        
        //összes és finished taskok a projectben
        int numberOfTasksInProject = 0;
        int numberOfFinishedTasksInProject = 0;
        
        //megszámolni a taskokat
        for (Sprint sprint : sprintList) {
            for (Task task : Task.getAllTasksBySprintId(em, sprint.getId())) {
                numberOfTasksInProject ++;
                if (task.getPosition() == 3) {
                    numberOfFinishedTasksInProject ++;
                }
            }            
        }
        
        //segédváltozók az arányokhoz
        Double taskRatio;
        Double taskPerSprint;
        Double finishedTaskPerSprint;
        
        //kiküszöbölni a 0-val való osztást        
        if (numberOfTasksInProject == 0) {
            taskRatio = 0.0;
        } else {
            taskRatio = (double)numberOfFinishedTasksInProject / numberOfTasksInProject;
        }
        
        if (sprintList.isEmpty()) {
            taskPerSprint = 0.0;
            finishedTaskPerSprint = 0.0;
        } else {
            taskPerSprint = (double) numberOfTasksInProject / sprintList.size();
            finishedTaskPerSprint = (double) numberOfFinishedTasksInProject / sprintList.size();            
        }
        
        statistics.put("id", id);
        statistics.put("sprintsInProject", sprintList.size());
        statistics.put("tasksInProject", numberOfTasksInProject);
        statistics.put("finishedTasksInProject", numberOfFinishedTasksInProject);
        statistics.put("finishedTasksPerAllTasksRatio", taskRatio);
        statistics.put("tasksPerSprint", taskPerSprint);
        statistics.put("finishedTaskPerSprint", finishedTaskPerSprint);
        
        return statistics;
    }
    
    public static JSONObject validateProjectById(EntityManager em, int id) {
        JSONObject result = new JSONObject();
        JSONArray sprintsResults = new JSONArray();
        
        List<Sprint> sprintList = Sprint.getAllSprintByProjectId(em, id);
        
        
        for (Sprint sprint : sprintList) {
            sprintsResults.put(Sprint.validateSprintById(em, sprint.getId()));
        }
        
        
        //megkeresni a legfiatalabb sprintet
        Sprint lastSprint = sprintList.get(0);
        for (int i = 0; i < sprintList.size(); i++) {
            
            
            //sprint régebbi, mint a lastSprint
            if (sprintList.get(i).getCreatedAt().compareTo(lastSprint.getCreatedAt()) > 0 ) {
                lastSprint = sprintList.get(i);
            }                        
        }    
        
        //legfiatalabbat kivenni a listából
        sprintList.remove(lastSprint);
        
        //megnézni, h a maradék le van-e zárva
        for (Sprint sprint : sprintList) {
            if (!sprint.getIsFinished()) {
                result.put("project-invalid", "Unfinished sprint id: " + sprint.getId() + "found!");
            }
        }
        
        //ha eddig minden ok, akkor a JSONObject üres
        if (result.length() == 0) {
            result.put("project_valid", "project id: " + id + " is valid.");
        }
        
        result.put("sprints", sprintsResults);
        
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
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
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