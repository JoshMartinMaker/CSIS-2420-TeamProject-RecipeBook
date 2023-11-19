package recipeBook;

import java.util.Arrays;

/**
 * Represents a recipe with a name, set of {@link Ingredients}, set of
 * instructions, and source website.
 * 
 * @author Josh Martin
 *
 */
public class Recipe implements Comparable<Recipe> {
	
	private final String name;					// The name of this recipe
	private final Ingredient[] ingredients;		// The ingredients this recipe requires
	private final String[] instructions;		// The instructions to make this recipe
	private final String website;				// The website this recipe was found on
	private final int number;					// This recipe's corresponding vertex in a graph
	
	private static int numberOfRecipes = 0;		// The number of recipes initialized
	
	/**
	 * Constructor for {@code Recipe} class.
	 * 
	 * @param name         The name of this recipe.
	 * @param ingredients  An array of strings whose elements contain this recipe's
	 *                     ingredients. Each ingredient string must be of the form
	 *                     "[quantityAndUnits]::[name]".
	 *                     
	 * @param instructions An array of strings whose elements represent steps to
	 *                     make this recipe.
	 *                     
	 * @param website      The website this recipe was found at.
	 */
	public Recipe(String name, String[] ingredientStrings, String[] instructions, String website) {
		
		this.name = name;
		this.instructions = instructions;
		this.website = website;
		this.number = numberOfRecipes++;
		
		// Initialize ingredients for each Ingredient string passed
		Ingredient[] ingredients = new Ingredient[ingredientStrings.length];
		String quantityAndUnits = null;
		String ingredientName = null;
		
		// For each ingredient
		for (int i = 0; i < ingredients.length; i++) {
			
			quantityAndUnits = ingredientStrings[i].split("::")[0];	// Get quantity and units
			ingredientName = ingredientStrings[i].split("::")[1];	// Get ingredient name
			
			ingredients[i] = new Ingredient(quantityAndUnits, ingredientName);	// Add Ingredient
		}
		
		this.ingredients = ingredients;
	}
	
	/**
	 * Constructor for {@code Recipe} class.
	 * 
	 * @param name         The name of this recipe.
	 * @param ingredients  An array of strings whose elements contain this recipe's
	 *                     ingredients. Each ingredient string must be of the form
	 *                     "[quantityAndUnits]::[name]".
	 *                     
	 * @param instructions An array of strings whose elements represent steps to
	 *                     make this recipe.
	 */
	public Recipe(String name, String[] ingredientStrings, String[] instructions) {
		this(name, ingredientStrings, instructions, null);
	}

	/**
	 * Returns the name of this recipe.
	 * 
	 * @return The name of this recipe.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the ingredients of this recipe.
	 * 
	 * @return The ingredients of this recipe as an array of {@code Ingredient}s.
	 */
	public Ingredient[] getIngredients() {
		return ingredients;
	}

	/**
	 * Returns the instructions for this recipe.
	 * 
	 * @return The instructions for this recipe as an array of strings.
	 */
	public String[] getInstructions() {
		return instructions;
	}

	/**
	 * Returns the website this recipe was found on.
	 * 
	 * @return The website this recipe was found on.
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * Returns this recipe's number.
	 * 
	 * @return This recipe's number.
	 */
	public int getNumber() {
		return number;
	}

	// TODO
	@Override
	public String toString() {
		return "Recipe [name=" + name + ", ingredients=" + Arrays.toString(ingredients) + ", instructions="
				+ Arrays.toString(instructions) + ", website=" + website + "]";
	}

	/**
	 * Compares two {@code Recipe}s by their name. This comparison is done
	 * lexicographically following the same rules as
	 * {@link String#compareTo(String)}.
	 * 
	 * @param otherRecipe The {@code Recipe} to compare to this recipe.
	 * @return A positive integer, 0, or a negative integer if this recipe's
	 *         {@code name} is lexicographically greater than, equal to, or less
	 *         than that of {@code otherRecipe}'s.
	 */
	@Override
	public int compareTo(Recipe otherRecipe) {
		return this.name.compareTo(otherRecipe.getName());
	}

	/**
	 * Test client for {@link Recipe} class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String[] ingredients1 = new String[3];
		String[] instructions1 = new String[3];
		
		for (int i = 0; i < ingredients1.length; i++) {
			ingredients1[i] = "qty " + (i + 1) + " units " + (i + 1) + "::name " + (i + 1);
			instructions1[i] = "step " + (i + 1);
		}
		
		Recipe recipe1 = new Recipe("recipe 1", ingredients1, instructions1);
		Recipe recipe2 = new Recipe("a", ingredients1, instructions1, "website 2");
		Recipe recipe3 = new Recipe("b", ingredients1, instructions1);
		
		printHeader("toString and Getters");

		System.out.println("recipe1: " + recipe1.toString());
		System.out.println("recipe1 name: " + recipe1.getName());
		System.out.println("recipe1 ingredients: " + Arrays.toString(recipe1.getIngredients()));
		System.out.println("recipe1 instructions: " + Arrays.toString(recipe1.getInstructions()));
		System.out.println("recipe1 website: " + recipe1.getWebsite());
		System.out.println("recipe1 number: " + recipe1.getNumber());
		System.out.println();
		System.out.println("recipe2 name: " + recipe2.getName());
		System.out.println("recipe2 website: " + recipe2.getWebsite());
		System.out.println("recipe2 number: " + recipe2.getNumber());
		System.out.println();
		System.out.println("recipe3 name: " + recipe3.getName());
		System.out.println("recipe3 number: " + recipe3.getNumber());
		System.out.println();
		System.out.println();
		
		printHeader("compareTo Method");
		
		System.out.println("equal recipes (recipe1.compareTo(recipe1))");
		System.out.println("Expected: 0");
		System.out.println("Actual: " + recipe1.compareTo(recipe1));
		System.out.println();
		
		System.out.println("(this recipe) < (other recipe) (recipe2.compareTo(recipe3))");
		System.out.println("Expected: negative integer");
		System.out.println("Actual: " + recipe2.compareTo(recipe3));
		System.out.println();

		System.out.println("(this recipe) > (other recipe) (recipe3.compareTo(recipe2))");
		System.out.println("Expected: positive integer");
		System.out.println("Actual: " + recipe3.compareTo(recipe2));
		System.out.println();
	}
	
	/**
	 * Prints a header for testing methods in {@code main}.
	 * 
	 * @param testInProgress The method to print a header for.
	 */
	private static void printHeader(String testInProgress) {
		System.out.println("--------------------------------");
		System.out.println(testInProgress + " Tests:");
		System.out.println("--------------------------------");
		System.out.println();
	}
}
