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
import org.json.JSONObject;

@Entity
@Table(name = "employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e")
    , @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id")
    , @NamedQuery(name = "Employee.findByName", query = "SELECT e FROM Employee e WHERE e.name = :name")})
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Collection<Assignment> assignmentCollection;

    public Employee() {
    }

    public Employee(Integer id) {
        this.id = id;
    }

    public Employee(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        j.put("name", this.name);
        return j;
    }

    public static List<Employee> getAllEmployees(EntityManager em) {
        
        List<Employee> employees = new ArrayList();
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getAllEmployees");

        List<Object[]> resultList = spq.getResultList();

        for (Object[] result : resultList) {
            int id = Integer.parseInt(result[0].toString());
            Employee emplyee = em.find(Employee.class, id);
            employees.add(emplyee);
        }

        return employees;
    }

    public static Employee getEmployeeById(EntityManager em, int employeeId) {
        
        StoredProcedureQuery spq = em.createStoredProcedureQuery("getEmployeeById");
        spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
        spq.setParameter("idIN", employeeId);

        Employee employeeFound = new Employee();

        List<Object[]> resultList = spq.getResultList();

        for (Object[] result : resultList) {
            int id = Integer.parseInt(result[0].toString());
            Employee employee = em.find(Employee.class, id);
            employeeFound = employee;
        }

        return employeeFound;
    }

    public static boolean createNewEmployee(EntityManager em, String name) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("createNewEmployee");
            spq.registerStoredProcedureParameter("nameIN", String.class, ParameterMode.IN);
            spq.setParameter("nameIN", name);
            spq.execute();

            return true;

        } catch (Exception e) {
            
            return false;
            
        }
    }

    public static boolean deleteEmployeeById(EntityManager em, int id) {
        
        try {
            StoredProcedureQuery spq = em.createStoredProcedureQuery("deleteEmployeeById");
            spq.registerStoredProcedureParameter("idIN", Integer.class, ParameterMode.IN);
            spq.setParameter("idIN", id);
            spq.execute();

            return true;

        } catch (Exception e) {
            
            return false;            
        }
    }
    
    //összetett fv    
    public static JSONObject getEmployeeStatisticsById(EntityManager em, int id){
        
        JSONObject statistics = new JSONObject();
        
        //get employee
        Employee employee = Employee.getEmployeeById(em, id);       
        
        //employee összes assignmentje
        List<Assignment> assignments = Assignment.getAllAssignmentByEmployeeId(em, id);
              
        // ha nincs neki
        if (assignments.isEmpty()) {
            
            statistics.put("msg", "This employee has no assignments");
            
        } else {
            
            //mennyi van neki összesen / mennyi finished
            int allAssignments = assignments.size();
            int finishedAssignments = 0;
            
            //segédváltozó ahhoz, h az employee hány projektben dolgozik
            ArrayList<Integer> projects = new ArrayList();

            for (Assignment assignment : assignments) {

                //foreign keyek castolása
                int taskId = Integer.parseInt(assignment.getTaskId().toString());
                int sprintId = Integer.parseInt(Task.getTaskById(em, taskId).getSprintId().toString());
                int projectId = Integer.parseInt(Sprint.getSprintById(em, sprintId).getProjectId().toString());
                
                //segédváltozó, hogy kiszűrjük, hogy ugyanaz a projekt többször is bekerüljön a listába
                boolean hasProjectAlreadyInProjects = false;

                //megnézni, h van-e már ilyen id-jú proj a projects-ben
                for (int i = 0; i< projects.size(); i++) {
                    if (projects.get(i) == projectId)
                        hasProjectAlreadyInProjects = true;
                }
                //ha nincs, hozzáadni
                if (!hasProjectAlreadyInProjects) {
                    projects.add(projectId);
                }
                //finishedet növelni
                if (Task.getTaskById(em, taskId).getPosition() == 3) {
                    finishedAssignments ++;
                }
            }
        
            Double ratio;
            
            //kiküszöbölni a 0-val való osztást
            if (allAssignments == 0) {
                ratio = 0.0;
            } else {
                ratio = (double)finishedAssignments / allAssignments;
            }

            //feltöülteni a JSON-t
            statistics.put("name", employee.getName());
            statistics.put("totalTasks", allAssignments);
            statistics.put("finishedTasks", finishedAssignments);
            statistics.put("projectCount", projects.size());
            statistics.put("ratio", ratio);
        }

        return statistics;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
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