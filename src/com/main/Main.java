package com.main;

import java.util.Scanner;

import com.utils.DataMng;
import com.utils.Importer;

public class Main {

	public static void main(String[] args) {
		double startTime = System.currentTimeMillis();
		int line = 370000;
		DataMng graph = new DataMng("data/GBPUSD1m.txt", ",",line);
		graph.patternSaver(0);
		Scanner key = new Scanner(System.in);
		while (line < graph.getTotalSize()) {
			Importer.importPoint("data/GBPUSD1m.txt", ",", graph, line);
			graph.patternSaver(line);
			graph.currentPattern();
			graph.patternRecog(line);

			double finishTime = System.currentTimeMillis();

			graph.renderGraph();

			System.out.println("Time: " + (finishTime - startTime) / 1000 + " Seconds");
			startTime = finishTime;
			//key.next();
			line++;
			System.out.println("Number Of Samples: " + graph.getSamples());
			System.out.println(" " + graph.getAccuracy() + "%");
			System.out.println("Out of " + line);

		}
		System.out.println("Total Of Samples: " + graph.getSamples());
		System.out.println(" " + graph.getAccuracy() + "%");
		double finishTime = System.currentTimeMillis();
		System.out.println("Total Time: " + (finishTime - startTime) / 1000 + " Seconds");

	}
}
