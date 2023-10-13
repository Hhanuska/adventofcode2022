package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Puzzle08 {
  private static URL resource = Puzzle08.class.getResource("/puzzleinput08.txt");

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
    ArrayList<ArrayList<Integer>> grid = createGrid();
    System.out.println("Solution 1: " + solve01(grid));
    System.out.println("Solution 2: " + solve02(grid));
  }

  public static ArrayList<ArrayList<Integer>> createGrid() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    ArrayList<ArrayList<Integer>> grid = new ArrayList<>();

    while (scanner.hasNextLine()) {
      grid.add(new ArrayList<>());
      String line = scanner.nextLine();
      String[] trees = line.split("");

      for (String tree : trees) {
        grid.get(grid.size() - 1).add(Integer.parseInt(tree));
      }
    }
    scanner.close();

    return grid;
  }

  public static int solve01(ArrayList<ArrayList<Integer>> grid) {
    int visible = 0;

    for (int row = 0; row < grid.size(); row++) {
      for (int col = 0; col < grid.get(row).size(); col++) {
        if (row == 0 || col == 0 || row == grid.size() - 1 || col == grid.get(row).size() - 1) {
          // tree is on the edge, always visible
          visible++;
          continue;
        }

        int currentTree = grid.get(row).get(col);

        boolean isVisibleFromLeft = true;
        boolean isVisibleFromRight = true;
        for (int colCheck = 0; colCheck < grid.get(row).size(); colCheck++) {
          if (colCheck == col) {
            if (isVisibleFromLeft) {
              // checked all trees to the left and is visible
              // no further checks required
              break;
            }
            // not visible from the left, keep checking
            continue;
          }

          if (grid.get(row).get(colCheck) >= currentTree) {
            if (colCheck < col) {
              isVisibleFromLeft = false;
              // skip all other trees on the left
              colCheck = col;
            } else {
              isVisibleFromRight = false;
            }
          }

          if (!isVisibleFromLeft && !isVisibleFromRight) {
            break;
          }
        }

        if (isVisibleFromLeft || isVisibleFromRight) {
          visible++;
          // visible from left or right, no need to check further
          continue;
        }

        boolean isVisibleFromTop = true;
        boolean isVisibleFromBottom = true;
        for (int rowCheck = 0; rowCheck < grid.size(); rowCheck++) {
          if (rowCheck == row) {
            if (isVisibleFromTop) {
              break;
            }
            continue;
          }

          if (grid.get(rowCheck).get(col) >= currentTree) {
            if (rowCheck < row) {
              isVisibleFromTop = false;
              rowCheck = row;
            } else {
              isVisibleFromBottom = false;
            }
          }

          if (!isVisibleFromTop && !isVisibleFromBottom) {
            break;
          }
        }

        if (isVisibleFromTop || isVisibleFromBottom) {
          visible++;
        }
      }
    }

    return visible;
  }

  public static int solve02(ArrayList<ArrayList<Integer>> grid) {
    int max = 0;

    for (int row = 0; row < grid.size(); row++) {
      for (int col = 0; col < grid.get(row).size(); col++) {
        int ss = getScenicScore(grid, row, col);
        if (ss > max) {
          max = ss;
        }
      }
    }

    return max;
  }

  public static int getScenicScore(ArrayList<ArrayList<Integer>> grid, int row, int col) {
    int left = 0;
    int right = 0;
    int top = 0;
    int bottom = 0;

    int treeSize = grid.get(row).get(col);

    // left and right
    for (int colCheck = col - 1; colCheck >= 0; colCheck--) {
      left++;
      if (grid.get(row).get(colCheck) >= treeSize) {
        break;
      }
    }
    for (int colCheck = col + 1; colCheck < grid.get(row).size(); colCheck++) {
      right++;
      if (grid.get(row).get(colCheck) >= treeSize) {
        break;
      }
    }

    // top and bottom
    for (int rowCheck = row - 1; rowCheck >= 0; rowCheck--) {
      top++;
      if (grid.get(rowCheck).get(col) >= treeSize) {
        break;
      }
    }
    for (int rowCheck = row + 1; rowCheck < grid.size(); rowCheck++) {
      bottom++;
      if (grid.get(rowCheck).get(col) >= treeSize) {
        break;
      }
    }

    return left * right * top * bottom;
  }
}
