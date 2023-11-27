package recipeBook;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;

/**
 * GUI for Recipe Book
 * @author Jennifer Weaver
 *
 */
public class RecipeBookGUI extends JFrame {

	private JPanel contentPane;
	private JTextField enterNameTxtF;
	private JTextField enterWebsiteTxtF;
	private JTextField enterIngredientTxtF;
	private JTextField enterQntyTxtF;
	private JTextField enterInstrTxtF;
	private JButton addRecipeBtn;
	private JList<String> allRecipesJList;
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private List<String> rList = new ArrayList<>();
	private RecipeBook recipes = new RecipeBook();
	private JTextArea ingredientsTxtArea;
	private String str6; // Instructions variable
	private JTextArea instructionsTxtArea;
	private String[] rList2;
	private JLabel recipeLbl; // Label for Recipe in the View All
	private JLabel recipeNameLbl;
	private Recipe newRecipe2;

	/**
	 * Launch the application. I have no idea :)
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecipeBookGUI frame = new RecipeBookGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RecipeBookGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 535);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = createTabbedPane();
		contentPane.add(tabbedPane);
		
		JPanel viewAllRecipes = viewAllRecipesTab(tabbedPane);
		viewAllRecipes.setLayout(null);
		
		JPanel viewRecipe = viewRecipeTab(tabbedPane);
		viewRecipe.setLayout(null);
		
		JPanel addNewRecipe = addNewRecipeTab(tabbedPane);
		addNewRecipe.setLayout(null);
		
	}

	private JTabbedPane createTabbedPane() {
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 10, 816, 478);
		return tabbedPane;
	}
	
	private JPanel viewAllRecipesTab(JTabbedPane tabbedPane) {
		JPanel viewAllRecipes = new JPanel();
		tabbedPane.addTab("View All Recipes", null, viewAllRecipes, null);
		
		JScrollPane viewAllScrollPn = new JScrollPane();
		viewAllScrollPn.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		viewAllScrollPn.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Might need to change if it is gonna go off the page
		viewAllScrollPn.setBounds(10, 70, 791, 371);
		viewAllRecipes.add(viewAllScrollPn);
		
		allRecipesJList = new JList<String>(model);
		allRecipesJList.setSelectedIndex(0);
		allRecipesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allRecipesJList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// Add function to call up the recipe from the recipe book and display on
				int index = allRecipesJList.getSelectedIndex();
				model.get(index);
				StringBuilder sbIngredients = new StringBuilder();
				StringBuilder sbInstructions = new StringBuilder();
				Recipe currentRecipe = recipes.getRecipe(model.get(index).stripLeading());
				
				for (Ingredient el : currentRecipe.getIngredients()) {
					sbIngredients.append("   " + el.toString() + "\n");
				}
				
				for (String el : currentRecipe.getInstructions()) {
					sbInstructions.append("   " + el + "\n");
				}
				recipeNameLbl.setText(model.get(index));
				recipeNameLbl.setText(currentRecipe.getName());
				ingredientsTxtArea.setText(sbIngredients.toString());
				instructionsTxtArea.setText(sbInstructions.toString());
			}
		});
		allRecipesJList.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		viewAllScrollPn.setViewportView(allRecipesJList);
		
		recipeLbl = new JLabel("Recipe");
		recipeLbl.setHorizontalAlignment(SwingConstants.CENTER);
		recipeLbl.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
		recipeLbl.setBounds(10, 49, 250, 19);
		viewAllRecipes.add(recipeLbl);
		
		JLabel similarRecipesLbl = new JLabel("Similar Recipes");
		similarRecipesLbl.setHorizontalAlignment(SwingConstants.CENTER);
		similarRecipesLbl.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
		similarRecipesLbl.setBounds(270, 49, 502, 19);
		viewAllRecipes.add(similarRecipesLbl);
		return viewAllRecipes;
	}

	private JPanel viewRecipeTab(JTabbedPane tabbedPane) {
		JPanel viewRecipe = new JPanel();
		tabbedPane.addTab("View Recipe", null, viewRecipe, null);
		
		recipeNameLbl = new JLabel("'Recipe Name'");
		recipeNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
		recipeNameLbl.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
		recipeNameLbl.setBounds(63, 23, 674, 31);
		viewRecipe.add(recipeNameLbl);
		
		JScrollPane ingredientsScrlPn = new JScrollPane();
		ingredientsScrlPn.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		ingredientsScrlPn.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ingredientsScrlPn.setBounds(47, 76, 283, 324);
		viewRecipe.add(ingredientsScrlPn);
		
		JLabel ingredientsLbl = new JLabel("Ingredients");
		ingredientsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		ingredientsLbl.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
		ingredientsScrlPn.setColumnHeaderView(ingredientsLbl);
		
		ingredientsTxtArea = new JTextArea();
		ingredientsTxtArea.setWrapStyleWord(true);
		ingredientsTxtArea.setLineWrap(true);
		ingredientsTxtArea.setRows(7);
		ingredientsTxtArea.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		
		ingredientsScrlPn.setViewportView(ingredientsTxtArea);
		
		JScrollPane instructionsScrlPn = new JScrollPane();
		instructionsScrlPn.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		instructionsScrlPn.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		instructionsScrlPn.setBounds(364, 76, 395, 324);
		viewRecipe.add(instructionsScrlPn);
		
		JLabel instructionsLbl = new JLabel("Instructions");
		instructionsLbl.setHorizontalAlignment(SwingConstants.CENTER);
		instructionsLbl.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
		instructionsScrlPn.setColumnHeaderView(instructionsLbl);
		
		instructionsTxtArea = new JTextArea();
		instructionsTxtArea.setWrapStyleWord(true);
		instructionsTxtArea.setLineWrap(true);
		instructionsTxtArea.setRows(7);
		instructionsTxtArea.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		instructionsScrlPn.setViewportView(instructionsTxtArea);
		return viewRecipe;
	}

	private JPanel addNewRecipeTab(JTabbedPane tabbedPane) {
		JPanel addNewRecipe = new JPanel();
		tabbedPane.addTab("Add New Recipe", null, addNewRecipe, null);
		enterNameTxtF = new JTextField();
		enterNameTxtF.setBounds(56, 78, 326, 39);
		addNewRecipe.add(enterNameTxtF);
		enterNameTxtF.setColumns(10);
		
		enterWebsiteTxtF = new JTextField();
		enterWebsiteTxtF.setBounds(433, 78, 318, 39);
		addNewRecipe.add(enterWebsiteTxtF);
		enterWebsiteTxtF.setColumns(10);
		
		enterIngredientTxtF = new JTextField(); // Change to JList - Keep as is
		enterIngredientTxtF.setBounds(56, 192, 257, 39);
		addNewRecipe.add(enterIngredientTxtF);
		enterIngredientTxtF.setColumns(10);
		
		enterQntyTxtF = new JTextField();
		enterQntyTxtF.setBounds(362, 192, 202, 39);
		addNewRecipe.add(enterQntyTxtF);
		enterQntyTxtF.setColumns(10);
		
		JButton addIngredientBtn = new JButton("Add Ingredient");
		addIngredientBtn.addActionListener(new ActionListener() {
			

			private String str; // Ingredient variable
			private String str2; // Quantity

			public void actionPerformed(ActionEvent e) {
				
				str = enterIngredientTxtF.getText();
				str2 = enterQntyTxtF.getText();
				
				rList.add(str2 + "::" + str);
				enterIngredientTxtF.setText("");
				enterQntyTxtF.setText("");
				
				for (String el : rList) {
					System.out.println(el);
				}
				//System.out.println("Ingredient added: " + str + " quantity: " + str2);
			}
		});
		addIngredientBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		addIngredientBtn.setBounds(615, 191, 136, 39);
		addNewRecipe.add(addIngredientBtn);
		
		enterInstrTxtF = new JTextField();
		enterInstrTxtF.setBounds(56, 308, 695, 39);
		addNewRecipe.add(enterInstrTxtF);
		enterInstrTxtF.setColumns(10);
		
		addRecipeBtn = new JButton("Add Recipe");
		addRecipeBtn.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				rList2 = new String[rList.size()];
				
				String str2 = enterNameTxtF.getText();
				String str3 = enterWebsiteTxtF.getText();
				str6 = enterInstrTxtF.getText();
				
				for (int i = 0; i < rList.size(); i++) {
					rList2[i] = rList.get(i);
				}
				newRecipe2 = new Recipe(str2, rList2, str6.split("::"), str3);
				recipes.addRecipe(newRecipe2);
				System.out.println("Recipe added: " + newRecipe2.getName());
				
				//model.addElement("           " + newRecipe2.getName()); // Space added at beginning to help with positioning the text in JList

				addRecipeToViewAll(newRecipe2);
				enterNameTxtF.setText("");
				enterWebsiteTxtF.setText("");
				enterInstrTxtF.setText("");
				rList.clear();
			}
		});
		addRecipeBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		addRecipeBtn.setBounds(332, 382, 157, 39);
		addNewRecipe.add(addRecipeBtn);
		
		JLabel enterRecipeName = new JLabel("Enter Recipe Name:");
		enterRecipeName.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		enterRecipeName.setBounds(56, 52, 169, 26);
		addNewRecipe.add(enterRecipeName);
		
		JLabel enterRecipeWebsite = new JLabel("Enter Recipe Website (Optional):");
		enterRecipeWebsite.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		enterRecipeWebsite.setBounds(433, 52, 202, 26);
		addNewRecipe.add(enterRecipeWebsite);
		
		JLabel enterIngredient = new JLabel("Enter Ingredient:");
		enterIngredient.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		enterIngredient.setBounds(56, 169, 142, 26);
		addNewRecipe.add(enterIngredient);
		
		JLabel enterQuantity = new JLabel("Enter Quantity / Units:");
		enterQuantity.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		enterQuantity.setBounds(362, 172, 181, 21);
		addNewRecipe.add(enterQuantity);
		
		JLabel enterInstructions = new JLabel("Enter Instructions");
		enterInstructions.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		enterInstructions.setBounds(56, 284, 115, 26);
		addNewRecipe.add(enterInstructions);
		
		JLabel separateInstructions = new JLabel("(Separate individual steps with ' :: ' ):");
		separateInstructions.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 13));
		separateInstructions.setBounds(173, 284, 413, 26);
		addNewRecipe.add(separateInstructions);
		return addNewRecipe;
	}
	
	public void addRecipeToViewAll(Recipe rec) {
		model.addElement("          " + rec.getName());
		allRecipesJList.setModel(model);
	}
}
