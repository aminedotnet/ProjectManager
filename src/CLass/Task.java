package CLass;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class Task implements Serializable { 
    private static final long serialVersionUID = 1L;  
    private String title ; 
    private String description ; 
    private LocalDate StartDate ; 
    private LocalDate EndDate ; 
    private String status  ; 
    private String assignedTo ; 
    private transient Period Duration ; 
    
    public Task (String title, String description, LocalDate StartDate, LocalDate EndDate, String assignedTo, String status){
        this.title = title ; 
        this.status = status ; 
        this.description = description ; 
        this.assignedTo = assignedTo ; 
        this.StartDate = StartDate ; 
        this.EndDate = EndDate ; 
        Duration = Period.between(StartDate, EndDate);
    }

    public int getDuration() {
        if (Duration == null) {  
            Duration = Period.between(StartDate, EndDate); 
        }
        return Duration.getYears()*365 + Duration.getMonths()*30 + Duration.getDays() ;  
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

    public LocalDate getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDate startDate) {
        StartDate = startDate;

        Duration = Period.between(StartDate, EndDate);
    }

    public LocalDate getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDate endDate) {
        EndDate = endDate;

        Duration = Period.between(StartDate, EndDate);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    @Override
    public String toString() {
        return "Task{title='" + title + "', assignedTo='" + assignedTo + "', status='" + status + "'}";
    }
}
