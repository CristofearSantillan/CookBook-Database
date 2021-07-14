package csulb.cecs323.model;
/*
SOURCES:
Used for understanding many to one relationship: https://en.wikibooks.org/wiki/Java_Persistence/ManyToOne
used for understanding association classes: https://en.wikibooks.org/wiki/Java_Persistence/ManyToMany
javadoc source used to understand how to properly implement: https://en.wikipedia.org/wiki/Javadoc
*/
import javax.persistence.*;

/**
 * Ingredient amount association class.
 * connected to ingredient and recipe by virture of foreign keys recipeId and ingrdientName
 */
@Entity
public class IngredientAmount {

    private float amount;

    private String units;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @Id
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_name")
    @Id
    private Ingredient ingredient;

    /**
     * No-Arg constructor that sets everything to null within IngredientAmount
     */
    public IngredientAmount(){}

    /**
     * stores information about ingredient amount
     * @param amount sets the new value for amount
     * @param units sets the new object for unit
     */
    public IngredientAmount(float amount, String units, Recipe recipe, Ingredient ingredient){
        this.amount = amount;
        this.units = units;
        this.setRecipe(recipe);
        this.setIngredient(ingredient);
    }

    /**
     * gets the value of ingredients
     * @return the value of ingredients being used
     */
    public float getAmount() {
        return amount;
    }

    /**
     * sets the value of ingredients belonging to ingredient amount
     * @param amount value that is input belonging to ingredient amount
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * gets the object that belongs to
     * @return object that is input to define grams,kilos,etc in ingredient amount
     */
    public String getUnits() {
        return units;
    }

    /**
     * gets the object belonging to ingredient amount
     * @param units object that is inputted which belongs to the ingredient amount class
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * gets the recipe class
     * @return the class in which specific amounts of ingredients and units are needed to correlate information
     */
    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * sets the recipe into the ingredient amount class
     * @param recipe places the class into the ingredient amount class for reference to be used
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.addIngredientAmount(this);
    }

    /**
     * get the current ingredient object
     * @return the object that belongs to ingredient for use in ingredient amount
     */
    public Ingredient getIngredient() {
        return ingredient;
    }

    /**
     * sets the ingredient object that needs to be accounted for
     * @param ingredient object that requires to be counted as a value and have units defined
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        ingredient.addIngredientAmount(this);
    }

    /**
     * converts the current object to string
     * @return the current object to a string if used as a string object
     */
    @Override
    public String toString(){
        return String.format("IngredientAmount[amount = %.2f, units = %s]", getAmount(), getUnits());
    }

}
