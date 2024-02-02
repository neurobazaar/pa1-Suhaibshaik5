package csc435.app;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WordCount {

    private static long B_Scanned = 0;
    private static long Start_Time;
    private static long End_Time;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: Please enter in this format java DataCleaner  <inputDirPath> <outputDirPath>");
            System.exit(1);
        }
        String IP_dir_Path_Val = args[0];
        String OP_dir_Path_Val = args[1];
        processDirectory(IP_dir_Path_Val, OP_dir_Path_Val);
        }

    private static void processDirectory(String inputDirPath, String outputDirPath) {
        File IP_dir_Path_Val = new File(inputDirPath);
        File[] files = IP_dir_Path_Val.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String subInputDir = file.getAbsolutePath();
                    String subOutputDir = outputDirPath + File.separator + file.getName();
                    new File(subOutputDir).mkdirs();
                    System.out.println("\t"+file.getName()+"\n");
                    processDirectory(subInputDir, subOutputDir);
                } else if (file.isFile()) {
                    Start_Time = System.nanoTime();
                    countWordsInFile(file, outputDirPath);
                    End_Time = System.nanoTime();
            
                    double Duration_sec = (End_Time - Start_Time) / 1e9;
                    double DataSize_MiB = B_Scanned / (1024.0 * 1024.0);
                    double Throughput = DataSize_MiB / Duration_sec;
                    System.out.printf("\t"+file.getName()+"\n");
                  System.out.printf("Total data size: %.2f MiB,\tTotal time: %.2f seconds,\tThroughput: %.2f MiB/second\n",  DataSize_MiB, Duration_sec, Throughput);
              
                }
            }
        }
    }

    private static void countWordsInFile(File inputFile, String OP_dir_Path_Val) {
        Map<String, Integer> wordCounts = new HashMap<>();
        Path outputPath = Paths.get(OP_dir_Path_Val, inputFile.getName());

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                B_Scanned += line.getBytes().length;
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                    }
                }
            }

            for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
