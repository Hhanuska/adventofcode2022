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
    System.out.println("Solution 2:");
    solve02();
  }

  public static int solve01() throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));

    int sum = 0;
    int cycle = 0;
    int register = 1;
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

      register += Integer.parseInt(parts[1]);
    }

    return sum;
  }

  public static void solve02() throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));

    CRT crt = new CRT();

    int register = 1;

    for (String line : lines) {
      String[] parts = line.split(" ");
      crt.incrCycle(register);

      if (parts[0].equals("noop")) {
        continue;
      }

      // parts[1].equals("addx");
      crt.incrCycle(register);

      register += Integer.parseInt(parts[1]);
    }

    crt.print();
  }

  private static class CRT {
    private char[][] crt = new char[6][40];

    private int cycle = 0;

    public void incrCycle(int register) {
      // use old value in this.draw(), then increment
      this.draw(this.cycle++, register);
    }

    private void draw(int cycle, int register) {
      int row = cycle / 40;
      int col = cycle % 40;

      if (Math.abs(col - register) <= 1) {
        crt[row][col] = '#';
      } else {
        // use whitespace instead of '.' for better readability
        crt[row][col] = ' ';
      }
    }

    public void print() {
      for (char[] row : crt) {
        System.out.println(row);
      }
    }
  }
}
