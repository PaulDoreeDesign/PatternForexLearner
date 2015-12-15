package com.utils;

import java.util.ArrayList;
import java.util.List;

import fundamentos.Grafica;

public class DataMng {
	private Grafica graph = new Grafica();

	private List<Double> allPoints = new ArrayList<Double>();
	private List<Double> points = new ArrayList<Double>();
	private List<List<Double>> patternList = new ArrayList<List<Double>>();
	private List<Double> perfomanceList = new ArrayList<Double>();
	private List<Double> patternToRec = new ArrayList<Double>();
	private List<Double> accuracyList = new ArrayList<Double>();
	
	private int samples = 0;

	public final int PATTSIZE = 30;

	/*
	 * @param path
	 * 
	 * @param delimiter Imports the file into the graph.
	 */
	public DataMng(String path, String del) {
		Importer.importFile(path, del, this);
		allPoints = points;
	}

	public DataMng(String path, String del, int stop) {
		Importer.importFile(path, del, this);
		allPoints = points;
		points = points.subList(0, stop);

	}

	public DataMng() {

	}

	public void insertPoint(double p) {
		points.add(p);
	}

	public double percentChange(double start, double end) {

		if (start == 0.0 || end == 0.0) {
			start += 0.1;
			end += 0.1;
		}
		double d = (end - start) / Math.abs(start) * 100.0;
		return d;

	}

	/*
	 * Finds pattern looking in the next 10 positions. And stores the pattern
	 * (in percent change)and the avrgoutcome.
	 */
	public void patternSaver(int start) {

		int x = points.size() - (PATTSIZE * 2);// The last point

		for (int y = Math.abs(start - PATTSIZE - 1); y < x; y++) {
			List<Double> pattern = new ArrayList<Double>();

			// Saving in pattern, the pattern.
			for (int i = PATTSIZE - 1; i > -1; i--) {
				pattern.add(percentChange(points.get(y - PATTSIZE), points.get(y - i)));
			}

			// Adding future points from 30-60
			List<Double> outcomeRange = new ArrayList<Double>();
			for (int i = PATTSIZE; i < (PATTSIZE * 2); i++) {
				outcomeRange.add(points.get(y + i));
			}

			// Getting the average of the future points
			double currentPoint = points.get(y);
			double avgOutcome = 0;
			for (double p : outcomeRange) {
				avgOutcome += p;
			}
			avgOutcome = (avgOutcome / outcomeRange.size());
			double futureOutcome = percentChange(currentPoint, avgOutcome);

			patternList.add(pattern);
			perfomanceList.add(futureOutcome);

		}

	}

	/*
	 * Saves into the list PatternToRec the current pattern that we want to
	 * recognize.
	 */
	public void currentPattern() {
		patternToRec.clear();
		for (int i = PATTSIZE; i > 0; i--) {
			patternToRec.add(percentChange(points.get((points.size() - 1) - PATTSIZE), points.get(points.size() - i)));

		}
	}

	/*
	 * Finds the best matches between the patterns that we already have and the
	 * pattern that we want to recognize.
	 */
	public void patternRecog(int start) {

		List<Double> predOutcome = new ArrayList<Double>();
		boolean patFound = false;

		for (List<Double> pattern : patternList) {
			//Calculates the percent change between the pattern and the other patterns. Then Average all the percents
			double howSim = 0;

			for (int i = 0; i < PATTSIZE; i++) {
				howSim += (100.0 - (Math.abs(percentChange(pattern.get(i), patternToRec.get(i)))));
			}
			howSim = howSim / PATTSIZE;
			
			if (howSim > 70) {
				int pattIndex = patternList.indexOf(pattern);
				
				int i = 0;
				for(double d : pattern)
					graph.inserta(i++, d);
				graph.otraGrafica();
				
				predOutcome.add(perfomanceList.get(pattIndex));
				
				patFound = true;
				

			}




		}
		
		
		//Average real outomce
		if(patFound){
			
			
			List<Double> realOutcomeList = allPoints.subList(start + 20, start + 30);
			double avgRealOutcome = 0;
			for (double p : realOutcomeList)
				avgRealOutcome += p;
			avgRealOutcome /= realOutcomeList.size();
			double realMovement = percentChange(allPoints.get(start), avgRealOutcome);
			
			graph.ponLineas(false);
			graph.inserta(35, realMovement);
			graph.ponLineas(true);
			graph.otraGrafica();

			// Average predicted outcome
			double avgPredOutcome = 0;
			for (double d : predOutcome)
				avgPredOutcome += d;
			avgPredOutcome /= predOutcome.size();
			
			
			//Accurate Stuff
			samples++;
			if(avgPredOutcome > patternToRec.get(PATTSIZE-1) && realMovement > patternToRec.get(PATTSIZE-1)){
				accuracyList.add(100.0);
			}else if(avgPredOutcome < patternToRec.get(PATTSIZE-1) && realMovement < patternToRec.get(PATTSIZE-1)){
				accuracyList.add(100.0);
			}else{
				accuracyList.add(0.0);
			}
			
			graph.ponColor(Grafica.rosa);
			graph.ponLineas(false);
			if(!predOutcome.isEmpty())
				graph.inserta(35, avgPredOutcome);
			graph.otraGrafica();
			graph.ponLineas(true);
			predOutcome.clear();
		}
		

	}

	/*
	 * Renders the graphic
	 * 
	 */
	public void renderGraph() {
		graph.pinta();
		graph = new Grafica();
	}

	public int getSize() {
		return points.size();
	}

	public int getTotalSize() {
		return allPoints.size();
	}

	public static double roundDown3(double d) {
		return (d * 1e3) / 1e3;
	}
	
	public double getAccuracy(){
		double total = 0;
		for(double d : accuracyList){
			total += d;
		}
		total /= samples;
		return total;
	}
	
	public int getSamples(){
		return samples;
	}

}
