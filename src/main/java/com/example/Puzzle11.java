package com.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Puzzle11 {
  private static URL resource = Puzzle11.class.getResource("/puzzleinput11.txt");

  public static void main(String[] args) throws URISyntaxException, IOException {
    Monkeys monkeys = new Monkeys();
    System.out.println("Solution 1: " + solve01(monkeys));
    // System.out.println("Solution 2: " + solve02());
  }

  public static int solve01(Monkeys monkeys) {
    for (int i = 0; i < 20; i++) {
      monkeys.round();
    }

    int[] counts = monkeys.get().stream().mapToInt((m) -> m.getInspectCount()).toArray();
    Arrays.sort(counts);

    return counts[counts.length - 1] * counts[counts.length - 2];
  }

  private static class Monkeys {
    private ArrayList<Monkey> monkeys = new ArrayList<>();

    public Monkeys() throws URISyntaxException, IOException {
      List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));

      for (String line : lines) {
        line = line.trim().replace(",", "");
        String[] parts = line.split(" ");
        if (line.startsWith("Monkey")) {
          monkeys.add(new Monkey());
          continue;
        }

        if (line.startsWith("Starting items")) {
          for (int i = 2; i < parts.length; i++) {
            monkeys.get(monkeys.size() - 1).addItem(Integer.parseInt(parts[i]));
          }
          continue;
        }

        if (line.startsWith("Operation")) {
          monkeys.get(monkeys.size() - 1).setOperation(new Operation(parts[4], parts[5]));
          continue;
        }

        if (line.startsWith("Test")) {
          monkeys.get(monkeys.size() - 1).setTest(new Test(Integer.parseInt(parts[3])));
          continue;
        }

        if (line.startsWith("If true")) {
          monkeys.get(monkeys.size() - 1).getTest().setTrueMonkey(Integer.parseInt(parts[5]));
          continue;
        }

        if (line.startsWith("If false")) {
          monkeys.get(monkeys.size() - 1).getTest().setFalseMonkey(Integer.parseInt(parts[5]));
          continue;
        }
      }

    }

    public void round() {
      for (Monkey monkey : monkeys) {
        while (monkey.hasItem()) {
          int wl = monkey.getNextItem();
          wl = monkey.getOperation().getNewWorryLevel(wl);
          wl /= 3;
          int nextMonkeyIndex = monkey.getTest().getTargetMonkey(wl);
          monkeys.get(nextMonkeyIndex).addItem(wl);
        }
      }
    }

    public ArrayList<Monkey> get() {
      return this.monkeys;
    }
  }

  private static class Monkey {
    private LinkedList<Integer> items = new LinkedList<>();
    private Operation op;
    private Test test;
    private int inspectCount = 0;

    public void addItem(int worryLevel) {
      items.add(worryLevel);
    }

    public int getNextItem() {
      this.inspectCount++;
      return this.items.removeFirst();
    }

    public boolean hasItem() {
      return this.items.size() > 0;
    }

    public void setOperation(Operation op) {
      this.op = op;
    }

    public Operation getOperation() {
      return this.op;
    }

    public void setTest(Test test) {
      this.test = test;
    }

    public Test getTest() {
      return this.test;
    }

    public int getInspectCount() {
      return this.inspectCount;
    }
  }

  private static class Operation {
    private String op;
    private String val;

    public Operation(String op, String val) {
      this.op = op;
      this.val = val;
    }

    public int getNewWorryLevel(int worryLevel) {
      int _val = val.equals("old") ? worryLevel : Integer.parseInt(val);
      if (op.equals("*")) {
        return worryLevel * _val;
      } else {
        return worryLevel + _val;
      }
    }
  }

  private static class Test {
    private int div;
    private int trueMonkey;
    private int falseMonkey;

    public Test(int div) {
      this.div = div;
    }

    public void setTrueMonkey(int tm) {
      this.trueMonkey = tm;
    }

    public void setFalseMonkey(int fm) {
      this.falseMonkey = fm;
    }

    public int getTargetMonkey(int worryLevel) {
      return worryLevel % div == 0 ? trueMonkey : falseMonkey;
    }
  }
}
