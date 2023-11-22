package recipeBook;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RedBlackBST;

/**
 * Represents a group of {@link Recipe}s.
 * 
 * @author Josh Martin
 *
 */
public class RecipeBook {
	
	private static final int MAX_SIMILAR_RECIPES = 5;		// The number of similar recipes to store
	private RedBlackBST<String, Recipe> recipesByName;		// The recipes in this recipe book sorted by name
	private RedBlackBST<Integer, Recipe> recipesByNumber;	// The recipes in this recipe book sorted by their number
	private EdgeWeightedGraph ingredientSimilarity;			// A graph connecting similar recipes with a similarity score
	
	/**
	 * Constructor for {@code RecipeBook} class. Initializes RecipeBook with a
	 * maximum of 100 {@link Recipe} objects.
	 */
	public RecipeBook() {
		this(100);
	}

	/**
	 * Constructor for {@code RecipeBook} class.
	 * 
	 * @param numberOfRecipes The maximum number of {@link Recipe} objects contained
	 *                        in this object.
	 */
	public RecipeBook(int numberOfRecipes) {
		recipesByName = new RedBlackBST<>();
		recipesByNumber = new RedBlackBST<>();
		ingredientSimilarity = new EdgeWeightedGraph(numberOfRecipes);
	}
	
	/**
	 * Adds {@code newRecipe} to this recipe book.
	 * 
	 * @param newRecipe The {@code Recipe} to be added.
	 */
	public void addRecipe(Recipe newRecipe) {
		addRecipeToSimilarityGraph(newRecipe);
		recipesByName.put(newRecipe.getName(), newRecipe);
		recipesByNumber.put(newRecipe.getNumber(), newRecipe);
	}

	/**
	 * Adds a recipe to this recipe book.
	 * 
	 * @param name         The name of the new recipe.
	 * @param ingredients  The ingredients of the new recipe. The elements of the
	 *                     array should be ordered as
	 *                     [quantity, units]++[ingredient name].
	 * 
	 * @param instructions The instructions of the new recipe.
	 */
	public void addRecipe(String name, String[] ingredients, String[] instructions) {
		
		Recipe newRecipe = new Recipe(name, ingredients, instructions);
		int newRecipeNumber = newRecipe.getNumber();
		
		addRecipeToSimilarityGraph(newRecipe);
		recipesByName.put(name, newRecipe);
		recipesByNumber.put(newRecipeNumber, newRecipe);
	}
	
	/**
	 * Adds a recipe to this recipe book.
	 * 
	 * @param name         The name of the new recipe.
	 * @param ingredients  The ingredients of the new recipe. The elements of the
	 *                     array should be ordered as
	 *                     [quantity, units]++[ingredient name].
	 * 
	 * @param instructions The instructions of the new recipe.
	 * @param website      The source website of this recipe.
	 */
	public void addRecipe(String name, String[] ingredients, String[] instructions, String website) {
		
		Recipe newRecipe = new Recipe(name, ingredients, instructions, website);
		int newRecipeNumber = newRecipe.getNumber();
		
		addRecipeToSimilarityGraph(newRecipe);
		recipesByName.put(name, newRecipe);
		recipesByNumber.put(newRecipeNumber, newRecipe);
	}
	
	/**
	 * Returns the recipe with the given name.
	 * 
	 * @param name The name of the desired recipe.
	 * @return The recipe with the given name.
	 */
	public Recipe getRecipe(String name) {
		return recipesByName.get(name);
	}
	
	/**
	 * Returns an {@code Iterable} containing the names of all recipes in
	 * this {@code RecipeBook}.
	 * 
	 * @return An {@code Iterable} containing the names of all recipes in
	 *         this {@code RecipeBook}.
	 */
	public Iterable<String> getAllRecipeNames() {
		return recipesByName.keys();
	}
	
	/**
	 * Returns an {@code Iterable} containing all {@code Recipe}s in this
	 * {@code RecipeBook}.
	 * 
	 * @return An {@code Iterable} containing all {@code Recipe}s in this
	 *         {@code RecipeBook}.
	 */
	public Iterable<Recipe> getAllRecipes() {
		
		Queue<Recipe> recipes = new Queue<>();
		
		// For each key, enqueue its associated Recipe
		for (String el : recipesByName.keys()) {
			recipes.enqueue(recipesByName.get(el));
		}
		
		return recipes;
	}
	
