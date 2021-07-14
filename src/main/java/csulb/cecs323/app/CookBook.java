package csulb.cecs323.app;

import csulb.cecs323.model.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.Arrays;

import java.util.Scanner;

public class CookBook {
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(CookBook.class.getName());
    private static final Scanner input = new Scanner(System.in);

    public CookBook(EntityManager manager) {
        this.entityManager = manager;
    }

    public static void main(String[] args) {
        LOGGER.fine("Creating EntityManagerFactory and EntityManager");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("CookBook");
        EntityManager manager = factory.createEntityManager();
        CookBook cookBook = new CookBook(manager);

        boolean running = true;
        int option;

        System.out.println("\nThis application is about chefs creating unique recipes that are then criticized by food critics.");
        System.out.println("These recipes are created with many different ingredients and consist of series of steps.");
        System.out.println("The interface below will allow any user to create new recipes, update existing recipes, and remove entities.");
        System.out.println("In addition, they can also execute three different queries from the six that are created.");

        while(running)
        {

            System.out.println("\nCook Book Menu:\n1. Create new recipe\n2. Update a recipe\n3. Delete a food critic\n4. Remove an entity\n5. Execute queries\n6. Quit");
            System.out.print("Enter Here: ");
            option = input.nextInt();

            switch (option)
            {
                case 1:
                    cookBook.addRecipe();
                    break;
                case 2:
                    cookBook.changeRecipe();
                    break;
                case 3:
                    cookBook.foodCriticDeletion();
                    break;
                case 4:
                    cookBook.entityDeletion();
                    break;
                case 5:
                    cookBook.queryExecution();
                    break;
                case 6:
                    running = false;
                    System.out.println("\nHave a nice day!");
                    break;
                default:
                    System.out.println("\nInvalid Input, Try Again!");
            }

        }

    }

    public <E> void createEntity(List<E> entities) {
        Iterator var2 = entities.iterator();

        Object next;
        while(var2.hasNext()) {
            next = var2.next();
            LOGGER.info("Persisting: " + next);
            this.entityManager.persist(next);
        }
        var2 = entities.iterator();

        while(var2.hasNext()) {
            next = var2.next();
            LOGGER.info("Persisted object after flush (non-null id): " + next);
        }

    }

    /**
     * For this function, a list of food critic that has followers will be printed on the console and the user will have to delete one from the list.
     * If there is no food critic that has followers then the user will automatically will return to the main menu.
     * After, they deleted from the database, then they will be prompted if they want to delete another food critic or go to the main menu.
     */
    public void foodCriticDeletion() {
        EntityTransaction remove = this.entityManager.getTransaction();
        Query foodCriticQuery;
        List<FoodCritic> foodCritics;
        int index;
        char done = 'y';
        while(done != 'n') {
            foodCriticQuery = this.entityManager.createQuery("SELECT DISTINCT f FROM FoodCritic f WHERE f.followers <> NULL");
            foodCritics = foodCriticQuery.getResultList();
            if (foodCritics.size() != 0) {
                System.out.println("\nBelow is a list of food critics that has followers.");
                for (int i = 0; i < foodCritics.size(); i++) {
                    System.out.println((i + 1) + ": " + foodCritics.get(i).getFirstName() + " " + foodCritics.get(i).getLastName() + ", email: " + foodCritics.get(i).getEmail());
                }
                System.out.print("\nSelect the food critic who you would like to delete [1-" + foodCritics.size() + "]: ");
                index = input.nextInt();
                remove.begin();
                this.entityManager.remove(foodCritics.get(index - 1));
                remove.commit();
            } else {
                System.out.println("\nSorry, there are no food critics that has followers!");
                break;
            }
            System.out.print("\nWould you like to delete another food critic (Y/N)? ");
            done = input.next().toLowerCase().charAt(0);
        }
    }

