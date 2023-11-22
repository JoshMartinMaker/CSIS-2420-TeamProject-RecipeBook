package recipeBook;

import java.io.Serializable;

/**
 * Represents an ingredient in a {@link Recipe}.
 * 
 * @author Josh Martin
 *
 */
public class Ingredient implements Comparable<Ingredient>, Serializable {

	private static final long serialVersionUID = 6272702650066621682L;
	private final String quantityAndUnits;		// The quantity of this ingredient with its units
	private final String name;					// The name of this ingredient
	
	/**
	 * Constructor for {@code Ingredient} class.
	 * 
	 * @param quantityAndUnits The quantity of this ingredient with its units.
	 * @param name The name of this ingredient.
	 */
	public Ingredient(String quantityAndUnits, String name) {
		this.quantityAndUnits = quantityAndUnits;
		this.name = name;
	}
	
	/**
	 * Returns the quantity and units of this ingredient.
	 * 
	 * @return The quantity and units of this ingredient.
	 */
	public String getQuantityAndUnits() {
		return quantityAndUnits;
	}
	
	/**
	 * Returns the name of this ingredient.
	 * 
	 * @return The name of this ingredient.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns this ingredient as a string. The ingredient is represented as ([{@code quantityAndUnits}], [{@code name}]).
	 * 
	 * @return This ingredient as a string.
	 */
	@Override
	public String toString() {
		return "(" + quantityAndUnits + ", " + name + ")";
	}
	
	/**
	 * Compares two {@code Ingredient}s by their name. This comparison is done
	 * lexicographically following the same rules as
	 * {@link String#compareTo(String)}.
	 * 
	 * @param otherIngredient The {@code Ingredient} to compare to this
	 *                        {@code Ingredient}.
	 * @return A positive integer, 0, or a negative integer if this
	 *         {@code Ingredient}'s {@code name} is lexicographically greater than,
	 *         equal to, or less than that of {@code otherIngredient}'s.
	 */
	@Override
	public int compareTo(Ingredient otherIngredient) {
		return this.name.compareTo(otherIngredient.getName());
	}
	
	/**
	 * Test client for {@link Ingredient} class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Ingredient ingredient1 = new Ingredient("quantity 1 units 1", "ingredient 1");
		Ingredient ingredient2 = new Ingredient("quantity 2 units 2", "a");
		Ingredient ingredient3 = new Ingredient("quantity 3 units 3", "b");
		
		printHeader("toString and Getters");
		
		System.out.println("ingredient1: " + ingredient1.toString());
		System.out.println("ingredient1 name: " + ingredient1.getName());
		System.out.println("ingredient1 quantityAndUnits: " + ingredient1.getQuantityAndUnits());
		System.out.println();
		System.out.println("ingredient2 name: " + ingredient2.getName());
		System.out.println("ingredient3 name: " + ingredient3.getName());
		System.out.println();
		System.out.println();
		
		printHeader("compareTo Method");
		
		System.out.println("equal ingredients (ingredient1.compareTo(ingredient1))");
		System.out.println("Expected: 0");
		System.out.println("Actual: " + ingredient1.compareTo(ingredient1));
		System.out.println();
		
		System.out.println("(this ingredient) < (other ingredient) (ingredient2.compareTo(ingredient3))");
		System.out.println("Expected: negative integer");
		System.out.println("Actual: " + ingredient2.compareTo(ingredient3));
		System.out.println();

		System.out.println("(this ingredient) > (other ingredient) (ingredient3.compareTo(ingredient2))");
		System.out.println("Expected: positive integer");
		System.out.println("Actual: " + ingredient3.compareTo(ingredient2));
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
