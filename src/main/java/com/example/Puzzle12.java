package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Puzzle12 {
  private static URL resource = Puzzle12.class.getResource("/puzzleinput12.txt");

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
    HeightMap hm = new HeightMap(lines);
    // System.out.println("Solution 1: " + solve01());
    // System.out.println("Solution 2: " + solve02());
  }

  private static class HeightMap {
    private int[][] heightMap;

    public HeightMap(List<String> lines) {
      this.heightMap = new int[lines.size()][lines.get(0).length()];
      for (int i = 0; i < lines.size(); i++) {
        for (int j = 0; j < lines.get(i).length(); j++) {
          char c = lines.get(i).charAt(j);
          if (c == 'S') {
            this.heightMap[i][j] = (int) 'a' - 1;
          } else if (c == 'E') {
            this.heightMap[i][j] = (int) 'z' + 1;
          } else {
            this.heightMap[i][j] = (int) c;
          }
        }
      }
    }

    public boolean canMove(int row, int col, String dir) {
      int startHeight = this.heightMap[row][col];
      int goalHeight;
      try {
        switch (dir) {
          case "up":
            goalHeight = this.heightMap[row - 1][col];
            break;
          case "down":
            goalHeight = this.heightMap[row + 1][col];
            break;
          case "left":
            goalHeight = this.heightMap[row][col - 1];
            break;
          case "right":
            goalHeight = this.heightMap[row][col + 1];
            break;
          default:
            return false;
        }
      } catch (IndexOutOfBoundsException e) {
        return false;
      }

      if (goalHeight <= startHeight + 1) {
        return true;
      } else {
        return false;
      }
    }
  }
}