    /**
     * This add recipe method will prompt user to add a new recipe to the system.
     * This method uses java Scanner class to interact with the user to input data.
     * This method will ask the user to enter information for the new recipe,
     * steps needed for the recipe, ingredients and their amounts.
     * This method will then ask the user to choose a cuisine and a chef from the system
     * to be assigned to the new recipe.
     * Afterwards, this new information will be updated in the database.
     * Some variables are reused.
     */
    public void addRecipe(){
        EntityTransaction create = this.entityManager.getTransaction();
        String name, description;

        List<Recipe> recipes = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        List<IngredientAmount> ingredientAmounts = new ArrayList<>();
        List<Step> steps = new ArrayList<>();

        /*
            Start user prompt for adding a new recipe.
            User will enter the name, description, prep time, cook time, difficulty rating
            and number of servings of the recipe.
         */
        int prepTime, cookTime, difficultyRating, numberOfServings;

        System.out.println("Please enter information for the recipe\n");
        System.out.print("Name of the recipe: ");
        name = input.next();
        input.nextLine();
        System.out.print("Recipe description: ");
        description = input.nextLine();
        System.out.print("Prep time: ");
        prepTime = input.nextInt();
        input.nextLine();
        System.out.print("Cook time: ");
        cookTime = input.nextInt();
        input.nextLine();
        System.out.print("Difficulty rating: ");
        difficultyRating = input.nextInt();
        input.nextLine();
        System.out.print("Number of servings: ");
        numberOfServings = input.nextInt();
        input.nextLine();
        System.out.println();

        Recipe newRecipe = new Recipe(name, description, prepTime, cookTime, difficultyRating, numberOfServings);

        /*
            Start user prompt for ingredients needed.
            User will enter the name of the ingredient.
            If it is a new ingredient, user will have to enter the type and description of the
            ingredient.
            Then user will have to enter the ingredient amount and its unit to measure.
            The user can then choose to enter new ingredients or continue to the next process.
         */
        System.out.println("Please enter ingredients needed in the recipe\n");

        char done = 'N';
        String type, units;
        float amount;
        while(done != 'Y' && done != 'y') {
            System.out.print("Ingredient name: ");
            name = input.nextLine();

            Ingredient newIngredient;
            IngredientAmount newIngredientAmount;
            Query ingredientQuery = this.entityManager.createQuery("SELECT i FROM Ingredient i WHERE i.name = '" + name + "'");
            List<Ingredient> sameIngredients = ingredientQuery.getResultList();

            if (sameIngredients.isEmpty()) {
                System.out.println("Since this is a new ingredient in the system, please enter the following information.");
                System.out.print("Ingredient type: ");
                type = input.nextLine();
                System.out.print("Ingredient Description: ");
                description = input.nextLine();
                newIngredient = new Ingredient(name, type, description);
            } else {
                newIngredient = sameIngredients.get(0);
            }

            System.out.print("Units to measure for the ingredient(eg. grams, tbsp): ");
            units = input.nextLine();
            System.out.print("Amount needed: ");
            amount = input.nextFloat();
            input.nextLine();
            newIngredientAmount = new IngredientAmount(amount, units, newRecipe, newIngredient);
            newIngredient.addIngredientAmount(newIngredientAmount);
            newRecipe.addIngredientAmount(newIngredientAmount);
            ingredientAmounts.add(newIngredientAmount);

            if(sameIngredients.isEmpty()){
                ingredients.add(newIngredient);
            }
            System.out.print("Done adding ingredients (Y/N)? ");
            done = input.next().charAt(0);
            input.nextLine();
        }

        /*
            Start user prompt for steps.
            User will enter the steps for the recipe, specifically description of the step and time needed.
            User can then choose to add more steps or continue to the next process.
         */

        System.out.println("Please enter the instructions for the recipe step by step\n");
        int stepNum = 1;
        int stepTime;
        done = 'N';
        while(done != 'Y' && done != 'y') {
            System.out.println("Step " + stepNum + " - ");
            System.out.print("Description: ");
            description = input.nextLine();
            System.out.print("Time needed: ");
            stepTime = input.nextInt();
            input.nextLine();
            steps.add(new Step(stepNum, description, stepTime));
            newRecipe.addStep(new Step(stepNum, description, stepTime));

            System.out.print("\nDone adding steps (Y/N)? ");
            done = input.next().charAt(0);
            input.nextLine();
            System.out.println();
            stepNum++;
        }

        /*
            Start user prompt for cuisine.
            User will choose one cuisine to assign it to the recipe.
            Cuisines are retrieved from the system.
         */
        int index = 0;
        Cuisine newCuisine;
        Query cuisineQuery = this.entityManager.createQuery("SELECT c FROM Cuisine c");
        List<Cuisine> sampleCuisines= cuisineQuery.getResultList();
        System.out.println("Here are some cuisines.");
        for(Cuisine c : sampleCuisines){
            System.out.println((sampleCuisines.indexOf(c)+1)+ ". Cuisine Name: " + c.getName() + ", Region: " + c.getRegion() + ", Religion: " + c.getReligion());
        }

        System.out.print("Enter a cuisine from [1-" + sampleCuisines.size() + "]: ");
        index = input.nextInt() - 1;
        input.nextLine();
        newCuisine = sampleCuisines.get(index);

        newCuisine.addRecipe(newRecipe);
        for(Ingredient i : ingredients){
            newCuisine.addIngredient(i);
        }


        /*
            Start user prompt for adding chef.
            User will choose one chef to assign it to the recipe.
            Chefs are retrieved from the system.
         */
        Query chefQuery = this.entityManager.createQuery("SELECT ch FROM Chef ch");
        List<Chef> sampleChefs = chefQuery.getResultList();
        Chef newChef;
        System.out.println("Here are some chefs.");
        for(Chef ch : sampleChefs){
            System.out.println((sampleChefs.indexOf(ch)+1)+ ". Chef: " + ch.getFirstName() + " " + ch.getLastName());
        }
        System.out.println("Please select one of these chefs for this recipe [1-"+ sampleChefs.size()+ "]: ");
        index = input.nextInt()-1;
        input.nextLine();
        newChef = sampleChefs.get(index);
        newChef.addRecipe(newRecipe);
        newChef.addCuisine(newCuisine);
        recipes.add(newRecipe);

        System.out.println("This recipe has been added. Thank you.");

        /*
            Adding new information into the database
         */
        create.begin();
        createEntity(recipes);
        createEntity(ingredients);
        createEntity(ingredientAmounts);
        create.commit();
    }