	/**
	 * Adds {@code newRecipe} to the {@code ingredientSimilarity} graph with its
	 * similarity score to each other recipe. Adds up to {@code MAX_SIMILAR_RECIPES}
	 * edges to the graph.
	 * 
	 * @param newRecipe The {@code Recipe} that should be added to the graph.
	 */
	private void addRecipeToSimilarityGraph(Recipe newRecipe) {

		int newRecipeVertex = newRecipe.getNumber();
		Recipe currentRecipe = null;
		double currentSimilarityScore = 0;
		RedBlackBST<Double, Queue<Integer>> similarityScores = new RedBlackBST<>();
		double minSimilarityScore = -1.0;
		
		// Initialize similarityScores
		for (int i = 1; i <= MAX_SIMILAR_RECIPES; i++) {
			similarityScores.put(-1.0*i, new Queue<>());
		}
		
		// For each Recipe in the graph
		for (Integer vertex : recipesByNumber.keys()) {
			
			currentRecipe = recipesByNumber.get(vertex);
			currentSimilarityScore = calculateSimilarityScore(newRecipe, currentRecipe);
			
			if (currentSimilarityScore > minSimilarityScore) {
				
				Queue<Integer> minScoreSimilarVertices = similarityScores.get(minSimilarityScore);
				Queue<Integer> currentSimilarVertices = similarityScores.get(currentSimilarityScore);
				
				if (minScoreSimilarVertices.isEmpty()) {
					similarityScores.deleteMin();
					minSimilarityScore = similarityScores.min();
				}
				else {
					minScoreSimilarVertices.dequeue();
				}
				
				if (currentSimilarVertices == null) {
					currentSimilarVertices = new Queue<>();
					similarityScores.put(currentSimilarityScore, currentSimilarVertices);
				}
				
				currentSimilarVertices.enqueue(vertex);
			}
		}
		
		// Add new edges to graph
		for (Double similarityScore : similarityScores.keys()) {

			if (similarityScore >= 0) {
				
				Queue<Integer> currentSimilarVertices = similarityScores.get(similarityScore);

				while (!currentSimilarVertices.isEmpty()) {

					int currentRecipeVertex = currentSimilarVertices.dequeue();

					ingredientSimilarity.addEdge(new Edge(newRecipeVertex, currentRecipeVertex, similarityScore));
				}
			}
		}
	}
	
	/**
	 * Calculates the similarity score of two recipes.
	 * 
	 * @param recipe1 The first recipe.
	 * @param recipe2 The second recipe.
	 * @return The similarity score of {@code recipe1} to {@code recipe2}.
	 */
	private double calculateSimilarityScore(Recipe recipe1, Recipe recipe2) {
		
		if (recipe1 == null || recipe2 == null) {
			return -1.0;
		}
		
		if (Arrays.compare(recipe1.getIngredients(), recipe2.getIngredients()) == 0) {
			return 1;
		}
		
		// TODO
		// ratio between number of ingredients multiplied by percentage of shared ingredients?
		
		return 0;
	}

	/**
	 * Returns the most similar recipes to {@code referenceRecipe}.
	 * 
	 * @param referenceRecipe The recipe that returned recipes should be similar to.
	 * @return An {@code Iterable} containing the most similar {@code Recipe}s to
	 *         {@code referenceRecipe}.
	 * @implNote The number of recipes returned is dictated by
	 *           {@code MAX_SIMILAR_RECIPES}.
	 */
	public Iterable<Recipe> getSimilarRecipes(Recipe referenceRecipe) {
		
		Queue<Recipe> similarRecipes = new Queue<>();
		int referenceVertex = referenceRecipe.getNumber();
		int otherVertex = 0;
		
		// TODO Return similar recipes in order
		
		// TODO Current implementation returns all edges connected to referenceRecipe,
		// which may be greater than MAX_SIMILAR_RECIPES. Change to be similar to
		// addRecipeToSimilarityGraph and only enqueue MAX_SIMILAR_RECIPES Recipes.
		
		
		// For each similar Recipe
		for (Edge el : ingredientSimilarity.adj(referenceVertex)) {
			
			otherVertex = el.other(referenceVertex);
			similarRecipes.enqueue(recipesByNumber.get(otherVertex));
		}
		
		return similarRecipes;
	}
	
