package csc435.app;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class SortWordCount {

    private static long totalWordCount = 0;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SortWordCount <inputDir> <outputDir>");
            System.exit(1);
        }
        String inputDir = args[0];
        String outputDir = args[1];
        processDirectory(inputDir, outputDir);
    }

    private static void processDirectory(String inputDirPath, String outputDirPath) {
        File inputDir = new File(inputDirPath);
        File[] files = inputDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String subInputDir = file.getAbsolutePath();
                    String subOutputDir = outputDirPath + File.separator + file.getName();
                    new File(subOutputDir).mkdirs();
                    System.out.printf("\t" + file.getName() + "\n");
                    processDirectory(subInputDir, subOutputDir);
                } else if (file.isFile()) {
                    long startTime = System.nanoTime();
                    totalWordCount += countWordsInFile(file);
                    long endTime = System.nanoTime();

                    double durationInSeconds = (endTime - startTime) / 1e9;
                    double throughput = totalWordCount / durationInSeconds;
                    System.out.printf("\t" + file.getName() + "\n");
                    System.out.printf("Total words: %d,\tTotal time: %.2f seconds,\tThroughput: %.2f words/second\n",
                            totalWordCount, durationInSeconds, throughput);

                }
            }
        }
    }

    private static long countWordsInFile(File inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            return reader.lines()
                    .map(line -> line.split("\\s+"))
                    .filter(parts -> parts.length == 2)
                    .mapToInt(parts -> Integer.parseInt(parts[1]))
                    .sum();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static List<Map.Entry<String, Integer>> sortWords(Map<String, Integer> wordCounts) {
        return wordCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
