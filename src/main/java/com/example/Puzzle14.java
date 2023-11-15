package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Puzzle14 {
  private static URL resource = Puzzle14.class.getResource("/puzzleinput14.txt");

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
    System.out.println(solve01(lines));
  }

  public static int solve01(List<String> lines) {
    Layout layout = genLayout(lines);
    int counter = 0;

    // for (ArrayList<Boolean> e : layout.layout) {
    // System.out.println(Arrays.toString(e.stream().map(el -> el ? "#" :
    // ".").toArray()));
    // }

    while (true) {
      int[] sandPos = Arrays.copyOf(layout.getSand(), layout.getSand().length);
      boolean fin = false;
      try {
        while (layout.canMove(sandPos)) {
          sandPos = layout.nextPos(sandPos);
        }
      } catch (Exception e) {
        e.printStackTrace();
        fin = true;
      }

      if (fin) {
        break;
      }

      layout.setOccupied(sandPos);
      counter++;

      // System.out.println(counter);
      // for (ArrayList<Boolean> e : layout.layout) {
      // System.out.println(Arrays.toString(e.stream().map(el -> el ? "#" :
      // ".").toArray()));
      // }
    }

    return counter;
  }

  private static Layout genLayout(List<String> lines) {
    ArrayList<ArrayList<Boolean>> result = new ArrayList<>();

    ArrayList<ArrayList<int[]>> parsedLines = new ArrayList<>();
    for (String line : lines) {
      parsedLines.add(parseLine(line));
    }

    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    for (ArrayList<int[]> line : parsedLines) {
      for (int[] coords : line) {
        if (coords[0] < minX) {
          minX = coords[0];
        }
        if (coords[1] < minY) {
          minY = coords[1];
        }
      }
    }
    // or sand position
    if (500 < minX) {
      minX = 500;
    }
    if (0 < minY) {
      minY = 0;
    }

    for (ArrayList<int[]> line : parsedLines) {
      int[] prevCoords = null;
      for (int[] coords : line) {
        int[] aCoords = { coords[0] - minX, coords[1] - minY };

        while (result.size() <= aCoords[0]) {
          result.add(new ArrayList<>());
        }
        while (result.get(aCoords[0]).size() <= aCoords[1]) {
          result.get(aCoords[0]).add(false);
        }

        if (prevCoords == null) {
          prevCoords = aCoords;
          continue;
        }

        result = fillMissing(result);

        boolean isX = prevCoords[0] == aCoords[0];
        int axis = isX ? 1 : 0;

        int start = prevCoords[axis] < aCoords[axis] ? prevCoords[axis] : aCoords[axis];
        int end = prevCoords[axis] < aCoords[axis] ? aCoords[axis] : prevCoords[axis];
        for (int i = start; i <= end; i++) {
          if (isX) {
            result.get(aCoords[0]).set(i, true);
          } else {
            result.get(i).set(aCoords[1], true);
          }
        }

        prevCoords = aCoords;
      }
    }

    return new Layout(fillMissing(result), new int[] { 500 - minX, 0 });
  }

  private static ArrayList<ArrayList<Boolean>> fillMissing(ArrayList<ArrayList<Boolean>> layout) {
    int maxLength = 0;

    for (ArrayList<Boolean> col : layout) {
      if (col.size() > maxLength) {
        maxLength = col.size();
      }
    }

    for (ArrayList<Boolean> col : layout) {
      while (col.size() < maxLength) {
        col.add(false);
      }
    }

    return layout;
  }

  private static ArrayList<int[]> parseLine(String line) {
    ArrayList<int[]> res = new ArrayList<>();

    String[] coords = line.split(" -> ");

    for (String coordPair : coords) {
      String[] xY = coordPair.split(",");
      int[] toInt = { Integer.parseInt(xY[0]), Integer.parseInt(xY[1]) };
      res.add(toInt);
    }

    return res;
  }

  private static class Layout {

    private ArrayList<ArrayList<Boolean>> layout;

    private int[] sand;

    public Layout(ArrayList<ArrayList<Boolean>> l, int[] sand) {
      this.layout = l;
      this.sand = sand;
    }

    public int[] getSand() {
      return this.sand;
    }

    public boolean canMove(int[] pos) {
      int x = pos[0];
      int y = pos[1];
      if (this.layout.get(x).get(y)) {
        return this.canMove(new int[] { pos[0], pos[1] - 1 });
      }

      if (this.layout.get(x).size() == y + 1) {
        return true;
      }

      if (this.layout.get(x).get(y + 1) == false) {
        return true;
      }

      if (this.layout.get(x - 1).get(y + 1) == false) {
        return true;
      }

      if (this.layout.get(x + 1).get(y + 1) == false) {
        return true;
      }

      return false;
    }

    public int[] nextPos(int[] pos) {
      int x = pos[0];
      int y = pos[1];
      if (this.layout.get(x).get(y)) {
        return this.nextPos(new int[] { pos[0], pos[1] - 1 });
      }

      if (this.layout.get(x).get(y + 1) == false) {
        return new int[] { x, y + 1 };
      }

      if (this.layout.get(x - 1).get(y + 1) == false) {
        return new int[] { x - 1, y + 1 };
      }

      if (this.layout.get(x + 1).get(y + 1) == false) {
        return new int[] { x + 1, y + 1 };
      }

      return pos;
    }

    public void setOccupied(int[] pos) {
      this.layout.get(pos[0]).set(pos[1], true);
    }
  }
}
