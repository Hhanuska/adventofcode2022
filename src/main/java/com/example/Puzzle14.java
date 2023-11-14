package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class Puzzle14 {
  private static URL resource = Puzzle14.class.getResource("/puzzleinput14.txt");

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
    System.out.println(genLayout(lines));
  }

  private static ArrayList<ArrayList<Boolean>> genLayout(List<String> lines) {
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

        boolean isX = prevCoords[0] == coords[0];
        int axis = isX ? 0 : 1;

        int start = prevCoords[axis] < aCoords[axis] ? prevCoords[axis] : aCoords[axis];
        int end = prevCoords[axis] < aCoords[axis] ? aCoords[axis] : prevCoords[axis];
        for (int i = start; i <= end; i++) {
          if (isX) {
            result.get(i).set(aCoords[1], true);
          } else {
            result.get(aCoords[0]).set(i, true);
          }
        }

        prevCoords = aCoords;
      }
    }

    return result;
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

  private static Pair<Integer, Integer> sandStart = new Pair<Integer, Integer>(500, 0);
  private static Pair<Integer, Integer> sandStartConvert = new Pair<Integer, Integer>(0, 0);
}
