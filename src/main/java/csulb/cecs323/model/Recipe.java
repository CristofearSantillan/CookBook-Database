package csulb.cecs323.model;
/*
SOURCES:
Annotation and class creation from Professor Brown CarClub Lab at | https://github.com/DavidMBrown091380/cecs323-jpa-car-club
Additional help from lab video at | https://csulb-my.sharepoint.com/personal/david_brown_csulb_edu/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Fdavid%5Fbrown%5Fcsulb%5Fedu%2FDocuments%2FCECS%20323%2FLabs%2FCECS%20323%20Lab%20JPA%20Car%20Club%20Intro%2Emp4&parent=%2Fpersonal%2Fdavid%5Fbrown%5Fcsulb%5Fedu%2FDocuments%2FCECS%20323%2FLabs&originalPath=aHR0cHM6Ly9jc3VsYi1teS5zaGFyZXBvaW50LmNvbS86djovZy9wZXJzb25hbC9kYXZpZF9icm93bl9jc3VsYl9lZHUvRVN0UzVGVFd0cUZKdFd1WEstY1cxcFFCMWh1clRjS0NPQTRpa2RlWUVXM2dEQT9ydGltZT1EY2ZQRlhVRTJVZw
Finally annotations for joining columns at | https://www.baeldung.com/jpa-join-column
*/
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/** Recipe class which contain the the information of name, time, description, difficulty mand serving of a recipe
 *  Furthermore, the connections to recipe contain Reviews, Steps, Chef, cuisine and ingredient amount
 *  All these different connections act as foreign keys to access classes that have connections to it
 */
