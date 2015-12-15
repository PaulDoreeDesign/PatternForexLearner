package com.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/*
 * Class Imports from a file to points and data
 */
public class Importer {
	
	/*
	 * @param filepath
	 * @param delimiter
	 * @param graph
	 * Imports the files and add the points to the graph
	 */
	public static void importFile(String path, String delim, DataMng graph){
		String currentLine;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(path));
			while((currentLine = br.readLine()) != null ){
					graph.insertPoint(loadPoint(currentLine, delim));
					
				
				
				
			}
				
		} catch (IOException e) {
				e.printStackTrace();
		}
		
		
		
	}
	
	/*
	 * @param filepath
	 * @param delimiter
	 * @param graph
	 * @param line
	 * Imports a point from a line
	 */
	public static void importPoint(String path, String delim,DataMng graph, int line){
		String currentLine;
		BufferedReader br;
		int count = 1;
		try {
			br = new BufferedReader(new FileReader(path));
			while((currentLine = br.readLine()) != null ){
				
				if(count++ == line){
					graph.insertPoint(loadPoint(currentLine, delim));
					
				}
				
				
			}
				
		} catch (IOException e) {
				e.printStackTrace();
		}
		
		
		
	}
	/*
	 * Loads the points specified in a String
	 * @param line of File
	 * @param delimiter
	 */
	private static double loadPoint(String line, String delim){
		String[] data = line.split(delim);
		double bid = Double.parseDouble(data[1].trim());
		double ask = Double.parseDouble(data[2].trim());
		return (bid+ask)/2;
		
	}
	


}
