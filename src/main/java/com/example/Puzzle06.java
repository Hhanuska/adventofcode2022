package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Puzzle06 {
  private static URL resource = Puzzle06.class.getResource("/puzzleinput06.txt");

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException, IOException {
    System.out.println("Solution 1: " + solve01());
    // System.out.println("Solution 2: " + solve02());
  }

  public static int solve01() throws URISyntaxException, FileNotFoundException, IOException {
    File file = Paths.get(resource.toURI()).toFile();
    InputStream inputStream = new FileInputStream(file);

    ArrayList<Character> last4 = new ArrayList<>();

    int counter = 0;

    while (inputStream.available() > 0) {
      if (last4.size() == 4) {
        Set<Character> set = new HashSet<>(last4);
        if (set.size() == last4.size()) {
          inputStream.close();

          return counter;
        }
      }

      char c = (char) (inputStream.read());
      last4.add(c);
      counter++;

      if (last4.size() > 4) {
        last4.remove(0);
      }
    }
    inputStream.close();
    return 0;
  }
}
