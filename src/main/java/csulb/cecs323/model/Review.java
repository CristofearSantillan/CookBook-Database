package csulb.cecs323.model;
/*
SOURCES:
Annotation and class creation from Professor Brown CarClub Lab at | https://github.com/DavidMBrown091380/cecs323-jpa-car-club
Additional help from lab video at | https://csulb-my.sharepoint.com/personal/david_brown_csulb_edu/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Fdavid%5Fbrown%5Fcsulb%5Fedu%2FDocuments%2FCECS%20323%2FLabs%2FCECS%20323%20Lab%20JPA%20Car%20Club%20Intro%2Emp4&parent=%2Fpersonal%2Fdavid%5Fbrown%5Fcsulb%5Fedu%2FDocuments%2FCECS%20323%2FLabs&originalPath=aHR0cHM6Ly9jc3VsYi1teS5zaGFyZXBvaW50LmNvbS86djovZy9wZXJzb25hbC9kYXZpZF9icm93bl9jc3VsYl9lZHUvRVN0UzVGVFd0cUZKdFd1WEstY1cxcFFCMWh1clRjS0NPQTRpa2RlWUVXM2dEQT9ydGltZT1EY2ZQRlhVRTJVZw
Finally annotations for joining columns at | https://www.baeldung.com/jpa-join-column
*/
import javax.persistence.*;
import java.time.LocalDate;

/**
 * contain the review of a recipe made by a food critic, this review can also be an update of another recipe
 * contain the review, rating, and date completed of the review
 * the connection that this class have with Recipe, FoodCritic, and a RecentReview which exist as foreign keys
 */
@Entity
@NamedNativeQuery(
        name = "ReturnReview",
        query = "SELECT * FROM REVIEW",
        resultClass = Review.class
)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"date_completed","description"})})
public class Review{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "critic_id", nullable = false)
    private FoodCritic foodCritic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @OneToOne
    @JoinColumn(name = "recent_review_id")
    private Review recentReview;

    @Column(name = "date_completed", nullable = false)
    private LocalDate dateCompleted;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false, length = 400)
    private String description;

    /**
     * an empty constructor for the review object
     */
    public Review(){}

    /**
     * Set the value of date completed, rating, and description of the review within construction
     * @param dateCompleted LocalDate value which act as the time the review was completed
     * @param rating float value for rating of the recipe of this review
     * @param description String value for the review description of the recipe
     */
    public Review(LocalDate dateCompleted, float rating, String description, FoodCritic foodCritic, Recipe recipe){
        this.dateCompleted = dateCompleted;
        this.rating = rating;
        this.description = description;
        this.setFoodCritic(foodCritic);
        this.setRecipe(recipe);
    }

    /**
     * get the value of the recipe of this review
     * @return Recipe object that the review is about
     */
    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * set the value of the recipe of this review
     * @param recipe inputted Recipe object that to be set as the recipe of the review
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.addReview(this);
    }

    /**
     * get the food critic that write this review
     * @return FoodCritic object that write this review
     */
    public FoodCritic getFoodCritic() {
        return foodCritic;
    }

    /**
     * set the food critic that write this review
     * @param foodCritic inputted value of FoodCritic to be set within the review object
     */
    public void setFoodCritic(FoodCritic foodCritic) {
        this.foodCritic = foodCritic;
    }

    /**
     * get the recent review of the review if it exist
     * @return the review that precede the current review if it exist
     */
    public Review getRecentReview() {
        return recentReview;
    }

    /**
     * set the recent review of the current review if it exist
     * @param recentReview inputted value of review that to be set as the review that precede the current review
     */
    public void setRecentReview(Review recentReview) {
        this.recentReview = recentReview;
    }

    /**
     * get the LocalDate value of when the review was completed
     * @return a value of LocalDate of when the review was completed
     */
    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    /**
     * set the LocalDate value of when the review was completed
     * @param dateCompleted inputted value of LocalDate to set when the review was completed
     */
    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    /**
     * get the rating value of the review
     * @return a float value which indicating the rating of the review for the recipe
     */
    public float getRating() {
        return rating;
    }

    /**
     * set the rating value of the review
     * @param rating the inputted float value to change the rating value of the review for the recipe
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * get the critique description of the review
     * @return a String which contain the critique of the review made by a food critic
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the critique description of the review
     * @param description the inputted string value to change the critique of a food critic
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    /**
     * The string format for the Review if it is used as a string
     */
    public String toString(){
        return String.format("Review[completed = %s, rating = %.2f, description = %s]", dateCompleted, rating, description);
    }

}