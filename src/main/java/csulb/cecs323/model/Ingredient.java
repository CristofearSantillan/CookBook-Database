package csulb.cecs323.model;
/*
SOURCES:
The first source was used to give myself a better understanding of how to use JPA: https://csulb-my.sharepoint.com/personal/david_brown_csulb_edu/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Fdavid%5Fbrown%5Fcsulb%5Fedu%2FDocuments%2FCECS%20323%2FLabs%2FCECS%20323%20Lab%20JPA%20Car%20Club%20Intro%2Emp4&parent=%2Fpersonal%2Fdavid%5Fbrown%5Fcsulb%5Fedu%2FDocuments%2FCECS%20323%2FLabs&originalPath=aHR0cHM6Ly9jc3VsYi1teS5zaGFyZXBvaW50LmNvbS86djovZy9wZXJzb25hbC9kYXZpZF9icm93bl9jc3VsYl9lZHUvRVN0UzVGVFd0cUZKdFd1WEstY1cxcFFCMWh1clRjS0NPQTRpa2RlWUVXM2dEQT9ydGltZT11YkFjYUhZRTJVZw
The second source was also used to gain a clearer understanding of how to properly map a many to many relationship in JPA: https://www.baeldung.com/jpa-many-to-many
used for understanding oneToMany implementationL: https://en.wikibooks.org/wiki/Java_Persistence/OneToMany
javadoc source used to understand how to properly implement: https://en.wikipedia.org/wiki/Javadoc
 */
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * contain the ingredient name, type and description made by a recipe.
 * these ingredients can then be used in the cuisine
 * ingredient is connected to cuisine, recipe and ingredient amount by virtue of the foreign key Name in ingredient
*/
@Entity
public class Ingredient {

    @Id
    private String name;

    private String type;

    private String description;

    @ManyToMany(mappedBy = "ingredientsUsed")
    private Set<Cuisine> cuisinesAffected;
    
    @OneToMany(mappedBy = "ingredient", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Set<IngredientAmount> ingredientAmounts;

    /**
     * creates a new hash object for cuisine and ingredient amount
     */
    public Ingredient(){
        this.cuisinesAffected = new HashSet<>();
        this.ingredientAmounts = new HashSet<>();
    }

    /**
     * method for storing the information regarding ingredient
     * @param name object that was input for name string
     * @param type object that was inputted for the type string
     * @param description inputted object belonging to description
     */
    public Ingredient(String name, String type, String description){
        this.name = name;
        this.type = type;
        this.description = description;
        this.cuisinesAffected = new HashSet<>();
        this.ingredientAmounts = new HashSet<>();
    }

    /**
     * gets the name object belonging to ingredient
     * @return the ingredient name object
     */
    public String getName() {return name;}

    /**
     * sets the name object belonging to ingredient
     * @param name inputted value of name that belongs to the ingredient
     */
    public void setName(String name) {this.name = name;}

    /**
     * gets the type object that is meant for ingredient
     * @return the object type that belongs to ingredient
     */
    public String getType() {return type;}

    /**
     * sets the type object that belonging to ingredient
     * @param type object that was input within the ingredient class
     */
    public void setType(String type) {this.type = type;}

    /**
     * gets te description onject belonging to ingredient
     * @return the object that is input within ingredient
     */
    public String getDescription() {return description;}

    /**
     * sets the description object in ingredient
     * @param description object that was input belonging to ingredient
     */
    public void setDescription(String description) {this.description = description;}

    /**
     * get the set of cuisines and the current aoumt of cuisines
     * @return the set of cuisines and the amount of cuisines object within the ingredients
     */
    public Set<Cuisine> getCuisinesAffected() {return cuisinesAffected;}

    /**
     * adds a new cuisine object from within the existing cuisine objects
     * @param cuisine the cuisine object that contains ingredients for it
     */
    public void addCuisine(Cuisine cuisine){
        this.cuisinesAffected.add(cuisine);
        cuisine.getIngredientsUsed().add(this);
    }

    /**
     * removes a built cuisine object
     * @param cuisine removes the cuisine object along with the ingredients aligned with it
     */
    public void removeCuisine(Cuisine cuisine){
        this.cuisinesAffected.remove(cuisine);
        cuisine.getIngredientsUsed().remove(this);
    }

    /**
     * gets the set of ingredients and their amount
     * @return the total value of the ingredient amount for a given ingredient
     */
    public Set<IngredientAmount> getIngredientAmounts() {return ingredientAmounts;}

    /**
     * adds an object of an ingredient along with it's total value
     * @param ingredientAmount value of ingredient amount that belongs to a particular ingredient
     */
    public void addIngredientAmount(IngredientAmount ingredientAmount){
        this.ingredientAmounts.add(ingredientAmount);
    }

    /**
     * removes an ingredient amount object
     * @param ingredientAmount removes the value that an ingredient holds
     */
    public void removeIngredientAmount(IngredientAmount ingredientAmount){
        this.ingredientAmounts.remove(ingredientAmount);
        ingredientAmount.setIngredient(null);
    }

    /**
     * converts the current object to string
     * @return the current object to a string if used as a string object
     */
    @Override
    public String toString(){
        return String.format("Ingredient[name = %s, type = %s, description = %s]", name, type, description);
    }

}