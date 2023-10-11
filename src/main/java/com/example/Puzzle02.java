package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class Puzzle02 {
  private static URL resource = Puzzle01.class.getResource("/puzzleinput02.txt");

  private static HashMap<Character, Integer> pointMap;
  static {
    pointMap = new HashMap<Character, Integer>();
    pointMap.put('X', 1);
    pointMap.put('Y', 2);
    pointMap.put('Z', 3);
    pointMap.put('A', 1);
    pointMap.put('B', 2);
    pointMap.put('C', 3);
  }

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
    System.out.println("Solution 1: " + solve01());
    // System.out.println("Solution 1: " + solve02());
  }

  public static int getPointsForRound(char you, char opponent) {
    if (pointMap.get(you) == pointMap.get(opponent)) {
      // draw
      return 3 + pointMap.get(you);
    }

    if ((you == 'X' && opponent == 'C') || (you == 'Y' && opponent == 'A') || (you == 'Z' && opponent == 'B')) {
      // you win
      return 6 + pointMap.get(you);
    }

    // you lose
    return pointMap.get(you);
  }

  public static int solve01() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    int totalPoints = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      totalPoints += getPointsForRound(line.charAt(2), line.charAt(0));
    }
    scanner.close();

    return totalPoints;
  }
}
