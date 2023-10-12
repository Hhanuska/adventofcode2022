package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Puzzle03 {
  private static URL resource = Puzzle03.class.getResource("/puzzleinput03.txt");

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
    System.out.println("Solution 1: " + solve01());
    System.out.println("Solution 2: " + solve02());
  }

  public static int getPriority(char character) {
    int ascii = character;

    if ((int) 'a' <= ascii && ascii <= (int) 'z') {
      return ascii - (int) 'a' + 1;
    }

    if ((int) 'A' <= ascii && ascii <= (int) 'Z') {
      return ascii - (int) 'A' + 27;
    }

    return 0;
  }

  public static int solve01() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    int priorityTotal = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      String firstHalf = line.substring(0, line.length() / 2);
      String secondHalf = line.substring(line.length() / 2);

      for (int i = 0; i < firstHalf.length(); i++) {
        char currentChar = firstHalf.charAt(i);
        if (secondHalf.indexOf(currentChar) != -1) {
          priorityTotal += getPriority(currentChar);
          break;
        }
      }
    }
    scanner.close();

    return priorityTotal;
  }

  public static List<String> readFile() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    List<String> lines = new ArrayList<String>();

    while (scanner.hasNextLine()) {
      lines.add(scanner.nextLine());
    }
    scanner.close();

    return lines;
  }

  public static int solve02() throws URISyntaxException, FileNotFoundException {
    List<String> lines = readFile();

    int priorityTotal = 0;

    for (int i = 2; i < lines.size(); i = i + 3) {
      String line = lines.get(i);
      for (int j = 0; j < line.length(); j++) {
        char currentChar = line.charAt(j);

        if (lines.get(i - 1).indexOf(currentChar) != -1 && lines.get(i - 2).indexOf(currentChar) != -1) {
          priorityTotal += getPriority(currentChar);
          break;
        }
      }
    }

    return priorityTotal;
  }
}
