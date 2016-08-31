package pp2Final;

// Eric Madsen CSCI 1302-A Final Project

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BabyNameRanking extends Application {

	//creation of user entry fields and related variables
	Text tTitle = new Text("Baby Name Popularity Ranking for years 2001 - 2010");
	Text author = new Text("eric madsen");
	private ComboBox<String> cbYear = new ComboBox<String>(FXCollections.observableArrayList(
			"2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "All"));
	private RadioButton rbM = new RadioButton("Male");
	private RadioButton rbF = new RadioButton("Female");
	private TextField tfName = new TextField();
	private Button btClear = new Button("Clear");
	private Button btFindRank = new Button("Find Ranking");
	private Button btPrintTop = new Button("Print Top 5");
	private String gender;
	private String year;
	private String name;
	static ArrayList<String> boyNameList = new ArrayList<String>();
	static ArrayList<Integer> boyCountList = new ArrayList<Integer>();
	static ArrayList<String> girlNameList = new ArrayList<String>();
	static ArrayList<Integer> girlCountList = new ArrayList<Integer>();

	@Override // Override the start method in the Application class
	public void start(Stage stage) {
		
		//create and set up the all years arrays
		createArrays();
		
		//create UI set-up
		GridPane pane = new GridPane();
		HBox title = new HBox(650);
		HBox radioButtons = new HBox(50);
		HBox actionButtons = new HBox(50);
		tTitle.setFont(Font.font("Calibri",FontWeight.BOLD,20));
		author.setFont(Font.font("Calibri",FontWeight.THIN,12));
		radioButtons.setPadding(new Insets(5,5,5,5));
		actionButtons.setPadding(new Insets(25,25,25,25));
		pane.setHgap(25);
		pane.setVgap(25);
		pane.setAlignment(Pos.BASELINE_CENTER);

		//set toggle for radio buttons
		ToggleGroup group = new ToggleGroup();
		rbM.setToggleGroup(group);
		rbF.setToggleGroup(group);
		cbYear.setValue("2001");

		//populate pane
		title.getChildren().addAll(tTitle);
		pane.add(title, 2, 0);
		pane.add(new Label("Select a year:"), 0, 1);
		pane.add(cbYear, 2, 1);
		pane.add(new Label("Boy or girl?"), 0, 2);
		radioButtons.getChildren().addAll(rbM, rbF);
		pane.add(radioButtons, 2, 2);
		pane.add(new Label("Enter a name:"), 0, 3);
		pane.add(tfName, 2, 3);
		actionButtons.getChildren().addAll(btClear, btFindRank, btPrintTop);
		pane.add(actionButtons, 2, 4);
		pane.add(author, 0, 5);

		// Create scene and stage
		Scene scene = new Scene(pane, 600, 340);
		stage.setTitle("PP2 Final Project - Spring 2015");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();

		// Lambda event handler for clear button		
		btClear.setOnAction(e -> {
			rbM.setSelected(false);
			rbF.setSelected(false);
			cbYear.setValue("2001");
			tfName.clear();
		});

		// Lambda event handler for find rank button
		btFindRank.setOnAction(e -> {
			if(rbM.isSelected())
				gender = "male";
			else if(rbF.isSelected())
				gender = "female";

			//error pop-up if no gender is selected
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText(null);
				alert.setContentText("Please select a gender");
				alert.showAndWait();
				return;
			}

			year = cbYear.getValue();
			name = tfName.getText();

			//results pop-up
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Result");
			alert.setHeaderText(null);
			if(year.equals("All")) {
				alert.setContentText(findAllRank(gender, name));
			}
			else {
				alert.setContentText(findRank(year, gender, name));
			}
			alert.showAndWait();
		});

		// Lambda event handler for top 5 button
		btPrintTop.setOnAction(e -> {
			if(rbM.isSelected())
				gender = "male";
			else if(rbF.isSelected()) 
				gender = "female";

			//error pop-up if no gender is selected
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText(null);
				alert.setContentText("Please select a gender");
				alert.showAndWait();
				return;
			}
			year = cbYear.getValue();

			//results pop-up
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Result");
			alert.setHeaderText(null);
			if(year.equalsIgnoreCase("All")) {
				alert.setContentText(findAllTop5(gender));
			}
			else {
				alert.setContentText("Top 5 rankings for " +gender+ " in " +year+ 
						"\n" +findTop5(year, gender));
			}
			alert.showAndWait();
		});
	}
	/**
	 * Main Method
	 **/
	public static void main(String[] args) {
		launch(args);
	}

	//search method for find rank button
	protected static String findRank(String year, String gender, String name) {
		try {
			URL url = new URL("http://www.cs.armstrong.edu/liang/data/babynamesranking" +year+ ".txt");
			Scanner input = new Scanner(url.openStream());

			while(input.hasNext()) {
				String line = input.nextLine();
				String[] tokens = line.trim().split("\t");

				if(gender.equals("male")) {
					//search for boy names
					String boyName = tokens[1];
					if(name.compareToIgnoreCase(boyName)==0) {
						return name+ " is ranked #" +tokens[0]+ "in year " +year;
					}
				}
				else {
					//search for girl names
					if(gender.equals("female")) {
						String girlName = tokens[3].replace(" ", "");
						if(name.compareToIgnoreCase(girlName)==0) {
							return name+ " is ranked #" +tokens[0]+ "in year " +year;
						}
					}
				}
			}
		}
		catch(IOException e) {
			System.out.println(e);
		}

		return name+ " is not in the ranking for " +year;
	}

	//search method for find top 5 button
	protected static String findTop5(String year, String gender) {
		String[] boyList = new String[5];
		String[] girlList = new String[5];
		String result = "";

		try {
			URL url = new URL("http://www.cs.armstrong.edu/liang/data/babynamesranking" +year+ ".txt");
			Scanner input = new Scanner(url.openStream());
			int i = 0;

			//build arrays with the first 5 entries for boy and girl
			while(input.hasNext() && i < 5) {
				String line = input.nextLine();
				String[] tokens = line.trim().split("\t");
				boyList[i] = tokens[0]+ "  " +tokens[1];
				girlList[i] = tokens[0]+ "  " +tokens[3];
				i++;
			}
		}
		catch(IOException e) {
			System.out.println(e);
		}

		if(gender.equals("male")) {
			result = boyList[0]+ "\n" +boyList[1]+ "\n" +boyList[2]+
					"\n" +boyList[3]+ "\n" +boyList[4];
		}

		else {
			if(gender.equals("female")) {
				result = girlList[0]+ "\n" +girlList[1]+ "\n" +girlList[2]+
						"\n" +girlList[3]+ "\n" +girlList[4];
			}
		}
		return result;
	}

	//method for all years ranking search
	private static String findAllRank(String gender, String name) {
		if(gender.equals("male"))
			if(boyNameList.contains(name)) {
				return name+ " is ranked #" +(boyNameList.indexOf(name) + 1)+ " over all 10 years";
			}
			else
				return name+ " is not in the ranking for 2001 - 2010";
		else {
			if(girlNameList.contains(name)) {
				return name+ " is ranked #" +(girlNameList.indexOf(name) + 1)+ " over all 10 years";
			}
			else return name+ " is not in the ranking for 2001 - 2010";
		}
	}

	private static String findAllTop5(String gender) {
		if(gender.equals("male")) {
			return "Top 5 ranking for boy names from 2001 - 2010:\n1  "
					+boyNameList.get(0)+ "\n2  " +boyNameList.get(1)+
					"\n3  " +boyNameList.get(2)+ "\n4  " +boyNameList.get(3)+
					"\n5  " +boyNameList.get(4); 
			}
		else {
			return "Top 5 ranking for girl names from 2001 - 2010:\n1  "
					+girlNameList.get(0)+ "\n2  " +girlNameList.get(1)+
					"\n3  " +girlNameList.get(2)+ "\n4  " +girlNameList.get(3)+
					"\n5  " +girlNameList.get(4);
		}			
	}

	//method to create all years ranking arrays
	private static void createArrays() {
		ArrayList<String> boyNames = new ArrayList<String>();
		ArrayList<Integer> boyCounts = new ArrayList<Integer>(); 
		ArrayList<String> girlNames = new ArrayList<String>();
		ArrayList<Integer> girlCounts = new ArrayList<Integer>();
		String boyName = "";
		String girlName = "";
		int bcount = 0;
		int gcount = 0;
		int allYear = 2001;

		try {
			do {
				// sort through all files and build arraylist of boy and girl
				URL url = new URL("http://www.cs.armstrong.edu/liang/data/babynamesranking" +allYear+ ".txt");
				Scanner input = new Scanner(url.openStream());
				while (input.hasNext()) {
					String line = input.nextLine();
					String[] tokens = line.replace(",", "").split("\t");
					boyNames.add(tokens[1].trim());
					boyCounts.add(Integer.valueOf(tokens[2].trim()));
					girlNames.add(tokens[3].trim());
					girlCounts.add(Integer.valueOf(tokens[4].trim()));
					boyName = tokens[1].trim();
					girlName = tokens[3].trim();
					bcount = Integer.valueOf(tokens[2].trim());
					gcount = Integer.valueOf(tokens[4].trim());
					buildBoyList(boyNames, boyCounts, boyName, bcount);
					buildGirlList(girlNames, girlCounts, girlName, gcount);
				}

				allYear++;
			} while (allYear < 2010);
		} catch (IOException e) {
			System.out.println(e);
		}

	}
	
	//populate boy name and count arrays with a single name and sum of occurances
	private static void buildBoyList(ArrayList<String> names, ArrayList<Integer> counts, String name, int count) {
		if (boyNameList.contains(name))
			boyCountList.set(boyNameList.indexOf(name), boyCountList.get(boyNameList.indexOf(name)) + count);
		else {
			boyNameList.add(name);
			boyCountList.add(count);
		}
		sortArray(boyNameList, boyCountList);
	}
	
	//populate girl name and count arrays with a single name and sum of occurances
	private static void buildGirlList(ArrayList<String> names, ArrayList<Integer> counts, String name, int count) {
		if (girlNameList.contains(name))
			girlCountList.set(girlNameList.indexOf(name), girlCountList.get(girlNameList.indexOf(name)) + count);
		else {
			girlNameList.add(name);
			girlCountList.add(count);
		}
		sortArray(girlNameList, girlCountList);
	}
	
	//sort name and count arrays in decending order
	private static void sortArray(ArrayList<String> names, ArrayList<Integer> counts){
		 for (int i = 0;i < counts.size();i++){
		   int currentMin = counts.get(i);
		   int currentMinIndex = i;		      
		      for (int j = i + 1;j < counts.size();j++){
		    	  if(counts.get(j) > currentMin)
		    		  currentMinIndex = j;
		      }
		      counts.set(currentMinIndex, counts.get(i));
		      counts.set(i, currentMin);
		      String temp = names.get(currentMinIndex);
		      names.set(currentMinIndex, names.get(i));
		      names.set(i,temp);
		 }
	}
}