	/**
	 * Returns the similarity scores of the most similar recipes to
	 * {@code referenceRecipe}.
	 * 
	 * @param referenceRecipe The recipe that returned similarity scores should be
	 *                        referenced to.
	 * @return An {@code Iterable} containing the similarity scores of the most
	 *         similar recipes to {@code referenceRecipe}.
	 * @implNote The number of similarity scores returned is dictated by
	 *           {@code MAX_SIMILAR_RECIPES}.
	 */
	public Iterable<Double> getSimilarRecipeScores(Recipe referenceRecipe) {
		
		Queue<Double> similarityScores = new Queue<>();
		
		// TODO Return similar scores in order
		
		// TODO Current implementation returns all edges connected to referenceRecipe,
		// which may be greater than MAX_SIMILAR_RECIPES. Change to be similar to
		// addRecipeToSimilarityGraph and only enqueue MAX_SIMILAR_RECIPES Recipes.
		
		
		// For each similar Recipe
		for (Edge el : ingredientSimilarity.adj(referenceRecipe.getNumber())) {
			similarityScores.enqueue(el.weight());
		}
		
		return similarityScores;
	}
	
	/**
	 * Saves this {@code RecipeBook} to the file
	 * {@code src/recipeBook/BinaryFiles/RecipeBook.ser}.
	 */
	public void saveRecipeBookToFile() {
		
		File fileName = new File("src/recipeBook/BinaryFiles/RecipeBook.ser");
		
		try (ObjectOutputStream serializer
				= new ObjectOutputStream(new FileOutputStream(fileName))) {
			
			for (Recipe currentRecipe : this.getAllRecipes()) {
				serializer.writeObject(currentRecipe);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads a {@code RecipeBook} from the file
	 * {@code src/recipeBook/BinaryFiles/RecipeBook.ser}.
	 * 
	 * @return The {@code RecipeBook} read from the file
	 *         {@code src/recipeBook/BinaryFiles/RecipeBook.ser}.
	 */
	public static RecipeBook readRecipeBookFromFile() {
		
		File fileName = new File("src/recipeBook/BinaryFiles/RecipeBook.ser");
		Queue<Recipe> recipes = new Queue<>();
		RecipeBook result = null;
		
		// Read Recipes from file
		try (ObjectInputStream deserializer
				= new ObjectInputStream(new FileInputStream(fileName))) {
			
			for (Recipe currentRecipe = (Recipe) deserializer.readObject();
					currentRecipe != null;
					currentRecipe = (Recipe) deserializer.readObject()) {
				recipes.enqueue(currentRecipe);
			}
		}
		catch (EOFException e) {
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// Add Recipes to new RecipeBook
		result = new RecipeBook();
		
		for (Recipe el : recipes) {
			result.addRecipe(el);
		}
		
		return result;
	}
	
	/**
	 * Test client for {@link RecipeBook} class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
//		RecipeBook readFromFile = readRecipeBookFromFile();
//		
//		for (Recipe el : readFromFile.getAllRecipes()) {
//			System.out.println(el);
//		}
		
		@SuppressWarnings("unused")
		RecipeBook recipeBook100 = new RecipeBook();
		RecipeBook recipeBook50 = new RecipeBook(50);
		
		String[] ingredients1 = new String[3];
		String[] instructions1 = new String[3];
		
		for (int i = 0; i < ingredients1.length; i++) {
			ingredients1[i] = "qty " + (i + 1) + " units " + (i + 1) + "::name " + (i + 1);
			instructions1[i] = "step " + (i + 1);
		}
		
		String[] ingredients2 = new String[5];
		String[] instructions2 = new String[5];
		
		for (int i = 0; i < ingredients2.length; i++) {
			ingredients2[i] = "qty " + (i + 1) + " units " + (i + 1) + "::name " + (i + 1);
			instructions2[i] = "step " + (i + 1);
		}
		
		Recipe recipe1 = new Recipe("recipe 1", ingredients1, instructions1);
		
		recipeBook50.addRecipe(recipe1);
		recipeBook50.addRecipe("recipe 2", ingredients1, instructions1);
		recipeBook50.addRecipe("recipe 3", ingredients1, instructions1, "website 3");
		
		printHeader("getRecipe Method");
		
		System.out.println("getRecipe(\"recipe 1\")");
		System.out.println("Expected: " + recipe1.toString());
		System.out.println("Actual:   " + recipeBook50.getRecipe("recipe 1"));
		System.out.println();
		System.out.println();
		
		printHeader("getAllRecipeNames Method");
		
		System.out.println("Expected: (recipe 1) (recipe 2) (recipe 3)");
		System.out.print("Actual:   ");
		
		for (String el : recipeBook50.getAllRecipeNames()) {
			System.out.print("(" + el.toString() + ") ");
		}
		
		System.out.println();
		System.out.println();
		System.out.println();
		
		printHeader("getAllRecipes Method");
		
		System.out.println("Expected: (recipe 1, 0) (recipe 2, 1) (recipe 3, 2)");
		System.out.print("Actual:   ");
		
		for (Recipe el : recipeBook50.getAllRecipes()) {
			System.out.print("(" + el.getName() + ", " + el.getNumber() + ") ");
		}
		
		System.out.println();
		System.out.println();
		System.out.println();
		
		printHeader("getSimilarRecipes Method");
		
		System.out.println("getSimilarRecipes(recipe1)");
		System.out.println("Expected: (recipe 2) (recipe 3)");
		System.out.print("Actual:   ");
		
		for (Recipe el : recipeBook50.getSimilarRecipes(recipeBook50.getRecipe("recipe 1"))) {
			System.out.print("(" + el.getName() + ") ");
		}
		
		System.out.println();
		System.out.println();
		System.out.println();
		
		printHeader("addRecipeToSimilarityGraph Method");

		Recipe similarRecipe = new Recipe("similar recipe", ingredients1, instructions1);
		Recipe notSimilarRecipe = new Recipe("not similar recipe", ingredients2, instructions2);
		
		RecipeBook similarRecipeBook = new RecipeBook();
		similarRecipeBook.addRecipe(recipe1);
		similarRecipeBook.addRecipe("recipe 2", ingredients1, instructions1);
		similarRecipeBook.addRecipe(similarRecipe);
		
		RecipeBook notSimilarRecipeBook = new RecipeBook();
		notSimilarRecipeBook.addRecipe(recipe1);
		notSimilarRecipeBook.addRecipe("recipe 2", ingredients1, instructions1);
		notSimilarRecipeBook.addRecipe(notSimilarRecipe);
		
		System.out.println("Adding similarRecipe");
		System.out.println("Expected : (0-3 1.0) (0-5 1.0) (3-5 1.0) [in any order]");
		System.out.print("Actual: ");
		
		for (Edge el : similarRecipeBook.ingredientSimilarity.edges()) {
			System.out.print("(" + el.toString() + ") ");
		}
		
		System.out.println();
		System.out.println();
		
		System.out.println("Adding notSimilarRecipe");
		System.out.println("Expected: (0-4 0.0) (0-6 1.0) (4-6 0.0) [in any order]");
		System.out.print("Actual: ");
		
		for (Edge el : notSimilarRecipeBook.ingredientSimilarity.edges()) {
			System.out.print("(" + el.toString() + ") ");
		}
		
		System.out.println();
		System.out.println();
		System.out.println();
		
		printHeader("getSimilarRecipeScores Method");
		
		System.out.println("getSimilarRecipeScores(recipe1) in notSimilarRecipeBook");
		System.out.println("Expected: (1.0) (0.0)");
		System.out.print("Actual: ");
		
		for (Double el : notSimilarRecipeBook.getSimilarRecipeScores(recipe1)) {
			System.out.print("(" + el + ") ");
		}
		
		System.out.println();
		System.out.println();
		System.out.println();
		
//		recipeBook50.saveRecipeBookToFile();
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
