package csulb.cecs323.model;
/*
SOURCES:
javadoc source used to understand how to properly implement javadoc comments: https://en.wikipedia.org/wiki/Javadoc
JPA OneToMany: https://en.wikibooks.org/wiki/Java_Persistence/OneToMany
 */
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * FoodCritic class represents a critic who makes a review on a specific recipe
 * A food critic is connected to review and users by foreign keys user id and review id
 */
@Entity
@DiscriminatorValue("FoodCritic")
@PrimaryKeyJoinColumn(name = "critic_id")
public class FoodCritic extends User{

    @Column(name = "current_platform")
    private String currentPlatform;

    @Column(name = "number_of_reviews")
    private int numberOfReviews;

    @OneToMany(mappedBy = "foodCritic", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "review_id")
    private Set<Review> submittedReviews;

    /**
     * No-Arg constructor: sets each attribute to null
     *                     calls on User No-Arg constructor via super()
     */
    public FoodCritic(){
        super();
        this.submittedReviews = new HashSet<>();
    }

    /**
     * Arg constructor: sets the attributes with a corresponding value in FoodCritic and initializes sets
     *                  calls User arg constructor to initialize it's attributes
     * @param firstName FoodCritics first name
     * @param lastName  FoodCritics last name
     * @param username  FoodCritics username
     * @param password  FoodCritics password
     * @param email     FoodCritics email
     * @param dateRegistered    date the food critic registered
     * @param currentPlatform   the social platform the food critic uses
     */
    public FoodCritic(String firstName, String lastName, String username, String password, String email, LocalDateTime dateRegistered, String currentPlatform){
        super(firstName, lastName, username, password, email, dateRegistered);
        this.currentPlatform = currentPlatform;
        this.submittedReviews = new HashSet<>();
    }

    /**
     * gets the current platform object
     * @return the object that is the platform the critic is using
     */
    public String getCurrentPlatform() {
        return currentPlatform;
    }

    /**
     * sets the current platform object
     * @param currentPlatform the current platform being used
     */
    public void setCurrentPlatform(String currentPlatform) {
        this.currentPlatform = currentPlatform;
    }

    /**
     * gets the total number of reviews done
     * @return the value of reviews the food critic has done
     */
    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    /**
     * gets the amount of reviews conducted
     * @return the reviews that the food critic has done so far
     */
    public Set<Review> getSubmittedReviews(){ return submittedReviews; }

    /**
     * adds a new review that the food critic has conducted into the review set.
     * @param review the value of reviews done and object of a new review by the food critic
     */
    public void addReview(Review review){
        this.submittedReviews.add(review);
        this.numberOfReviews++;
    }

    /**
     * removes a review that the food critic has conducted
     * @param review removes the value of reviews done as well as the object
     */
    public void removeReview(Review review){
        if(this.submittedReviews.contains(review)){
            this.submittedReviews.remove(review);
            this.numberOfReviews--;
        }
    }
    /**
     * converts the current object to string
     * @return the current object to a string if used as a string object
     */
    @Override
    public String toString(){
        return super.toString() + "\nThis food critic has done " + numberOfReviews + "reviews and is on " + currentPlatform;
    }

}