@Entity
@NamedNativeQuery(
        name = "ReturnRecipe",
        query = "SELECT * FROM RECIPE",
        resultClass = Recipe.class
)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","description"})})
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private long recipeId;

    @OneToMany( mappedBy = "recipe", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "review_id")
    private Set<Review> reviewSet;

    @ElementCollection
    @CollectionTable(name = "recipe_step", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Step> stepList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_id")
    private Chef chefCreator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuisine_id")
    private Cuisine cuisine;

    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "ingredient_amount")
    private Set<IngredientAmount> ingredientAmounts;

    @Column(nullable = false, length = 17)
    private String name;

    @Column(nullable = false, length = 400)
    private String description;

    @Column(name = "prep_time", nullable = false)
    private int prepTime;

    @Column(name = "cook_time", nullable = false)
    private int cookTime;

    @Column(name = "difficulty_rating", nullable = false)
    private int difficultyRating;

    @Column(name = "number_of_serving", nullable = false)
    private int numberOfServings;

    /**
     * empty constructor to account for connected attributes is no beginning arguments were given
     */
    public Recipe() {
        this.reviewSet = new HashSet<>();
        this.stepList = new ArrayList<>();
        this.ingredientAmounts = new HashSet<>();
    }

    /**
     * Constructor which account for setting the values of the objects and the list of reviews, steps, and ingredient amount upon construction
     * @param name name of the recipe
     * @param description description of the origin, taste, smell, and values behind the recipe
     * @param prepTime the time require to prepare for the recipe
     * @param cookTime the time require to cook the recipe
     * @param difficultyRating rating of difficulty for the recipe
     * @param numberOfServings servings of recipe once completed
     */
    public Recipe(String name, String description, int prepTime, int cookTime, int difficultyRating, int numberOfServings) {
        this.name = name;
        this.description = description;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.difficultyRating = difficultyRating;
        this.numberOfServings = numberOfServings;
        this.reviewSet = new HashSet<>();
        this.stepList = new ArrayList<>();
        this.ingredientAmounts = new HashSet<>();
    }

    /**
     * get the id surrogate of the recipe
     * @return id of the recipe in long
     */
    public long getRecipeId() {
        return recipeId;
    }

    /**
     * get the name of recipe
     * @return name of recipe in String format
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the recipe
     * @param name the input of String for setting the name of the recipe
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get the origin, taste, smell, and values description of the recipe
     * @return the description of the recipe in String format
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the origin, taste, smell, and values description of the recipe
     * @param description the input of String into the description of the recipe
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the amount of time required to prepare the recipe
     * @return the amount of time required for recipe to be prepared in Local Time
     */
    public int getPrepTime() {
        return prepTime;
    }

    /**
     * Set the amount of time required to prepare the recipe
     * @param prepTime the input of LocalTime for the amount of time required to prepare the recipe
     */
    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    /**
     * get the amount of time required to cook the recipe
     * @return the amount of time required for recipe to be cooked in Local Time
     */
    public int getCookTime() {
        return cookTime;
    }

    /**
     * set the amount of time required to cook the recipe
     * @param cookTime the input of LocalTime for the amount of time required to cook the recipe
     */
    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    /**
     * get the difficulty rating of the recipe
     * @return the difficulty rating of the recipe in integer
     */
    public int getDifficultyRating() {
        return difficultyRating;
    }

    /**
     * set the difficulty rating of the recipe in integer
     * @param difficultyRating the inputted value in integer for difficulty rating
     */
    public void setDifficultyRating(int difficultyRating) {
        this.difficultyRating = difficultyRating;
    }

    /**
     * get the number of serving from completing the recipe
     * @return the number of serving from completing the recipe in integer
     */
    public int getNumberOfServings() {
        return numberOfServings;
    }

    /**
     * set the number of serving from completing the recipe in integer
     * @param numberOfServings the inputted values in integer for the number of servings after completing the recipe
     */
    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    /**
     * get the chef that is making the recipe
     * @return the Chef object that make this recipe
     */
    public Chef getChefCreator() {
        return chefCreator;
    }

    /**
     * set the chef object that is making the recipe
     * @param chef the inputted value of Chef that makes the recipe
     */
    public void setChefCreator(Chef chef) {
        this.chefCreator = chef;
    }

    /**
     * get the cuisine object that this recipe belong to
     * @return the cuisine object that the recipe belong to
     */
    public Cuisine getCuisine() {
        return cuisine;
    }

    /**
     * set the cuisine that the recipe belong to
     * @param cuisine the inputted value of cuisine object to set the cuisine object within the recipe
     */
    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    /**
     * get the set of ingredients and its amount for the current recipe
     * @return the the set of ingredientsAmount object which contain the ingredients and the amounts required for the recipe
     */
    public Set<IngredientAmount> getIngredientAmounts() {return ingredientAmounts;}

    /**
     * add a singular object of ingredient and its amount to the set of ingredient amount existing within the recipe
     * @param ingredientAmount the value of ingredientAmount that contain the ingredient and amounts for the recipe
     */
    public void addIngredientAmount(IngredientAmount ingredientAmount){
        this.ingredientAmounts.add(ingredientAmount);
    }

    /**
     * remove a singular object of IngredientAmount from the set of ingredients and amounts of the recipe
     * @param ingredientAmount the inputted IngredientAmount value to be removed from the set
     */
    public void removeIngredientAmount(IngredientAmount ingredientAmount){
        this.ingredientAmounts.remove(ingredientAmount);
        ingredientAmount.setRecipe(null);
    }

    /**
     * get the set of reviews of the current recipe
     * @return a set of object type Review of the recipe
     */
    public Set<Review> getReviewSet() {return reviewSet;}

    /**
     * add a singular value of Review into the set of reviews of the recipe
     * @param review the inputted Review value to be added into the set of reviews of the recipe
     */
    public void addReview(Review review){
        this.reviewSet.add(review);
    }

    /**
     * remove a singular value of Review from the set of reviews of the recipe
     * @param review the inputted Review value to be removed from the set of reviews of the recipe
     */
    public void removeReview(Review review){
        this.reviewSet.remove(review);
        review.setRecipe(null);
    }

    /**
     * get the set of Step object of the recipe
     * @return a set of steps for following the recipe
     */
    public List<Step> getStepList() {return stepList;}

    /**
     * add the step of the recipe into the set of steps within the recipe
     * @param step the value of step for adding into the set of steps of the recipe
     */
    public void addStep(Step step){
        this.stepList.add(step);
    }

    /**
     * remove the step from the set of steps within the recipe
     * @param step the value of step for removing from the set of steps of the recipe
     */
    public void removeStep(Step step){
        this.stepList.remove(step);
    }

    @Override
    /**
     * The string format for the recipe if it is used as a string
     */
    public String toString(){
        return String.format("Recipe[id = %d, name = %s, description = %s, prepTime = %s, cookTime = %s, rating = %d, servings = %d]", recipeId, name, description, prepTime, cookTime, difficultyRating, numberOfServings);
    }

}