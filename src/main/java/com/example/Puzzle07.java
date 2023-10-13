package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class Puzzle07 {
  private static URL resource = Puzzle07.class.getResource("/puzzleinput07.txt");

  public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
    FileSystem fs = createFileSystem();
    System.out.println("Solution 1: " + solve01(fs.getCurrentNode()));
    System.out.println("Solution 2: " + solve02(fs.getCurrentNode()));
  }

  public static FileSystem createFileSystem() throws URISyntaxException, FileNotFoundException {
    File file = Paths.get(resource.toURI()).toFile();
    Scanner scanner = new Scanner(file);

    FileSystem fs = new FileSystem();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] parts = line.split(" ");
      boolean isCommand = parts[0].equals("$");

      if (isCommand && parts[1].equals("ls")) {
        // nothing to save here
        // use following lines
        continue;
      }

      if (isCommand && parts[1].equals("cd")) {
        String folderName = parts[2];
        if (!folderName.equals("..") && !folderName.equals("/")) {
          fs.addFile(folderName, 0);
        }
        fs.move(folderName);
        continue;
      }

      // input only shows ls and cd commands
      // every non-command line is part of the response of ls
      int fileSize = parts[0].equals("dir") ? 0 : Integer.parseInt(parts[0]);
      String fileName = parts[1];
      fs.addFile(fileName, fileSize);
    }
    scanner.close();

    fs.moveToRoot();

    return fs;
  }

  public static int solve01(Node fs) {
    int totalSizeToReturn = 0;

    int totalFsSize = fs.getSize();
    if (totalFsSize < 100000) {
      totalSizeToReturn += totalFsSize;
    }

    for (String key : fs.getChildren().keySet()) {
      if (fs.getChild(key).getFileSize() == 0) {
        totalSizeToReturn += solve01(fs.getChild(key));
      }
    }

    return totalSizeToReturn;
  }

  public static int solve02(Node fs) {
    int spaceNeeded = 30_000_000 - (70_000_000 - fs.getSize());
    ArrayList<Integer> dirs = new ArrayList<>();
    fillQualifiedDirs(fs, spaceNeeded, dirs);

    return dirs.stream().mapToInt(val -> val).min().orElseThrow(NoSuchElementException::new);
  }

  public static void fillQualifiedDirs(Node fs, int minSize, ArrayList<Integer> dirs) {
    int totalFsSize = fs.getSize();
    if (totalFsSize > minSize) {
      dirs.add(totalFsSize);
    }

    for (String key : fs.getChildren().keySet()) {
      if (fs.getChild(key).getFileSize() == 0) {
        fillQualifiedDirs(fs.getChild(key), minSize, dirs);
      }
    }
  }

  private static class FileSystem {
    private Node root;

    private Node currentNode;

    public FileSystem() {
      this.root = new Node("/", 0, null);
      this.currentNode = this.root;
    }

    public Node move(String fileName) {
      if (fileName.equals("..")) {
        this.currentNode = this.currentNode.getParent();
      } else if (fileName.equals("/")) {
        return this.moveToRoot();
      } else {
        this.currentNode = this.currentNode.getChild(fileName);
      }

      return this.currentNode;
    }

    public Node moveToRoot() {
      this.currentNode = root;
      return this.currentNode;
    }

    public void addFile(String fileName, int fileSize) {
      this.currentNode.addChild(fileName, fileSize);
    }

    public Node getCurrentNode() {
      return this.currentNode;
    }
  }

  private static class Node {
    private HashMap<String, Node> children = new HashMap<>();
    private Node parent = null;
    private String fileName = null;
    private int fileSize = 0;

    public Node(String fileName, int fileSize, Node parent) {
      this.fileName = fileName;
      this.fileSize = fileSize;
      this.parent = parent;
    }

    public void addChild(String fileName, int fileSize) {
      this.children.put(fileName, new Node(fileName, fileSize, this));
    }

    public HashMap<String, Node> getChildren() {
      return this.children;
    }

    public Node getChild(String fileName) {
      return this.children.get(fileName);
    }

    public Node getParent() {
      return this.parent;
    }

    public String getFileName() {
      return this.fileName;
    }

    public int getFileSize() {
      return this.fileSize;
    }

    public int getChildrenSize() {
      int totalSize = 0;

      Set<String> it = this.getChildren().keySet();
      for (String key : it) {
        int size = this.getChild(key).getFileSize();

        if (size == 0) {
          totalSize += this.getChild(key).getChildrenSize();
        } else {
          totalSize += size;
        }
      }

      return totalSize;
    }

    public int getSize() {
      return this.getFileSize() + this.getChildrenSize();
    }
  }
}
