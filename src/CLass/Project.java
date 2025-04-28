package CLass;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {
    private static final long serialVersionUID = 1L; 
    private String title ; 
    private String description ; 
    private ArrayList<Task> tasks ;

    public Project(String title, String description) {
        this.title = title;
        this.description = description;
        tasks = new ArrayList<>() ; 
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
    public ArrayList<Task>  get_tasks(){
        return tasks ; 
    }
    public
    void addtask(Task task){
        tasks.add(task) ; 
    }
    Task get_task(int index){
        return tasks.get(index) ; 
    }
    void update_tasks(ArrayList<Task> lists){
        tasks = lists ; 
    }
    @Override
    public String toString() {
        return "Project{title='" + title + "', description='" + description + "', tasks=" + tasks + "}";
    }
}