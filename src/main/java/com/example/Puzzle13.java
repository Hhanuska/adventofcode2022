package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONArray;

public class Puzzle13 {
  private static URL resource = Puzzle13.class.getResource("/puzzleinput13.txt");

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
    System.out.println("Solution 1: " + solve01(lines));
    System.out.println("Solution 2: " + solve02(lines));
  }

  public static int solve01(List<String> lines) {
    int sum = 0;

    for (int i = 0; i < lines.size(); i = i + 3) {
      JSONArray left = new JSONArray(lines.get(i));
      JSONArray right = new JSONArray(lines.get(i + 1));

      Compare comparison = compare(left, right);
      if (comparison == Compare.LOWER) {
        sum += i / 3 + 1;
      }
    }

    return sum;
  }

  public static int solve02(List<String> lines) {
    String divider1 = "[[2]]";
    String divider2 = "[[6]]";
    lines.add(divider1);
    lines.add(divider2);

    for (int i = 0; i < lines.size(); i++) {
      if (lines.get(i).isEmpty()) {
        lines.remove(i);
      }
    }

    lines.sort((line1, line2) -> {
      JSONArray l1 = new JSONArray(line1);
      JSONArray l2 = new JSONArray(line2);

      Compare comparison = compare(l1, l2);
      if (comparison == Compare.LOWER) {
        return -1;
      } else if (comparison == Compare.HIGHER) {
        return 1;
      } else {
        return 0;
      }
    });

    return (lines.indexOf(divider1) + 1) * (lines.indexOf(divider2) + 1);
  }

  public static Compare compare(JSONArray a, JSONArray b) {
    for (int i = 0; i < a.length(); i++) {
      if (i >= b.length()) {
        return Compare.HIGHER;
      }

      Compare compResult = compare(a.get(i), b.get(i));

      if (compResult == Compare.EQUAL) {
        if (i == a.length() - 1 && a.length() == b.length()) {
          return Compare.EQUAL;
        }
        continue;
      }

      return compResult;
    }

    if (a.length() == 0 && b.length() == 0) {
      return Compare.EQUAL;
    }

    return Compare.LOWER;
  }

  public static Compare compare(Object a, Object b) {
    if (a instanceof Integer && b instanceof Integer) {
      return compare((int) a, (int) b);
    } else if (a instanceof Integer && b instanceof JSONArray) {
      return compare((int) a, (JSONArray) b);
    } else if (a instanceof JSONArray && b instanceof Integer) {
      return compare((JSONArray) a, (int) b);
    } else {
      return compare((JSONArray) a, (JSONArray) b);
    }
  }

  public static Compare compare(int a, int b) {
    if (a < b)
      return Compare.LOWER;
    if (a == b)
      return Compare.EQUAL;

    return Compare.HIGHER;
  }

  public static Compare compare(JSONArray a, int b) {
    JSONArray bConverted = new JSONArray();
    bConverted.put(b);
    return compare(a, bConverted);
  }

  public static Compare compare(int a, JSONArray b) {
    JSONArray aConverted = new JSONArray();
    aConverted.put(a);
    return compare(aConverted, b);
  }

  private static enum Compare {
    LOWER,
    EQUAL,
    HIGHER
  }
}
