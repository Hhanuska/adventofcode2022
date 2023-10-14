package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Puzzle10 {
  private static URL resource = Puzzle10.class.getResource("/puzzleinput10.txt");

  public static void main(String[] args) throws URISyntaxException, IOException {
    System.out.println("Solution 1: " + solve01());
    // System.out.println("Solution 2: " + solve02());
  }

  private static int register = 1;

  public static int solve01() throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));

    int sum = 0;
    int cycle = 0;
    for (String line : lines) {
      String[] parts = line.split(" ");
      cycle++;
      // divisible by 20 and result is odd
      if (((float) cycle / 20) % 1 == 0 && (cycle / 20) % 2 == 1) {
        sum += register * cycle;
      }

      if (parts[0].equals("noop")) {
        continue;
      }

      // parts[1].equals("addx");
      cycle++;
      if (((float) cycle / 20) % 1.0 == 0 && (cycle / 20) % 2 == 1) {
        sum += register * cycle;
      }

      addx(Integer.parseInt(parts[1]));
    }

    return sum;
  }

  private static int addx(int val) {
    register += val;
    return register;
  }
}
