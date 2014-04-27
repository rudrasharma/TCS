package org.csu.cs517.tmcs.simulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

public class MainMenu {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	  private static Logger logger = Logger.getLogger(MainMenu.class);

	public String getValidFile(String fileType){
		print("Please specify the "+fileType+" file");
			String fileName = null;
			try {
				fileName = br.readLine();
				while(!existingFile(fileName)){
					print("The file specified:"+fileName+" is not valid, please re-input the "+fileType+" file" );
					fileName = br.readLine();
				}				
			} catch (IOException e) {
				logger.error(e);
			}
			
		return fileName;

	}
	public void printMainMenu(){
		print("MAIN MENU");
		print("1) Load default configuration map");
		print("2) Specify input file");
	}
	public void printEventMenu(){
		print("EVENT MENU");
		print("1) Load default event file");
		print("2) Specify input file");
	}	
	private boolean existingFile(String filePath){
		boolean fileExists = false;
		Path path = Paths.get(filePath);
		if(Files.isRegularFile(path)){
			fileExists = true;
		}
		return fileExists;
	}
	private void print(String str){
		System.out.println(str);
	}
	public Integer getNumericalInput(int maxEntry){
		Integer inputInt = null;
		try {
			print("Please input a selection between 1 and "+maxEntry);
			String input = br.readLine();
			while (inputInt == null) {
				try {
					Integer parsedInt = Integer.parseInt(input);
					if (parsedInt > 0 && parsedInt <= maxEntry) {
						inputInt = parsedInt;
					}else{
						print(input	+ " is not a valid selection please enter a number between 1 and " + maxEntry);
						input = br.readLine();
					}
				} catch (NumberFormatException e) {
					print(input	+ " is not a number please enter a number between 1 and " + maxEntry);
					input = br.readLine();
				}
			}
			
		} catch (IOException e) {
			logger.error(e);
		}
		return inputInt;
	}
}
