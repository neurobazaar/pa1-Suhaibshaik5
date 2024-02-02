package csc435.app;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class CleanDataset {

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
             clean_dataset(IP_dir_Path_Val, OP_dir_Path_Val);
            
        }

    private static void clean_dataset(String IP_dir_Path_Val, String OP_dir_Path_Val) {
            File directory = new File(IP_dir_Path_Val);
            File[] files = directory.listFiles();
    
            if (files != null) {
	       for (File file : files)
		   if (file.isDirectory()) {
		   System.out.println("\t"+file.getName()+"\n");
		   clean_dataset(file.getAbsolutePath(), OP_dir_Path_Val + File.separator + file.getName());
		   } else {  
		         Start_Time = System.nanoTime();
		         File_DataCleaning(file, OP_dir_Path_Val);
		         End_Time = System.nanoTime();
		     
		         double Duration_sec = (End_Time - Start_Time) / 1e9;
		         double DataSize_MiB = B_Scanned / (1024.0 * 1024.0);
		         double Throughput = DataSize_MiB / Duration_sec;
		         System.out.printf("\t"+file.getName()+"\n");
		         System.out.printf("Total data size: %.2f MiB,\tTotal time: %.2f seconds,\tThroughput: %.2f MiB/second\n",  DataSize_MiB, Duration_sec, Throughput);
							              
		      }
		    }
	          } 


    private static void File_DataCleaning(File inputFile, String OP_dir_Path_Val) {
        Path outputPath = Paths.get(OP_dir_Path_Val, inputFile.getName());
        outputPath.toFile().getParentFile().mkdirs(); 
          try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	         BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
	            String line;
		     while ((line = reader.readLine()) != null) {
		         String cleanedLine = File_Line_DataCleaning(line);
		         writer.write(cleanedLine);
                         writer.newLine();
                 }
         } catch (IOException e) {
		         e.printStackTrace();
		      }
            B_Scanned += inputFile.length(); 
        }

    private static String File_Line_DataCleaning(String line) {
            line = line.replace('\r', ' '); 
            line = line.replaceAll("[^\\w\\s]", ""); 
            line = line.replaceAll("\\s+", " "); 
            return line;
        }
    }
 
