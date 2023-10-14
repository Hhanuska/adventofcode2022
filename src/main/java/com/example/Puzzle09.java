package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;

public class Puzzle09 {
  private static URL resource = Puzzle09.class.getResource("/puzzleinput09.txt");

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
    System.out.println("Solution 1: " + solve01());
    System.out.println("Solution 2: " + solve02());
  }

  public static int solve01() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    RopeSim sim = new RopeSim();
    HashSet<String> uniqueTailPositions = new HashSet<>();
    uniqueTailPositions.add(sim.getTailPosition());

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] parts = line.split(" ");

      for (int i = 0; i < Integer.parseInt(parts[1]); i++) {
        sim.move(parts[0].charAt(0));
        uniqueTailPositions.add(sim.getTailPosition());
      }
    }
    scanner.close();

    return uniqueTailPositions.size();
  }

  private static class RopeSim {
    private class Position {
      int x = 0;
      int y = 0;
    }

    Position head = new Position();
    Position tail = new Position();

    public void move(char dir) {
      moveHead(dir);
      moveTail();
    }

    public String getTailPosition() {
      return tail.x + "-" + tail.y;
    }

    private void moveHead(char dir) {
      switch (dir) {
        case 'U':
          head.y++;
          break;
        case 'D':
          head.y--;
          break;
        case 'L':
          head.x--;
          break;
        case 'R':
          head.x++;
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    private void moveTail() {
      int dist = Math.abs(this.head.x - this.tail.x) + Math.abs(this.head.y - this.tail.y);

      boolean moveRequired = dist > 2 || (dist == 2 && (head.x == tail.x || head.y == tail.y));

      if (!moveRequired) {
        return;
      }

      if (head.x != tail.x)
        tail.x = head.x > tail.x ? tail.x + 1 : tail.x - 1;
      if (head.y != tail.y)
        tail.y = head.y > tail.y ? tail.y + 1 : tail.y - 1;
    }
  }

  public static int solve02() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    RopeSim2 sim = new RopeSim2();
    HashSet<String> uniqueTailPositions = new HashSet<>();
    uniqueTailPositions.add(sim.getTailPosition());

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] parts = line.split(" ");

      for (int i = 0; i < Integer.parseInt(parts[1]); i++) {
        sim.move(parts[0].charAt(0));
        uniqueTailPositions.add(sim.getTailPosition());
      }
    }
    scanner.close();

    return uniqueTailPositions.size();
  }

  private static class RopeSim2 {
    private class Position {
      int x = 0;
      int y = 0;
    }

    Position[] positions = new Position[10];

    public RopeSim2() {
      for (int i = 0; i < positions.length; i++) {
        positions[i] = new Position();
      }
    }

    public void move(char dir) {
      moveHead(dir);
      moveTail();
    }

    public String getTailPosition() {
      return positions[positions.length - 1].x + "-" + positions[positions.length - 1].y;
    }

    private void moveHead(char dir) {
      switch (dir) {
        case 'U':
          positions[0].y++;
          break;
        case 'D':
          positions[0].y--;
          break;
        case 'L':
          positions[0].x--;
          break;
        case 'R':
          positions[0].x++;
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    private void moveTail() {
      for (int i = 1; i < positions.length; i++) {
        moveTailingKnot(positions[i - 1], positions[i]);
      }
    }

    private void moveTailingKnot(Position head, Position tail) {
      int dist = Math.abs(head.x - tail.x) + Math.abs(head.y - tail.y);

      boolean moveRequired = dist > 2 || (dist == 2 && (head.x == tail.x || head.y == tail.y));

      if (!moveRequired) {
        return;
      }

      if (head.x != tail.x)
        tail.x = head.x > tail.x ? tail.x + 1 : tail.x - 1;
      if (head.y != tail.y)
        tail.y = head.y > tail.y ? tail.y + 1 : tail.y - 1;
    }
  }
}
