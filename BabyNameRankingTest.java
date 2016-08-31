package pp2Final;

//Eric Madsen CSCI 1302-A Final Project JUnit Tests
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

public class BabyNameRankingTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testFindRankingMethods() {
		String year = "2004";
		String gender = "female";
		String name = "Maria";
		assertTrue(BabyNameRanking.findRank(year, gender, name).equals(
				findRank(year, gender, name)));
		assertTrue(BabyNameRanking.findTop5(year, gender).equals(
				findTop5(year, gender)));
		
	}
		public String findRank(String year, String gender, String name) {
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
		
		public static String findTop5(String year, String gender) {
			String[] boyList = new String[5];
			String[] girlList = new String[5];
			String result = "";

			try {
				URL url = new URL("http://www.cs.armstrong.edu/liang/data/babynamesranking" +year+ ".txt");
				Scanner input = new Scanner(url.openStream());
				int i = 0;
				while(input.hasNext() && i < 5) {
					String line = input.nextLine();
					String[] tokens = line.trim().split("\t");
					boyList[i] = tokens[0]+ "  " +tokens[1];
					girlList[i] = tokens[0]+ "  " +tokens[3];
					i++;
				}
				input.close();
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
}


