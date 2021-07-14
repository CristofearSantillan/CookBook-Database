package csulb.cecs323.model;
/*
SOURCES:
This website help me understand how to map a One-To-Many relationship between recipe and cuisine: https://en.wikibooks.org/wiki/Java_Persistence/OneToMany
This was used to figure out which is better for any relationship a list or set: https://dzone.com/articles/why-set-is-better-than-list-in-manytomany
*/
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Cuisine class represents a style or culture of cooking
 * A cuisine can be created in my different recipes but some recipes will have different ingredients
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","region"})})
public class Cuisine {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuisine_id")
    private long cuisineId;

    private String name;

    private String region;

    private String religion;

    @ManyToMany(mappedBy = "familiarCuisines")
    private Set<Chef> expertChefs;

    @ManyToMany
    @JoinTable(
        name = "cuisine_ingredient",
        joinColumns = @JoinColumn(name = "cuisine_id", referencedColumnName = "cuisine_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_name", referencedColumnName = "name")
    )
    private Set<Ingredient> ingredientsUsed;

    @OneToMany(mappedBy = "cuisine")
    private Set<Recipe> recipesCreated;

    /**
     * No-arg Constructor: initializes each cuisine attribute to null and creates empty Sets
     */
    public Cuisine() {
        this.expertChefs = new HashSet<>();
        this.ingredientsUsed = new HashSet<>();
        this.recipesCreated = new HashSet<>();
    }

    /**
     * Arg Constructor: initializes cuisine attributes with the given argument values and creates empty Sets
     * @param name   the name of the cuisine
     * @param region   the region this cuisine is from
     * @param religion  the religion that make up this cuisine
     */
    public Cuisine(String name, String region, String religion){
        this.name = name;
        this.region = region;
        this.religion = religion;
        this.expertChefs = new HashSet<>();
        this.ingredientsUsed = new HashSet<>();
        this.recipesCreated = new HashSet<>();
    }

    /**
     * get the id surrogate of the cuisine
     * @return id of the cuisine in long
     */
    public long getCuisineId() {
        return cuisineId;
    }

    /**
     * Obtains the name of the current cuisine
     * @return   the name of the cusine
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the current cuisine
     * @param name  the name to which to switch to for the cuisine name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtains the region that the cuisine is located
     * @return  the region of the current cuisine
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets and replaces the region of the cuisine
     * @param region  the new region to which is going to be set to
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Obtains the religion that the cuisine is influence by
     * @return  the religion of the current cuisine
     */
    public String getReligion() {
        return religion;
    }

    /**
     * Sets a new religion for the current cuisine
     * @param religion  the new religion that will switch the current cuisine religion
     */
    public void setReligion(String religion) {
        this.religion = religion;
    }

    /**
     * Obtains the chefs that are associated with the current cuisine
     * @return  the list of chefs that are familiar with the current cuisine
     */
    public Set<Chef> getExpertChefs(){
        return expertChefs;
    }

    /**
     * Adds a new chef into the cuisine's chef list
     * and that specific chef entity will also add the current cuisine into their cuisine list
     * @param chef  the new chef that will be added into cuisine's chef list
     */
    public void addChef(Chef chef){
        this.expertChefs.add(chef);
        chef.getFamiliarCuisines().add(this);
    }

    /**
     * Removes a specific chef from cuisine's chef list
     * and in addition, the specific chef entity will remove the current cuisine from its cuisine list
     * @param chef   the chef that will be removed from the chef list
     */
    public void removeChef(Chef chef){
        this.expertChefs.remove(chef);
        chef.getFamiliarCuisines().remove(this);
    }

    /**
     * Obtains the ingredients that make up the cuisine
     * @return  the list of ingredients of the current cuisine
     */
    public Set<Ingredient> getIngredientsUsed(){
        return ingredientsUsed;
    }

    /**
     * Adds new ingredient into the list of cuisine ingredients
     * and the specific ingredient entity will add the current cuisine into their cuisine list
     * @param ingredient  the ingredient that will be added into the ingredient list
     */
    public void addIngredient(Ingredient ingredient){
        this.ingredientsUsed.add(ingredient);
        ingredient.getCuisinesAffected().add(this);
    }

    /**
     * Removes a specific ingredient from the list of cuisine ingredients
     * and the specific ingredient entity will remove the current cuisine from their cuisine list
     * @param ingredient  the specific ingredient that will be removed from the ingredient list
     */
    public void removeIngredient(Ingredient ingredient){
        this.ingredientsUsed.remove(ingredient);
        ingredient.getCuisinesAffected().remove(this);
    }

    /**
     * Obtains the different recipes that the current cuisine has
     * @return  the list of recipes that is associated with the current cuisine
     */
    public Set<Recipe> getRecipesCreated(){
        return recipesCreated;
    }

    /**
     * Adds a new recipe into the recipe list of cuisine
     * and that recipe entity will set their cuisine attribute to the current cuisine
     * @param recipe  the new recipe that will be added into the recipe list
     */
    public void addRecipe(Recipe recipe){
        this.recipesCreated.add(recipe);
        recipe.setCuisine(this);
    }

    /**
     * Removes a specific recipe from the cuisine recipe list
     * and the specific recipe entity will set their cuisine attribute to null
     * @param recipe  the recipe that will be removed from the recipe list
     */
    public void removeRecipe(Recipe recipe){
        this.recipesCreated.remove(recipe);
        recipe.setCuisine(null);
    }

    /**
     * Prints a cuisine entity with their appropriate attribute value
     * @return  string format that prints the attribute value of the current cuisine
     */
    @Override
    public String toString(){
        return String.format("Cuisine[id = %d, name = %s, region = %s, religion = %s]", cuisineId, name, region, religion);
    }

}
