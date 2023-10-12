package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Puzzle01 {
  private static URL resource = Puzzle01.class.getResource("/puzzleinput01.txt");

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
    System.out.println("Solution 1: " + solve01());
    System.out.println("Solution 2: " + solve02());
  }

  public static int solve01() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    int max = 0;
    int current = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      if (line.isEmpty()) {
        if (current > max) {
          max = current;
        }
        current = 0;
      } else {
        current += Integer.parseInt(line);
      }
    }
    scanner.close();

    return max;
  }

  public static int solve02() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    ArrayList<Integer> caloriesList = new ArrayList<Integer>();

    int current = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      if (line.isEmpty()) {
        caloriesList.add(current);

        current = 0;
      } else {
        current += Integer.parseInt(line);
      }
    }
    scanner.close();

    caloriesList.sort(Comparator.reverseOrder());

    return caloriesList.get(0) + caloriesList.get(1) + caloriesList.get(2);
  }
}
