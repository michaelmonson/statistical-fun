package com.sirorlando;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Created a console application to compute basic statistics upon
 * a series of data values. The application takes a CSV file of data 
 * as its single argument.
 * 
 * @author michaelmonson
 *
 */
public class StatisticsApplication {

	/**
     * Runs the Statistical Analysis application based on 
     * a CSV file that is included in the application.
     * 
     * <p>For the purposes of this exercise, user data cannot
     * be created in the CSV file, so it must be altered manually.</p>
     * 
     * However, any command line arguments supplied by the user
     * when running the application <i>will</i> be passed through and used.
     * Specifically the filename. 
     *
     * @param args an array of String arguments to be parsed
     */
    public void run(String[] userArgs) {

        CommandLine line = parseArguments(userArgs);

        if (line.hasOption("filename")) {	//Mandatory filename. 

            System.out.println(line.getOptionValue("filename"));
            String fileName = line.getOptionValue("filename");

            double[] data = readData(fileName);
            calculateAndPrintStats(data);

        } else {							//If not supplied, we provide a help prompt.
            printAppHelp();
        }
    }

    /**
     * Parses application arguments.
     * 
     * <p>CommandLineParser parses the command line arguments.
     *    The application exits if there is a ParseException.
     *    The parser returns parsed arguments in CommandLine object.</p>
     *
     * @param args application arguments
     * @return <code>CommandLine</code> which represents a list of application arguments.
     */
    private CommandLine parseArguments(String[] args) {

        Options options = getOptions();
        CommandLine line = null;

        CommandLineParser parser = new DefaultParser();

        try {
            line = parser.parse(options, args);

        } catch (ParseException ex) {

            System.err.println("Failed to parse command line arguments");
            System.err.println(ex.toString());
            printAppHelp();

            System.exit(1);
        }

        return line;
    }

    /**
     * Reads application data from a file
     *
     * @param fileName file of application data
     * @return array of double values
     */
    private double[] readData(String fileName) {

        ArrayList<Double> data = new ArrayList<Double>();
        double[] mydata = null;

        // CSVReader is used to read CSV data:
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName));
             CSVReader csvReader = new CSVReaderBuilder(reader).build()) {

            String[] nextLine;

            //Read the CSV file line by line; parse data into a list of Double values:
            while ((nextLine = csvReader.readNext()) != null) {
                for (String e : nextLine) {
                	data.add(Double.parseDouble(e));                	
                }
            }

            /*
             * We need primitive data types to calculate the statistics; 
             * therefore, we transform list into an array of primitive values. 
             * ArrayUtils comes from the Apache Commons Lang library.
             */
            mydata = ArrayUtils.toPrimitive(data.toArray(new Double[0]));

        } catch (IOException ex) {

            System.err.println("Failed to read file");
            System.err.println(ex.toString());
            System.exit(1);
        }

        return mydata;
    }

    /**
     * Generates application command line options
     *
     * @return application <code>Options</code>
     */
    private Options getOptions() {

        Options options = new Options();

        options.addOption("f", "filename", true, "file name to load data from");
        return options;
    }

    /**
     * Prints application help.  Uses Apache's HelpFormatter to accomplish this.
     */
    private void printAppHelp() {

        Options options = getOptions();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("JavaStatisticsMain", options, true);
    }

    /**
     * Calculates and prints data statistics
     *
     * @param data input data
     */
    private void calculateAndPrintStats(double[] data) {

        System.out.format("Geometric mean: %f%n", StatUtils.geometricMean(data));
        System.out.format("Arithmetic mean: %f%n", StatUtils.mean(data));
        System.out.format("Max: %f%n", StatUtils.max(data));
        System.out.format("Min: %f%n", StatUtils.min(data));
        System.out.format("Sum: %f%n", StatUtils.sum(data));
        System.out.format("Variance: %f%n", StatUtils.variance(data));
    }
	
	
}
