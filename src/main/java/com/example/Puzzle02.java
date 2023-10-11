package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

  private static HashMap<Character, Integer> outcomeMap;
  static {
    outcomeMap = new HashMap<>();
    outcomeMap.put('X', 0);
    outcomeMap.put('Y', 3);
    outcomeMap.put('Z', 6);
  }

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
    System.out.println("Solution 1: " + solve01());
    System.out.println("Solution 1: " + solve02());
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

  private static List<Character> orderedChars = Arrays.asList('A', 'B', 'C');

  public static char toPick(char opponent, char todo) throws IllegalArgumentException {
    int opponentIndex = orderedChars.indexOf(opponent);

    if (todo == 'X') {
      if (opponentIndex > 0) {
        return orderedChars.get(opponentIndex - 1);
      } else {
        return orderedChars.get(opponentIndex + 2);
      }
    }

    if (todo == 'Y') {
      return opponent;
    }

    if (todo == 'Z') {
      if (opponentIndex < 2) {
        return orderedChars.get(opponentIndex + 1);
      } else {
        return orderedChars.get(opponentIndex - 2);
      }
    }

    throw new IllegalArgumentException("Todo must equal to X, Y or Z");
  }

  public static int solve02() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    int totalPoints = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      char toPick = Puzzle02.toPick(line.charAt(0), line.charAt(2));
      int toAdd = pointMap.get(toPick) + outcomeMap.get(line.charAt(2));
      totalPoints += toAdd;
    }
    scanner.close();

    return totalPoints;
  }
}