    /**
     * This function allow user to change the description, preparation time, cook time, and difficulty rating of a recipe
     * User will be prompt to input a string to search for the recipe by name then they will need to choose which attribute to change with an integer value
     * Description will require user to input a string to change the value of it
     * Prep time and Cook time require the user to input integer values in minutes
     * Difficulty rating require user to input an integer to change it
     * User will be prompt to continue
     */
    public void changeRecipe() {
        Query getRecipeQuery;
        Query changeRecipeQuery;
        List<Recipe> recipeList;
        int recipeIndex;
        int attributeIndex;
        boolean finishUpdate = false;
        boolean completeSearching = false;
        char resumeChoice;
        String newDescription = "";
        int newPrepTime;
        int newCookTime;
        int newDifficultyRating;
        EntityTransaction update = this.entityManager.getTransaction();
        update.begin();

        // Check with user if they are with changing recipe, prompt again to check if they are done updating
        while(!completeSearching)
        {
           System.out.print("\nInput the string of recipe to search for\nEnter here: ");
           // take in string input of user to search for one with similar with query
           String recipeSearch = input.next();
           getRecipeQuery = this.entityManager.createQuery("SELECT r FROM Recipe r WHERE  LOWER(r.name) LIKE '%" + recipeSearch + "%'");
           recipeList = getRecipeQuery.getResultList();
           if(recipeList.size() > 0)
           {
               System.out.println("\nBelow is a list of Recipe from the search:");
               for(int i = 0; i < recipeList.size(); i++) {
                   System.out.println((i + 1) + ": " + recipeList.get(i).getName());
               }
               System.out.print("\nSelect the Recipe you would like to make changes to [1-"+recipeList.size()+"]: ");
               recipeIndex = input.nextInt();
               // check if player is finish updating the actual recipe
               while(!finishUpdate)
               {
                   System.out.print("Select the Integer values of " + recipeList.get(recipeIndex-1).getName() + " you would like to change: \n1. description\n2. prep time\n3. cook time\n4. difficulty rating\n");
                   attributeIndex = input.nextInt();
                   switch(attributeIndex)
                   {
                       case 1:
                           // Change description of the recipe
                           System.out.println("Input the String value you would like to change the description to\nEnter Here: ");
                           newDescription = input.next();
                           newDescription += input.nextLine();

                           changeRecipeQuery = this.entityManager.createQuery("UPDATE Recipe SET description = '" + newDescription + "' WHERE recipeId = " + recipeIndex);
                           changeRecipeQuery.executeUpdate();
                           System.out.println("Updated description of the recipe to " + newDescription);
                           break;
                       case 2:
                           // Change prep time of the recipe
                           System.out.println("Input the Integer value you would like to change the prep time (in minutes) to\nEnter Here: ");
                           newPrepTime = input.nextInt();

                           changeRecipeQuery = this.entityManager.createQuery("UPDATE Recipe SET prepTime = '" + newPrepTime + "' WHERE recipeId = " + recipeIndex);
                           changeRecipeQuery.executeUpdate();
                           System.out.println("Updated prep time of the recipe to " + newPrepTime + " minutes");
                           break;
                       case 3:
                           // change cook time of the recipe
                           System.out.println("Input the Integer value you would like to change the cook time (in minutes) to\nEnter Here: ");
                           newCookTime = input.nextInt();

                           changeRecipeQuery = this.entityManager.createQuery("UPDATE Recipe SET cookTime = '" + newCookTime + "' WHERE recipeId = " + recipeIndex);
                           changeRecipeQuery.executeUpdate();
                           System.out.println("Updated cook time of the recipe to " + newCookTime + " minutes");
                           break;
                       case 4:
                           // change difficult rating of the recipe
                           System.out.println("Input the Integer value you would like to change the difficulty rating to\nEnter Here: ");
                           newDifficultyRating = input.nextInt();

                           changeRecipeQuery = this.entityManager.createQuery("UPDATE Recipe SET difficultyRating = '" + newDifficultyRating + "' WHERE recipeId = " + recipeIndex);
                           changeRecipeQuery.executeUpdate();
                           System.out.println("Updated difficulty rating of the recipe to " + newDifficultyRating);
                           break;
                       default:
                           System.out.println("Invalid Input, Try Again!");
                   }

                   System.out.println("Are you finish updating this recipe? (Y/N)");
                   resumeChoice = input.next().toLowerCase().charAt(0);
                   if(resumeChoice == 'y')
                   {
                       finishUpdate = true;
                   }

               }


           }
           else
           {
               System.out.println("No Result found from: " + recipeSearch);
           }

           System.out.println("Would you like to continue searching? (Y/N)");
           if(input.next().toLowerCase().charAt(0) == 'n')
           {
               completeSearching = true;
           }

        }


        update.commit();
    }

