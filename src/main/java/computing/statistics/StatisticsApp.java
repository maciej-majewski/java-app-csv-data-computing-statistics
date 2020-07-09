package computing.statistics;

import com.opencsv.CSVReaderBuilder;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class StatisticsApp {

    public void run(String[] args) {

        CommandLine line = parseArguments(args);
        if (line.hasOption("filename")) {

            System.out.println(line.getOptionValue("filename"));
            String fileName = line.getOptionValue("filename");

            double[] data = readData(fileName);
            // double[] data = readData("src\\main\\resources\\data.csv");
            calculateAndPrintStats(data);

        } else {
            printAppHelp();
        }

    }

    //  @return <code>CommandLine</code> which represents a list of application arguments.
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


     // @return array of double values
    private double[] readData(String fileName) {

        var data = new ArrayList<Double>();
        double[] mydata = null;

        try (var reader = Files.newBufferedReader(Paths.get(fileName));
             var csvReader = new CSVReaderBuilder(reader).build()) {

            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {

                for (String e : nextLine) {

                    data.add(Double.parseDouble(e));
                }
            }

            mydata = ArrayUtils.toPrimitive(data.toArray(new Double[0]));

        } catch (IOException ex) {

            System.err.println("Failed to read file");
            System.err.println(ex.toString());
            System.exit(1);
        }

        return mydata;
    }


    // @return application <code>Options</code>
    private Options getOptions() {

        var options = new Options();

        options.addOption("f", "filename", true, "file name to load data from");
        return options;
    }

    private void printAppHelp() {

        Options options = getOptions();

        var formatter = new HelpFormatter();
        formatter.printHelp("StatisticsApp", options, true);
    }

    private void calculateAndPrintStats(double[] data) {

        System.out.format("Geometric mean: %f%n", StatUtils.geometricMean(data));
        System.out.format("Arithmetic mean: %f%n", StatUtils.mean(data));
        System.out.format("Max: %f%n", StatUtils.max(data));
        System.out.format("Min: %f%n", StatUtils.min(data));
        System.out.format("Sum: %f%n", StatUtils.sum(data));
        System.out.format("Variance: %f%n", StatUtils.variance(data));
    }
}
