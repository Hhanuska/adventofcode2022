package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Puzzle12 {
  private static URL resource = Puzzle12.class.getResource("/puzzleinput12.txt");

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
    HeightMap hm = new HeightMap(lines);
    System.out.println("Solution 1: " + solve01(hm));
    // System.out.println("Solution 2: " + solve02());
  }

  public static int solve01(HeightMap hm) {
    while (hm.getDistance(hm.getEnd().row, hm.getEnd().col) == Integer.MAX_VALUE) {
      Coords check = hm.getLowestDistanceUnvisited();
      if (check == null) {
        // no more coords to check
        break;
      }

      ArrayList<String> possibleMoves = hm.getPossibleMoves(check.row, check.col);
      for (String direction : possibleMoves) {
        Coords nextCoords = hm.getCoordsAfterMove(check.row, check.col, direction);
        int nextDistance = hm.getDistance(check.row, check.col) + 1;
        if (nextDistance < hm.getDistance(nextCoords.row, nextCoords.col)) {
          hm.setDistance(nextCoords.row, nextCoords.col, nextDistance);
        }
      }

      hm.setVisited(hm.getId(check.row, check.col));
    }

    return hm.getDistance(hm.getEnd().row, hm.getEnd().col);
  }

  private static class HeightMap {
    private int[][] heightMap;
    private int[][] distances;

    private HashSet<String> unvisited = new HashSet<>();

    private Coords start;
    private Coords end;

    public HeightMap(List<String> lines) {
      this.heightMap = new int[lines.size()][lines.get(0).length()];
      this.distances = new int[lines.size()][lines.get(0).length()];

      for (int[] row : this.distances) {
        Arrays.fill(row, Integer.MAX_VALUE);
      }

      for (int i = 0; i < lines.size(); i++) {
        for (int j = 0; j < lines.get(i).length(); j++) {
          char c = lines.get(i).charAt(j);
          String id = this.getId(i, j);
          if (c == 'S') {
            this.heightMap[i][j] = (int) 'a';
            this.start = new Coords(i, j);
            this.distances[i][j] = 0;
          } else if (c == 'E') {
            this.heightMap[i][j] = (int) 'z';
            this.end = new Coords(i, j);
          } else {
            this.heightMap[i][j] = (int) c;
          }
          unvisited.add(id);
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

    public ArrayList<String> getPossibleMoves(int row, int col) {
      ArrayList<String> r = new ArrayList<>();
      for (String dir : new String[] { "up", "down", "left", "right" }) {
        if (this.canMove(row, col, dir)) {
          r.add(dir);
        }
      }

      return r;
    }

    public Coords getCoordsAfterMove(int row, int col, String dir) {
      try {
        switch (dir) {
          case "up":
            return new Coords(row - 1, col);
          case "down":
            return new Coords(row + 1, col);
          case "left":
            return new Coords(row, col - 1);
          case "right":
            return new Coords(row, col + 1);
          default:
            return null;
        }
      } catch (IndexOutOfBoundsException e) {
        return null;
      }
    }

    public int[][] getHeightMap() {
      return this.heightMap;
    }

    public String getId(int row, int col) {
      return row + "-" + col;
    }

    public Coords getStart() {
      return this.start;
    }

    public Coords getEnd() {
      return this.end;
    }

    public Coords getLowestDistanceUnvisited() {
      if (this.unvisited.isEmpty()) {
        return null;
      }

      int minRow = -1;
      int minCol = -1;

      for (int row = 0; row < this.distances.length; row++) {
        for (int col = 0; col < this.distances[row].length; col++) {
          if (!this.unvisited.contains(this.getId(row, col))) {
            continue;
          }

          if (minRow == -1 || minCol == -1) {
            minRow = row;
            minCol = col;
            continue;
          }

          int minDist = this.distances[minRow][minCol];
          int checkDist = this.distances[row][col];
          if (checkDist < minDist) {
            minRow = row;
            minCol = col;
          }
        }
      }

      return new Coords(minRow, minCol);
    }

    public int getDistance(int row, int col) {
      return this.distances[row][col];
    }

    public void setDistance(int row, int col, int distance) {
      this.distances[row][col] = distance;
    }

    public void setVisited(String id) {
      this.unvisited.remove(id);
    }
  }

  private static class Coords {
    public int row;
    public int col;

    public Coords(int row, int col) {
      this.row = row;
      this.col = col;
    }
  }
}
