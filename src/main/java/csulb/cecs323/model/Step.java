package csulb.cecs323.model;
/*
SOURCES:
Annotation and class creation from Professor Brown CarClub Lab at | https://github.com/DavidMBrown091380/cecs323-jpa-car-club
Additional help from lab video at | https://csulb-my.sharepoint.com/personal/david_brown_csulb_edu/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Fdavid%5Fbrown%5Fcsulb%5Fedu%2FDocuments%2FCECS%20323%2FLabs%2FCECS%20323%20Lab%20JPA%20Car%20Club%20Intro%2Emp4&parent=%2Fpersonal%2Fdavid%5Fbrown%5Fcsulb%5Fedu%2FDocuments%2FCECS%20323%2FLabs&originalPath=aHR0cHM6Ly9jc3VsYi1teS5zaGFyZXBvaW50LmNvbS86djovZy9wZXJzb25hbC9kYXZpZF9icm93bl9jc3VsYl9lZHUvRVN0UzVGVFd0cUZKdFd1WEstY1cxcFFCMWh1clRjS0NPQTRpa2RlWUVXM2dEQT9ydGltZT1EY2ZQRlhVRTJVZw
Finally annotations for joining columns at | https://www.baeldung.com/jpa-join-column
 */
import javax.persistence.*;

/**
 * The class of step within a recipe which contain its order, description, and time for it
 * The only aggregate connection with recipe require a recipe to exist for an object of this class to exist
 */

@Embeddable
@NamedNativeQuery(
        name = "Return_Step",
        query = "SELECT * FROM STEP",
        resultClass = Step.class
)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"orderNumber","description"})})
public class Step {


    @Column(name = "order_number",nullable = false)
    private int orderNumber;

    @Column(length = 500)
    private String description;

    private int time;

    /**
     * Empty constructor for the Step class if no values were inputed
     */
    public Step(){}

    /**
     * The constructor for step which take in the order number of it, the description for what to do, and the time it takes
     * @param orderNumber integer value for order that the current step object exist in recipe as
     * @param description the String value for description of what needed to be done in this step
     * @param time the LocalTime value for amount of time this step required
     */
    public Step(int orderNumber, String description, int time) {
        this.orderNumber = orderNumber;
        this.description = description;
        this.time = time;
    }

    /**
     * get the order number of the step existence within a recipe
     * @return an integer value that the determine the order of the step within a recipe
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * set the order number of step existence within a recipe
     * @param orderNumber the inputted value of integer that determine the of the step within a recipe
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * get the description of the action require to take in step
     * @return an String value that describe the action to take of the step
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description of the action require to take in the step
     * @param description the inputted value of String that describe the action to take for the step
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the LocalTime object that determine the time required for the step
     * @return a LocalTime value that contain the time required to complete the current step
     */
    public int getTime() {
        return time;
    }

    /**
     * set the LocalTime object that determine the time require for the step
     * @param time a LocalTime value that contain the time to be set for the time require for this step
     */
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    /**
     * The string format for the step if it is used as a string
     */
    public String toString(){
        return String.format("Step[number = %d, description = %s, time = %s]", orderNumber, description, time);
    }

}