    /**
     * In this function, the user is asked what kind of class they would like to delete entities from.
     * After, they are prompted with the consequences of removing a entity from a specific class.
     * They will have a choice to either move forward, go back to the main menu, or choose a different class to delete from.
     * If they chose to move forward, then the console will print out all of the class's content and the user will choice one from the list to delete from.
     * Finally, once they deleted an entity, then they can choose to leave to the main menu or choose a different class to delete from.
     */
    public void entityDeletion(){
        EntityTransaction delete = this.entityManager.getTransaction();
        int option, deletionOption, deletionIndex, size = 0;
        char done = 'y';
        Query entityQuery;
        List<?> queryResult;
        String[] decisions = {"Chef","Cuisine", "Recipe", "Ingredient", "IngredientAmount", "Step", "FoodCritic", "Review"};
        String decisionName;

        while(done != 'n') {

            System.out.println("\nHere are the list of classes in the database:");
            for (int i = 0; i < decisions.length; i++) { System.out.println((i + 1) + ". " + decisions[i]); }
            System.out.print("\nWhich class would you like to delete from? ");
            option = input.nextInt();
            decisionName = decisions[option - 1];
            if(option == 6){
                entityQuery = this.entityManager.createQuery("SELECT o FROM " + decisions[option - 4] + " o");
            } else {
                entityQuery = this.entityManager.createQuery("SELECT o FROM " + decisionName + " o");
            }
            queryResult = entityQuery.getResultList();

            if (queryResult.size() != 0) {

                System.out.println("\nYou chose to delete from " + decisionName + " class.\n");
                System.out.print("Upon removing a " + decisionName + " entity, ");

                switch (option){
                    case 1:
                        System.out.println("any recipe entities created by a chef will also be deleted and disconnected from any cuisine that they are expert in.");
                        break;
                    case 2:
                        System.out.println("any relationship between ingredients, chefs, and recipes will only be disconnected instead of being deleted.");
                        break;
                    case 3:
                        System.out.println("any ingredient amount used in a recipe and the steps it takes to create a recipe will be removed along with it.\nAlso, the chef that created this recipe and the cuisine connected to this recipe will not be removed but instead will be disconnected.\nFinally, the reviews that left on this recipe will be removed.");
                        break;
                    case 4:
                        System.out.println("any ingredient amount that has been used for a recipe using this ingredient is removed as well.\nBut, the cuisine that is associated with this ingredient will only be disconnected.");
                        break;
                    case 5:
                        System.out.println("the ingredient and the recipe that correspond together will only be disconnected instead of being deleted.");
                        break;
                    case 6:
                        System.out.println("the connection with the specific recipe will be disconnected instead of deleting the whole recipe.");
                        break;
                    case 7:
                        System.out.println("the reviews that a specific food critic have made will also be removed along with it.");
                        break;
                    case 8:
                        System.out.println("the connection between recipe and food critic will only disconnected.\nIt will not remove a recipe nor a food critic from the database.");
                        break;
                }

                System.out.print("\nAre you sure you want to delete an entity from this class (Y/N)? ");
                done = input.next().toLowerCase().charAt(0);

                if(done != 'y'){
                    System.out.print("\nWould you like to try to remove from a different class (Y/N)? ");
                    done = input.next().toLowerCase().charAt(0);
                    continue;
                }

                if(option != 6) {
                    System.out.println("\nBelow is a list of " + decisionName + "(s).");
                }

                List<Chef> chefs = null; List<Cuisine> cuisines = null; List<Recipe> recipes = null; List<Ingredient> ingredients = null; List<IngredientAmount> ingredientAmounts = null; List<Step> steps = null; List<FoodCritic> foodCritics = null; List<Review> reviews = null;
                Recipe recipeStepDeletion = null; FoodCritic foodCriticReview = null;

                switch (option){
                    case 1:
                        chefs = entityQuery.getResultList();
                        size = chefs.size();
                        for (int i = 0; i < chefs.size(); i++) {
                            System.out.println((i + 1) + ": " + chefs.get(i).getFirstName() + " " + chefs.get(i).getLastName() + ", email: " + chefs.get(i).getEmail());
                        }
                        break;
                    case 2:
                        cuisines = entityQuery.getResultList();
                        size = cuisines.size();
                        for (int i = 0; i < cuisines.size(); i++) {
                            System.out.println((i + 1) + ": " + cuisines.get(i).getName() + ", region: " + cuisines.get(i).getRegion() + ", religion: " + cuisines.get(i).getReligion());
                        }
                        break;
                    case 3:
                        recipes = entityQuery.getResultList();
                        size = recipes.size();
                        for (int i = 0; i < recipes.size(); i++) {
                            System.out.println((i + 1) + ": " + recipes.get(i).getName() + ", description: " + recipes.get(i).getDescription() + ", number of servings: " + recipes.get(i).getNumberOfServings());
                        }
                        break;
                    case 4:
                        ingredients = entityQuery.getResultList();
                        size = ingredients.size();
                        for (int i = 0; i < ingredients.size(); i++) {
                            System.out.println((i + 1) + ": " + ingredients.get(i).getName() + ", description: " + ingredients.get(i).getDescription() + ", type: " + ingredients.get(i).getType());
                        }
                        break;
                    case 5:
                        ingredientAmounts = entityQuery.getResultList();
                        size = ingredientAmounts.size();
                        for (int i = 0; i < ingredientAmounts.size(); i++) {
                            System.out.println((i + 1) + ": recipe: " + ingredientAmounts.get(i).getRecipe().getName() + ", ingredient: " + ingredientAmounts.get(i).getIngredient().getName() + ", amount: " + ingredientAmounts.get(i).getAmount() + ", units: " + ingredientAmounts.get(i).getUnits());
                        }
                        break;
                    case 6:
                        System.out.println("\nFrom the Recipes below, which one would you like to remove a Step from?");
                        List<Recipe> recipeSteps = entityQuery.getResultList();
                        for (int i = 0; i < recipeSteps.size(); i++) {
                            System.out.println((i + 1) + ": " + recipeSteps.get(i).getName() + ", description: " + recipeSteps.get(i).getDescription() + ", number of servings: " + recipeSteps.get(i).getNumberOfServings());
                        }
                        System.out.print("Enter Here: ");
                        deletionOption = input.nextInt();
                        recipeStepDeletion = recipeSteps.get(deletionOption-1);
                        System.out.println("\nBelow is a list of " + decisionName + "(s) from recipe " + recipeStepDeletion.getName() + ".");
                        steps = recipeStepDeletion.getStepList();
                        size = steps.size();
                        for (Step s: steps) {
                            System.out.println(s.getOrderNumber() + ":  description: " + s.getDescription() + ", time: " + s.getTime());
                        }
                        break;
                    case 7:
                        foodCritics = entityQuery.getResultList();
                        size = foodCritics.size();
                        for (int i = 0; i < foodCritics.size(); i++) {
                            System.out.println((i + 1) + ": " + foodCritics.get(i).getFirstName() + " " + foodCritics.get(i).getLastName() + ", email: " + foodCritics.get(i).getEmail());
                        }
                        break;
                    case 8:
                        reviews = entityQuery.getResultList();
                        size = reviews.size();
                        for (int i = 0; i < reviews.size(); i++) {
                            System.out.println((i + 1) + ": food critic: " + reviews.get(i).getFoodCritic().getFirstName() + " " + reviews.get(i).getFoodCritic().getLastName() + ", recipe: " + reviews.get(i).getRecipe().getName() + ", description: " + reviews.get(i).getDescription() + ", rating: " + reviews.get(i).getRating());
                        }
                        break;
                }

                System.out.print("\nWhich " + decisionName + " would you like to delete [1-" + size + "]: ");
                deletionOption = input.nextInt();
                deletionIndex = deletionOption-1;

                delete.begin();
                switch (option){
                    case 1:
                        this.entityManager.remove(chefs.get(deletionIndex));
                        break;
                    case 2:
                        entityQuery = this.entityManager.createQuery("UPDATE Recipe SET cuisine = NULL WHERE cuisine.cuisineId = " + cuisines.get(deletionIndex).getCuisineId());
                        entityQuery.executeUpdate();
                        this.entityManager.remove(cuisines.get(deletionIndex));
                        break;
                    case 3:
                        this.entityManager.remove(recipes.get(deletionIndex));
                        break;
                    case 4:
                        this.entityManager.remove(ingredients.get(deletionIndex));
                        break;
                    case 5:
                        this.entityManager.remove(ingredientAmounts.get(deletionIndex));
                        break;
                    case 6:
                        recipeStepDeletion.removeStep(steps.get(deletionIndex));
                        steps = recipeStepDeletion.getStepList();
                        for(int i = 0; i < steps.size(); i++) { steps.get(i).setOrderNumber(i+1); }
                        break;
                    case 7:
                        this.entityManager.remove(foodCritics.get(deletionIndex));
                        break;
                    case 8:
                        entityQuery = this.entityManager.createQuery("SELECT f FROM FoodCritic f WHERE f.userID = " + reviews.get(deletionIndex).getFoodCritic().getUserID());
                        foodCriticReview = (FoodCritic)entityQuery.getSingleResult();
                        foodCriticReview.removeReview(reviews.get(deletionIndex));
                        this.entityManager.remove(reviews.get(deletionIndex));
                        break;
                }
                delete.commit();

                System.out.println("\nA " + decisionName + " has been deleted along with its consequences!");

            } else {
                System.out.println("\nSorry, there are no data under the " + decisionName + " class!");
            }

            System.out.print("\nWould you like to try to remove from a different class (Y/N)? ");
            done = input.next().toLowerCase().charAt(0);

        }
    }

