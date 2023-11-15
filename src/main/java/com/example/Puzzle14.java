package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Puzzle14 {
  private static URL resource = Puzzle14.class.getResource("/puzzleinput14.txt");

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
    System.out.println("Solution 1: " + solve01(lines));
    System.out.println("Solution 2: " + solve02(lines));
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

  public static int solve02(List<String> lines) {
    Layout layout = addFloor(genLayout(lines));
    int counter = 0;

    while (true) {
      int[] sandCopy = Arrays.copyOf(layout.getSand(), layout.getSand().length);
      int[] sandPos = new int[] { sandCopy[0] + layout.xOffset, sandCopy[1] };
      boolean fin = false;
      try {
        while (layout.canMove(sandPos, true)) {
          sandPos = layout.nextPos(sandPos);
        }
      } catch (Exception e) {
        e.printStackTrace();
        fin = true;
      }

      if (fin || layout.layout.get(sandPos[0]).get(sandPos[1])) {
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

  private static Layout addFloor(Layout l) {
    for (ArrayList<Boolean> x : l.layout) {
      x.add(false);
    }

    for (ArrayList<Boolean> x : l.layout) {
      x.add(true);
    }

    return l;
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
      return canMove(pos, false);
    }

    private int xOffset = 0;

    public boolean canMove(int[] pos, boolean expandX) {
      int x = pos[0];
      int y = pos[1];
      try {
        if (this.layout.get(x).size() == y + 1) {
          return true;
        }
      } catch (Exception e) {
        if (!expandX) {
          throw e;
        }

        int len = this.layout.get(x - 1).size();
        this.layout.add(new ArrayList<>(Collections.nCopies(len, false)));
        this.layout.get(x).set(len - 1, true);
        // System.out.println("Added x to end");
        // for (ArrayList<Boolean> s : this.layout) {
        // System.out.println(Arrays.toString(s.stream().map(el -> el ? "#" :
        // ".").toArray()));
        // }
        return canMove(pos, expandX);
      }

      if (this.layout.get(x).get(y + 1) == false) {
        return true;
      }

      try {
        if (this.layout.get(x - 1).get(y + 1) == false) {
          return true;
        }
      } catch (Exception e) {
        if (!expandX) {
          throw e;
        }

        int len = this.layout.get(x).size();
        this.layout.add(0, new ArrayList<>(Collections.nCopies(len, false)));
        this.layout.get(0).set(len - 1, true);
        this.xOffset++;
        // System.out.println("Added x to front");
        // for (ArrayList<Boolean> s : this.layout) {
        // System.out.println(Arrays.toString(s.stream().map(el -> el ? "#" :
        // ".").toArray()));
        // }
        return canMove(new int[] { pos[0] + 1, pos[1] }, expandX);
      }

      try {
        if (this.layout.get(x + 1).get(y + 1) == false) {
          return true;
        }
      } catch (Exception e) {
        if (!expandX) {
          throw e;
        }

        int len = this.layout.get(x).size();
        this.layout.add(new ArrayList<>(Collections.nCopies(len, false)));
        this.layout.get(x + 1).set(len - 1, true);
        // System.out.println("Added x to front");
        // for (ArrayList<Boolean> s : this.layout) {
        // System.out.println(Arrays.toString(s.stream().map(el -> el ? "#" :
        // ".").toArray()));
        // }
        return canMove(pos, expandX);
      }

      return false;
    }

    public int[] nextPos(int[] pos) {
      int x = pos[0];
      int y = pos[1];
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
