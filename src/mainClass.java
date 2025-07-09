import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class mainClass {

	public static void main(String[] args) throws IOException {
		// Scanner input to find if user or admin
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Pick your login\n-----------\n1. User\n2. Admin");
		String choice = keyboard.nextLine();
		boolean notClosed = true;
		// Selection between user and admin
		while(notClosed) {
			if(choice.equals("1")) {// if login is user
				System.out.println("Pick an option:\n-----------\n");
				// uses scanner to find if VRN needs to be checked
				System.out.println("1. Check VRN,\n2. Save Daily VRN,\n3. Close");
				String c1 = keyboard.nextLine();
				if(c1.equals("1")) {
					// Runs the VRN method to check the info
					System.out.println("VRN info is " + getVRNInfo(readRegNumber())+"\n");
				}
				if(c1.equals("2")) {
					saveVRNtoDailyVRN(readRegNumber());// Gets the VRN number from the camera and saves it in database
					System.out.println("Saved VRN\n");			
				}
				if(c1.equals("3")) {
					notClosed = false;
				}
			}
			// if admin
			else if(choice.equals("2")) {
				System.out.println("Pick an option:\n-----------");
				System.out.println("1. Display vehicles,\n2. Add notes to vehicle,\n3. Remove records,\n4. Close\n-----------");
				String c2 = keyboard.nextLine();
				// Display list of vehicles
				if(c2.equals("1")) {
					displayRecordsAdmin();
				}
				// Add notes
				if(c2.equals("2")) {
					System.out.println("What comment would you like to add");
					String com = keyboard.nextLine(); 
					addNotesOfIntrest(com);
				}
				// Remove records
				if(c2.equals("3")) {
					removeRecord("BV65KNZ");
				}
				if(c2.equals("4")) {
					notClosed = false;
				}
				
			}
		}
		keyboard.close();
	}// VRN camera pickup
	public static String readRegNumber() throws IOException {
		// reads the file vrn camera where all the camera reading go
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\quaid\\eclipse-workspace\\ANPR\\src\\VRNCamera.txt"));
		String str = br.readLine(); // reads the first line
		br.close();
		return str; // returns the first line of the vrn
	}
	// Daily VRN - VRN, Date, Time, Comment
	// Gets the VRN info from the vehicle info file
	public static String getVRNInfo(String VRN) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\quaid\\eclipse-workspace\\ANPR\\src\\VehicleInfo.txt"));
		String line = br.readLine();
		boolean inData = false;
		String info = null;
		while (line != null) { // Checks the whole database
			String substr = line.substring(0,9); // Checks the first 10 characters of the string
			if (substr.equals(VRN)) {
				info = line; // sets the line to info
				inData = true;
			}	
			line = br.readLine(); // Used to read next line
		}
		if(inData == false) { // if it is not in the database then this goes
			br.close();
			return "not in database";
		}
		br.close();
		return info;
	}
	// "\"SP\""
	// displays all records, or specific records depending on comments
	public static void displayRecordsAdmin() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\quaid\\eclipse-workspace\\ANPR\\src\\VehicleInfo.txt"));
		String line = br.readLine();
		while(line != null) { // goes through every line in the file
			System.out.println(line); // prints every line out
			line = br.readLine();
		}
		br.close();
	}
	public static void saveVRNtoDailyVRN(String VRN) throws IOException {
		// creates a str with the vrn and comma
		String str = VRN + ",";
		// opens as writer of daily vrn file and true means its not overwritten false means overwrite
	    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\quaid\\eclipse-workspace\\ANPR\\src\\DailyVRNData.txt", true));
	    writer.append(str + "\n"); // adds to the next free space in the file and makes a new line for the next
	    writer.close();
	}
	// ADD new comments, Update comments and remove comments
	public static void addNotesOfIntrest(String comment) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\quaid\\eclipse-workspace\\ANPR\\src\\DailyVRNData.txt", true));
	    writer.append(comment); // adds to the next free space in the file and makes a new line for the next
	    writer.close();
	}
	public static void removeRecord(String VRN) throws IOException {
		File inputFile = new File("C:\\Users\\quaid\\eclipse-workspace\\ANPR\\src\\VehicleInfo.txt");
        File tempFile = new File("C:\\Users\\quaid\\eclipse-workspace\\ANPR\\src\\temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = "\""+VRN+"\"";
        String currentLine;
        System.out.println(lineToRemove);
        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.substring(0,9);

            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close(); 
        reader.close(); 
        boolean successful = tempFile.renameTo(inputFile);
        System.out.println(successful);
	}
	
}
	