    /**
     * This Function Provide 6 different queries for the user to choose 3 from the list
     * The queries will execute upon getting all three choice from the user
     * The resulting output for the three queries will be listed and numbered of results
     */
    public void queryExecution(){
        ArrayList<String> executionOptions = new ArrayList<String>(Arrays.asList("Find the chef with the most numbers of review","Find the name of the chef that use the least amount of ingredients","find the ingredient of a specific type that have never been used in a recipe","Show recipes, their average rating greater than overall average rating of all recipes, and a chef who created the recipe","show foodcritics and their reviews and recipes of these reviews","Let the user pick a chef and then the console displays the recipes that the chef made, finally the user picks a recipe and the steps are displayed for them"));
        ArrayList<Integer> originalOption = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6));
        ArrayList<Integer> resultOptions = new ArrayList<Integer>();
        while (executionOptions.size() > 3){
            System.out.println("\nUser Choice for executing a query.");
            for(int i = 0; i < executionOptions.size(); i++){ System.out.println((i+1) + ". " + executionOptions.get(i)); }
            System.out.print("Enter Here: ");
            int user_input = input.nextInt();
            executionOptions.remove(user_input-1);
            resultOptions.add(originalOption.get(user_input-1));
            originalOption.remove(user_input-1);
        }
        for (int i = 0; i < 3; i++){
            int queryChoice = resultOptions.get(i);
            switch(queryChoice){
                case 1:
                    System.out.println("\nThe result of the chef with the most numbers of review");
                    Query chefQuery = this.entityManager.createNativeQuery("SELECT U.FIRST_NAME AS CHEF_FIRST_NAME, U.LAST_NAME AS CHEF_LAST_NAME, U.EMAIL AS CHEF_EMAIL, COUNT(*) AS NUMBER_OF_REVIEWS FROM USER_TABLE U INNER JOIN CHEF C on U.USER_ID = C.CHEF_ID INNER JOIN RECIPE R on C.CHEF_ID = R.CHEF_ID INNER JOIN REVIEW R2 on R.RECIPE_ID = R2.RECIPE_ID GROUP BY U.FIRST_NAME, U.LAST_NAME, U.EMAIL HAVING COUNT(*) >= ALL(SELECT COUNT(*) FROM USER_TABLE INNER JOIN CHEF on USER_TABLE.USER_ID = CHEF.CHEF_ID INNER JOIN RECIPE on CHEF.CHEF_ID = RECIPE.CHEF_ID INNER JOIN REVIEW on RECIPE.RECIPE_ID = REVIEW.RECIPE_ID GROUP BY USER_TABLE.USER_ID)");

                    List<Object[]> chefs = chefQuery.getResultList();
                    for(int i2 = 0; i2 < chefs.size(); i2++) {
                        System.out.println((i2 + 1) + ": " + "Chef's First Name: " + chefs.get(i2)[0] + " Chef's Last Name: " + chefs.get(i2)[1] + " Chef's Email: " + chefs.get(i2)[2] + " Number of Reviews: " + chefs.get(i2)[3]);
                    }

                    break;
                case 2:
                    System.out.println("\nThe result of the chef that use the least amount of ingredients");
                    Query singleChef = this.entityManager.createNativeQuery("SELECT U.FIRST_NAME AS CHEF_FIRST_NAME, U.LAST_NAME AS CHEF_LAST_NAME, U.USER_ID AS CHEF_ID, U.EMAIL AS CHEF_EMAIL, COUNT(I.INGREDIENT_NAME) as NUMBER_OF_INGREDIENT FROM CHEF C LEFT OUTER JOIN RECIPE R on C.CHEF_ID = R.CHEF_ID LEFT OUTER JOIN INGREDIENTAMOUNT I on R.RECIPE_ID = I.RECIPE_ID LEFT OUTER JOIN INGREDIENT I2 on I.INGREDIENT_NAME = I2.NAME LEFT OUTER JOIN USER_TABLE U on C.CHEF_ID = U.USER_ID WHERE C.CHEF_ID IN ( SELECT CHEF_ID FROM (SELECT C2.CHEF_ID, COUNT(I.INGREDIENT_NAME) as \"# ingredient\" FROM CHEF C2 LEFT OUTER JOIN RECIPE R on C2.CHEF_ID = R.CHEF_ID LEFT OUTER JOIN INGREDIENTAMOUNT I on R.RECIPE_ID = I.RECIPE_ID LEFT OUTER JOIN INGREDIENT I2 on I.INGREDIENT_NAME = I2.NAME GROUP BY C2.CHEF_ID) ingrCount WHERE \"# ingredient\" = (SELECT MIN(\"# ingredient\") FROM (SELECT COUNT(I.INGREDIENT_NAME) as \"# ingredient\" FROM CHEF C2 LEFT OUTER JOIN RECIPE R on C2.CHEF_ID = R.CHEF_ID LEFT OUTER JOIN INGREDIENTAMOUNT I on R.RECIPE_ID = I.RECIPE_ID LEFT OUTER JOIN INGREDIENT I2 on I.INGREDIENT_NAME = I2.NAME GROUP BY C2.CHEF_ID) as ingrCount2)) GROUP BY C.CHEF_ID, U.FIRST_NAME, U.LAST_NAME, U.USER_ID, U.EMAIL");
                    List<Object[]> chef = singleChef.getResultList();
                    for(int i2 = 0; i2 < chef.size(); i2++) {
                        System.out.println((i2 + 1) + ": " + "Chef's First Name: " + chef.get(i2)[0] + " Chef's Last Name: " + chef.get(i2)[1] + " Chef's ID: " + chef.get(i2)[2] + " Chef's Email: " + chef.get(i2)[3] + " Number of Ingredient: " + chef.get(i2)[4]);
                    }
                    break;
                case 3:
                    System.out.println("\nThe ingredients that have never been used in a recipe");
                    Query ingredientQuery =  this.entityManager.createNativeQuery("SELECT i1.NAME AS INGREDIENT_NAME, i1.TYPE AS INGREDIENT_TYPE\n" +
                            "FROM INGREDIENT i1\n" +
                            "EXCEPT\n" +
                            "SELECT i2.NAME, i2.TYPE\n" +
                            "FROM INGREDIENT i2 INNER JOIN INGREDIENTAMOUNT ON i2.NAME = INGREDIENT_NAME\n" +
                            "                   INNER JOIN RECIPE USING(RECIPE_ID)\n");
                    List<Object[]> ingredients = ingredientQuery.getResultList();
                    for(int i2 = 0; i2 < ingredients.size(); i2++) {
                        System.out.println((i2 + 1) + ": " + "Ingredient Name: " + ingredients.get(i2)[0] + ", Ingredient Type: " + ingredients.get(i2)[1]);
                    }
                    break;
                case 4:
                    System.out.println("\nResult of average rating of recipe greater than overall average rating of all recipes, and a chef who created the recipe");
                    Query recipeAverageQuery =  this.entityManager.createNativeQuery("SELECT recipe.NAME as recipe, AVG(r1.rating) AS average_rating, user_table.FIRST_NAME AS chef_first_name, user_table.LAST_NAME AS chef_last_name\n" +
                            "FROM recipe INNER JOIN review r1 ON recipe.RECIPE_ID = r1.RECIPE_ID\n" +
                            "            INNER JOIN chef on recipe.CHEF_ID = chef.CHEF_ID\n" +
                            "            INNER JOIN user_table on chef.CHEF_ID = user_table.USER_ID\n" +
                            "GROUP BY recipe.NAME, USER_TABLE.FIRST_NAME, user_table.LAST_NAME\n" +
                            "HAVING AVG(rating) > (SELECT AVG(rating) FROM review)");
                    List<Object[]> recipes = recipeAverageQuery.getResultList();
                    for(int i2 = 0; i2 < recipes.size(); i2++) {
                        System.out.println((i2 + 1) + ": " + "Recipe: " + recipes.get(i2)[0] + ", Average Rating: " + recipes.get(i2)[1] + ", Chef First Name: " + recipes.get(i2)[2] + ", Chef Last Name: " + recipes.get(i2)[3]);
                    }
                    break;
                case 5:
                    System.out.println("\nResult of food critics and their reviews and recipes of these reviews");
                    Query foodCriticsQuery =  this.entityManager.createNativeQuery("SELECT user_table.USER_ID AS id, user_table.FIRST_NAME AS food_critic_first_name, user_table.LAST_NAME AS food_critic_last_name, review.DESCRIPTION AS review, recipe.NAME AS recipe\n" +
                            "FROM USER_TABLE\n" +
                            "         INNER JOIN foodcritic ON USER_TABLE.USER_ID = FOODCRITIC.CRITIC_ID\n" +
                            "         LEFT OUTER JOIN review ON FOODCRITIC.CRITIC_ID = review.CRITIC_ID\n" +
                            "         LEFT OUTER JOIN recipe ON RECIPE.RECIPE_ID = review.RECIPE_ID");
                    List<Object[]> foodCritics = foodCriticsQuery.getResultList();
                    for(int i2 = 0; i2 < foodCritics.size(); i2++) {
                        System.out.println((i2 + 1) + ": " + "Id: " + foodCritics.get(i2)[0] + ", Food Critic First Name: " + foodCritics.get(i2)[1] + ", Food Critic Last Name: " + foodCritics.get(i2)[2] + ", Review: " + foodCritics.get(i2)[3] + ", Recip: " + foodCritics.get(i2)[4]);
                    }
                    break;
                case 6:

                    Query recipeQuery = this.entityManager.createQuery("SELECT r FROM Recipe r");
                    List<Recipe> recipeList = recipeQuery.getResultList();

                    for(int i2 = 0; i2 < recipeList.size(); i2++) {
                        System.out.println((i2 + 1) + ": " + recipeList.get(i2).getName());
                    }
                    System.out.print("\nSelect the Recipe you would like to make changes to [1-"+recipeList.size()+"]: ");
                    int recipeIndex2 = input.nextInt();

                    System.out.println("\nResult of food critics and their reviews and recipes of these reviews");
                    Query stepsQuery =  this.entityManager.createNativeQuery("SELECT s.ORDER_NUMBER AS STEP_NUMBER, s.DESCRIPTION AS STEP_DESCRIPTION, s.TIME STEP_TIME\n" +
                            "FROM CHEF INNER JOIN RECIPE R on CHEF.CHEF_ID = R.CHEF_ID\n" +
                            "          INNER JOIN RECIPE_STEP S on R.RECIPE_ID = S.RECIPE_ID\n" +
                            "WHERE R.RECIPE_ID = " + recipeIndex2);
                    List<Object[]> steps = stepsQuery.getResultList();

                    for(int i2 = 0; i2 < steps.size(); i2++) {
                        System.out.println((i2 + 1) + ": " + "Step Number: " + steps.get(i2)[0] + " Step Description: " + steps.get(i2)[1] + " Step Time: " + steps.get(i2)[2]);
                    }

                    break;
            }
        }
    }

}





