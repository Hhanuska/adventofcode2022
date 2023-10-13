package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class Puzzle05 {
  private static URL resource = Puzzle05.class.getResource("/puzzleinput05.txt");

  public static ArrayList<String> readFile() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    ArrayList<String> lines = new ArrayList<String>();

    while (scanner.hasNextLine()) {
      lines.add(scanner.nextLine());
    }
    scanner.close();

    return lines;
  }

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
    ArrayList<String> lines = readFile();
    System.out.println("Solution 1: " + solve01(lines));
    System.out.println("Solution 2: " + solve02(lines));
  }

  public static ArrayList<Stack<Character>> fillStacks(ArrayList<String> lines) {
    ArrayList<String> stackLines = getStackLines(lines);
    String stackNumsLine = stackLines.get(stackLines.size() - 1);
    ArrayList<String> stackNums = new ArrayList<>(Arrays.asList(stackNumsLine.split(" ")));
    stackNums.removeIf((str) -> {
      return str.isEmpty();
    });

    ArrayList<Stack<Character>> stacks = new ArrayList<>();

    for (int i = stackLines.size() - 2; i >= 0; i--) {
      for (String stackNum : stackNums) {
        char crate = stackLines.get(i).charAt(stackNumsLine.indexOf(stackNum));
        if (crate == ' ') {
          continue;
        }

        try {
          stacks.get(Integer.parseInt(stackNum) - 1);
        } catch (IndexOutOfBoundsException err) {
          stacks.add(new Stack<>());
        }

        stacks.get(Integer.parseInt(stackNum) - 1).push(crate);
      }
    }

    return stacks;
  }

  public static ArrayList<String> getStackLines(ArrayList<String> lines) {
    ArrayList<String> stackLines = new ArrayList<>();
    for (String line : lines) {
      if (line.isEmpty()) {
        break;
      }

      stackLines.add(line);
    }

    return stackLines;
  }

  public static void executeMove(ArrayList<Stack<Character>> stacks, String move) {
    String[] parts = move.split(" ");
    int amount = Integer.parseInt(parts[1]);
    int from = Integer.parseInt(parts[3]) - 1;
    int to = Integer.parseInt(parts[5]) - 1;

    for (int i = 0; i < amount; i++) {
      char crate = stacks.get(from).pop();
      stacks.get(to).push(crate);
    }
  }

  public static String solve01(ArrayList<String> lines) {
    ArrayList<Stack<Character>> stacks = fillStacks(lines);
    int instructionsStartIndex = lines.indexOf("") + 1;

    for (int i = instructionsStartIndex; i < lines.size(); i++) {
      String move = lines.get(i);
      executeMove(stacks, move);
    }

    String toReturn = "";
    for (int i = 0; i < stacks.size(); i++) {
      toReturn += stacks.get(i).peek();
    }

    return toReturn;
  }

  public static void executeMove2(ArrayList<Stack<Character>> stacks, String move) {
    String[] parts = move.split(" ");
    int amount = Integer.parseInt(parts[1]);
    int from = Integer.parseInt(parts[3]) - 1;
    int to = Integer.parseInt(parts[5]) - 1;

    ArrayList<Character> crates = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      char crate = stacks.get(from).pop();
      crates.add(crate);
    }

    for (int i = crates.size() - 1; i >= 0; i--) {
      stacks.get(to).push(crates.get(i));
    }
  }

  public static String solve02(ArrayList<String> lines) {
    ArrayList<Stack<Character>> stacks = fillStacks(lines);
    int instructionsStartIndex = lines.indexOf("") + 1;

    for (int i = instructionsStartIndex; i < lines.size(); i++) {
      String move = lines.get(i);
      executeMove2(stacks, move);
    }

    String toReturn = "";
    for (int i = 0; i < stacks.size(); i++) {
      toReturn += stacks.get(i).peek();
    }

    return toReturn;
  }
}
