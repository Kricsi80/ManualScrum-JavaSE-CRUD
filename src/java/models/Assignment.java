package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;

@Entity
@Table(name = "assignment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Assignment.findAll", query = "SELECT a FROM Assignment a")
    , @NamedQuery(name = "Assignment.findById", query = "SELECT a FROM Assignment a WHERE a.id = :id")})
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Task taskId;

    public Assignment() {
    }

    public Assignment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Task getTaskId() {
        return taskId;
    }

    public void setTaskId(Task taskId) {
        this.taskId = taskId;
    }

   //START of own code
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("id", this.id);
        
        // dokumentáció
        j.put("employeeId", Integer.parseInt(this.employeeId.toString()));
        j.put("taskId", Integer.parseInt(this.taskId.toString()));
        return j;
    }
    
    public static Assignment getAssignmentById(EntityManager em, int id) {
        
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getAssignmentById");
        spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
        spq.setParameter("idIN", id);

        Assignment assignmentFound = new Assignment();

        List<Object[]> resultList = spq.getResultList();

        for (Object[] result : resultList) {
            int assId = Integer.parseInt(result[0].toString());
            Assignment assignment = em.find(Assignment.class, assId);
            assignmentFound = assignment;
        }
        
        return assignmentFound;
    }

    public static List<Assignment> getAllAssignmentByEmployeeId(EntityManager em, int employeeId) {
        
        List<Assignment> assignments = new ArrayList();
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getAllAssignmentByEmployeeId");
        spq.registerStoredProcedureParameter("employee_idIN", Integer.class, ParameterMode.IN);
        spq.setParameter("employee_idIN", employeeId);

        List<Object[]> resulList = spq.getResultList();

        for (Object[] result : resulList) {
            int id = Integer.parseInt(result[0].toString());
            Assignment assignment = em.find(Assignment.class, id);
            assignments.add(assignment);
        }

        return assignments;
    }

    public static List<Assignment> getAllAssignmentByTaskId(EntityManager em, int taskId) {
        
        List<Assignment> assignments = new ArrayList();
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getAllAssignmentByTaskId");
        spq.registerStoredProcedureParameter("task_idIN", Integer.class, ParameterMode.IN);
        spq.setParameter("task_idIN", taskId);

        List<Object[]> resulList = spq.getResultList();

        for (Object[] result : resulList) {
            int id = Integer.parseInt(result[0].toString());
            Assignment assignment = em.find(Assignment.class, id);
            assignments.add(assignment);
        }

        return assignments;
    }

    public static boolean createNewAssignment(EntityManager em, int taskId, int employeeId) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("createNewAssignment");
            spq.registerStoredProcedureParameter("task_idIN", Integer.class, ParameterMode.IN);
            spq.registerStoredProcedureParameter("employee_idIN", Integer.class, ParameterMode.IN);

            spq.setParameter("task_idIN", taskId);
            spq.setParameter("employee_idIN", employeeId);
            spq.execute();
            
            return true;
            
        } catch (Exception ex) {
            
            return false;
        }
    }

    public static boolean deleteAssignmentById(EntityManager em, int id) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("deleteAssignmentById");
            spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("idIN", id);
            spq.execute();
            
            return true;
            
        } catch (Exception ex) {
            
            return false;
        }
    }

    public static boolean deleteAllAssignmentByEmployeeId(EntityManager em, int employeeId) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("deleteAllAssignmentByEmployeeId");
            spq.registerStoredProcedureParameter("employee_idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("employee_idIN", employeeId);
            spq.execute();
            
            return true;
            
        } catch (Exception ex) {
            
            return false;
        }
    }

    public static boolean deleteAllAssignmentByTaskId(EntityManager em, int taskId) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("deleteAllAssignmentByTaskId");
            spq.registerStoredProcedureParameter("task_idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("task_idIN", taskId);
            spq.execute();
            
            return true;
            
        } catch (Exception ex) {
            
            return false;
        }
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
        if (!(object instanceof Assignment)) {
            return false;
        }
        Assignment other = (Assignment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Assignment[ id=" + id + " ]";
    }

}
