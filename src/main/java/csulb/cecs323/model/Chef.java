package csulb.cecs323.model;
/*
SOURCES:
This website help me understand how to create a subclass for Chef: https://en.wikibooks.org/wiki/Java_Persistence/Inheritance
This was used to get more knowledge on Many-To-Many relationship: https://www.infoworld.com/article/3387643/java-persistence-with-jpa-and-hibernate-part-2-many-to-many-relationships.html
*/
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Chef class represents a subclass of User and a person with expertise in the culinary field.
 * A Chef creates many recipes based on different cultures and its recipe is judged by many food critics.
 */
@Entity
@DiscriminatorValue("Chef")
@PrimaryKeyJoinColumn(name = "chef_id")
public class Chef extends User{

    @Column(name = "years_of_experience")
    private int yearsOfExperience;

    @ManyToMany
    @JoinTable(
        name = "chef_cuisine",
        joinColumns = @JoinColumn(name = "chef_id", referencedColumnName = "chef_id"),
        inverseJoinColumns = @JoinColumn(name = "cuisine_id", referencedColumnName = "cuisine_id")
    )
    private Set<Cuisine> familiarCuisines;

    @OneToMany(mappedBy = "chefCreator", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Recipe> recipesProduced;

    /**
     * No-arg Constructor: sets each attribute inside of Chef to null and initializes Sets. In addition,
     *                     it also calls the User no-arg constructor by "super()"
     */
    public Chef() {
        super();
        this.familiarCuisines = new HashSet<>();
        this.recipesProduced = new HashSet<>();
    }

    /**
     * Arg Constructor: sets each attribute with the corresponding argument value for Chef and initializes Sets.
     *                  Also, calls User's arg constructor to initialize the Chef's name, username, password, etc.
     * @param firstName  the chef's first name
     * @param lastName   the chef's last name
     * @param username   chef's username to login
     * @param password   chef's password to have access to his account
     * @param email      chef's email address
     * @param dateRegistered   the exact date that the chef has registered into the system
     * @param yearsOfExperience   chef's amount of experience in the culinary field
     */
    public Chef(String firstName, String lastName, String username, String password, String email, LocalDateTime dateRegistered, int yearsOfExperience) {
        super(firstName, lastName, username, password, email, dateRegistered);
        this.yearsOfExperience = yearsOfExperience;
        this.familiarCuisines = new HashSet<>();
        this.recipesProduced = new HashSet<>();
    }

    /**
     * Obtains the amount of years the chef have worked in cooking
     * @return  the years that the chef has experienced in cooking
     */
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    /**
     * Sets the chef's years of experience in cooking
     * @param yearsOfExperience   the years that the chef had cooked thus far
     */
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    /**
     * Obtains the different cuisines that the chef is expert in
     * @return  the list of cuisines that the chef has
     */
    public Set<Cuisine> getFamiliarCuisines(){
        return familiarCuisines;
    }

    /**
     * Adds a new cuisine that the chef have familiarized with into the chef's cuisines Set
     * and the specific cuisine entity will also add the chef that is associated with
     * @param cuisine   the new cuisine that will be added into the cuisine Set
     */
    public void addCuisine(Cuisine cuisine){
        this.familiarCuisines.add(cuisine);
        cuisine.getExpertChefs().add(this);
    }

    /**
     * Removes a specific cuisine from the chef's cuisines Set
     * and the specific cuisine entity will also remove the chef from its association
     * @param cuisine   the new cuisine that will be added into the cuisine Set
     */
    public void removeCuisine(Cuisine cuisine){
        this.familiarCuisines.remove(cuisine);
        cuisine.getExpertChefs().remove(this);
    }

    /**
     * Obtains the recipes that the chef has created for a cuisine
     * @return  the recipe list of the current chef
     */
    public Set<Recipe> getRecipesProduced(){
        return recipesProduced;
    }

    /**
     * Adds a new recipe that the chef has recently made
     * and that specific recipe entity will have an assigned chef
     * @param recipe   the new recipe that was made
     */
    public void addRecipe(Recipe recipe){
        this.recipesProduced.add(recipe);
        recipe.setChefCreator(this);
    }

    /**
     * Removes a specific recipe from the chef's recipe list
     * and that specific recipe entity sets their chef to null
     * @param recipe  the recipe that will be removed from the recipe list
     */
    public void removeRecipe(Recipe recipe){
        this.recipesProduced.remove(recipe);
        recipe.setChefCreator(null);
    }

    /**
     * Prints a description of a chef entity with their corresponding attribute value
     * @return  a formatted string with the values of each chef's attributes
     */
    @Override
    public String toString(){
        return super.toString() + "\nChef's years of experience in his field: " + yearsOfExperience;
    }

}
