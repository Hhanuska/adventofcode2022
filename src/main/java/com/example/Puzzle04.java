package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Puzzle04 {
  private static URL resource = Puzzle04.class.getResource("/puzzleinput04.txt");

  public static ArrayList<String> readFile() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    ArrayList<String> lines = new ArrayList<String>();

    while (scanner.hasNextLine()) {
      lines.add(scanner.nextLine());
    }
    scanner.close();

    return lines;
  }

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
    ArrayList<String> lines = readFile();
    System.out.println("Solution 1: " + solve01(lines));
    System.out.println("Solution 1: " + solve02(lines));
  }

  public static int solve01(ArrayList<String> lines) {
    int counter = 0;

    for (String line : lines) {
      int[] sections = Arrays.stream(line.split("-|,")).mapToInt(Integer::parseInt).toArray();

      int firstRange = sections[1] - sections[0];
      int secondRange = sections[3] - sections[2];

      int widerStartIndex = Math.max(firstRange, secondRange) == firstRange ? 0 : 2;
      int narrowerStartIndex = widerStartIndex == 0 ? 2 : 0;

      if (sections[widerStartIndex] <= sections[narrowerStartIndex]
          && sections[widerStartIndex + 1] >= sections[narrowerStartIndex + 1]) {
        counter++;
      }
    }

    return counter;
  }

  public static int solve02(ArrayList<String> lines) {
    int counter = 0;

    for (String line : lines) {
      int[] sections = Arrays.stream(line.split("-|,")).mapToInt(Integer::parseInt).toArray();

      int lowerStartIndex = sections[0] <= sections[2] ? 0 : 2;
      int higherStartIndex = lowerStartIndex == 0 ? 2 : 0;

      if (sections[lowerStartIndex + 1] >= sections[higherStartIndex]) {
        counter++;
      }
    }

    return counter;
  }
}
